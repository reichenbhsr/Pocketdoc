package gsonSerialisation.serializer;

import com.google.gson.*;
import managers.SyndromManager;
import models.Answer;
import models.Syndrom;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Syndrom Objekt zu einem Json Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Syndrom Id</li>
 * <li>Name</li>
 * <li>Symptoms</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class SyndromSerializer implements JsonSerializer<Syndrom> {
    final String ID = "syndrom_id";
    final String NAME = "name";
    final String SYMPTOMS = "symptoms";
    final String ANSWER_ID = "answer_id";

    @Override
    public JsonElement serialize(Syndrom syndrom, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        object.addProperty(ID, syndrom.getId());
        object.addProperty(NAME, syndrom.getName());
        setSymptoms(object, syndrom);

        return object;
    }

    void setSymptoms(JsonObject object, Syndrom syndrom) {
        syndrom = new SyndromManager().getAndFetch(syndrom);
        final Set<Answer> symptoms = syndrom.getSymptoms();

        if (symptoms != null) {

            JsonArray array = new JsonArray();
            for (Answer answer : symptoms) {
                JsonObject underObject = new JsonObject();
                underObject.addProperty(ANSWER_ID, answer.getId());

                array.add(underObject);
            }
            object.add(SYMPTOMS, array);
        }
    }
}
