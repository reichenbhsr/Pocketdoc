package database.mappers;

import models.Question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class QuestionConnector extends DatabaseConnector{

    public int create(Question question){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM Questions WHERE id = " + question.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Question already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO Questions (name, is_symptom, answer_yes, answer_no, depends_on) VALUES (" +
                        "'" + question.getName() + "'," +
                        "'" + question.isSymptom() + "'," +
                        "'" + question.getAnswerYes().getId() + "'," +
                        "'" + question.getAnswerNo().getId() + "'," +
                        "'" + (question.getDependsOn() != null ? question.getDependsOn().getId() : "") + "');";

                return stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Question");
        }

        return -1;
    }

    public Question update(Question question){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE Questions SET " +
                            "name='" + question.getName() + "'," +
                            " is_symptom='" + question.isSymptom() +"',"+
                            " answer_yes='" + question.getAnswerYes().getId() +"',"+
                            " answer_no='" + question.getAnswerNo().getId() +"',"+
                            " depends_on='" + question.getDependsOn().getId() +"' "+
                            " WHERE id ='" + question.getId() + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Question");
        }

        return question;
    }

    public Question read(int questionId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM Questions WHERE id='"+ questionId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                Question question = new Question();
                question.setId(set.getInt("id"));
                question.setName(set.getString("name"));
                question.setSymptom(set.getBoolean("is_symptom"));
                question.setAnswerNoId(set.getInt("answer_no"));
                question.setAnswerYesId(set.getInt("answer_yes"));
                question.setDependsOnId(set.getInt("depends_on"));

                return question;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Question");
        }

        return null;
    }

    public ArrayList<Question> readAll(){

        ArrayList<Question> questions = new ArrayList<Question>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM Questions;";

            ResultSet set = stmt.executeQuery(SQL);

            Question question;
            while (set.next())
            {
                question = new Question();
                question.setId(set.getInt("id"));
                question.setName(set.getString("name"));
                question.setSymptom(set.getBoolean("is_symptom"));
                question.setAnswerNoId(set.getInt("answer_no"));
                question.setAnswerYesId(set.getInt("answer_yes"));
                question.setDependsOnId(set.getInt("depends_on"));

                questions.add(question);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all questions");
        }

        return questions;
    }

    public void delete(int questionId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM Questions WHERE id ='" + questionId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Question");
        }

    }
}


