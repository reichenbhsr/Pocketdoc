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

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/pocketdoc", "postgres",
                    "password");

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
