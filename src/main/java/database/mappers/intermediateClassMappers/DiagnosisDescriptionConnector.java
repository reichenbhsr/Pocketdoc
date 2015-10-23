package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.intermediateClassModels.DiagnosisDescription;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Roman on 04.10.2015.
 */
public class DiagnosisDescriptionConnector extends DatabaseConnector{

    public int create(DiagnosisDescription diagnosisDescription){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM diagnosis_descriptions WHERE id = " + diagnosisDescription.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Diagnosis description already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO diagnosis_descriptions (description, language, diagnosis) VALUES (" +
                        "" + (diagnosisDescription.getDescription() == null ? null : "'" + diagnosisDescription.getDescription() + "'") + "," +
                        "" + diagnosisDescription.getLanguage().getId() + "," +
                        "" + diagnosisDescription.getDiagnosis().getId() + ");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                diagnosisDescription.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Diagnosis description");
        }

        return -1;
    }

    public DiagnosisDescription update(DiagnosisDescription diagnosisDescription){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE diagnosis_descriptions SET " +
                    "description=" + (diagnosisDescription.getDescription() == null ? null : "'" + diagnosisDescription.getDescription() + "'") + "," +
                    " language=" + diagnosisDescription.getLanguage().getId() +","+
                    " diagnosis=" + diagnosisDescription.getDiagnosis().getId() +""+
                    " WHERE id =" + diagnosisDescription.getId() + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Diagnosis description");
        }

        return diagnosisDescription;
    }

    public DiagnosisDescription read(int diagnosisDescriptionId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM diagnosis_descriptions WHERE id='"+ diagnosisDescriptionId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                DiagnosisDescription diagnosisDescription = new DiagnosisDescription();
                diagnosisDescription.setId(set.getInt("id"));
                diagnosisDescription.setDescription(set.getString("description"));
                diagnosisDescription.setDiagnosisId(set.getInt("diagnosis"));
                diagnosisDescription.setLanguageId(set.getInt("language"));

                return diagnosisDescription;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Diagnosis description");
        }

        return null;
    }

    public HashSet<DiagnosisDescription> readSetOfDiagnosisDescriptions(int diagnosisId){
        HashSet<DiagnosisDescription> set = new HashSet<DiagnosisDescription>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM diagnosis_descriptions WHERE diagnosis= " + diagnosisId + " ;";

            ResultSet res = stmt.executeQuery(SQL);

            DiagnosisDescription description;
            while (res.next())
            {
                description = new DiagnosisDescription();
                description.setId(res.getInt("id"));
                description.setLanguageId(res.getInt("language"));
                description.setDiagnosisId(res.getInt("diagnosis"));
                description.setDescription(res.getString("description"));

                set.add(description);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read set of diagnosis descriptions");
        }

        return set;
    }

    public ArrayList<DiagnosisDescription> readAll(){

        ArrayList<DiagnosisDescription> diagnosisDescriptions = new ArrayList<DiagnosisDescription>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM diagnosis_descriptions;";

            ResultSet set = stmt.executeQuery(SQL);

            DiagnosisDescription diagnosisDescription;
            while (set.next())
            {
                diagnosisDescription = new DiagnosisDescription();
                diagnosisDescription.setId(set.getInt("id"));
                diagnosisDescription.setDescription(set.getString("description"));
                diagnosisDescription.setDiagnosisId(set.getInt("diagnosis"));
                diagnosisDescription.setLanguageId(set.getInt("language"));

                diagnosisDescriptions.add(diagnosisDescription);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all Diagnosis descriptions");
        }

        return diagnosisDescriptions;
    }

    public void delete(int diagnosisDescriptionId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM diagnosis_descriptions WHERE id ='" + diagnosisDescriptionId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Diagnosis description");
        }

    }

    public void deleteFromDiagnosis(int diagnosisId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM diagnosis_descriptions WHERE diagnosis ='" + diagnosisId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Diagnosis description from diagnosis");
        }

    }

    public void deleteFromLanguage(int languageId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM diagnosis_descriptions WHERE langauge ='" + languageId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Diagnosis description from language");
        }

    }

}
