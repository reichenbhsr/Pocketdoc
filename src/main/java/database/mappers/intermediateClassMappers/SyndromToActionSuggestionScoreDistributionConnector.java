package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.intermediateClassModels.SyndromToActionSuggestionScoreDistribution;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Roman on 04.10.2015.
 */
public class SyndromToActionSuggestionScoreDistributionConnector extends DatabaseConnector {

    public int create(SyndromToActionSuggestionScoreDistribution stassd){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM score_distribution_syndroms_to_action_suggestions WHERE id = " + stassd.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Syndrom to action suggestions score distribution already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO score_distribution_syndroms_to_action_suggestions (score, syndrom, action_suggestion) VALUES (" +
                        "" + stassd.getScore() + "," +
                        "" + stassd.getSyndrom().getId() + "," +
                        "" + stassd.getActionSuggestion().getId() + ");";

                int rows = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
                int id = 0;
                if (rows > 0)
                {
                    ResultSet set = stmt.getGeneratedKeys();
                    if (set.next())
                        id = set.getInt(1);
                }
                stassd.setId(id);
                return id;
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Syndrom to action suggestions score distribution");
        }

        return -1;
    }

    public SyndromToActionSuggestionScoreDistribution update(SyndromToActionSuggestionScoreDistribution stassd){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE score_distribution_syndroms_to_action_suggestions SET " +
                    "score=" + stassd.getScore() + "," +
                    " syndrom=" + stassd.getSyndrom().getId() +","+
                    " action_suggestion=" + stassd.getActionSuggestion().getId() +" "+
                    " WHERE id =" + stassd.getId() + ";";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Syndrom to action suggestions score distribution");
        }

        return stassd;
    }

    public SyndromToActionSuggestionScoreDistribution read(int stassdId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM score_distribution_syndroms_to_action_suggestions WHERE id='"+ stassdId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                SyndromToActionSuggestionScoreDistribution stassd = new SyndromToActionSuggestionScoreDistribution();
                stassd.setId(set.getInt("id"));
                stassd.setScore(set.getInt("score"));
                stassd.setActionSuggestionId(set.getInt("action_suggestion"));
                stassd.setSyndromId(set.getInt("syndrom"));

                return stassd;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Syndrom to action suggestions score distribution");
        }

        return null;
    }

    public HashSet<SyndromToActionSuggestionScoreDistribution> readSetOfSyndromToActionSuggestionScoreDistribution(int actionSuggestionId){
        HashSet<SyndromToActionSuggestionScoreDistribution> set = new HashSet<SyndromToActionSuggestionScoreDistribution>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM score_distribution_syndroms_to_action_suggestions WHERE action_suggestion= " + actionSuggestionId + " ;";

            ResultSet res = stmt.executeQuery(SQL);

            SyndromToActionSuggestionScoreDistribution syndromToActionSuggestionScores;
            while (res.next())
            {
                syndromToActionSuggestionScores = new SyndromToActionSuggestionScoreDistribution();
                syndromToActionSuggestionScores.setId(res.getInt("id"));
                syndromToActionSuggestionScores.setSyndromId(res.getInt("syndrom"));
                syndromToActionSuggestionScores.setActionSuggestionId(res.getInt("action_suggestion"));
                syndromToActionSuggestionScores.setScore(res.getInt("score"));

                set.add(syndromToActionSuggestionScores);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read set of Syndrom To ActionSuggestion Scores");
        }

        return set;
    }

    public HashSet<SyndromToActionSuggestionScoreDistribution> readSetOfActionSuggestionToSyndromScoreDistribution(int syndromId){
        HashSet<SyndromToActionSuggestionScoreDistribution> set = new HashSet<SyndromToActionSuggestionScoreDistribution>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM score_distribution_syndroms_to_action_suggestions WHERE syndrom= " + syndromId + " ;";

            ResultSet res = stmt.executeQuery(SQL);

            SyndromToActionSuggestionScoreDistribution syndromToActionSuggestionScores;
            while (res.next())
            {
                syndromToActionSuggestionScores = new SyndromToActionSuggestionScoreDistribution();
                syndromToActionSuggestionScores.setId(res.getInt("id"));
                syndromToActionSuggestionScores.setSyndromId(res.getInt("syndrom"));
                syndromToActionSuggestionScores.setActionSuggestionId(res.getInt("action_suggestion"));
                syndromToActionSuggestionScores.setScore(res.getInt("score"));

                set.add(syndromToActionSuggestionScores);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read set of ActionSuggestion to Syndrom Scores");
        }

        return set;
    }

    public ArrayList<SyndromToActionSuggestionScoreDistribution> readAll(){

        ArrayList<SyndromToActionSuggestionScoreDistribution> stassds = new ArrayList<SyndromToActionSuggestionScoreDistribution>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM score_distribution_syndroms_to_action_suggestions;";

            ResultSet set = stmt.executeQuery(SQL);

            SyndromToActionSuggestionScoreDistribution stassd;
            while (set.next())
            {
                stassd = new SyndromToActionSuggestionScoreDistribution();
                stassd.setId(set.getInt("id"));
                stassd.setScore(set.getInt("score"));
                stassd.setActionSuggestionId(set.getInt("action_suggestion"));
                stassd.setSyndromId(set.getInt("syndrom"));

                stassds.add(stassd);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all Syndrom to action suggestions score distribution");
        }

        return stassds;
    }

    public void deleteSyndroms(int  syndromId){
        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM score_distribution_syndroms_to_action_suggestions WHERE syndrom ='" + syndromId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Syndrom to action suggestions score distribution syndroms");
        }
    }

    public void deleteFromActionSuggestion(int  actionSuggestionId){
        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM score_distribution_syndroms_to_action_suggestions WHERE acton_suggestion ='" + actionSuggestionId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Syndrom to action suggestions score distribution from action suggestion");
        }
    }

    public void delete(int stassdId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM score_distribution_syndroms_to_action_suggestions WHERE id ='" + stassdId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Syndrom to action suggestions score distribution");
        }

    }

}
