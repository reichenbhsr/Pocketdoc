package database.mappers;

import database.utils.DatabaseConnectionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Diese Klasse wird von jedem Mapper benutzt um all Transaktionen abzuhandeln.
 * Damit sparen wir viel unnötiger Code in den Mapper.
 *
 * @author Oliver Frischknecht
 */
public abstract class DatabaseTransaction {
    Session session;
    Transaction transaction;

    public Session beginTransaction() {
        session = DatabaseConnectionUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        return session;
    }

    public void commit() {
        transaction.commit();
    }

    public void handleError(Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
    }

    public void endTransaction() {
        session.close();
    }
}
