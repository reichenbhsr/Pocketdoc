package gsonSerialisation.serializer;

import com.google.gson.*;
import models.Setting;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Setting Objekt zu einem Json Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Setting Id</li>
 * <li>Name</li>
 * <li>Value</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class SettingSerializer implements JsonSerializer<Setting> {
    final String ID = "setting_id";
    final String NAME = "name";
    final String VALUE = "value";
    final String TYPE = "type";


    @Override
    public JsonElement serialize(Setting setting, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        object.addProperty(ID, setting.getId());
        object.addProperty(NAME, setting.getName());
        object.addProperty(VALUE, setting.getValue());
        object.addProperty(TYPE, setting.getType());

        return object;
    }
}
