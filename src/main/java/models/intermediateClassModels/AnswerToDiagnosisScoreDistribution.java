package models.intermediateClassModels;

import database.mappers.AnswerConnector;
import database.mappers.DiagnosisConnector;
import models.Answer;
import models.Diagnosis;

import javax.persistence.*;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle score_distribution_answers_to_diagnoses
 *
 * @author Oliver Frischknecht
 */

public class AnswerToDiagnosisScoreDistribution {

    private int id;

    private int answerId;
    private Answer answer;

    private int diagnosisId;
    private Diagnosis diagnosis;

    private int score;

    public AnswerToDiagnosisScoreDistribution() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setAnswerId(int answerId) { this.answerId = answerId;}
    public Answer getAnswer() {

        if (answer == null){
            AnswerConnector con = new AnswerConnector();
            answer = con.read(answerId);
        }

        return answer;
    }
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public void setDiagnosisId(int diagnosisId) { this.diagnosisId = diagnosisId;}
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

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
