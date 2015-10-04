package database.mappers;

import models.History;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class HistoryConnector extends DatabaseConnector {

    public int create(History history){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM histories WHERE id = " + history.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("History already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO histories (consecutive_questions, last_answer) VALUES (" +
                        "'" + history.getConsecutiveQuestions() + "'," +
                        "'" + history.getLastAnswer().getId() + "');";

                return stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create history");
        }

        return -1;
    }

    public History update(History history){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE histories SET " +
                    "consecutive_questions='" + history.getConsecutiveQuestions() + "'," +
                    " last_answer='" + history.getLastAnswer().getId() +"' "+
                    " WHERE id ='" + history.getId() + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update history");
        }

        return history;
    }

    public History read(int historyId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM histories WHERE id='"+ historyId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                History history = new History();
                history.setId(set.getInt("id"));
                history.setConsecutiveQuestions(set.getInt("consecutive_questions"));
                history.setLastAnswerId(set.getInt("last_answer"));

                return history;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read history");
        }

        return null;
    }

    public ArrayList<History> readAll(){

        ArrayList<History> histories = new ArrayList<History>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM histories;";

            ResultSet set = stmt.executeQuery(SQL);

            History history;
            while (set.next())
            {
                history = new History();
                history.setId(set.getInt("id"));
                history.setConsecutiveQuestions(set.getInt("consecutive_questions"));
                history.setLastAnswerId(set.getInt("last_answer"));

                histories.add(history);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all histories");
        }

        return histories;
    }

    public void delete(int historyId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM histories WHERE id ='" + historyId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete history");
        }

    }

}
