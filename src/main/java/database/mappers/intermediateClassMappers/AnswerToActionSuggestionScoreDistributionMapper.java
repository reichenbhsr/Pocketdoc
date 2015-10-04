package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseMapper;
import database.mappers.DatabaseMapperException;
import database.mappers.DatabaseTransaction;
import models.intermediateClassModels.AnswerToActionSuggestionScoreDistribution;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "score_distribution_answers_to_action_suggestions"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class AnswerToActionSuggestionScoreDistributionMapper extends DatabaseTransaction implements DatabaseMapper<AnswerToActionSuggestionScoreDistribution> {
    @Override
    public int create(AnswerToActionSuggestionScoreDistribution answerToActionSuggestionScoreDistribution) {
        Session session = beginTransaction();
        try {
            Integer id;
            List answerToActionSuggestionsScoreDistributions = session.createQuery("FROM AnswerToActionSuggestionScoreDistribution atassd WHERE atassd.id = :id")
                    .setInteger("id", answerToActionSuggestionScoreDistribution.getId())
                    .list();
            if (answerToActionSuggestionsScoreDistributions.size() > 0) {
                throw new DatabaseMapperException("AnswerToActionSuggestionScoreDistribution already exists call AnswerToActionSuggestionScoreDistributionMapper.read()");
            } else {
                id = (Integer) session.save(answerToActionSuggestionScoreDistribution);
            }
            commit();
            return id;

        } catch (Exception e) {
            handleError(e);
            return -1;
        } finally {
            endTransaction();
        }
    }

    @Override
    public AnswerToActionSuggestionScoreDistribution update(AnswerToActionSuggestionScoreDistribution answerToActionSuggestionScoreDistribution) {
        Session session = beginTransaction();
        try {
            session.update(answerToActionSuggestionScoreDistribution);
            commit();
            return answerToActionSuggestionScoreDistribution;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public AnswerToActionSuggestionScoreDistribution read(int answerToActionSuggestionScoreDistributionId) {
        Session session = beginTransaction();
        try {
            List answerToActionSuggestionsScoreDistributions = getList(answerToActionSuggestionScoreDistributionId, session);
            commit();
            return (AnswerToActionSuggestionScoreDistribution) answerToActionSuggestionsScoreDistributions.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public AnswerToActionSuggestionScoreDistribution readAndFetch(int id) {
        return read(id);
    }

    private List getList(int answerToActionSuggestionScoreDistributionId, Session session) throws DatabaseMapperException {
        List answerToActionSuggestionsScoreDistributions = session.createQuery("FROM AnswerToActionSuggestionScoreDistribution atassd WHERE atassd.id = :id")
                .setInteger("id", answerToActionSuggestionScoreDistributionId)
                .list();
        if (answerToActionSuggestionsScoreDistributions.size() < 1) {
            throw new DatabaseMapperException("AnswerToActionSuggestionScoreDistribution doesn't exist");
        }
        return answerToActionSuggestionsScoreDistributions;
    }

    @Override
    public ArrayList<AnswerToActionSuggestionScoreDistribution> readAll() {
        Session session = beginTransaction();
        try {
            Integer id;
            List answerToActionSuggestionsScoreDistributions = session.createQuery("FROM AnswerToActionSuggestionScoreDistribution").list();
            if (answerToActionSuggestionsScoreDistributions.size() < 1) {
                throw new DatabaseMapperException("There are currently no AnswerToActionSuggestionScoreDistributions!");
            }
            commit();
            return (ArrayList<AnswerToActionSuggestionScoreDistribution>) answerToActionSuggestionsScoreDistributions;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int answerToActionSuggestionScoreDistributionId) {
        Session session = beginTransaction();
        try {
            List answerToActionSuggestionsScoreDistributions = getList(answerToActionSuggestionScoreDistributionId, session);
            session.delete((AnswerToActionSuggestionScoreDistribution) answerToActionSuggestionsScoreDistributions.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
