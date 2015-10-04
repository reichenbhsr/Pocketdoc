package gsonSerialisation.deserializer.intermediateClassDeserializer;

import com.google.gson.*;
import managers.ActionSuggestionManager;
import managers.LanguageManager;
import models.ActionSuggestion;
import models.Language;
import models.intermediateClassModels.ActionSuggestionDescription;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem ActionSuggestionDescription Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Description Id</li>
 * <li>Language Id</li>
 * <li>Action Suggestion Id</li>
 * <li>Description</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class ActionSuggestionDescriptionDeserializer implements JsonDeserializer<ActionSuggestionDescription> {
    final String ID = "description_id";
    final String ACTION_SUGGESTION_ID = "action_suggestion_id";
    final String LANGUAGE_ID = "language_id";
    final String DESCRIPTION = "description";

    @Override
    public ActionSuggestionDescription deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final ActionSuggestionDescription description = new ActionSuggestionDescription();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, description);
        setActionSuggestion(jsonObject, description);
        setLanguage(jsonObject, description);
        setDescription(jsonObject, description);

        return description;

    }

    private void setId(final JsonObject jsonObject, final ActionSuggestionDescription description) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            description.setId(id);
        }
    }

    private void setActionSuggestion(final JsonObject jsonObject, final ActionSuggestionDescription description) {
        if (jsonObject.has(ACTION_SUGGESTION_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ACTION_SUGGESTION_ID).getAsInt();
            final ActionSuggestion actionSuggestion = new ActionSuggestionManager().get(id);
            description.setActionSuggestion(actionSuggestion);
        }
    }

    private void setLanguage(final JsonObject jsonObject, final ActionSuggestionDescription description) {
        if (jsonObject.has(LANGUAGE_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(LANGUAGE_ID).getAsInt();
            final Language language = new LanguageManager().get(id);
            description.setLanguage(language);
        }
    }

    private void setDescription(final JsonObject jsonObject, final ActionSuggestionDescription description) {
        if (jsonObject.has(DESCRIPTION)) {
            final String text = jsonObject.getAsJsonPrimitive(DESCRIPTION).getAsString();
            description.setDescription(text);
        }
    }
}
