package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseMapper;
import database.mappers.DatabaseMapperException;
import database.mappers.DatabaseTransaction;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "score_distribution_answers_to_diagnoses"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 * <p>
 *
 * @author Oliver Frischknecht
 */
public class AnswerToDiagnosisScoreDistributionMapper extends DatabaseTransaction implements DatabaseMapper<AnswerToDiagnosisScoreDistribution> {
    @Override
    public int create(AnswerToDiagnosisScoreDistribution answerToDiagnosisScoreDistribution) {
        Session session = beginTransaction();
        try {
            Integer id;
            List answerToDiagnosisScoreDistributions = session.createQuery("FROM AnswerToDiagnosisScoreDistribution atdsd WHERE atdsd.id = :id")
                    .setInteger("id", answerToDiagnosisScoreDistribution.getId())
                    .list();
            if (answerToDiagnosisScoreDistributions.size() > 0) {
                throw new DatabaseMapperException("AnswerToDiagnosisScoreDistribution already exists call AnswerToDiagnosisScoreDistributionMapper.read()");
            } else {
                id = (Integer) session.save(answerToDiagnosisScoreDistribution);
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
    public AnswerToDiagnosisScoreDistribution update(AnswerToDiagnosisScoreDistribution answerToDiagnosisScoreDistribution) {
        Session session = beginTransaction();
        try {
            session.update(answerToDiagnosisScoreDistribution);
            commit();
            return answerToDiagnosisScoreDistribution;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public AnswerToDiagnosisScoreDistribution read(int answerToDiagnosisScoreDistributionId) {
        Session session = beginTransaction();
        try {
            List answerToDiagnosisScoreDistributions = getList(answerToDiagnosisScoreDistributionId, session);
            commit();
            return (AnswerToDiagnosisScoreDistribution) answerToDiagnosisScoreDistributions.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public AnswerToDiagnosisScoreDistribution readAndFetch(int id) {
        return read(id);
    }

    private List getList(int answerToDiagnosisScoreDistributionId, Session session) throws DatabaseMapperException {
        List answerToDiagnosisScoreDistributions = session.createQuery("FROM AnswerToDiagnosisScoreDistribution atdsd WHERE atdsd.id = :id")
                .setInteger("id", answerToDiagnosisScoreDistributionId)
                .list();
        if (answerToDiagnosisScoreDistributions.size() < 1) {
            throw new DatabaseMapperException("AnswerToDiagnosisScoreDistribution doesn't exist");
        }
        return answerToDiagnosisScoreDistributions;
    }

    @Override
    public ArrayList<AnswerToDiagnosisScoreDistribution> readAll() {
        Session session = beginTransaction();
        try {
            Integer id;
            List answerToDiagnosisScoreDistributions = session.createQuery("FROM AnswerToDiagnosisScoreDistribution").list();
            if (answerToDiagnosisScoreDistributions.size() < 1) {
                throw new DatabaseMapperException("There are currently no AnswerToDiagnosisScoreDistributions!");
            }
            commit();
            return (ArrayList<AnswerToDiagnosisScoreDistribution>) answerToDiagnosisScoreDistributions;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int answerToDiagnosisScoreDistributionId) {
        Session session = beginTransaction();
        try {
            List answerToDiagnosisScoreDistributions = getList(answerToDiagnosisScoreDistributionId, session);
            session.delete((AnswerToDiagnosisScoreDistribution) answerToDiagnosisScoreDistributions.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
