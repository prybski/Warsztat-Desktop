package pl.edu.pwsztar.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.dao.VehicleDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.util.manager.SessionFactoryManager;

import java.util.List;

public class VehicleRepository implements VehicleDAO {

    private SessionFactoryManager sessionFactoryManager;

    {
        sessionFactoryManager = new SessionFactoryManager();
    }

    @Override
    public List<Vehicle> findAll() {
        Query<Vehicle> vehicleQuery = sessionFactoryManager.getCurrentSession().createQuery("from Vehicle", Vehicle.class);

        return vehicleQuery.getResultList();
    }

    @Override
    public List<Vehicle> findByClient(Client client) {
        Query<Vehicle> vehicleQuery = sessionFactoryManager.getCurrentSession().createQuery("from Vehicle where client = :client", Vehicle.class);
        vehicleQuery.setParameter("client", client);

        return vehicleQuery.getResultList();
    }

    public SessionFactoryManager getSessionFactoryManager() {
        return sessionFactoryManager;
    }

    public void setSessionFactoryManager(SessionFactoryManager sessionFactoryManager) {
        this.sessionFactoryManager = sessionFactoryManager;
    }
}
