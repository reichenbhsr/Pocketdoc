package models;

import models.intermediateClassModels.AnswerToActionSuggestionScoreDistribution;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle answers
 *
 * @author Oliver Frischknecht
 */

public class Answer {

    private int id;

    private Set<AnswerToActionSuggestionScoreDistribution> answerToActionSuggestionScoreDistributions;

    private Set<AnswerToDiagnosisScoreDistribution> answerToDiagnosisScoreDistributions;

    private Set<Question> dependencyFrom;

    private Question answerYesOf;

    private Question answerNoOf;

    public Answer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<AnswerToActionSuggestionScoreDistribution> getAnswerToActionSuggestionScoreDistributions() {
        return answerToActionSuggestionScoreDistributions;
    }

    public void setAnswerToActionSuggestionScoreDistributions(Set<AnswerToActionSuggestionScoreDistribution> answerToActionSuggestionScoreDistributions) {
        this.answerToActionSuggestionScoreDistributions = answerToActionSuggestionScoreDistributions;
    }

    public Set<AnswerToDiagnosisScoreDistribution> getAnswerToDiagnosisScoreDistributions() {
        return answerToDiagnosisScoreDistributions;
    }

    public void setAnswerToDiagnosisScoreDistributions(Set<AnswerToDiagnosisScoreDistribution> answerToDiagnosisScoreDistributions) {
        this.answerToDiagnosisScoreDistributions = answerToDiagnosisScoreDistributions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Answer) {
            return ((Answer) obj).getId() == id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Set<Question> getDependencyFrom() {
        return dependencyFrom;
    }

    public void setDependencyFrom(Set<Question> dependencyFrom) {
        this.dependencyFrom = dependencyFrom;
    }

    public Question getAnswerYesOf() {
        return answerYesOf;
    }

    public void setAnswerYesOf(Question answerYesOf) {
        this.answerYesOf = answerYesOf;
    }

    public Question getAnswerNoOf() {
        return answerNoOf;
    }

    public void setAnswerNoOf(Question answerNoOf) {
        this.answerNoOf = answerNoOf;
    }

    /**
     * Gibt die Frage zu dieser Antwort zurück (egal ob Ja oder Nein)
     *
     * @return Question
     */
    public Question getAnswerOf() {
        if (answerYesOf == null) {
            return answerNoOf;
        } else {
            return answerYesOf;
        }
    }
}
