package gsonSerialisation.serializer;

import com.google.gson.*;
import models.Answer;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Answer Objekt zu einem Json Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Answer Id</li>
 * <li>Has Dependency</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class AnswerSerializer implements JsonSerializer<Answer> {
    final String ID = "answer_id";
    final String HAS_DEPENDENCY = "has_dependency";

    @Override
    public JsonElement serialize(Answer answer, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        object.addProperty(ID, answer.getId());
        setHasDependency(object, answer);
        return object;

    }

    void setHasDependency(JsonObject object, Answer answer) {
        Boolean hasDependency = answer.getDependencyFrom() != null;
        object.addProperty(HAS_DEPENDENCY, hasDependency);
    }
}
