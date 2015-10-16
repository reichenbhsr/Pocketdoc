package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.intermediateClassModels.ActionSuggestionDescription;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Roman on 04.10.2015.
 */
public class ActionSuggestionDescriptionConnector extends DatabaseConnector{

    public int create(ActionSuggestionDescription actionSuggestionDescription){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM actionSuggestionDescription WHERE id = " + actionSuggestionDescription.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Action suggestion description already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO actionSuggestionDescription (description, language, action_suggestion) VALUES (" +
                        (actionSuggestionDescription.getDescription() == null ? null : "'" + actionSuggestionDescription.getDescription() + "'") + "," +
                        "" + actionSuggestionDescription.getLanguage().getId() + "," +
                        "" + actionSuggestionDescription.getActionSuggestion().getId() + ");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                actionSuggestionDescription.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Action suggestion description");
        }

        return -1;
    }

    public ActionSuggestionDescription update(ActionSuggestionDescription actionSuggestionDescription){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE action_suggestion_descriptions SET " +
                    "description=" + (actionSuggestionDescription.getDescription() == null ? null : "'" + actionSuggestionDescription.getDescription() + "'") + "," +
                    " language=" + actionSuggestionDescription.getLanguage().getId() +","+
                    " action_suggestion=" + actionSuggestionDescription.getActionSuggestion().getId() +","+
                    " WHERE id =" + actionSuggestionDescription.getId() + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Action suggestion description");
        }

        return actionSuggestionDescription;
    }

    public ActionSuggestionDescription read(int actionSuggestionDescriptionId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM action_suggestion_descriptions WHERE id='"+ actionSuggestionDescriptionId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                ActionSuggestionDescription actionSuggestionDescription = new ActionSuggestionDescription();
                actionSuggestionDescription.setId(set.getInt("id"));
                actionSuggestionDescription.setDescription(set.getString("description"));
                actionSuggestionDescription.setLanguageId(set.getInt("language"));
                actionSuggestionDescription.setActionSuggestionId(set.getInt("action_suggestion"));

                return actionSuggestionDescription;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read action suggestion description");
        }

        return null;
    }

    public HashSet<ActionSuggestionDescription> readSetOfActionSuggestionDescriptions(int actionSuggestionId){
        HashSet<ActionSuggestionDescription> set = new HashSet<ActionSuggestionDescription>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM action_suggestion_descriptions WHERE action_suggestion= " + actionSuggestionId + " ;";

            ResultSet res = stmt.executeQuery(SQL);

            ActionSuggestionDescription description;
            while (res.next())
            {
                description = new ActionSuggestionDescription();
                description.setId(res.getInt("id"));
                description.setLanguageId(res.getInt("language"));
                description.setActionSuggestionId(res.getInt("action_suggestion"));
                description.setDescription(res.getString("description"));

                set.add(description);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read set of action_suggestion descriptions");
        }

        return set;
    }

    public ArrayList<ActionSuggestionDescription> readAll(){

        ArrayList<ActionSuggestionDescription> actionSuggestionDescriptions = new ArrayList<ActionSuggestionDescription>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM action_suggestion_descriptions;";

            ResultSet set = stmt.executeQuery(SQL);

            ActionSuggestionDescription actionSuggestionDescription;
            while (set.next())
            {
                actionSuggestionDescription = new ActionSuggestionDescription();
                actionSuggestionDescription.setId(set.getInt("id"));
                actionSuggestionDescription.setDescription(set.getString("description"));
                actionSuggestionDescription.setLanguageId(set.getInt("language"));
                actionSuggestionDescription.setActionSuggestionId(set.getInt("action_suggestion"));

                actionSuggestionDescriptions.add(actionSuggestionDescription);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all action suggestion descriptions");
        }

        return actionSuggestionDescriptions;
    }

    public void delete(int actionSuggestionDescriptionId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM action_suggestion_description WHERE id ='" + actionSuggestionDescriptionId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete action suggestion description");
        }

    }

}
