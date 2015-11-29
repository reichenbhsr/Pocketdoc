package gsonSerialisation.deserializer;

import com.google.gson.*;
import models.Setting;

import java.lang.reflect.Type;

/**
 * Diese Klasse wird gebraucht um mithilfe von Gson, ein Json Objekt zu einem Setting Objekt umzuwandeln
 * <p>
 * Es können folgende Elemente umgewandelt werden:
 * <ul>
 * <li>Setting Id</li>
 * <li>Name</li>
 * <li>Value</li>
 * <li>Type</li>
 * </ul>
 *
 * @author Nathan Bourquin
 */
public class SettingDeserializer implements JsonDeserializer<Setting> {
    final String ID = "setting_id";
    final String NAME = "name";
    final String VALUE = "value";
    final String TYPE = "type";


    @Override
    public Setting deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final Setting setting = new Setting();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        setId(jsonObject, setting);
        setName(jsonObject, setting);
        setValue(jsonObject, setting);
        setType(jsonObject, setting);

        return setting;

    }

    private void setId(final JsonObject jsonObject, final Setting setting) {
        if (jsonObject.has(ID)) {
            final int id = jsonObject.getAsJsonPrimitive(ID).getAsInt();
            setting.setId(id);
        }
    }

    private void setName(final JsonObject jsonObject, final Setting setting) {
        if (jsonObject.has(NAME)) {
            final String name = jsonObject.getAsJsonPrimitive(NAME).getAsString();
            setting.setName(name);
        }
    }

    private void setValue(final JsonObject jsonObject, final Setting setting) {
        if (jsonObject.has(VALUE)) {
            final String value = jsonObject.getAsJsonPrimitive(VALUE).getAsString();
            setting.setValue(value);
        } else if (jsonObject.has(NAME)) {
            throw new JsonParseException("Setting need a value when it has a name");
        }
    }

    private void setType(final JsonObject jsonObject, final Setting setting) {
        if (jsonObject.has(TYPE)) {
            final int type = jsonObject.getAsJsonPrimitive(TYPE).getAsInt();
            setting.setType(type);
        }
    }
}
