package models.intermediateClassModels;

import database.mappers.DiagnosisConnector;
import database.mappers.LanguageConnector;
import models.Diagnosis;
import models.Language;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle diagnosis_designations
 *
 * @author Roman Eichenberger
 */
public class DiagnosisDesignation {

    private int id;

    private int diagnosisId;
    private Diagnosis diagnosis;

    private int languageId;
    private Language language;

    private String designation;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public void setDiagnosisId(int diagnosisId) { this.diagnosisId = diagnosisId; }
    public Diagnosis getDiagnosis() {

        if (diagnosis == null){
            DiagnosisConnector con = new DiagnosisConnector();
            diagnosis = con.read(diagnosisId);
        }

        return diagnosis;
    }
    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setLanguageId(int languageId) { this.languageId = languageId; }
    public Language getLanguage() {

        if (language == null){
            LanguageConnector con = new LanguageConnector();
            language = con.read(languageId);
        }

        return language;
    }
    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
