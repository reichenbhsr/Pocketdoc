package database.mappers;

import models.Answer;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "answers"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class AnswerMapper extends DatabaseTransaction implements DatabaseMapper<Answer> {

    @Override
    public int create(Answer answer) {
        Session session = beginTransaction();
        try {
            Integer id;
            List answers = session.createQuery("FROM Answer a WHERE a.id = :id")
                    .setInteger("id", answer.getId())
                    .list();
            if (answers.size() > 0) {
                throw new DatabaseMapperException("Answer already exists call AnswerMapper.read()");
            } else {
                id = (Integer) session.save(answer);
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
    public Answer update(Answer answer) {

        Session session = beginTransaction();
        try {
            session.update(answer);
            commit();
            return answer;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Answer read(int answerId) {
        Session session = beginTransaction();
        try {
            List answers = getList(answerId, session);
            commit();
            return (Answer) answers.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Answer readAndFetch(int id) {
        Session session = beginTransaction();
        try {
            List answers = getList(id, session);
            commit();

            final Answer answer = (Answer) answers.get(0);
            Hibernate.initialize(answer.getAnswerToActionSuggestionScoreDistributions());
            Hibernate.initialize(answer.getAnswerToDiagnosisScoreDistributions());
            return answer;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    private List getList(int answerId, Session session) throws DatabaseMapperException {
        List answers = session.createQuery("FROM Answer a WHERE a.id = :id")
                .setInteger("id", answerId)
                .list();
        if (answers.size() < 1) {
            throw new DatabaseMapperException("Answer doesn't exist");
        }
        return answers;
    }

    @Override
    public ArrayList<Answer> readAll() {
        Session session = beginTransaction();
        try {
            List answers = session.createQuery("FROM Answer ").list();
            if (answers.size() < 1) {
                throw new DatabaseMapperException("There are currently no answers!");
            }
            commit();
            return (ArrayList<Answer>) answers;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int answerId) {
        Session session = beginTransaction();
        try {
            List answers = getList(answerId, session);
            session.delete((Answer) answers.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
