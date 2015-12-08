package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.intermediateClassModels.DiagnosisDesignation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Roman on 04.10.2015.
 */
public class DiagnosisDesignationConnector extends DatabaseConnector{

    public int create(DiagnosisDesignation diagnosisDesignation){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM diagnosis_designations WHERE id = " + diagnosisDesignation.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Diagnosis designation already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO diagnosis_designations (designation, language, diagnosis) VALUES (" +
                        "" + (diagnosisDesignation.getDesignation() == null ? null : "'" + diagnosisDesignation.getDesignation() + "'") + "," +
                        "" + diagnosisDesignation.getLanguage().getId() + "," +
                        "" + diagnosisDesignation.getDiagnosis().getId() + ");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                diagnosisDesignation.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Diagnosis designation");
        }

        return -1;
    }

    public DiagnosisDesignation update(DiagnosisDesignation diagnosisDesignation){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE diagnosis_designations SET " +
                    "designation=" + (diagnosisDesignation.getDesignation() == null ? null : "'" + diagnosisDesignation.getDesignation() + "'") + "," +
                    " language=" + diagnosisDesignation.getLanguage().getId() +","+
                    " diagnosis=" + diagnosisDesignation.getDiagnosis().getId() +""+
                    " WHERE id =" + diagnosisDesignation.getId() + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Diagnosis designation");
        }

        return diagnosisDesignation;
    }

    public DiagnosisDesignation read(int diagnosisDesignationId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM diagnosis_designations WHERE id='"+ diagnosisDesignationId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                DiagnosisDesignation diagnosisDesignation = new DiagnosisDesignation();
                diagnosisDesignation.setId(set.getInt("id"));
                diagnosisDesignation.setDesignation(set.getString("designation"));
                diagnosisDesignation.setDiagnosisId(set.getInt("diagnosis"));
                diagnosisDesignation.setLanguageId(set.getInt("language"));

                return diagnosisDesignation;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Diagnosis designation");
        }

        return null;
    }

    public HashSet<DiagnosisDesignation> readSetOfDiagnosisDesignations(int diagnosisId){
        HashSet<DiagnosisDesignation> set = new HashSet<DiagnosisDesignation>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM diagnosis_designations WHERE diagnosis= " + diagnosisId + " ;";

            ResultSet res = stmt.executeQuery(SQL);

            DiagnosisDesignation designation;
            while (res.next())
            {
                designation = new DiagnosisDesignation();
                designation.setId(res.getInt("id"));
                designation.setLanguageId(res.getInt("language"));
                designation.setDiagnosisId(res.getInt("diagnosis"));
                designation.setDesignation(res.getString("designation"));

                set.add(designation);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read set of diagnosis designations");
        }

        return set;
    }

    public ArrayList<DiagnosisDesignation> readAll(){

        ArrayList<DiagnosisDesignation> diagnosisDesignations = new ArrayList<DiagnosisDesignation>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM diagnosis_designations;";

            ResultSet set = stmt.executeQuery(SQL);

            DiagnosisDesignation diagnosisDesignation;
            while (set.next())
            {
                diagnosisDesignation = new DiagnosisDesignation();
                diagnosisDesignation.setId(set.getInt("id"));
                diagnosisDesignation.setDesignation(set.getString("designation"));
                diagnosisDesignation.setDiagnosisId(set.getInt("diagnosis"));
                diagnosisDesignation.setLanguageId(set.getInt("language"));

                diagnosisDesignations.add(diagnosisDesignation);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all Diagnosis designations");
        }

        return diagnosisDesignations;
    }

    public void delete(int diagnosisDesignationId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM diagnosis_designations WHERE id ='" + diagnosisDesignationId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Diagnosis designation");
        }

    }

    public void deleteFromDiagnosis(int diagnosisId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM diagnosis_designations WHERE diagnosis ='" + diagnosisId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Diagnosis designations from diagnosis");
        }

    }

    public void deleteFromLanguage(int languageId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM diagnosis_designations WHERE langauge ='" + languageId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Diagnosis designations from language");
        }

    }

}
