package gsonSerialisation.serializer;

import com.google.gson.*;
import managers.DiagnosisManager;
import models.Answer;
import models.Diagnosis;
import models.intermediateClassModels.DiagnosisDescription;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Diagnosis Objekt zu einem Json Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Diagnosis Id</li>
 * <li>Name</li>
 * <li>Perfect Diagnosis</li>
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
 * <p>
 * Für die Perfekte Diagnose (Array) wird den {@link gsonSerialisation.serializer.AnswerSerializer} gebraucht.
 *
 * @author Nathan Bourquin
 */
public class DiagnosisSerializer implements JsonSerializer<Diagnosis> {
    final String ID = "diagnosis_id";
    final String NAME = "name";
    final String DESCRIPTIONS = "descriptions";
    final String PERFECT_DIAGNOSIS = "perfect_diagnosis";

    final String DESCRIPTIONS_ID = "description_id";
    final String DESCRIPTIONS_LANGUAGE_ID = "language_id";
    final String DESCRIPTIONS_LANGUAGE_NAME = "language_name";
    final String DESCRIPTIONS_DESCRIPTION = "description";

    final String PERFECT_DIAGNOSIS_ANSWER = "answer";


    @Override
    public JsonElement serialize(Diagnosis diagnosis, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        diagnosis = new DiagnosisManager().getAndFetch(diagnosis);

        object.addProperty(ID, diagnosis.getId());
        object.addProperty(NAME, diagnosis.getName());
        setDescriptions(object, diagnosis);
        setPerfectDiagnoses(object, diagnosis, jsonSerializationContext);
        return object;
    }

    void setDescriptions(JsonObject object, Diagnosis diagnosis) {
        final Set<DiagnosisDescription> descriptions = diagnosis.getDescriptions();

        if (descriptions != null) {

            JsonArray array = new JsonArray();
            for (DiagnosisDescription description : descriptions) {
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

    void setPerfectDiagnoses(JsonObject object, Diagnosis diagnosis, JsonSerializationContext jsonSerializationContext) {
        final Set<Answer> answersForPerfectDiagnosis = diagnosis.getAnswersForPerfectDiagnosis();
        if (answersForPerfectDiagnosis != null) {
            JsonArray array = new JsonArray();
            for (Answer answer : answersForPerfectDiagnosis) {
                JsonObject underObject = new JsonObject();
                underObject.add(PERFECT_DIAGNOSIS_ANSWER, jsonSerializationContext.serialize(answer));

                array.add(underObject);
            }
            object.add(PERFECT_DIAGNOSIS, array);
        }

    }
}
