package calculators.Fakes;

import managers.SyndromManager;
import models.Syndrom;

import java.util.ArrayList;

/**
 * Created by nbourqui on 07.11.2014.
 */
public class SyndromManagerFake extends SyndromManager {

    ArrayList<Syndrom> list = new ArrayList();

    public void addSyndrom(Syndrom syndrom){
        list.add(syndrom);
    }

    @Override
    public Syndrom getAndFetch(Syndrom syndrom) {
        return syndrom;
    }

    @Override
    public ArrayList<Syndrom> getAll() {
        return list;
    }
}
