package models.intermediateClassModels;

import database.mappers.ActionSuggestionConnector;
import database.mappers.AnswerConnector;
import models.ActionSuggestion;
import models.Answer;

import javax.persistence.*;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle score_distribution_answers_to_action_suggestions
 *
 * @author Oliver Frischknecht
 */

public class AnswerToActionSuggestionScoreDistribution {

    private int id;

    private int answerId;
    private Answer answer;

    private int actionSuggestionId;
    private ActionSuggestion actionSuggestion;

    private int score;

    public AnswerToActionSuggestionScoreDistribution() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setAnswerId(int answerId) { this.answerId = answerId; }
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

    public void setActionSuggestionId(int actionSuggestionId) { this.actionSuggestionId = actionSuggestionId; }
    public ActionSuggestion getActionSuggestion() {

        if (actionSuggestion == null){
            ActionSuggestionConnector con = new ActionSuggestionConnector();
            actionSuggestion = con.read(actionSuggestionId);
        }

        return actionSuggestion;
    }
    public void setActionSuggestion(ActionSuggestion actionSuggestion) {
        this.actionSuggestion = actionSuggestion;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
