package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.Answer;
import models.Syndrom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

/**
 * Created by Roman on 13.10.2015.
 */
public class AnswersToSyndromsConnector extends DatabaseConnector {

    public int create(Answer answer, Syndrom syndrom){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM answers_to_syndroms WHERE answer = " + answer.getId() + " AND syndrom = " + syndrom.getId() +";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Answer of syndrom already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO answers_to_syndroms (answer, syndrom) VALUES("+ answer.getId() + ", " + syndrom.getId() +");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create answer of syndrom");
        }

        return -1;
    }

    public HashSet<Answer> readAnswersOfSyndrom(int syndromId){

        HashSet<Answer> answers = new HashSet<Answer>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT answers.* FROM answers, answers_to_syndroms WHERE answers_to_syndroms.answer = answers.id AND answers_to_syndroms.syndrom = " + syndromId + ";";

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
            System.out.println("SQL Error read answers of syndrom");
        }

        return answers;
    }

    public void delete(int syndromId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM answers_to_syndroms WHERE syndrom =" + syndromId + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete answers to syndrom");
        }

    }

    public void deleteFromAnswer(int answerId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM answers_to_syndroms WHERE answer =" + answerId + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete answers to syndrom from answer");
        }

    }

}
