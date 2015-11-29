package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Roman on 10.10.2015.
 */
public class AnswerToHistoryConnector extends DatabaseConnector {

    public int create(Answer answer, History history, Question question){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM answers_to_histories WHERE answer = " + answer.getId() + " AND history = " + history.getId() +" AND followup is NULL;";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Answer of history already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO answers_to_histories (answer, history, question) VALUES(" + ((answer.getId() > -1) ? answer.getId() : null) + ", " + history.getId() + ", " + question.getId() +");";

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

    public void storeCurrentRunToFollowup(Followup followup){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            StringBuilder SQL = new StringBuilder();
            SQL.append("UPDATE answers_to_histories SET followup = ")
                    .append(followup.getId())
                    .append(" WHERE history = ")
                    .append(followup.getUser().getHistory().getId())
                    .append(" AND followup is null;");

            stmt.execute(SQL.toString());
        }
        catch (SQLException ex){
            System.out.println("SQL Error store current run to followup");
        }
    }

    public ArrayList<Question> getYesAnsweredQuestionsFromFollowup(Followup followup){

        ArrayList<Question> questions = new ArrayList<Question>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            StringBuilder SQL = new StringBuilder();
            SQL.append("SELECT * from answers_to_histories WHERE followup = ")
                    .append(followup.getId())
                    .append(" ORDER BY id ASC;");

            ResultSet set = stmt.executeQuery(SQL.toString());

            Answer answer;
            while (set.next())
            {
                answer = new Answer();
                answer.setId(set.getInt("answer"));

                Question question = answer.getAnswerYesOf();

                if (question != null)
                    questions.add(question);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read yes answered questions of followup");
        }

        return questions;

    }

    public int deleteAnswersAfterCurrent(int questionId, int historyId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            StringBuilder SQL = new StringBuilder();
            SQL.append("DELETE FROM answers_to_histories WHERE id >= (SELECT id FROM answers_to_histories WHERE question = ")
                    .append(questionId)
                    .append(" AND history = ")
                    .append(historyId)
                    .append(") AND history = ")
                    .append(historyId)
                    .append(" AND followup is null;");


            return stmt.executeUpdate(SQL.toString());
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete answers after current to history");
        }

        return 0;
    }

    public void deleteFromFollowup(int followupId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            StringBuilder SQL = new StringBuilder();
            SQL.append("DELETE FROM answers_to_histories WHERE followup = ")
                    .append(followupId)
                    .append(";");

            stmt.execute(SQL.toString());
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete answer to history from followup");
        }

    }

    public void delete(int historyId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            StringBuilder SQL = new StringBuilder();
            SQL.append("DELETE FROM answers_to_histories WHERE history = ")
                    .append(historyId)
                    .append(" AND followup is null;");

            stmt.execute(SQL.toString());
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete answer to history");
        }

    }

}
