package gsonSerialisation.deserializer;

import com.google.gson.*;
import models.ActionSuggestion;
import models.Answer;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem Answer Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Answer Id</li>
 * </ul>
 * <p>
 * Die Antwort kann auch in einem Answer Objekt verpackt sein.
 *
 * @author Nathan Bourquin
 */
public class AnswerDeserializer implements JsonDeserializer<Answer> {
    final String ID = "answer_id";
    final String ALTERNATIVE_ANSWER = "answer";


    @Override
    public Answer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final Answer answer = new Answer();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, answer);
        return answer;

    }

    private void setId(final JsonObject jsonObject, final Answer answer) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            answer.setId(id);
        } else if (jsonObject.has(ALTERNATIVE_ANSWER)) {
            final JsonObject alternativeObject = jsonObject.getAsJsonObject(ALTERNATIVE_ANSWER);
            if (alternativeObject.has(ID)) {
                final int id = alternativeObject.getAsJsonPrimitive(ID).getAsInt();
                answer.setId(id);
            }
        }
    }
}
