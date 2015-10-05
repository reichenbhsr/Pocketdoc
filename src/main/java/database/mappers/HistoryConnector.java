package database.mappers;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import models.Answer;
import models.History;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

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
                        history.getConsecutiveQuestions() + "," +
                        (history.getLastAnswer() == null ? null : history.getLastAnswer().getId()) + ");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                history.setId(id);
                return id;
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
                    "consecutive_questions=" + history.getConsecutiveQuestions() + "," +
                    " last_answer=" + (history.getLastAnswer() == null ? null : history.getLastAnswer().getId()) +
                    " WHERE id =" + history.getId() + ";";

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
                history.setAnswers(readGivenAnswers(historyId));

                return history;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read history");
        }

        return null;
    }

    private HashSet<Answer> readGivenAnswers(int historyId){

        HashSet<Answer> givenAnswers = new HashSet<Answer>();

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM answers_to_histories WHERE history='"+ historyId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            Answer answer;
            while (set.next())
            {
                answer = new Answer();
                answer.setId(set.getInt("answer"));

                givenAnswers.add(answer);
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error read history answers");
        }

        return givenAnswers;

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
