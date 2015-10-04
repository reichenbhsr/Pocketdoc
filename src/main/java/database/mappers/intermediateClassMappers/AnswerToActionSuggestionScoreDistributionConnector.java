package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseConnector;
import models.intermediateClassModels.AnswerToActionSuggestionScoreDistribution;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Roman on 04.10.2015.
 */
public class AnswerToActionSuggestionScoreDistributionConnector extends DatabaseConnector {

    public int create(AnswerToActionSuggestionScoreDistribution atassd){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT id FROM score_distribution_answers_to_action_suggestions WHERE id = " + atassd.getId() + ";";
            ResultSet res = stmt.executeQuery(SQL);

            if (res.next())
                System.out.println("Answer to action suggestion score distribution already exists!");
            else {

                stmt = connection.createStatement();
                SQL = "INSERT INTO score_distribution_answers_to_action_suggestions (answer, action_suggestion, score) VALUES (" +
                        "'" + atassd.getAnswer().getId() + "'," +
                        "'" + atassd.getActionSuggestion().getId() + "'," +
                        "'" + atassd.getScore() + "');";

                return stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
            }
        }
        catch (SQLException ex){
            System.out.println("SQL Error create Answer to action suggestion score distribution");
        }

        return -1;
    }

    public AnswerToActionSuggestionScoreDistribution update(AnswerToActionSuggestionScoreDistribution atassd){

        try {
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "UPDATE score_distribution_answers_to_action_suggestions SET " +
                    "answer='" + atassd.getAnswer().getId() + "'," +
                    " action_suggestion='" + atassd.getActionSuggestion().getId() +"',"+
                    " score='" + atassd.getScore() +"',"+
                    " WHERE id ='" + atassd.getId() + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error update Answer to action suggestion score distribution");
        }

        return atassd;
    }

    public AnswerToActionSuggestionScoreDistribution read(int answerToActionSuggestionScoreDistributionId){

        try{
            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM score_distribution_answers_to_action_suggestions WHERE id='"+ answerToActionSuggestionScoreDistributionId + "';";

            ResultSet set = stmt.executeQuery(SQL);

            if (set.next())
            {
                AnswerToActionSuggestionScoreDistribution atassd = new AnswerToActionSuggestionScoreDistribution();
                atassd.setId(set.getInt("id"));
                atassd.setScore(set.getInt("score"));
                atassd.setAnswerId(set.getInt("answer"));
                atassd.setActionSuggestionId(set.getInt("action_suggestion"));

                return atassd;
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read Answer to action suggestion score distribution");
        }

        return null;
    }

    public ArrayList<AnswerToActionSuggestionScoreDistribution> readAll(){

        ArrayList<AnswerToActionSuggestionScoreDistribution> atassds = new ArrayList<AnswerToActionSuggestionScoreDistribution>();

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM score_distribution_answers_to_action_suggestions;";

            ResultSet set = stmt.executeQuery(SQL);

            AnswerToActionSuggestionScoreDistribution atassd;
            while (set.next())
            {
                atassd = new AnswerToActionSuggestionScoreDistribution();
                atassd.setId(set.getInt("id"));
                atassd.setScore(set.getInt("score"));
                atassd.setAnswerId(set.getInt("answer"));
                atassd.setActionSuggestionId(set.getInt("action_suggestion"));

                atassds.add(atassd);
            }

        }
        catch (SQLException ex){
            System.out.println("SQL Error read all Answer to action suggestion score distributions");
        }

        return atassds;
    }

    public void delete(int atassdId){

        try{

            establishConnection();

            Statement stmt = connection.createStatement();
            String SQL = "DELETE FROM score_distribution_answers_to_action_suggestions WHERE id ='" + atassdId + "';";

            stmt.execute(SQL);
        }
        catch (SQLException ex){
            System.out.println("SQL Error delete Answer to action suggestion score distributions");
        }

    }

}
