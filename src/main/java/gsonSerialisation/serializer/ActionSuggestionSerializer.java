package gsonSerialisation.serializer;

import com.google.gson.*;
import managers.ActionSuggestionManager;
import models.ActionSuggestion;
import models.intermediateClassModels.ActionSuggestionDescription;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein ActionSuggestion Objekt zu einem Json Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Action Suggestion Id</li>
 * <li>Name</li>
 * <li>Description</li>
 * </ul>
 * <p>
 * Die Descriptions (Array) werden aus der Zwischentabelle geholt und mit folgenden Elemente aufgefüllt:
 * <ul>
 * <li>Description Id</li>
 * <li>Language Id</li>
 * <li>Language Name</li>
 * <li>Description</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class ActionSuggestionSerializer implements JsonSerializer<ActionSuggestion> {
    final String ID = "action_suggestion_id";
    final String NAME = "name";
    final String DESCRIPTIONS = "descriptions";

    final String DESCRIPTIONS_ID = "description_id";
    final String DESCRIPTIONS_LANGUAGE_ID = "language_id";
    final String DESCRIPTIONS_LANGUAGE_NAME = "language_name";
    final String DESCRIPTIONS_DESCRIPTION = "description";


    @Override
    public JsonElement serialize(ActionSuggestion actionSuggestion, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        object.addProperty(ID, actionSuggestion.getId());
        object.addProperty(NAME, actionSuggestion.getName());
        setDescriptions(object, actionSuggestion);

        return object;
    }

    void setDescriptions(JsonObject object, ActionSuggestion actionSuggestion) {
        actionSuggestion = new ActionSuggestionManager().getAndFetch(actionSuggestion);
        final Set<ActionSuggestionDescription> descriptions = actionSuggestion.getDescriptions();

        if (descriptions != null) {

            JsonArray array = new JsonArray();
            for (ActionSuggestionDescription description : descriptions) {
                JsonObject underObject = new JsonObject();
                underObject.addProperty(DESCRIPTIONS_ID, description.getId());
                underObject.addProperty(DESCRIPTIONS_LANGUAGE_ID, description.getLanguage().getId());
                underObject.addProperty(DESCRIPTIONS_LANGUAGE_NAME, description.getLanguage().getName());
                underObject.addProperty(DESCRIPTIONS_DESCRIPTION, description.getDescription());

                array.add(underObject);
            }
            object.add(DESCRIPTIONS, array);
        }
    }

}
