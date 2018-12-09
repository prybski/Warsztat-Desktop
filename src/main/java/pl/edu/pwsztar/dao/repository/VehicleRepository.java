package pl.edu.pwsztar.dao.repository;

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
        sessionFactoryManager.openCurrentSession();

        Query<Vehicle> vehicleQuery = sessionFactoryManager.getCurrentSession().createQuery("from Vehicle", Vehicle.class);
        List<Vehicle> vehiclesFromDb = vehicleQuery.getResultList();

        sessionFactoryManager.closeCurrentSession();

        return vehiclesFromDb;
    }

    @Override
    public List<Vehicle> findByClient(Client client) {
        sessionFactoryManager.openCurrentSession();

        Query<Vehicle> vehicleQuery = sessionFactoryManager.getCurrentSession().createQuery("from Vehicle", Vehicle.class);
        List<Vehicle> vehiclesFromDb = vehicleQuery.getResultList();

        sessionFactoryManager.closeCurrentSession();

        return vehiclesFromDb;
    }
}