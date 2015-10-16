package models;

import database.mappers.intermediateClassMappers.AnswerToDiagnosisScoreDistributionConnector;
import database.mappers.intermediateClassMappers.DiagnosisDescriptionConnector;
import database.mappers.intermediateClassMappers.PerfectDiagnosisDiagnosesToAnswersConnector;
import database.mappers.intermediateClassMappers.QuestionDescriptionConnector;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;
import models.intermediateClassModels.DiagnosisDescription;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle diagnoses
 *
 * @author Oliver Frischknecht
 */

public class Diagnosis {

    private int id;

    private String name;

    private Set<AnswerToDiagnosisScoreDistribution> answerToDiagnosisScoreDistributions;

    private Set<DiagnosisDescription> descriptions;

    private Set<Answer> answersForPerfectDiagnosis;

    public Diagnosis() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DiagnosisDescription> getDescriptions() {

        if (descriptions == null){
            DiagnosisDescriptionConnector ddc = new DiagnosisDescriptionConnector();
            descriptions = ddc.readSetOfDiagnosisDescriptions(id);
        }

        return descriptions;
    }

    public void setDescriptions(Set<DiagnosisDescription> descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Diagnosis) {
            return ((Diagnosis) obj).getId() == id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }


    public Set<Answer> getAnswersForPerfectDiagnosis() {
        if (answersForPerfectDiagnosis == null){
            PerfectDiagnosisDiagnosesToAnswersConnector pdac = new PerfectDiagnosisDiagnosesToAnswersConnector();
            answersForPerfectDiagnosis = pdac.readPerfectAnswersForDiagnosis(id);
        }
        return answersForPerfectDiagnosis;
    }

    public void setAnswersForPerfectDiagnosis(Set<Answer> answersForPerfectDiagnosis) {
        this.answersForPerfectDiagnosis = answersForPerfectDiagnosis;
    }

    public Set<AnswerToDiagnosisScoreDistribution> getAnswerToDiagnosisScoreDistributions() {

        if (answerToDiagnosisScoreDistributions == null){
            AnswerToDiagnosisScoreDistributionConnector atdsc = new AnswerToDiagnosisScoreDistributionConnector();
            answerToDiagnosisScoreDistributions = atdsc.readSetOfDiagnosis(id);
        }

        return answerToDiagnosisScoreDistributions;
    }

    public void setAnswerToDiagnosisScoreDistributions(Set<AnswerToDiagnosisScoreDistribution> answerToDiagnosisScoreDistributions) {
        this.answerToDiagnosisScoreDistributions = answerToDiagnosisScoreDistributions;
    }
}
