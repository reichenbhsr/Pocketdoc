package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseMapper;
import database.mappers.DatabaseMapperException;
import database.mappers.DatabaseTransaction;
import models.intermediateClassModels.SyndromToActionSuggestionScoreDistribution;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "score_distribution_syndroms_to_action_suggestions"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class SyndromToActionSuggestionScoreDistributionMapper extends DatabaseTransaction implements DatabaseMapper<SyndromToActionSuggestionScoreDistribution> {
    @Override
    public int create(SyndromToActionSuggestionScoreDistribution syndromToActionSuggestionScoreDistribution) {
        Session session = beginTransaction();
        try {
            Integer id;
            List syndromToActionSuggestionScoreDistributions = session.createQuery("FROM SyndromToActionSuggestionScoreDistribution stassd WHERE stassd.id = :id")
                    .setInteger("id", syndromToActionSuggestionScoreDistribution.getId())
                    .list();
            if (syndromToActionSuggestionScoreDistributions.size() > 0) {
                throw new DatabaseMapperException("SyndromToActionSuggestionScoreDistribution already exists call SyndromToActionSuggestionScoreDistributionMapper.read()");
            } else {
                id = (Integer) session.save(syndromToActionSuggestionScoreDistribution);
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
    public SyndromToActionSuggestionScoreDistribution update(SyndromToActionSuggestionScoreDistribution syndromToActionSuggestionScoreDistribution) {
        Session session = beginTransaction();
        try {
            session.update(syndromToActionSuggestionScoreDistribution);
            commit();
            return syndromToActionSuggestionScoreDistribution;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public SyndromToActionSuggestionScoreDistribution read(int syndromToActionSuggestionScoreDistributionId) {
        Session session = beginTransaction();
        try {
            Integer id;
            List syndromToActionSuggestionScoreDistributions = session.createQuery("FROM SyndromToActionSuggestionScoreDistribution stassd WHERE stassd.id = :id")
                    .setInteger("id", syndromToActionSuggestionScoreDistributionId)
                    .list();
            if (syndromToActionSuggestionScoreDistributions.size() < 1) {
                throw new DatabaseMapperException("SyndromToActionSuggestionScoreDistribution doesn't exist");
            }
            commit();
            return (SyndromToActionSuggestionScoreDistribution) syndromToActionSuggestionScoreDistributions.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public SyndromToActionSuggestionScoreDistribution readAndFetch(int id) {
        return read(id);
    }

    @Override
    public ArrayList<SyndromToActionSuggestionScoreDistribution> readAll() {
        Session session = beginTransaction();
        try {
            List syndromToActionSuggestionScoreDistributions = getList(session);
            commit();
            return (ArrayList<SyndromToActionSuggestionScoreDistribution>) syndromToActionSuggestionScoreDistributions;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    private List getList(Session session) throws DatabaseMapperException {
        List syndromToActionSuggestionScoreDistributions = session.createQuery("FROM SyndromToActionSuggestionScoreDistribution").list();
        if (syndromToActionSuggestionScoreDistributions.size() < 1) {
            throw new DatabaseMapperException("There are currently no SyndromToActionSuggestionScoreDistributions!");
        }
        return syndromToActionSuggestionScoreDistributions;
    }

    @Override
    public void delete(int syndromToActionSuggestionScoreDistributionId) {
        Session session = beginTransaction();
        try {
            List syndromToActionSuggestionScoreDistributions = getList(session);
            session.delete((SyndromToActionSuggestionScoreDistribution) syndromToActionSuggestionScoreDistributions.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
