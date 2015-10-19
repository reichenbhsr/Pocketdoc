package gsonSerialisation.deserializer;

import com.google.gson.*;
import managers.AnswerManager;
import models.Answer;
import models.Question;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem Question Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Question Id</li>
 * <li>Name</li>
 * <li>Dependence</li>
 * <li>Is Symptom</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class QuestionDeserializer implements JsonDeserializer<Question> {
    final String ID = "question_id";
    final String NAME = "name";
    final String DEPENDENCE_ID = "dependence";
    final String IS_SYMPTOM = "is_symptom";
    final String FORCE_DEPENDENT_ASKING = "force_dependent_asking"; // RE


    @Override
    public Question deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final Question question = new Question();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        setId(jsonObject, question);
        setDependence(jsonObject, question, jsonDeserializationContext);
        setIsSymptom(jsonObject, question);
        setName(jsonObject, question);
        setForceDependentAsking(jsonObject, question);
        return question;

    }

    private void setId(final JsonObject jsonObject, final Question question) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            question.setId(id);
        }
    }

    private void setDependence(final JsonObject jsonObject, final Question question, JsonDeserializationContext gson) {
        if (jsonObject.has(DEPENDENCE_ID)) {
            JsonObject object = jsonObject.getAsJsonObject(DEPENDENCE_ID);
            final Answer answer = gson.deserialize(object, Answer.class);
            question.setDependsOn(answer);
        }
    }

    private void setIsSymptom(final JsonObject jsonObject, final Question question) {
        if (jsonObject.has(IS_SYMPTOM)) {
            final boolean isSymptom = jsonObject.getAsJsonPrimitive(IS_SYMPTOM).getAsBoolean();
            question.setSymptom(isSymptom);
        }
    }

    private void setName(final JsonObject jsonObject, final Question question) {
        if (jsonObject.has(NAME)) {
            final String name = jsonObject.getAsJsonPrimitive(NAME).getAsString();
            question.setName(name);
        }
    }

    private void setForceDependentAsking(final JsonObject jsonObject, final Question question) { // RE
        if (jsonObject.has(FORCE_DEPENDENT_ASKING)) {
            final boolean forceDependentAsking = jsonObject.getAsJsonPrimitive(FORCE_DEPENDENT_ASKING).getAsBoolean();
            question.setForceDependentAsking(forceDependentAsking);
        }
    }
}
