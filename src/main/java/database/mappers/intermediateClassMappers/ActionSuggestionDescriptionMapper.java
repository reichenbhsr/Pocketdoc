package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseMapper;
import database.mappers.DatabaseMapperException;
import database.mappers.DatabaseTransaction;
import models.ActionSuggestion;
import models.intermediateClassModels.ActionSuggestionDescription;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "action_suggestion_descriptions"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}.
 *
 * @author Oliver Frischknecht
 */
public class ActionSuggestionDescriptionMapper extends DatabaseTransaction implements DatabaseMapper<ActionSuggestionDescription> {

    @Override
    public int create(ActionSuggestionDescription actionSuggestionDescription) {
        Session session = beginTransaction();
        try {
            Integer id;
            List actionSuggestionDescriptions = session.createQuery("FROM ActionSuggestionDescription asd WHERE asd.id = :id")
                    .setInteger("id", actionSuggestionDescription.getId())
                    .list();
            if (actionSuggestionDescriptions.size() > 0) {
                throw new DatabaseMapperException("ActionSuggestionDescription already exists call ActionSuggestionDescriptionMapper.read()");
            } else {
                id = (Integer) session.save(actionSuggestionDescription);
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
    public ActionSuggestionDescription update(ActionSuggestionDescription actionSuggestionDescription) {
        Session session = beginTransaction();
        try {
            session.update(actionSuggestionDescription);
            commit();
            return actionSuggestionDescription;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public ActionSuggestionDescription read(int actionSuggestionDescriptionId) {
        Session session = beginTransaction();
        try {
            List actionSuggestionDescriptions = getList(actionSuggestionDescriptionId, session);
            commit();
            return (ActionSuggestionDescription) actionSuggestionDescriptions.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public ActionSuggestionDescription readAndFetch(int id) {
        return read(id);
    }

    private List getList(int actionSuggestionDescriptionId, Session session) throws DatabaseMapperException {
        List actionSuggestionDescriptions = session.createQuery("FROM ActionSuggestionDescription asd WHERE asd.id = :id")
                .setInteger("id", actionSuggestionDescriptionId)
                .list();
        if (actionSuggestionDescriptions.size() < 1) {
            throw new DatabaseMapperException("ActionSuggestionDescription doesn't exist");
        }
        return actionSuggestionDescriptions;
    }

    @Override
    public ArrayList<ActionSuggestionDescription> readAll() {
        Session session = beginTransaction();
        try {
            Integer id;
            List actionSuggestionDescriptions = session.createQuery("FROM ActionSuggestionDescription ").list();
            if (actionSuggestionDescriptions.size() < 1) {
                throw new DatabaseMapperException("There are currently no ActionSuggestionDescriptions!");
            }
            commit();
            return (ArrayList<ActionSuggestionDescription>) actionSuggestionDescriptions;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int actionSuggestionDescriptionId) {
        Session session = beginTransaction();
        try {
            List actionSuggestionDescriptions = getList(actionSuggestionDescriptionId, session);
            session.delete((ActionSuggestionDescription) actionSuggestionDescriptions.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
