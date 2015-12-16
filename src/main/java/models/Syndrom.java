package models;

import database.mappers.intermediateClassMappers.AnswersToSyndromsConnector;
import database.mappers.intermediateClassMappers.SyndromToActionSuggestionScoreDistributionConnector;
import models.intermediateClassModels.SyndromToActionSuggestionScoreDistribution;

import java.util.Set;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle syndroms
 *
 * @author Oliver Frischknecht
 */

public class Syndrom {

    private int id;

    private String name;

    private Set<Answer> symptoms;

    private Set<SyndromToActionSuggestionScoreDistribution> scoreDistributions;

    public Syndrom() {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Set<Answer> getSymptoms() {

        if (symptoms == null){
            AnswersToSyndromsConnector asc = new AnswersToSyndromsConnector();
            symptoms = asc.readAnswersOfSyndrom(id);
        }

        return symptoms;
    }
    public void setSymptoms(Set<Answer> symptoms) {
        this.symptoms = symptoms;
    }

    public Set<SyndromToActionSuggestionScoreDistribution> getScoreDistributions() {

        if (scoreDistributions == null){
            SyndromToActionSuggestionScoreDistributionConnector stasc = new SyndromToActionSuggestionScoreDistributionConnector();
            scoreDistributions = stasc.readSetOfActionSuggestionToSyndromScoreDistribution(id);
        }

        return scoreDistributions;
    }
    public void setScoreDistributions(Set<SyndromToActionSuggestionScoreDistribution> scoreDistributions) {
        this.scoreDistributions = scoreDistributions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Syndrom) {
            return ((Syndrom) obj).getId() == id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
