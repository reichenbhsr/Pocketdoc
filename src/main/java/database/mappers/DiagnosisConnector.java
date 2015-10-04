package database.mappers;

import models.Diagnosis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class DiagnosisConnector extends DatabaseConnector {

    public int create(Diagnosis diagnosis){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM diagnosis WHERE id = " + diagnosis.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Diagnosis already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO diagnosis (name) VALUES (" +
                        "'" + diagnosis.getName() + "');";

                return stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Diagnosis");
        }

        return -1;
    }

    public Diagnosis update(Diagnosis diagnosis){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE diagnosis SET " +
                    "name='" + diagnosis.getName() + "'," +
                    " WHERE id ='" + diagnosis.getId() + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Diagnosis");
        }

        return diagnosis;
    }

    public Diagnosis read(int diagnosisId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM diagnoses WHERE id='"+ diagnosisId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(set.getInt("id"));
                diagnosis.setName(set.getString("name"));

                return diagnosis;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Diagnosis");
        }

        return null;
    }

    public ArrayList<Diagnosis> readAll(){

        ArrayList<Diagnosis> diagnoses = new ArrayList<Diagnosis>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM diagnoses;";

            ResultSet set = stmt.executeQuery(SQL);

            Diagnosis diagnosis;
            while (set.next())
            {
                diagnosis = new Diagnosis();
                diagnosis.setId(set.getInt("id"));
                diagnosis.setName(set.getString("name"));

                diagnoses.add(diagnosis);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all diagnoses");
        }

        return diagnoses;
    }

    public void delete(int diagnosisId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM diagnoses WHERE id ='" + diagnosisId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete diagnosis");
        }

    }

}
