package database.mappers;

import models.History;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "histories"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class HistoryMapper extends DatabaseTransaction implements DatabaseMapper<History> {

    @Override
    public int create(History history) {
        Session session = beginTransaction();
        try {
            Integer id;
            List histories = session.createQuery("FROM History h WHERE h.id = :id")
                    .setInteger("id", history.getId())
                    .list();
            if (histories.size() > 0) {
                throw new DatabaseMapperException("History already exists call HistoryMapper.read()");
            } else {
                id = (Integer) session.save(history);
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
    public History update(History history) {

        Session session = beginTransaction();
        try {
            session.update(history);
            commit();
            return history;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public History read(int historyId) {
        Session session = beginTransaction();
        try {
            List histories = getList(historyId, session);
            commit();
            return (History) histories.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    private List getList(int historyId, Session session) throws DatabaseMapperException {
        List histories = session.createQuery("FROM History h WHERE h.id = :id")
                .setInteger("id", historyId)
                .list();
        if (histories.size() < 1) {
            throw new DatabaseMapperException("History doesn't exist");
        }
        return histories;
    }

    @Override
    public ArrayList<History> readAll() {
        Session session = beginTransaction();
        try {
            Integer id;
            List histories = session.createQuery("FROM History").list();
            if (histories.size() < 1) {
                throw new DatabaseMapperException("There are currently no histories!");
            }
            commit();
            return (ArrayList<History>) histories;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public History readAndFetch(int historyId) {
        Session session = beginTransaction();
        try {
            List histories = getList(historyId, session);
            commit();

            final History history = (History) histories.get(0);
            Hibernate.initialize(history.getAnswers());
            return history;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int historyId) {
        Session session = beginTransaction();
        try {
            List histories = getList(historyId, session);
            session.delete((History) histories.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
