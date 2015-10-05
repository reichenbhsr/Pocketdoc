package database.mappers;

import models.ActionSuggestion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class ActionSuggestionConnector extends DatabaseConnector{

    public int create(ActionSuggestion actionSuggestion){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM action_suggestions WHERE id = " + actionSuggestion.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Action Suggestion already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO action_suggestions (name) VALUES (" +
                        (actionSuggestion.getName() == null ? null : "'" + actionSuggestion.getName() + "'") + ");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                       id = set.getInt(1);
                }
                actionSuggestion.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Action suggestion");
        }

        return -1;

    }

    public ActionSuggestion update(ActionSuggestion actionSuggestion){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE action_suggestions SET " +
                    " name=" + (actionSuggestion.getName() == null ? null : "'" + actionSuggestion.getName() + "'") +" "+
                    " WHERE id =" + actionSuggestion.getId() + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Action suggestion");
        }

        return actionSuggestion;
    }

    public ActionSuggestion read(int actionSuggestionId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM action_suggestions WHERE id='"+ actionSuggestionId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                ActionSuggestion actionSuggestion = new ActionSuggestion();
                actionSuggestion.setId(set.getInt("id"));
                actionSuggestion.setName(set.getString("name"));

                return actionSuggestion;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Action suggestion");
        }

        return null;
    }

    public ArrayList<ActionSuggestion> readAll(){

        ArrayList<ActionSuggestion> actionSuggestions = new ArrayList<ActionSuggestion>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM action_suggestions;";

            ResultSet set = stmt.executeQuery(SQL);

            ActionSuggestion actionSuggestion;
            while (set.next())
            {
                actionSuggestion = new ActionSuggestion();
                actionSuggestion.setId(set.getInt("id"));
                actionSuggestion.setName(set.getString("name"));

                actionSuggestions.add(actionSuggestion);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all Action suggestions");
        }

        return actionSuggestions;
    }

    public void delete(int actionSuggestionId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM action_suggestions WHERE id ='" + actionSuggestionId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Action suggestion");
        }

    }

}
