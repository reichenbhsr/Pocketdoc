package gsonSerialisation.deserializer.intermediateClassDeserializer;

import com.google.gson.*;
import managers.DiagnosisManager;
import managers.LanguageManager;
import models.Diagnosis;
import models.Language;
import models.intermediateClassModels.DiagnosisDescription;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem DiagnosisDescription Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Description Id</li>
 * <li>Language Id</li>
 * <li>Diagnosis Id</li>
 * <li>Description</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class DiagnosisDescriptionDeserializer implements JsonDeserializer<DiagnosisDescription> {
    final String ID = "description_id";
    final String DIAGNOSIS_ID = "diagnosis_id";
    final String LANGUAGE_ID = "language_id";
    final String DESCRIPTION = "description";

    @Override
    public DiagnosisDescription deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final DiagnosisDescription description = new DiagnosisDescription();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, description);
        setDiagnosis(jsonObject, description);
        setLanguage(jsonObject, description);
        setDescription(jsonObject, description);

        return description;

    }

    private void setId(final JsonObject jsonObject, final DiagnosisDescription description) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            description.setId(id);
        }
    }

    private void setDiagnosis(final JsonObject jsonObject, final DiagnosisDescription description) {
        if (jsonObject.has(DIAGNOSIS_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(DIAGNOSIS_ID).getAsInt();
            final Diagnosis diagnosis = new DiagnosisManager().get(id);
            description.setDiagnosis(diagnosis);
        }
    }

    private void setLanguage(final JsonObject jsonObject, final DiagnosisDescription description) {
        if (jsonObject.has(LANGUAGE_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(LANGUAGE_ID).getAsInt();
            final Language language = new LanguageManager().get(id);
            description.setLanguage(language);
        }
    }

    private void setDescription(final JsonObject jsonObject, final DiagnosisDescription description) {
        if (jsonObject.has(DESCRIPTION)) {
            final String text = jsonObject.getAsJsonPrimitive(DESCRIPTION).getAsString();
            description.setDescription(text);
        }
    }
}
