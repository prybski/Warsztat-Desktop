package pl.edu.pwsztar.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import pl.edu.pwsztar.util.callback.SessionCallback;
import pl.edu.pwsztar.util.callback.TransactionCallback;

public class HibernateUtil {

    private static ThreadLocal<Session> sessionThreadLocal;
    private static SessionFactory sessionFactory;

    static {
        sessionThreadLocal = new ThreadLocal<>();

        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
    }

    public static Session getSession() {
        Session session;

        if (sessionThreadLocal.get() == null) {
            session = sessionFactory.openSession();

            sessionThreadLocal.set(session);
        } else {
            session = sessionThreadLocal.get();
        }

        return session;
    }

    public static void withinSession(SessionCallback sessionCallback) {
        Session session = getSession();

        try {
            sessionCallback.doInSession();
        } catch (RuntimeException e) {
            throw new HibernateException(e.getCause());
        } finally {
            if (session.isOpen()) {
                closeSession();
            }
        }
    }

    public static void withinTransaction(TransactionCallback transactionCallback) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();

        try {
            transactionCallback.doInTransaction();

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new HibernateException(e.getCause());
        } finally {
            if (session.isOpen()) {
                closeSession();
            }
        }
    }

    private static void closeSession() {
        Session session;

        if (sessionThreadLocal.get() != null) {
            session = sessionThreadLocal.get();
            session.close();

            sessionThreadLocal.remove();
        }
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }
}
