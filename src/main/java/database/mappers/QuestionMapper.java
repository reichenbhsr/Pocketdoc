package database.mappers;

import models.Question;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "questions"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class QuestionMapper extends DatabaseTransaction implements DatabaseMapper<Question> {

    @Override
    public int create(Question question) {
        Session session = beginTransaction();
        try {
            Integer id;
            List questions = session.createQuery("FROM Question q WHERE q.name = :name OR q.id = :id")
                    .setString("name", question.getName())
                    .setInteger("id", question.getId())
                    .list();
            if (questions.size() > 0) {
                throw new DatabaseMapperException("Question already exists call QuestionMapper.read()");
            } else {
                id = (Integer) session.save(question);
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
    public Question update(Question question) {

        Session session = beginTransaction();
        try {
            session.update(question);
            commit();
            return question;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Question read(int questionId) {
        Session session = beginTransaction();
        try {
            List questions = getList(questionId, session);
            commit();
            return (Question) questions.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Question readAndFetch(int id) {
        Session session = beginTransaction();
        try {
            List questions = getList(id, session);
            commit();

            final Question question = (Question) questions.get(0);
            Hibernate.initialize(question.getDescriptions());
            Hibernate.initialize(question.getAnswerNo());
            Hibernate.initialize(question.getAnswerYes());
            Hibernate.initialize(question.getDependsOn());
            return question;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    private List getList(int questionId, Session session) throws DatabaseMapperException {
        List questions = session.createQuery("FROM Question q WHERE q.id = :id")
                .setInteger("id", questionId)
                .list();
        if (questions.size() < 1) {
            throw new DatabaseMapperException("Question doesn't exist");
        }
        return questions;
    }

    @Override
    public ArrayList<Question> readAll() {
        Session session = beginTransaction();
        try {
            List questions = session.createQuery("FROM Question").list();
            if (questions.size() < 1) {
                throw new DatabaseMapperException("There are currently no questions!");
            }
            commit();
            return (ArrayList<Question>) questions;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int questionId) {
        Session session = beginTransaction();
        try {
            List questions = getList(questionId, session);
            session.delete((Question) questions.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
