package pl.edu.pwsztar.dao.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.dao.VehicleDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.util.HibernateUtil;

import java.util.List;

public class VehicleRepository implements VehicleDAO {

    @Override
    public List<Vehicle> findAll() {
        Query<Vehicle> vehicleQuery = HibernateUtil.getSession().createQuery("from Vehicle", Vehicle.class);
        List<Vehicle> vehiclesFromDb = vehicleQuery.getResultList();

        HibernateUtil.closeSession();

        return vehiclesFromDb;
    }

    @Override
    public List<Vehicle> findAllByClient(Client client) {
        Query<Vehicle> vehicleQuery = HibernateUtil.getSession().createQuery("select distinct j.vehicle from Job j where j.client = :client", Vehicle.class);
        vehicleQuery.setParameter("client", client);

        List<Vehicle> vehiclesFromDb = vehicleQuery.getResultList();

        HibernateUtil.closeSession();

        return vehiclesFromDb;
    }

    @Override
    public void add(Vehicle vehicle) {
//        HibernateUtil.getSession();
//
//        HibernateUtil.withinTransaction(() -> {
//            HibernateUtil.getSession().save(vehicle);
//        });
//
//        HibernateUtil.closeSession();
    }
}
