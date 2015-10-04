package gsonSerialisation.deserializer;

import com.google.gson.*;
import models.ActionSuggestion;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem ActionSuggestion Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Action Suggestion Id</li>
 * <li>Name</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class ActionSuggestionDeserializer implements JsonDeserializer<ActionSuggestion> {
    final String ID = "action_suggestion_id";
    final String NAME = "name";

    @Override
    public ActionSuggestion deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final ActionSuggestion suggestion = new ActionSuggestion();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, suggestion);
        setName(jsonObject, suggestion);

        return suggestion;

    }

    private void setId(final JsonObject jsonObject, final ActionSuggestion suggestion) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            suggestion.setId(id);
        }
    }

    private void setName(final JsonObject jsonObject, final ActionSuggestion suggestion) {
        if (jsonObject.has(NAME)) {
            final String name = jsonObject.getAsJsonPrimitive(NAME).getAsString();
            suggestion.setName(name);
        }
    }
}
