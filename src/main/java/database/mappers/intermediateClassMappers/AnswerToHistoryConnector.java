package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.Answer;
import models.History;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Roman on 10.10.2015.
 */
public class AnswerToHistoryConnector extends DatabaseConnector {

    public int create(Answer answer, History history){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM answers_to_histories WHERE answer = " + answer.getId() + " AND history = " + history.getId() +";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Answer already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO answers_to_histories (answer, history) VALUES("+ answer.getId() + ", " + history.getId() +");";

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
            System.out.println("SQL Error create answer to history");
        }

        return -1;
    }

    public HashSet<Answer> readGivenAnswersOfHistory(int historyId){

        HashSet<Answer> answers = new HashSet<Answer>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM answers WHERE answers_to_histories.answer == id AND answers_to_histories.history = " + historyId + ";";

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
            System.out.println("SQL Error read given answers of history");
        }

        return answers;
    }


}
