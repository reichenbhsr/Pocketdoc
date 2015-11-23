package models;

import database.mappers.ActionSuggestionConnector;
import database.mappers.DiagnosisConnector;
import database.mappers.UserConnector;

import java.util.Date;

/**
 * Created by Roman on 23.11.2015.
 */
public class Followup {

    private int id;

    private int userId;
    private User user;

    private int diagnosisId;
    private Diagnosis diagnosis;

    private int actionSuggestionId;
    private ActionSuggestion actionSuggestion;

    private Date timeStamp;


    public int getId(){ return id; }
    public void setId(int id) { this.id = id;}

    public void setUserId(int id) { userId = id; }
    public void setUser(User user) { this.user = user; }
    public User getUser(){

        if (user == null){
            UserConnector con = new UserConnector();
            user = con.getUser(userId);
        }

        return user;
    }

    public void setDiagnosisId(int id) { diagnosisId = id; }
    public void setDiagnosis(Diagnosis diagnosis) { this.diagnosis = diagnosis; }
    public Diagnosis getDiagnosis(){

        if (diagnosis == null){
            DiagnosisConnector con = new DiagnosisConnector();
            diagnosis = con.read(diagnosisId);
        }

        return diagnosis;
    }

    public void setActionSuggestionId(int id) { actionSuggestionId = id; }
    public void setActionSuggestion(ActionSuggestion actionSuggestion) { this.actionSuggestion = actionSuggestion; }
    public ActionSuggestion getActionSuggestion(){

        if (actionSuggestion == null){
            ActionSuggestionConnector con = new ActionSuggestionConnector();
            actionSuggestion = con.read(actionSuggestionId);
        }

        return actionSuggestion;
    }

    public void setTimestamp(Date timeStamp) { this.timeStamp = timeStamp; }
    public Date getTimeStamp() { return timeStamp; }
}
