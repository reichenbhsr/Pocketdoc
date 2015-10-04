package database.mappers;

import models.Syndrom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class SyndromConnector extends DatabaseConnector {

    public int create(Syndrom syndrom){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM syndroms WHERE id = " + syndrom.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Syndrom already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO syndroms (name) VALUES (" +
                        "'" + syndrom.getName() +  "');";

                return stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Syndrom");
        }

        return -1;
    }

    public Syndrom update(Syndrom syndrom){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE syndroms SET " +
                    "name='" + syndrom.getName() + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Syndrom");
        }

        return syndrom;
    }

    public Syndrom read(int syndromId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM syndroms WHERE id='"+ syndromId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                Syndrom syndrom = new Syndrom();
                syndrom.setId(set.getInt("id"));
                syndrom.setName(set.getString("name"));

                return syndrom;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Syndrom");
        }

        return null;
    }

    public ArrayList<Syndrom> readAll(){

        ArrayList<Syndrom> syndroms = new ArrayList<Syndrom>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM syndroms;";

            ResultSet set = stmt.executeQuery(SQL);

            Syndrom syndrom;
            while (set.next())
            {
                syndrom = new Syndrom();
                syndrom.setId(set.getInt("id"));
                syndrom.setName(set.getString("name"));

                syndroms.add(syndrom);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all syndroms");
        }

        return syndroms;
    }

    public void delete(int syndromId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM syndroms WHERE id ='" + syndromId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Syndrom");
        }

    }

}
