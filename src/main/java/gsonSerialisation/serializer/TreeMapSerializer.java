package gsonSerialisation.serializer;

import com.google.gson.*;
import models.ActionSuggestion;
import models.Diagnosis;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein TreeMap Objekt zu einem Json Objekt umzuwandeln
 * <p>
 * Es können nur TreeMaps bestehend aus Diagnosis/ActionSuggestion und Integer (Score) umgewandelt werden
 * <p>
 * Für die Umwandlung der Diagnosen wird der {@link gsonSerialisation.serializer.DiagnosisSerializer} gebraucht, für die Umwandlung der Handlungsempfehlungen {@link gsonSerialisation.serializer.ActionSuggestionSerializer}
 *
 * @author Nathan Bourquin
 */
public class TreeMapSerializer<T> implements JsonSerializer<TreeMap<T, Integer>> {

    final String SCORE = "score";

    @Override
    public JsonElement serialize(TreeMap<T, Integer> treeMap, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray array = new JsonArray();
        for (Map.Entry<T, Integer> tIntegerEntry : treeMap.entrySet()) {
            JsonObject object = new JsonObject();
            final T key = tIntegerEntry.getKey();
            if (key instanceof Diagnosis) {
                Diagnosis diagnosis = (Diagnosis) key;
                object = jsonSerializationContext.serialize(diagnosis).getAsJsonObject();
            } else if (key instanceof ActionSuggestion) {
                ActionSuggestion actionSuggestion = (ActionSuggestion) key;
                object = jsonSerializationContext.serialize(actionSuggestion).getAsJsonObject();
            }
            object.addProperty(SCORE, tIntegerEntry.getValue());
            array.add(object);
        }

        return array;
    }

}
