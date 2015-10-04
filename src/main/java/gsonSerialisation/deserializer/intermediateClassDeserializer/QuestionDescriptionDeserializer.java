package gsonSerialisation.deserializer.intermediateClassDeserializer;

import com.google.gson.*;
import managers.LanguageManager;
import managers.QuestionManager;
import models.Language;
import models.Question;
import models.intermediateClassModels.QuestionDescription;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem QuestionDescription Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Description Id</li>
 * <li>Language Id</li>
 * <li>Question Id</li>
 * <li>Description</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class QuestionDescriptionDeserializer implements JsonDeserializer<QuestionDescription> {
    final String ID = "description_id";
    final String QUESTION_ID = "question_id";
    final String LANGUAGE_ID = "language_id";
    final String DESCRIPTION = "description";

    @Override
    public QuestionDescription deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final QuestionDescription description = new QuestionDescription();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, description);
        setDiagnosis(jsonObject, description);
        setLanguage(jsonObject, description);
        setDescription(jsonObject, description);

        return description;

    }

    private void setId(final JsonObject jsonObject, final QuestionDescription description) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            description.setId(id);
        }
    }

    private void setDiagnosis(final JsonObject jsonObject, final QuestionDescription description) {
        if (jsonObject.has(QUESTION_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(QUESTION_ID).getAsInt();
            final Question question = new QuestionManager().get(id);
            description.setQuestion(question);
        }
    }

    private void setLanguage(final JsonObject jsonObject, final QuestionDescription description) {
        if (jsonObject.has(LANGUAGE_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(LANGUAGE_ID).getAsInt();
            final Language language = new LanguageManager().get(id);
            description.setLanguage(language);
        }
    }

    private void setDescription(final JsonObject jsonObject, final QuestionDescription description) {
        if (jsonObject.has(DESCRIPTION)) {
            final String text = jsonObject.getAsJsonPrimitive(DESCRIPTION).getAsString();
            description.setDescription(text);
        }
    }
}
