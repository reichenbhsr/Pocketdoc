package calculators.Fakes;

import managers.AnswerManager;
import managers.SettingManager;
import models.Answer;
import models.Setting;

/**
 * Created by nbourqui on 17.12.2014.
 */
public class SettingManagerFake extends SettingManager {

    @Override
    public Setting get(int id) {
        final Setting setting = new Setting();
        switch (id){
            case SettingManager.CONSECUTIVE_QUESTIONS:
                setting.setValue(5);
                return setting;
            case SettingManager.MIN_DIFFERENCE:
                setting.setValue(50);
                return setting;
            default:
                return null;
        }
    }
}
