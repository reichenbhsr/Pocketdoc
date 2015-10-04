package database.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * This Singleton returns a SessionFactory which is will establish the connection between database and application
 * from this factory we can get Sessions which won't open a new connection between database and application but
 * rather take the existing connection and give it a new job to do.
 * @author Oliver Frischknecht
 */
public class DatabaseConnectionUtil {
    private static SessionFactory sessionFactory=null;

    private DatabaseConnectionUtil(){
        sessionFactory = createSessionFactory();
    }

    public static synchronized SessionFactory  getSessionFactory(){
        if(sessionFactory==null){
            new DatabaseConnectionUtil();
        }
        return sessionFactory;
    }

    private static SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(
                configuration.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }
}
