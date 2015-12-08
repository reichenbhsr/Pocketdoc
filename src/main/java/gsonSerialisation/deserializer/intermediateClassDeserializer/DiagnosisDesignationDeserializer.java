package gsonSerialisation.deserializer.intermediateClassDeserializer;

import com.google.gson.*;
import managers.DiagnosisManager;
import managers.LanguageManager;
import models.Diagnosis;
import models.Language;
import models.intermediateClassModels.DiagnosisDesignation;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem DiagnosisDesignation Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Designation Id</li>
 * <li>Language Id</li>
 * <li>Diagnosis Id</li>
 * <li>Designation</li>
 * </ul>
 *
 * @author Roman Eichenberger
 */
public class DiagnosisDesignationDeserializer implements JsonDeserializer<DiagnosisDesignation> {
    final String ID = "designation_id";
    final String DIAGNOSIS_ID = "diagnosis_id";
    final String LANGUAGE_ID = "language_id";
    final String DESIGNATION = "designation";

    @Override
    public DiagnosisDesignation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final DiagnosisDesignation designation = new DiagnosisDesignation();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, designation);
        setDiagnosis(jsonObject, designation);
        setLanguage(jsonObject, designation);
        setDesignation(jsonObject, designation);

        return designation;

    }

    private void setId(final JsonObject jsonObject, final DiagnosisDesignation designation) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            designation.setId(id);
        }
    }

    private void setDiagnosis(final JsonObject jsonObject, final DiagnosisDesignation designation) {
        if (jsonObject.has(DIAGNOSIS_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(DIAGNOSIS_ID).getAsInt();
            final Diagnosis diagnosis = new DiagnosisManager().get(id);
            designation.setDiagnosis(diagnosis);
        }
    }

    private void setLanguage(final JsonObject jsonObject, final DiagnosisDesignation designation) {
        if (jsonObject.has(LANGUAGE_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(LANGUAGE_ID).getAsInt();
            final Language language = new LanguageManager().get(id);
            designation.setLanguage(language);
        }
    }

    private void setDesignation(final JsonObject jsonObject, final DiagnosisDesignation designation) {
        if (jsonObject.has(DESIGNATION)) {
            final String text = jsonObject.getAsJsonPrimitive(DESIGNATION).getAsString();
            designation.setDesignation(text);
        }
    }
}
