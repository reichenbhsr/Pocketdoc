package managers.intermediateClassManagers;

import database.mappers.intermediateClassMappers.DiagnosisDesignationConnector;
import models.intermediateClassModels.DiagnosisDesignation;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse DiagnosisDesignation geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link IntermediateManager}
 *
 * @author Roman Eichenberger
 */
public class DiagnosisDesignationManager implements IntermediateManager<DiagnosisDesignation> {

    private DiagnosisDesignationConnector diagnosisDesignationMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public DiagnosisDesignationManager() {
        diagnosisDesignationMapper = new DiagnosisDesignationConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public DiagnosisDesignationManager(DiagnosisDesignationConnector mapper) {
        diagnosisDesignationMapper = mapper;
    }

    @Override
    public DiagnosisDesignation add(DiagnosisDesignation diagnosisDesignation) {
        diagnosisDesignationMapper.create(diagnosisDesignation);
        return diagnosisDesignation;
    }

    @Override
    public DiagnosisDesignation update(DiagnosisDesignation diagnosisDesignation) {
        DiagnosisDesignation oldDiagnosisDesignation = diagnosisDesignationMapper.read(diagnosisDesignation.getId());
        if (oldDiagnosisDesignation == null) {
            throw new IllegalArgumentException("DiagnosisDesignation " + diagnosisDesignation.getId() + " doesn't exist");
        } else {
            if (diagnosisDesignation.getDiagnosis() == null) {
                diagnosisDesignation.setDiagnosis(oldDiagnosisDesignation.getDiagnosis());
            }
            if (diagnosisDesignation.getDesignation() == null) {
                diagnosisDesignation.setDesignation(oldDiagnosisDesignation.getDesignation());
            }
            if (diagnosisDesignation.getLanguage() == null) {
                diagnosisDesignation.setLanguage(oldDiagnosisDesignation.getLanguage());
            }
            diagnosisDesignation = diagnosisDesignationMapper.update(diagnosisDesignation);
            return diagnosisDesignation;
        }
    }

    @Override
    public DiagnosisDesignation get(int id) {
        return diagnosisDesignationMapper.read(id);
    }

    @Override
    public ArrayList<DiagnosisDesignation> getAll() {
        return diagnosisDesignationMapper.readAll();
    }

    @Override
    public void remove(int id) {
        diagnosisDesignationMapper.delete(id);
    }
}
