package database.mappers;

import models.Diagnosis;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "diagnoses"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class DiagnosisMapper extends DatabaseTransaction implements DatabaseMapper<Diagnosis> {

    @Override
    public int create(Diagnosis diagnosis) {
        Session session = beginTransaction();
        try {
            Integer id;
            List diagnoses = session.createQuery("FROM Diagnosis d WHERE d.name = :name OR d.id = :id")
                    .setString("name", diagnosis.getName())
                    .setInteger("id", diagnosis.getId())
                    .list();
            if (diagnoses.size() > 0) {
                throw new DatabaseMapperException("Diagnosis already exists call DiagnosisMapper.read()");
            } else {
                id = (Integer) session.save(diagnosis);
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
    public Diagnosis update(Diagnosis diagnosis) {

        Session session = beginTransaction();
        try {
            session.update(diagnosis);
            commit();
            return diagnosis;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Diagnosis read(int diagnosisId) {
        Session session = beginTransaction();
        try {
            List diagnoses = getList(diagnosisId, session);
            commit();
            return (Diagnosis) diagnoses.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Diagnosis readAndFetch(int id) {
        Session session = beginTransaction();
        try {
            List diagnoses = getList(id, session);
            commit();

            final Diagnosis diagnosis = (Diagnosis) diagnoses.get(0);
            Hibernate.initialize(diagnosis.getDescriptions());
            Hibernate.initialize(diagnosis.getAnswerToDiagnosisScoreDistributions());
            Hibernate.initialize(diagnosis.getAnswersForPerfectDiagnosis());
            return diagnosis;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    private List getList(int diagnosisId, Session session) throws DatabaseMapperException {
        List diagnoses = session.createQuery("FROM Diagnosis d WHERE d.id = :id")
                .setInteger("id", diagnosisId)
                .list();
        if (diagnoses.size() < 1) {
            throw new DatabaseMapperException("Diagnosis doesn't exist");
        }
        return diagnoses;
    }

    @Override
    public ArrayList<Diagnosis> readAll() {
        Session session = beginTransaction();
        try {
            List diagnoses = session.createQuery("FROM Diagnosis").list();
            if (diagnoses.size() < 1) {
                throw new DatabaseMapperException("There are currently no diagnoses!");
            }
            commit();
            return (ArrayList<Diagnosis>) diagnoses;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int diagnosisId) {
        Session session = beginTransaction();
        try {
            List diagnoses = getList(diagnosisId, session);
            session.delete((Diagnosis) diagnoses.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
