package gsonSerialisation.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Followup;
import models.intermediateClassModels.DiagnosisDesignation;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Followup Objekt zu einem Json Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Followup Id</li>
 * <li>User Id</li>
 * <li>Diagnosis Id</li>
 * <li>Action Suggestion Id</li>
 * <li>Timestamp</li>
 * </ul>
 *
 * @author Roman Eichenberger
 */
public class FollowupSerializer implements JsonSerializer<Followup> {

    final String ID = "followup_id";
    final String USER_ID = "user_id";
    final String DIAGNOSIS_ID = "diagnosis_id";
    final String DIAGNOSIS_NAME = "diagnosis_name";
    final String ACTION_SUGGESTION_ID = "action_suggestion_id";
    final String TIMESTAMP = "timestamp"; // RE

    @Override
    public JsonElement serialize(Followup followup, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        object.addProperty(ID, followup.getId());
        object.addProperty(USER_ID, followup.getUser().getId());
        object.addProperty(DIAGNOSIS_ID, followup.getDiagnosis().getId());
        Set<DiagnosisDesignation> designations = followup.getDiagnosis().getDesignations();
        for(DiagnosisDesignation designation: designations) {
            if (designation.getLanguage().getId() == followup.getUser().getLanguage().getId())
                object.addProperty(DIAGNOSIS_NAME, designation.getDesignation());
        }
        object.addProperty(ACTION_SUGGESTION_ID, followup.getActionSuggestion().getId());
        object.addProperty(TIMESTAMP, followup.getTimeStamp().getTime());

        return object;
    }

}
