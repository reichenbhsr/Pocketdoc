package managers;

import database.mappers.SettingConnector;
import models.Setting;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse Setting geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.BasicManager}
 *
 * @author Oliver Frischknecht
 */
public class SettingManager implements BasicManager<Setting> {
//    private DatabaseMapper<Setting> settingMapper; FIXME
    private SettingConnector settingMapper;
    final public static int MIN_DIFFERENCE = 1;
    final public static int CONSECUTIVE_QUESTIONS = 2;


    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public SettingManager() {
        settingMapper = new SettingConnector();
//        settingMapper = new SettingMapper(); FIXME
        setDefaultSettings();
    }

    /**
     * In dieser Methode werden Default Settings gespeichert. Ihre Ids sind final und können überall geholt werden.
     */
    private void setDefaultSettings() {
        if (get(MIN_DIFFERENCE) == null) {
            Setting minimumDifference = new Setting();
            minimumDifference.setId(MIN_DIFFERENCE);
            minimumDifference.setName("Punkteabstand");
            minimumDifference.setValue("40");
            addSetting(minimumDifference);
        }

        if (get(CONSECUTIVE_QUESTIONS) == null) {
            Setting consecutiveQuestions = new Setting();
            consecutiveQuestions.setId(CONSECUTIVE_QUESTIONS);
            consecutiveQuestions.setName("Anzahl Runden als erstplazierte Diagnose");
            consecutiveQuestions.setValue("5");
            addSetting(consecutiveQuestions);

        }
    }

    private Setting addSetting(Setting setting) {
        settingMapper.create(setting);
        return setting;
    }

    public SettingManager(SettingConnector mapper) {
        settingMapper = mapper;
    }


    @Override
    public Setting add() {
        Setting setting = new Setting();
        setting.setValue("0");
        settingMapper.create(setting);
        return setting;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Attribute die nicht vom Objekt in der Datenbank geholt werden falls sie leer sind:
     * <ul>
     * <li>Value</li>
     * </ul>
     */
    @Override
    public Setting update(Setting setting) {
        Setting oldSetting = get(setting.getId());
        if (oldSetting == null) {
            throw new IllegalArgumentException("Setting " + setting.getId() + " doesn't exist!");
        } else {
            if (setting.getName() == null) {
                setting.setName(oldSetting.getName());
            }
            return settingMapper.update(setting);
        }
    }

    @Override
    public Setting get(int id) {
        return settingMapper.read(id);
    }

    @Override
    public ArrayList<Setting> getAll() {
        return settingMapper.readAll();
    }

    @Override
    public void remove(int id) {
        settingMapper.delete(id);
    }
}
