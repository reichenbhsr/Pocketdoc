package database.mappers;

import models.Answer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class AnswerConnector extends DatabaseConnector {

    public int create(Answer answer){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM answers WHERE id = " + answer.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Answer already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO answers DEFAULT VALUES;";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                answer.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Answer");
        }

        return -1;
    }

    public Answer update(Answer answer){

        // not implemented, because there are no fields to update!
        return answer;
    }

    public Answer read(int answerId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM answers WHERE id="+ answerId + ";";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                Answer answer = new Answer();
                answer.setId(set.getInt("id"));

                return answer;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Answer");
        }

        return null;
    }

    public ArrayList<Answer> readAll(){

        ArrayList<Answer> answers = new ArrayList<Answer>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM answers;";

            ResultSet set = stmt.executeQuery(SQL);

            Answer answer;
            while (set.next())
            {
                answer = new Answer();
                answer.setId(set.getInt("id"));

                answers.add(answer);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all answers");
        }

        return answers;
    }

    public void delete(int answerId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM answers WHERE id ='" + answerId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Answer");
        }

    }

}
