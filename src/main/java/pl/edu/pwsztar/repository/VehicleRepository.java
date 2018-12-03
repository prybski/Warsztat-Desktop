package pl.edu.pwsztar.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import pl.edu.pwsztar.dao.VehicleDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;

import java.util.List;

public class VehicleRepository implements VehicleDAO {

    private SessionFactory sessionFactory;
    private Session currentSession;
    private Transaction currentTransaction;

    public VehicleRepository() {
    }

    // metody zarządzające sesją i transakcją
    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionWithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();

        return currentSession;
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

    private SessionFactory getSessionFactory() {
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();

        return sessionFactory;
    }

    private void closeSessionFactory() {
        sessionFactory.close();
    }

    // gettery i settery
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

    @Override
    public List<Vehicle> findAll() {
        Query<Vehicle> vehicleQuery = getCurrentSession().createQuery("from Vehicle", Vehicle.class);

        return vehicleQuery.getResultList();
    }

    @Override
    public List<Vehicle> findByClient(Client client) {
        Query<Vehicle> vehicleQuery = getCurrentSession().createQuery("from Vehicle where client = :client", Vehicle.class);
        vehicleQuery.setParameter("client", client);

        return vehicleQuery.getResultList();
    }
}
