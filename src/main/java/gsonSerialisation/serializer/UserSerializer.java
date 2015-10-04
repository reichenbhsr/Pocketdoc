package gsonSerialisation.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.User;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein User Objekt zu einem Json Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>User Id</li>
 * <li>Name</li>
 * <li>Passwort</li>
 * <li>History Id</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class UserSerializer implements JsonSerializer<User> {
    final String ID = "user_id";
    final String NAME = "name";
    final String PASSWORD = "password";
    final String HISTORY_ID = "history_id";

    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        object.addProperty(ID, user.getId());
        object.addProperty(NAME, user.getName());
        object.addProperty(PASSWORD, user.getPassword());
        object.addProperty(HISTORY_ID, user.getHistory().getId());

        return object;

    }
}
