package models.intermediateClassModels;

import database.mappers.ActionSuggestionConnector;
import database.mappers.QuestionConnector;
import database.mappers.SyndromConnector;
import models.ActionSuggestion;
import models.Syndrom;

import javax.persistence.*;
import javax.swing.*;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle score_distribution_syndroms_to_action_suggestions
 *
 * @author Oliver Frischknecht
 */

public class SyndromToActionSuggestionScoreDistribution {

    private int id;

    private int syndromId;
    private Syndrom syndrom;

    private int actionSuggestionId;
    private ActionSuggestion actionSuggestion;

    private int score;

    public SyndromToActionSuggestionScoreDistribution() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setSyndromId(int syndromId){ this.syndromId = syndromId; }
    public Syndrom getSyndrom() {

        if (syndrom == null){
            SyndromConnector con = new SyndromConnector();
            syndrom = con.read(syndromId);
        }

        return syndrom;
    }
    public void setSyndrom(Syndrom syndrom) {
        this.syndrom = syndrom;
    }

    public void setActionSuggestionId(int actionSuggestionId){ this.actionSuggestionId = actionSuggestionId; }
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
