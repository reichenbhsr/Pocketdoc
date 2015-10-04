package gsonSerialisation.deserializer;

import com.google.gson.*;
import models.ActionSuggestion;
import models.Answer;
import models.Diagnosis;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem Diagnosis Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Diagnosis Id</li>
 * <li>Name</li>
 * <li>Perfect Diagnosis</li>
 * </ul>
 * <p>
 * Für die Perfekte Diagnose (Array) wird den {@link gsonSerialisation.deserializer.AnswerDeserializer} gebraucht.
 *
 * @author Nathan Bourquin
 */
public class DiagnosisDeserializer implements JsonDeserializer<Diagnosis> {
    final String ID = "diagnosis_id";
    final String NAME = "name";
    final String PERFECT_DIAGNOSIS = "perfect_diagnosis";


    @Override
    public Diagnosis deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final Diagnosis diagnosis = new Diagnosis();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, diagnosis);
        setName(jsonObject, diagnosis);
        setPerfectDiagnosis(jsonObject, diagnosis, jsonDeserializationContext);
        return diagnosis;

    }

    private void setId(final JsonObject jsonObject, final Diagnosis diagnosis) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            diagnosis.setId(id);
        }
    }

    private void setName(final JsonObject jsonObject, final Diagnosis diagnosis) {
        if (jsonObject.has(NAME)) {
            final String name = jsonObject.getAsJsonPrimitive(NAME).getAsString();
            diagnosis.setName(name);
        }
    }

    private void setPerfectDiagnosis(final JsonObject jsonObject, final Diagnosis diagnosis, JsonDeserializationContext gson) {
        if (jsonObject.has(PERFECT_DIAGNOSIS)) {
            final JsonArray array = jsonObject.getAsJsonArray(PERFECT_DIAGNOSIS);
            Set<Answer> answers = new HashSet<Answer>();
            for (JsonElement jsonElement : array) {
                final Answer answer = gson.deserialize(jsonElement, Answer.class);
                answers.add(answer);
            }
            diagnosis.setAnswersForPerfectDiagnosis(answers);
        }
    }

}
