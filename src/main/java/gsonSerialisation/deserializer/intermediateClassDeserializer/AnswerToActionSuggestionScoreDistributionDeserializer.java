package gsonSerialisation.deserializer.intermediateClassDeserializer;

import com.google.gson.*;
import managers.ActionSuggestionManager;
import managers.AnswerManager;
import models.ActionSuggestion;
import models.Answer;
import models.intermediateClassModels.AnswerToActionSuggestionScoreDistribution;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem AnswerToActionSuggestionScoreDistribution Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Distribution Id</li>
 * <li>Answer Id</li>
 * <li>Action Suggestion Id</li>
 * <li>Score</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class AnswerToActionSuggestionScoreDistributionDeserializer implements JsonDeserializer<AnswerToActionSuggestionScoreDistribution> {
    private final String ID = "distribution_id";
    private final String ACTION_SUGGESTION_ID = "action_suggestion_id";
    private final String ANSWER_ID = "answer_id";
    private final String SCORE = "score";

    @Override
    public AnswerToActionSuggestionScoreDistribution deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final AnswerToActionSuggestionScoreDistribution distribution = new AnswerToActionSuggestionScoreDistribution();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, distribution);
        setActionSuggestion(jsonObject, distribution);
        setAnswer(jsonObject, distribution);
        setScore(jsonObject, distribution);
        return distribution;

    }

    private void setId(final JsonObject jsonObject, final AnswerToActionSuggestionScoreDistribution distribution) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            distribution.setId(id);
        }
    }

    private void setScore(final JsonObject jsonObject, final AnswerToActionSuggestionScoreDistribution distribution) {
        if (jsonObject.has(SCORE)) {
            final int score = jsonObject.getAsJsonPrimitive(SCORE).getAsInt();
            distribution.setScore(score);
        }
    }

    private void setActionSuggestion(final JsonObject jsonObject, final AnswerToActionSuggestionScoreDistribution distribution) {
        if (jsonObject.has(ACTION_SUGGESTION_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ACTION_SUGGESTION_ID).getAsInt();
            final ActionSuggestion actionSuggestion = new ActionSuggestionManager().get(id);
            distribution.setActionSuggestion(actionSuggestion);
        }
    }

    private void setAnswer(final JsonObject jsonObject, final AnswerToActionSuggestionScoreDistribution distribution) {
        if (jsonObject.has(ANSWER_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ANSWER_ID).getAsInt();
            final Answer answer = new AnswerManager().get(id);
            distribution.setAnswer(answer);
        }
    }
}
