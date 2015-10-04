package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseMapper;
import database.mappers.DatabaseMapperException;
import database.mappers.DatabaseTransaction;
import models.intermediateClassModels.QuestionDescription;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "question_descriptions"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class QuestionDescriptionMapper extends DatabaseTransaction implements DatabaseMapper<QuestionDescription> {

    @Override
    public int create(QuestionDescription questionDescription) {
        Session session = beginTransaction();
        try {
            Integer id;
            List questionDescriptions = session.createQuery("FROM QuestionDescription qd WHERE qd.id = :id")
                    .setInteger("id", questionDescription.getId())
                    .list();
            if (questionDescriptions.size() > 0) {
                throw new DatabaseMapperException("QuestionDescription already exists call QuestionDescriptionMapper.read()");
            } else {
                id = (Integer) session.save(questionDescription);
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
    public QuestionDescription update(QuestionDescription questionDescription) {
        Session session = beginTransaction();
        try {
            session.update(questionDescription);
            commit();
            return questionDescription;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public QuestionDescription read(int questionDescriptionId) {
        Session session = beginTransaction();
        try {
            List questionDescriptions = getList(questionDescriptionId, session);
            commit();
            return (QuestionDescription) questionDescriptions.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public QuestionDescription readAndFetch(int id) {
        return read(id);
    }

    private List getList(int questionDescriptionId, Session session) throws DatabaseMapperException {
        List questionDescriptions = session.createQuery("FROM QuestionDescription qd WHERE qd.id = :id")
                .setInteger("id", questionDescriptionId)
                .list();
        if (questionDescriptions.size() < 1) {
            throw new DatabaseMapperException("QuestionDescription doesn't exist");
        }
        return questionDescriptions;
    }

    @Override
    public ArrayList<QuestionDescription> readAll() {
        Session session = beginTransaction();
        try {
            Integer id;
            List questionDescriptions = session.createQuery("FROM QuestionDescription").list();
            if (questionDescriptions.size() < 1) {
                throw new DatabaseMapperException("There are currently no QuestionDescriptions!");
            }
            commit();
            return (ArrayList<QuestionDescription>) questionDescriptions;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int questionDescriptionId) {
        Session session = beginTransaction();
        try {
            List questionDescriptions = getList(questionDescriptionId, session);
            session.delete((QuestionDescription) questionDescriptions.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
