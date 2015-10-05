package database.mappers;

import models.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class LanguageConnector extends DatabaseConnector {

    public int create(Language language){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM languages WHERE id = " + language.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Language already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO languages (name) VALUES (" +
                        (language.getName() == null ? null : "'" + language.getName() + "'") + ");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                language.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Language");
        }

        return -1;
    }

    public Language update(Language language){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE languages SET " +
                    "name=" + (language.getName() == null ? null : "'" + language.getName() + "'") + "," +
                    " WHERE id =" + language.getId() + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update language");
        }

        return language;
    }

    public Language read(int languageId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM languages WHERE id='"+ languageId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                Language language = new Language();
                language.setId(set.getInt("id"));
                language.setName(set.getString("name"));

                return language;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Language");
        }

        return null;
    }

    public ArrayList<Language> readAll(){

        ArrayList<Language> languages = new ArrayList<Language>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM languages;";

            ResultSet set = stmt.executeQuery(SQL);

            Language language;
            while (set.next())
            {
                language = new Language();
                language.setId(set.getInt("id"));
                language.setName(set.getString("name"));

                languages.add(language);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all languages");
        }

        return languages;
    }

    public void delete(int languageId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM languages WHERE id ='" + languageId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Language");
        }

    }

}
