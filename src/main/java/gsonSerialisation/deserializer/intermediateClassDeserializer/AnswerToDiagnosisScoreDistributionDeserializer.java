package gsonSerialisation.deserializer.intermediateClassDeserializer;

import com.google.gson.*;
import managers.ActionSuggestionManager;
import managers.AnswerManager;
import managers.DiagnosisManager;
import models.ActionSuggestion;
import models.Answer;
import models.Diagnosis;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem AnswerToDiagnosisScoreDistribution Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Description Id</li>
 * <li>Answer Id</li>
 * <li>Diagnosis Id</li>
 * <li>Score</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class AnswerToDiagnosisScoreDistributionDeserializer implements JsonDeserializer<AnswerToDiagnosisScoreDistribution> {
    private final String ID = "distribution_id";
    private final String DIAGNOSIS_ID = "diagnosis_id";
    private final String ANSWER_ID = "answer_id";
    private final String SCORE = "score";

    @Override
    public AnswerToDiagnosisScoreDistribution deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final AnswerToDiagnosisScoreDistribution distribution = new AnswerToDiagnosisScoreDistribution();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, distribution);
        setDiagnosis(jsonObject, distribution);
        setAnswer(jsonObject, distribution);
        setScore(jsonObject, distribution);
        return distribution;

    }

    private void setId(final JsonObject jsonObject, final AnswerToDiagnosisScoreDistribution distribution) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            distribution.setId(id);
        }
    }

    private void setScore(final JsonObject jsonObject, final AnswerToDiagnosisScoreDistribution distribution) {
        if (jsonObject.has(SCORE)) {
            final int score = jsonObject.getAsJsonPrimitive(SCORE).getAsInt();
            distribution.setScore(score);
        }
    }

    private void setDiagnosis(final JsonObject jsonObject, final AnswerToDiagnosisScoreDistribution distribution) {
        if (jsonObject.has(DIAGNOSIS_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(DIAGNOSIS_ID).getAsInt();
            final Diagnosis diagnosis = new DiagnosisManager().get(id);
            distribution.setDiagnosis(diagnosis);
        }
    }

    private void setAnswer(final JsonObject jsonObject, final AnswerToDiagnosisScoreDistribution distribution) {
        if (jsonObject.has(ANSWER_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ANSWER_ID).getAsInt();
            final Answer answer = new AnswerManager().get(id);
            distribution.setAnswer(answer);
        }
    }
}
