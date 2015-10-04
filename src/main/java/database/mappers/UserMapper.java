package database.mappers;

import models.User;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "users"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class UserMapper extends DatabaseTransaction implements DatabaseMapper<User> {

    @Override
    public int create(User user) {
        Session session = beginTransaction();
        try {
            Integer id;
            List users = session.createQuery("FROM User u WHERE u.name = :name OR u.id = :id")
                    .setString("name", user.getName())
                    .setInteger("id", user.getId())
                    .list();
            if (users.size() > 0) {
                throw new DatabaseMapperException("User already exists call UserMapper.read()");
            } else {
                id = (Integer) session.save(user);
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
    public User update(User user) {
        Session session = beginTransaction();
        try {
            session.update(user);
            commit();
            return user;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public User read(int userId) {
        Session session = beginTransaction();
        try {
            List users = getList(userId, session);
            commit();
            return (User) users.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public User readAndFetch(int id) {
        return read(id);
    }

    private ArrayList<User> getList(int userId, Session session) throws DatabaseMapperException {
        List users = session.createQuery("FROM User u WHERE u.id = :id")
                .setInteger("id", userId)
                .list();
        if (users.size() < 1) {
            throw new DatabaseMapperException("User doesn't exist");
        }
        return (ArrayList<User>) users;
    }

    @Override
    public ArrayList<User> readAll() {
        Session session = beginTransaction();
        try {
            Integer id;
            List users = session.createQuery("FROM User").list();
            if (users.size() < 1) {
                throw new DatabaseMapperException("There are currently no users!");
            }
            commit();
            return (ArrayList<User>) users;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int userId) {
        Session session = beginTransaction();
        try {
            List users = getList(userId, session);
            session.delete((User) users.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }

}
