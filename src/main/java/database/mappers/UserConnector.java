package database.mappers;

import models.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Roman on 02.10.2015.
 */
public class UserConnector extends DatabaseConnector {

    public int createUser(User user)
    {

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM User where id = " + user.getId() + ";";
            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
                System.out.println("User already exists!");
            else{
                // Create User
                stmt = connection.createStatement();
                SQL = "INSERT INTO Users (name, password, history) VALUES (" +
                        "'" + user.getName() + "'," +
                        "'" + user.getPassword() + "'," +
                        "'" + user.getHistory().getId() + "');";

                return stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error createUser");
        }

        return -1;
    }

    public User updateUser(User user)
    {
        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE Users SET " +
                            " name='" + user.getName() + "'," +
                            " password='" + user.getPassword() +"',"+
                            " history='" + user.getHistory().getId() +"' "+
                            " WHERE id ='" + user.getId() + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error updateUser");
        }

        return user;
    }

    public User getUser(int userId)
    {
        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM Users WHERE id='"+ userId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                User user = new User();
                user.setId(set.getInt("id"));
                user.setName(set.getString("name"));
                user.setPassword(set.getString("password"));
                user.setHistoryId(set.getInt("history"));

                return user;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error getUser");
        }

        return null;
    }

    public ArrayList<User>  readAll()
    {

        ArrayList<User> users = new ArrayList<User>();

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM Users;";

            ResultSet set = stmt.executeQuery(SQL);

            User user;
            while (set.next())
            {
                user = new User();
                user.setId(set.getInt("id"));
                user.setName(set.getString("name"));
                user.setPassword(set.getString("password"));
                user.setHistoryId(set.getInt("history"));

                users.add(user);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error readAll User");
        }

        return users;
    }

    public void delete(int userId)
    {
        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM Users WHERE id ='" + userId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete User");
        }
    }

}
