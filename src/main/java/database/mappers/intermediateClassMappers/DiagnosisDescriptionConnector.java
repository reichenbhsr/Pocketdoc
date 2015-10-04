package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.intermediateClassModels.DiagnosisDescription;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class DiagnosisDescriptionConnector extends DatabaseConnector{

    public int create(DiagnosisDescription diagnosisDescription){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM diagnosis_descripitons WHERE id = " + diagnosisDescription.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Diagnosis description already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO Questions (description, language, diagnosis) VALUES (" +
                        "'" + diagnosisDescription.getDescription() + "'," +
                        "'" + diagnosisDescription.getLanguage().getId() + "'," +
                        "'" + diagnosisDescription.getDiagnosis().getId() + "');";

                return stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
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
            String SQL = "UPDATE diagnosis_descripitons SET " +
                    "description='" + diagnosisDescription.getDescription() + "'," +
                    " language='" + diagnosisDescription.getLanguage().getId() +"',"+
                    " diagnosis='" + diagnosisDescription.getDiagnosis().getId() +"'"+
                    " WHERE id ='" + diagnosisDescription.getId() + "';";

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
            String SQL = "SELECT * FROM diagnoses_descriptions WHERE id='"+ diagnosisDescriptionId + "';";

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

    public ArrayList<DiagnosisDescription> readAll(){

        ArrayList<DiagnosisDescription> diagnosisDescriptions = new ArrayList<DiagnosisDescription>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM diagnoses_descriptions;";

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
            String SQL = "DELETE FROM diagnoses_descriptions WHERE id ='" + diagnosisDescriptionId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Diagnosis description");
        }

    }

}
