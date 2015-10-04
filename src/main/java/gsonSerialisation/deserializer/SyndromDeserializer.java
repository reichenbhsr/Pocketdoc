package gsonSerialisation.deserializer;

import com.google.gson.*;
import models.Answer;
import models.Syndrom;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem Syndrom Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Syndrom Id</li>
 * <li>Name</li>
 * <li>Syndroms</li>
 * </ul>
 * <p>
 * Für die Syndrome (Array) wird den {@link gsonSerialisation.deserializer.AnswerDeserializer} gebraucht.
 *
 * @author Nathan Bourquin
 */
public class SyndromDeserializer implements JsonDeserializer<Syndrom> {
    final String ID = "syndrom_id";
    final String NAME = "name";
    final String SYMPTOMS = "symptoms";


    @Override
    public Syndrom deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final Syndrom syndrom = new Syndrom();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, syndrom);
        setName(jsonObject, syndrom);
        setSymptoms(jsonObject, syndrom, jsonDeserializationContext);

        return syndrom;

    }

    private void setId(final JsonObject jsonObject, final Syndrom syndrom) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            syndrom.setId(id);
        }
    }

    private void setName(final JsonObject jsonObject, final Syndrom syndrom) {
        if (jsonObject.has(NAME)) {
            final String name = jsonObject.getAsJsonPrimitive(NAME).getAsString();
            syndrom.setName(name);
        }
    }

    private void setSymptoms(final JsonObject jsonObject, final Syndrom syndrom, JsonDeserializationContext jsonDeserializationContext) {
        if (jsonObject.has(SYMPTOMS)) {
            Set<Answer> answers = new HashSet();

            final JsonArray jsonArray = jsonObject.getAsJsonArray(SYMPTOMS);
            for (JsonElement jsonElement : jsonArray) {
                final Answer answer = jsonDeserializationContext.deserialize(jsonElement, Answer.class);
                answers.add(answer);
            }

            syndrom.setSymptoms(answers);
        }
    }
}
