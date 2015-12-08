package managers.intermediateClassManagers;

import database.mappers.intermediateClassMappers.DiagnosisDescriptionConnector;
import models.intermediateClassModels.DiagnosisDescription;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse DiagnosisDescription geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.intermediateClassManagers.IntermediateManager}
 *
 * @author Oliver Frischknecht
 */
public class DiagnosisDescriptionManager implements IntermediateManager<DiagnosisDescription> {

    private DiagnosisDescriptionConnector diagnosisDescriptionMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public DiagnosisDescriptionManager() {
        diagnosisDescriptionMapper = new DiagnosisDescriptionConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public DiagnosisDescriptionManager(DiagnosisDescriptionConnector mapper) {
        diagnosisDescriptionMapper = mapper;
    }

    @Override
    public DiagnosisDescription add(DiagnosisDescription diagnosisDescription) {
        diagnosisDescriptionMapper.create(diagnosisDescription);
        return diagnosisDescription;
    }

    @Override
    public DiagnosisDescription update(DiagnosisDescription diagnosisDescription) {
        DiagnosisDescription oldDiagnosisDescription = diagnosisDescriptionMapper.read(diagnosisDescription.getId());
        if (oldDiagnosisDescription == null) {
            throw new IllegalArgumentException("DiagnosisDescription " + diagnosisDescription.getId() + " doesn't exist");
        } else {
            if (diagnosisDescription.getDiagnosis() == null) {
                diagnosisDescription.setDiagnosis(oldDiagnosisDescription.getDiagnosis());
            }
            if (diagnosisDescription.getDescription() == null) {
                diagnosisDescription.setDescription(oldDiagnosisDescription.getDescription());
            }
            if (diagnosisDescription.getLanguage() == null) {
                diagnosisDescription.setLanguage(oldDiagnosisDescription.getLanguage());
            }
            diagnosisDescription = diagnosisDescriptionMapper.update(diagnosisDescription);
            return diagnosisDescription;
        }
    }

    @Override
    public DiagnosisDescription get(int id) {
        return diagnosisDescriptionMapper.read(id);
    }

    @Override
    public ArrayList<DiagnosisDescription> getAll() {
        return diagnosisDescriptionMapper.readAll();
    }

    @Override
    public void remove(int id) {
        diagnosisDescriptionMapper.delete(id);
    }
}
