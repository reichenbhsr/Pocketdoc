package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Roman on 04.10.2015.
 */
public class AnswerToDiagnosisScoreDistributionConnector extends DatabaseConnector {

    public int create(AnswerToDiagnosisScoreDistribution atdsd){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM score_distribution_answers_to_diagnoses WHERE id = " + atdsd.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Answer to diagnosis score distribution already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO score_distribution_answers_to_diagnoses (score, answer, diagnosis) VALUES (" +
                        "" + atdsd.getScore() + "," +
                        "" + atdsd.getAnswer().getId() + "," +
                        "" + atdsd.getDiagnosis().getId() + ");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                atdsd.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Answer to diagnosis score distribution");
        }

        return -1;
    }

    public AnswerToDiagnosisScoreDistribution update(AnswerToDiagnosisScoreDistribution atdsd){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE score_distribution_answers_to_diagnoses SET " +
                    "score=" + atdsd.getScore() + "," +
                    " answer=" + atdsd.getAnswer().getId() +","+
                    " diagnosis=" + atdsd.getDiagnosis().getId() +" "+
                    " WHERE id =" + atdsd.getId() + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Answer to diagnosis score distribution");
        }

        return atdsd;
    }

    public AnswerToDiagnosisScoreDistribution read(int atdsdId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM score_distribution_answers_to_diagnoses WHERE id='"+ atdsdId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                AnswerToDiagnosisScoreDistribution atdsd = new AnswerToDiagnosisScoreDistribution();
                atdsd.setId(set.getInt("id"));
                atdsd.setScore(set.getInt("score"));
                atdsd.setDiagnosisId(set.getInt("diagnosis"));
                atdsd.setAnswerId(set.getInt("answer"));

                return atdsd;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Answer to diagnosis score distribution");
        }

        return null;
    }

    public ArrayList<AnswerToDiagnosisScoreDistribution> readAll(){

        ArrayList<AnswerToDiagnosisScoreDistribution> atdsds = new ArrayList<AnswerToDiagnosisScoreDistribution>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM score_distribution_answers_to_diagnoses;";

            ResultSet set = stmt.executeQuery(SQL);

            AnswerToDiagnosisScoreDistribution atdsd;
            while (set.next())
            {
                atdsd = new AnswerToDiagnosisScoreDistribution();
                atdsd.setId(set.getInt("id"));
                atdsd.setScore(set.getInt("score"));
                atdsd.setDiagnosisId(set.getInt("diagnosis"));
                atdsd.setAnswerId(set.getInt("answer"));

                atdsds.add(atdsd);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all Answer to diagnosis score distribution");
        }

        return atdsds;
    }

    public HashSet<AnswerToDiagnosisScoreDistribution> readSetOfAnswer(int answerId){

        HashSet<AnswerToDiagnosisScoreDistribution> set = new HashSet<AnswerToDiagnosisScoreDistribution>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM score_distribution_answers_to_diagnoses WHERE answer = " + answerId + " ;";

            ResultSet res = stmt.executeQuery(SQL);

            AnswerToDiagnosisScoreDistribution atdsd;
            while (res.next())
            {
                atdsd = new AnswerToDiagnosisScoreDistribution();
                atdsd.setId(res.getInt("id"));
                atdsd.setScore(res.getInt("score"));
                atdsd.setDiagnosisId(res.getInt("diagnosis"));
                atdsd.setAnswerId(res.getInt("answer"));

                set.add(atdsd);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all Answer to diagnosis score distribution");
        }

        return set;
    }

    public void delete(int atdsdId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM score_distribution_answers_to_diagnoses WHERE id ='" + atdsdId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Answer to diagnosis score distribution");
        }

    }

}
