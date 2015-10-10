package database.mappers;

import models.Question;
import models.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class DatabaseConnector {

    protected static Connection connection;

    public static void establishConnection()
    {
        try{
            if (connection != null && !connection.isClosed())
                return;
        }
        catch (SQLException ex){
            System.out.println("Connection Check Failed! ");
        }

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;

        }

        try {

            // Heroku connection from Localhost
            //connection = DriverManager.getConnection("jdbc:postgresql://ec2-79-125-21-70.eu-west-1.compute.amazonaws.com:5432/d3sd306179mt7j?user=zcdhqlrumnxkeo&password=vFddXxjvToPRJwSesubS3Mux4U&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");

            // Heroku connection
            connection = DriverManager.getConnection("jdbc:postgresql://ec2-79-125-21-70.eu-west-1.compute.amazonaws.com:5432/d3sd306179mt7j?user=zcdhqlrumnxkeo&password=vFddXxjvToPRJwSesubS3Mux4U");

            // Localhost Connection
            //connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pocketdoc", "postgres", "password");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (connection != null)
            System.out.println("Connection Established!");

    }

    public ArrayList<Question> loadDataConstruct()
    {

        ArrayList<Question> questions = new ArrayList<Question>();

        try{



        }
        catch (Exception ex){ // SQLException
            System.out.println("SQL Error load Data Construct");
        }

        return questions;

    }
}
