package database.mappers;

import models.Syndrom;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "syndroms"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class SyndromMapper extends DatabaseTransaction implements DatabaseMapper<Syndrom> {

    @Override
    public int create(Syndrom syndrom) {
        Session session = beginTransaction();
        try {
            Integer id;
            List syndroms = session.createQuery("FROM Syndrom s WHERE s.name = :name OR s.id = :id")
                    .setString("name", syndrom.getName())
                    .setInteger("id", syndrom.getId())
                    .list();
            if (syndroms.size() > 0) {
                throw new DatabaseMapperException("Syndrom already exists call UserMapper.read()");
            } else {
                id = (Integer) session.save(syndrom);
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
    public Syndrom update(Syndrom syndrom) {

        Session session = beginTransaction();
        try {
            session.update(syndrom);
            commit();
            return syndrom;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Syndrom read(int syndromId) {
        Session session = beginTransaction();
        try {
            List syndroms = getList(syndromId, session);
            commit();
            return (Syndrom) syndroms.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Syndrom readAndFetch(int id) {
        Session session = beginTransaction();
        try {
            List syndroms = getList(id, session);
            commit();

            final Syndrom syndrom = (Syndrom) syndroms.get(0);
            Hibernate.initialize(syndrom.getScoreDistributions());
            Hibernate.initialize(syndrom.getSymptoms());
            return syndrom;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    private List getList(int syndromId, Session session) throws DatabaseMapperException {
        List syndroms = session.createQuery("FROM Syndrom s WHERE s.id = :id")
                .setInteger("id", syndromId)
                .list();
        if (syndroms.size() < 1) {
            throw new DatabaseMapperException("Syndrom doesn't exist");
        }
        return syndroms;
    }

    @Override
    public ArrayList<Syndrom> readAll() {
        Session session = beginTransaction();
        try {
            List syndroms = session.createQuery("FROM Syndrom ").list();
            if (syndroms.size() < 1) {
                throw new DatabaseMapperException("There are currently no syndroms!");
            }
            commit();
            return (ArrayList<Syndrom>) syndroms;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int syndromId) {
        Session session = beginTransaction();
        try {
            List syndroms = getList(syndromId, session);
            session.delete((Syndrom) syndroms.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
