package database.mappers;

import models.Followup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Roman on 23.11.2015.
 */
public class FollowupConntector extends DatabaseConnector{

    public int create(Followup followup){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            java.util.Date now = new java.util.Date();
            String SQL = "SELECT id FROM followups WHERE id = " + followup.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Followup already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO followups (usr, diagnosis, action_suggestion, timestamp) VALUES (" +
                        "" + followup.getUser().getId()  + "," +
                        "" + followup.getDiagnosis().getId()  + "," +
                        "" + followup.getActionSuggestion().getId()  + "," +
                        "'" + new Timestamp(now.getTime())  +  "');";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                followup.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Followup");
        }

        return -1;

    }

    public ArrayList<Followup> readFromUser(int userId){

        ArrayList<Followup> followups = new ArrayList<Followup>();

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM followups WHERE usr = " + userId + ";";

            ResultSet set = stmt.executeQuery(SQL);

            Followup followup;
            while (set.next())
            {
                followup = new Followup();
                followup.setId(set.getInt("id"));
                followup.setDiagnosisId(set.getInt("diagnosis"));
                followup.setUserId(set.getInt("usr"));
                followup.setActionSuggestionId(set.getInt("action_suggestion"));
                followup.setTimestamp(set.getTimestamp("timestamp"));

                followups.add(followup);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read followups from user");
        }

        return followups;

    }

    public void delete(int followupId)
    {
        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM followups WHERE id ='" + followupId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Followup");
        }
    }

}
