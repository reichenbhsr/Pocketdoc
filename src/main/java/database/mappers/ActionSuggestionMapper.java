package database.mappers;

import models.ActionSuggestion;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "action_suggestions"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class ActionSuggestionMapper extends DatabaseTransaction implements DatabaseMapper<ActionSuggestion> {

    @Override
    public int create(ActionSuggestion actionSuggestion) {
        Session session = beginTransaction();
        try {
            Integer id;
            List actionSuggestions = session.createQuery("FROM ActionSuggestion action_suggestion WHERE action_suggestion.name = :name OR action_suggestion.id = :id")
                    .setString("name", actionSuggestion.getName())
                    .setInteger("id", actionSuggestion.getId())
                    .list();
            if (actionSuggestions.size() > 0) {
                throw new DatabaseMapperException("ActionSuggestion already exists call ActionSuggestionMapper.read()");
            } else {
                id = (Integer) session.save(actionSuggestion);
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
    public ActionSuggestion update(ActionSuggestion actionSuggestion) {

        Session session = beginTransaction();
        try {
            session.update(actionSuggestion);
            commit();
            return actionSuggestion;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public ActionSuggestion read(int actionSuggestionId) {
        Session session = beginTransaction();
        try {
            List actionSuggestions = getList(actionSuggestionId, session);
            commit();
            return (ActionSuggestion) actionSuggestions.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public ActionSuggestion readAndFetch(int id) {
        Session session = beginTransaction();
        try {
            List actionSuggestions = getList(id, session);
            commit();

            final ActionSuggestion actionSuggestion = (ActionSuggestion) actionSuggestions.get(0);
            Hibernate.initialize(actionSuggestion.getAnswerToActionSuggestionScoreDistributions());
            Hibernate.initialize(actionSuggestion.getSyndromToActionSuggestionScoreDistributions());
            Hibernate.initialize(actionSuggestion.getDescriptions());
            return (ActionSuggestion) actionSuggestion;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    private List getList(int actionSuggestionId, Session session) throws DatabaseMapperException {
        List actionSuggestions = session.createQuery("FROM ActionSuggestion action_suggestion WHERE action_suggestion.id = :id")
                .setInteger("id", actionSuggestionId)
                .list();
        if (actionSuggestions.size() < 1) {
            throw new DatabaseMapperException("ActionSuggestion doesn't exist");
        }
        return actionSuggestions;
    }

    @Override
    public ArrayList<ActionSuggestion> readAll() {
        Session session = beginTransaction();
        try {
            List actionSuggestions = session.createQuery("FROM ActionSuggestion ").list();
            if (actionSuggestions.size() < 1) {
                throw new DatabaseMapperException("There are currently no action suggestions!");
            }
            commit();
            return (ArrayList<ActionSuggestion>) actionSuggestions;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int actionSuggestionId) {
        Session session = beginTransaction();
        try {
            List actionSuggestions = getList(actionSuggestionId, session);
            session.delete((ActionSuggestion) actionSuggestions.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }

}
