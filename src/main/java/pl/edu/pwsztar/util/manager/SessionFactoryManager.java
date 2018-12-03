package pl.edu.pwsztar.util.manager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class SessionFactoryManager {

    private SessionFactory sessionFactory;
    private Session currentSession;
    private Transaction currentTransaction;

    public void openCurrentSession() {
        currentSession = openSessionFactory().openSession();
    }

    public void openCurrentSessionWithTransaction() {
        currentSession = openSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
    }

    public void closeCurrentSession() {
        currentSession.close();
        closeSessionFactory();
    }

    public void closeCurrentSessionWithTransaction() {
        currentTransaction.commit();
        currentSession.close();
        closeSessionFactory();
    }

    private SessionFactory openSessionFactory() {
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();

        return sessionFactory;
    }

    private void closeSessionFactory() {
        sessionFactory.close();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }
}
