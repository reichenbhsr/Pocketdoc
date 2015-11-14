package gsonSerialisation.deserializer;

import com.google.gson.*;
import managers.HistoryManager;
import managers.LanguageManager;
import models.Answer;
import models.History;
import models.Language;
import models.User;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem User Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Syndrom Id</li>
 * <li>Name</li>
 * <li>Password</li>
 * <li>History Id</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class UserDeserializer implements JsonDeserializer<User> {
    final String ID = "user_id";
    final String NAME = "name";
    final String PASSWORD = "password";
    final String EMAIL = "email";
    final String GENDER = "gender";
    final String AGE_CATEGORY = "age_category";
    final String HISTORY_ID = "history_id";
    final String LANGUAGE_ID = "lang";


    @Override
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final User user = new User();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, user);
        setName(jsonObject, user);
        setPassword(jsonObject, user);
        setEmail(jsonObject, user);
        setGender(jsonObject, user);
        setAgeCategory(jsonObject, user);
        setHistory(jsonObject, user);
        setLanguage(jsonObject, user);
        return user;

    }

    private void setId(final JsonObject jsonObject, final User user) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            user.setId(id);
        }
    }

    private void setName(final JsonObject jsonObject, final User user) {
        if (jsonObject.has(NAME)) {
            final String name = jsonObject.getAsJsonPrimitive(NAME).getAsString();
            user.setName(name);
        }
    }

    private void setPassword(final JsonObject jsonObject, final User user) {
        if (jsonObject.has(PASSWORD)) {
            final String password = jsonObject.getAsJsonPrimitive(PASSWORD).getAsString();
            user.setPassword(password);
        }
    }

    private void setEmail(final JsonObject jsonObject, final User user) {
        if (jsonObject.has(EMAIL)) {
            final String email = jsonObject.getAsJsonPrimitive(EMAIL).getAsString();
            user.setEmail(email);
        }
    }

    private void setGender(final JsonObject jsonObject, final User user) {
        if (jsonObject.has(GENDER)) {
            final int gender = jsonObject.getAsJsonPrimitive(GENDER).getAsInt();
            user.setGender(gender);
        }
    }

    private void setAgeCategory(final JsonObject jsonObject, final User user) {
        if (jsonObject.has(AGE_CATEGORY)) {
            final int age_category = jsonObject.getAsJsonPrimitive(AGE_CATEGORY).getAsInt();
            user.setAgeCategory(age_category);
        }
    }

    private void setHistory(final JsonObject jsonObject, final User user) {
        if (jsonObject.has(HISTORY_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(HISTORY_ID).getAsInt();
            final History history = new HistoryManager().get(id);
            user.setHistory(history);
        }
    }

    private void setLanguage(final JsonObject jsonObject, final User user) {
        if (jsonObject.has(LANGUAGE_ID)) {
            final int id = jsonObject.getAsJsonPrimitive(LANGUAGE_ID).getAsInt();
            final Language language = new LanguageManager().get(id);
            user.setLanguage(language);
        }
    }

}
