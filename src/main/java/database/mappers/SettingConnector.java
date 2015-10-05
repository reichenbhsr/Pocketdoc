package database.mappers;

import models.Setting;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class SettingConnector extends DatabaseConnector{

    public int create(Setting setting){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM settings WHERE id = " + setting.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Setting already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO settings (name, value) VALUES (" +
                        (setting.getName() == null ? null : "'" + setting.getName() + "'") + "," +
                        "'" + setting.getValue() + "');";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                setting.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Setting");
        }

        return -1;
    }

    public Setting update(Setting setting){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE settings SET " +
                    "name=" + (setting.getName() == null ? null : "'" + setting.getName() + "'") + "," +
                    " value='" + setting.getValue() +"',"+
                    " WHERE id =" + setting.getId() + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Setting");
        }

        return setting;
    }

    public Setting read(int settingId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM settings WHERE id='"+ settingId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                Setting setting = new Setting();
                setting.setId(set.getInt("id"));
                setting.setName(set.getString("name"));
                setting.setValue(set.getInt("value"));

                return setting;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Setting");
        }

        return null;
    }

    public ArrayList<Setting> readAll(){

        ArrayList<Setting> settings = new ArrayList<Setting>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM settings;";

            ResultSet set = stmt.executeQuery(SQL);

            Setting setting;
            while (set.next())
            {
                setting = new Setting();
                setting.setId(set.getInt("id"));
                setting.setName(set.getString("name"));
                setting.setValue(set.getInt("value"));

                settings.add(setting);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all Settings");
        }

        return settings;
    }

    public void delete(int settingId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM settings WHERE id ='" + settingId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Setting");
        }

    }

}
