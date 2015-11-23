package gsonSerialisation.deserializer;

import com.google.gson.*;
import models.Followup;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem Followup Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Followup Id</li>
 * <li>User</li>
 * <li>Diagnosis</li>
 * <li>Action Suggestion</li>
 * <li>Timestamp</li>
 * </ul>
 *
 * @author Roman Eichenberger
 */
public class FollowupDeserializer implements JsonDeserializer<Followup> {

    final String ID = "followup_id";
    final String USER_ID = "user_id";
    final String DIAGNOSIS_ID = "diagnosis_id";
    final String ACTION_SUGGESTION_ID = "action_suggestion_id";
    final String TIMESTAMP = "timestamp";

    @Override
    public Followup deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final Followup followup = new Followup();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        setId(jsonObject, followup);
        setUser(jsonObject, followup);
        setActionSuggestion(jsonObject, followup);
        setDiagnosis(jsonObject, followup);
        setTimestamp(jsonObject, followup);

        return followup;
    }

    private void setId(final JsonObject jsonObject, final Followup followup) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            followup.setId(id);
        }
    }

    private void setUser(final JsonObject jsonObject, final Followup followup) {
        if (jsonObject.has(USER_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(USER_ID).getAsInt();
            followup.setUserId(id);
        }
    }

    private void setDiagnosis(final JsonObject jsonObject, final Followup followup) {
        if (jsonObject.has(DIAGNOSIS_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(DIAGNOSIS_ID).getAsInt();
            followup.setDiagnosisId(id);
        }
    }

    private void setActionSuggestion(final JsonObject jsonObject, final Followup followup) {
        if (jsonObject.has(ACTION_SUGGESTION_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ACTION_SUGGESTION_ID).getAsInt();
            followup.setActionSuggestionId(id);
        }
    }

    private void setTimestamp(final JsonObject jsonObject, final Followup followup) {
        if (jsonObject.has(TIMESTAMP)) {
            final int timestamp = jsonObject.getAsJsonPrimitive(TIMESTAMP).getAsInt();
            followup.setTimestamp(new Date(timestamp));
        }
    }
}
