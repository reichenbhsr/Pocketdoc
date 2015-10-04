package database.mappers;

import models.Setting;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "settings"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class SettingMapper extends DatabaseTransaction implements DatabaseMapper<Setting> {

    @Override
    public int create(Setting setting) {
        Session session = beginTransaction();
        try {
            Integer id;

            //TODO: determine if s.name = :name is necessary
            List settings = session.createQuery("FROM Setting s WHERE s.id = :id")
                    .setInteger("id", setting.getId())
                    .list();
            if (settings.size() > 0) {
                throw new DatabaseMapperException("Setting already exists call SettingManager.get()");
            } else {
                id = (Integer) session.save(setting);
                System.out.println("setting with id: " + id + " created!");
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
    public Setting update(Setting setting) {

        Session session = beginTransaction();
        try {
            session.update(setting);
            commit();
            return setting;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Setting read(int settingId) {
        Session session = beginTransaction();
        try {
            List settings = getList(settingId, session);
            commit();
            return (Setting) settings.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public Setting readAndFetch(int id) {
        return read(id);
    }

    private List getList(int settingId, Session session) throws DatabaseMapperException {
        List settings = session.createQuery("FROM Setting s WHERE s.id = :id")
                .setInteger("id", settingId)
                .list();
        if (settings.size() < 1) {
            throw new DatabaseMapperException("Setting doesn't exist");
        }
        return settings;
    }

    @Override
    public ArrayList<Setting> readAll() {
        Session session = beginTransaction();
        try {
            Integer id;
            List settings = session.createQuery("FROM Setting ").list();
            if (settings.size() < 1) {
                throw new DatabaseMapperException("There are currently no settings!");
            }
            commit();
            return (ArrayList<Setting>) settings;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int settingId) {
        Session session = beginTransaction();
        try {
            List settings = getList(settingId, session);
            session.delete((Setting) settings.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
