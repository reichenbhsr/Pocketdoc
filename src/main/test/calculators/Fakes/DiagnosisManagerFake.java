package calculators.Fakes;

import managers.DiagnosisManager;
import managers.HistoryManager;
import models.Diagnosis;
import models.History;

import java.util.ArrayList;

/**
 * Created by nbourqui on 17.12.2014.
 */
public class DiagnosisManagerFake extends DiagnosisManager {

    ArrayList<Diagnosis> list;

    public DiagnosisManagerFake() {
        list = new ArrayList();
    }

    public DiagnosisManagerFake(ArrayList<Diagnosis> list) {
        this.list = new ArrayList(list);
    }

    @Override
    public Diagnosis getAndFetch(Diagnosis diagnosis) {
        return diagnosis;
    }

    @Override
    public ArrayList<Diagnosis> getAll() {
        return list;
    }
}
