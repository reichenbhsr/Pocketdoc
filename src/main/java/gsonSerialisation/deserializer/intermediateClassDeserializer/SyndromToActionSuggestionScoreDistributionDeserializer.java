package gsonSerialisation.deserializer.intermediateClassDeserializer;

import com.google.gson.*;
import managers.ActionSuggestionManager;
import managers.SyndromManager;
import models.ActionSuggestion;
import models.Syndrom;
import models.intermediateClassModels.SyndromToActionSuggestionScoreDistribution;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem SyndromToActionSuggestionScoreDistribution Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Distribution Id</li>
 * <li>Syndrom Id</li>
 * <li>Action Suggestion Id</li>
 * <li>Score</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class SyndromToActionSuggestionScoreDistributionDeserializer implements JsonDeserializer<SyndromToActionSuggestionScoreDistribution> {
    private final String ID = "distribution_id";
    private final String ACTION_SUGGESTION_ID = "action_suggestion_id";
    private final String SYNDROM_ID = "syndrom_id";
    private final String SCORE = "score";

    @Override
    public SyndromToActionSuggestionScoreDistribution deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final SyndromToActionSuggestionScoreDistribution distribution = new SyndromToActionSuggestionScoreDistribution();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, distribution);
        setActionSuggestion(jsonObject, distribution);
        setSyndrom(jsonObject, distribution);
        setScore(jsonObject, distribution);
        return distribution;

    }

    private void setId(final JsonObject jsonObject, final SyndromToActionSuggestionScoreDistribution distribution) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            distribution.setId(id);
        }
    }

    private void setScore(final JsonObject jsonObject, final SyndromToActionSuggestionScoreDistribution distribution) {
        if (jsonObject.has(SCORE)) {
            final int score = jsonObject.getAsJsonPrimitive(SCORE).getAsInt();
            distribution.setScore(score);
        }
    }

    private void setActionSuggestion(final JsonObject jsonObject, final SyndromToActionSuggestionScoreDistribution distribution) {
        if (jsonObject.has(ACTION_SUGGESTION_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ACTION_SUGGESTION_ID).getAsInt();
            final ActionSuggestion actionSuggestion = new ActionSuggestionManager().get(id);
            distribution.setActionSuggestion(actionSuggestion);
        }
    }

    private void setSyndrom(final JsonObject jsonObject, final SyndromToActionSuggestionScoreDistribution distribution) {
        if (jsonObject.has(SYNDROM_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(SYNDROM_ID).getAsInt();
            final Syndrom syndrom = new SyndromManager().get(id);
            distribution.setSyndrom(syndrom);
        }
    }
}
