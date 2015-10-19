package database.mappers;

import models.Question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

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
                SQL = "INSERT INTO Questions (name, is_symptom, answer_yes, answer_no, force_dependent_asking, depends_on) VALUES (" +
                        (question.getName() == null ? null : "'" + question.getName() + "'") + "," +
                        "'" + question.isSymptom() + "'," +
                        "" + question.getAnswerYes().getId() + "," +
                        "" + question.getAnswerNo().getId() + "," +
                        "" + question.getForceDependentAsking() + "," +
                        "" + (question.getDependsOn() != null ? question.getDependsOn().getId() : null) + ");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                question.setId(id);
                return id;
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
                            "name=" + (question.getName() == null ? null : "'" + question.getName() + "'") + "," +
                            " is_symptom='" + question.isSymptom() +"',"+
                            " answer_yes=" + question.getAnswerYes().getId() +","+
                            " answer_no=" + question.getAnswerNo().getId() +","+
                            " force_dependent_asking=" + question.getForceDependentAsking() +","+
                            " depends_on=" + (question.getDependsOn() != null ? question.getDependsOn().getId() : null) +" "+
                            " WHERE id =" + question.getId() + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Question");
        }

        return question;
    }

    public HashSet<Question> readDependendQuestions(int answerId){

        HashSet<Question> dependentQuestions = new HashSet<Question>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM Questions WHERE depends_on='" + answerId + "';";

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
                question.setForceDependentAsking(set.getBoolean("force_dependent_asking"));

                dependentQuestions.add(question);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read dependent questions");
        }

        return dependentQuestions;

    }

    public Question readAnswerYesOf(int answerId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM Questions WHERE answer_yes='"+ answerId + "';";

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
                question.setForceDependentAsking(set.getBoolean("force_dependent_asking"));

                return question;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Question");
        }

        return null;

    }

    public Question readAnswerNoOf(int answerId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM Questions WHERE answer_no='"+ answerId + "';";

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
                question.setForceDependentAsking(set.getBoolean("force_dependent_asking"));

                return question;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Question");
        }

        return null;

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
                question.setForceDependentAsking(set.getBoolean("force_dependent_asking"));

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
                question.setForceDependentAsking(set.getBoolean("force_dependent_asking"));

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


