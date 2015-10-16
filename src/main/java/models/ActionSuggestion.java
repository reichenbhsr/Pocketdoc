package models;

import database.mappers.ActionSuggestionConnector;
import database.mappers.intermediateClassMappers.ActionSuggestionDescriptionConnector;
import database.mappers.intermediateClassMappers.AnswerToActionSuggestionScoreDistributionConnector;
import database.mappers.intermediateClassMappers.SyndromToActionSuggestionScoreDistributionConnector;
import models.intermediateClassModels.ActionSuggestionDescription;
import models.intermediateClassModels.AnswerToActionSuggestionScoreDistribution;
import models.intermediateClassModels.SyndromToActionSuggestionScoreDistribution;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle action_suggestions
 *
 * @author Oliver Frischknecht
 */

public class ActionSuggestion {

    private int id;

    private String name;

    private Set<AnswerToActionSuggestionScoreDistribution> answerToActionSuggestionScoreDistributions;

    private Set<SyndromToActionSuggestionScoreDistribution> syndromToActionSuggestionScoreDistributions;

    private Set<ActionSuggestionDescription> descriptions;

    public ActionSuggestion() {

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

    public Set<ActionSuggestionDescription> getDescriptions() {

        if (descriptions == null)
        {
            ActionSuggestionDescriptionConnector asc = new ActionSuggestionDescriptionConnector();
            descriptions = asc.readSetOfActionSuggestionDescriptions(id);
        }

        return descriptions;
    }

    public void setDescriptions(Set<ActionSuggestionDescription> descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ActionSuggestion) {
            return ((ActionSuggestion) obj).getId() == id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Set<SyndromToActionSuggestionScoreDistribution> getSyndromToActionSuggestionScoreDistributions() {

        if (syndromToActionSuggestionScoreDistributions == null){
            SyndromToActionSuggestionScoreDistributionConnector stasc = new SyndromToActionSuggestionScoreDistributionConnector();
            syndromToActionSuggestionScoreDistributions = stasc.readSetOfSyndromToActionSuggestionScoreDistribution(id);
        }

        return syndromToActionSuggestionScoreDistributions;
    }

    public void setSyndromToActionSuggestionScoreDistributions(Set<SyndromToActionSuggestionScoreDistribution> syndromToActionSuggestionScoreDistributions) {
        this.syndromToActionSuggestionScoreDistributions = syndromToActionSuggestionScoreDistributions;
    }

    public Set<AnswerToActionSuggestionScoreDistribution> getAnswerToActionSuggestionScoreDistributions() {

        if (answerToActionSuggestionScoreDistributions == null){
            AnswerToActionSuggestionScoreDistributionConnector stasc = new AnswerToActionSuggestionScoreDistributionConnector();
            answerToActionSuggestionScoreDistributions = stasc.readSetOfAnswerToActionSuggestionScores(id);
        }

        return answerToActionSuggestionScoreDistributions;
    }

    public void setAnswerToActionSuggestionScoreDistributions(Set<AnswerToActionSuggestionScoreDistribution> answerToActionSuggestionScoreDistributions) {
        this.answerToActionSuggestionScoreDistributions = answerToActionSuggestionScoreDistributions;
    }
}
