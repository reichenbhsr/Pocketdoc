package models.intermediateClassModels;

import database.mappers.ActionSuggestionConnector;
import database.mappers.LanguageConnector;
import models.ActionSuggestion;
import models.Language;

import javax.persistence.*;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle action_suggestion_descriptions
 *
 * @author Oliver Frischknecht
 */
public class ActionSuggestionDescription {

    private int id;

    private int actionSuggestionId;
    private ActionSuggestion actionSuggestion;

    private int languageId;
    private Language language;

    private String description;

    public ActionSuggestionDescription() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
