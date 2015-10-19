package gsonSerialisation.serializer;

import com.google.gson.*;
import managers.QuestionManager;
import models.Answer;
import models.Question;
import models.intermediateClassModels.QuestionDescription;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Question Objekt zu einem Json Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Question Id</li>
 * <li>Name</li>
 * <li>Answer Yes</li>
 * <li>Answer No</li>
 * <li>Dependence</li>
 * <li>Is Symptom</li>
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
 * Für die Antworten und Dependences (Array) werden den {@link gsonSerialisation.serializer.AnswerSerializer} gebraucht.
 *
 * @author Nathan Bourquin
 */
public class QuestionSerializer implements JsonSerializer<Question> {
    final String ID = "question_id";
    final String NAME = "name";
    final String DEPENDENCE = "dependence";
    final String IS_SYMPTOM = "is_symptom";
    final String ANSWER_YES = "answer_yes";
    final String ANSWER_NO = "answer_no";
    final String FORCE_DEPENDENT_ASKING = "force_dependent_asking"; // RE

    final String DESCRIPTIONS = "descriptions";

    final String DESCRIPTIONS_ID = "description_id";
    final String DESCRIPTIONS_LANGUAGE_ID = "language_id";
    final String DESCRIPTIONS_LANGUAGE_NAME = "language_name";
    final String DESCRIPTIONS_DESCRIPTION = "description";


    @Override
    public JsonElement serialize(Question question, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        object.addProperty(ID, question.getId());
        object.addProperty(NAME, question.getName());
        object.addProperty(IS_SYMPTOM, question.isSymptom());
        object.addProperty(FORCE_DEPENDENT_ASKING, question.getForceDependentAsking()); // RE
        setDescriptions(object, question);
        setDependence(object, question, jsonSerializationContext);
        setNoAnswer(object, question, jsonSerializationContext);
        setYesAnswer(object, question, jsonSerializationContext);

        return object;
    }

    private void setDescriptions(JsonObject object, Question question) {
        question = new QuestionManager().getAndFetch(question);
        final Set<QuestionDescription> descriptions = question.getDescriptions();

        if (descriptions != null) {

            JsonArray array = new JsonArray();
            for (QuestionDescription description : descriptions) {
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

    private void setDependence(JsonObject object, Question question, JsonSerializationContext jsonSerializationContext) {
        final Answer answer = question.getDependsOn();
        if (answer != null) {
            object.add(DEPENDENCE, jsonSerializationContext.serialize(answer));
        }
    }

    private void setNoAnswer(JsonObject object, Question question, JsonSerializationContext jsonSerializationContext) {
        final Answer answer = question.getAnswerNo();

        object.add(ANSWER_NO, jsonSerializationContext.serialize(answer));
    }

    void setYesAnswer(JsonObject object, Question question, JsonSerializationContext jsonSerializationContext) {
        final Answer answer = question.getAnswerYes();

        object.add(ANSWER_YES, jsonSerializationContext.serialize(answer));
    }
}
