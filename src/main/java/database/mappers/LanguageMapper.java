package database.mappers;

import models.Language;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "languages"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class LanguageMapper extends DatabaseTransaction implements DatabaseMapper<Language> {

    @Override
    public int create(Language language) {
        Session session = beginTransaction();
        try {
            Integer id;
            List languages = session.createQuery("FROM Language l WHERE l.id = :id OR l.name = :name")
                    .setString("name", language.getName())
                    .setInteger("id", language.getId())
                    .list();
            if (languages.size() > 0) {
                throw new DatabaseMapperException("Language already exists call LanguageMapper.read()");
            } else {
                id = (Integer) session.save(language);
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
    public Language update(Language language) {

        Session session = beginTransaction();
        try {
            session.update(language);
            commit();
            return language;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Language read(int languageId) {
        Session session = beginTransaction();
        try {
            List languages = getList(languageId, session);
            commit();
            return (Language) languages.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Language readAndFetch(int id) {
        return read(id);
    }

    private List getList(int languageId, Session session) throws DatabaseMapperException {
        List languages = session.createQuery("FROM Language l WHERE l.id = :id")
                .setInteger("id", languageId)
                .list();
        if (languages.size() < 1) {
            throw new DatabaseMapperException("Language doesn't exist");
        }
        return languages;
    }

    @Override
    public ArrayList<Language> readAll() {
        Session session = beginTransaction();
        try {
            Integer id;
            List languages = session.createQuery("FROM Language").list();
            if (languages.size() < 1) {
                throw new DatabaseMapperException("There are currently no languages!");
            }
            commit();
            return (ArrayList<Language>) languages;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int languageId) {
        Session session = beginTransaction();
        try {
            List languages = getList(languageId, session);
            session.delete((Language) languages.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
