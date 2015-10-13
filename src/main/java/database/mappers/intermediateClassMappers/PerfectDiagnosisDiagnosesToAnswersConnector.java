package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.Answer;
import models.Diagnosis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

/**
 * Created by Roman on 13.10.2015.
 */
public class PerfectDiagnosisDiagnosesToAnswersConnector extends DatabaseConnector {

    public int create(Answer answer, Diagnosis diagnosis){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM perfect_diagnosis_diagnoses_to_answers WHERE answer = " + answer.getId() + " AND diagnosis = " + diagnosis.getId() +";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Perfect diagnosis diagnoses to answers already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO perfect_diagnosis_diagnoses_to_answers (answer, diagnosis) VALUES("+ answer.getId() + ", " + diagnosis.getId() +");";

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
            System.out.println("SQL Error create perfect diagnosis to answer");
        }

        return -1;
    }

    public HashSet<Answer> readPerfectAnswersForDiagnosis(int diagnosisId){

        HashSet<Answer> answers = new HashSet<Answer>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT answers.* FROM answers, perfect_diagnosis_diagnoses_to_answers WHERE perfect_diagnosis_diagnoses_to_answers.answer = answers.id AND perfect_diagnosis_diagnoses_to_answers.diagnosis = " + diagnosisId + ";";

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
            System.out.println("SQL Error read perfect answers of diagnosis");
        }

        return answers;
    }

    public void delete(int diagnosisId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM perfect_diagnosis_diagnoses_to_answers WHERE diagnosis =" + diagnosisId + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete perfect answer to diagnosis");
        }

    }

}
