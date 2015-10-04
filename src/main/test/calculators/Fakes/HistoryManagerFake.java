package calculators.Fakes;

import managers.HistoryManager;
import models.History;

/**
 * Created by nbourqui on 17.12.2014.
 */
public class HistoryManagerFake extends HistoryManager {

    @Override
    public History getAndFetch(History history) {
        return history;
    }

    @Override
    public History update(History history) {
        return history;
    }


}
