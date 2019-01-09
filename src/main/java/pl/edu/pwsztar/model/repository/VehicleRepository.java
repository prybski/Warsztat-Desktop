package pl.edu.pwsztar.model.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.model.dao.VehicleDAO;
import pl.edu.pwsztar.util.HibernateUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class VehicleRepository implements VehicleDAO {

    @Override
    public void add(Vehicle vehicle) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().save(vehicle));
    }

    @Override
    public void addVehicleWithJob(Vehicle vehicle) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().save(vehicle));
    }

    @Override
    public List<Vehicle> findAll() {
        AtomicReference<List<Vehicle>> vehiclesFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Vehicle> vehicleQuery = HibernateUtil.getSession().createQuery("from Vehicle", Vehicle.class);

            vehiclesFromDb.set(vehicleQuery.getResultList());
        });

        return vehiclesFromDb.get();
    }

    @Override
    public Vehicle findOneByVehicleVinNumber(String vinNumber) {
        AtomicReference<Vehicle> vehicleFromDB = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Vehicle> vehicleQuery = HibernateUtil.getSession().createQuery("select distinct j.vehicle from Job j where j.vehicle.vinNumber = :vinNumber", Vehicle.class);
            vehicleQuery.setParameter("vinNumber", vinNumber);

            vehicleFromDB.set(vehicleQuery.getSingleResult());
        });

        return vehicleFromDB.get();
    }

    @Override
    public List<Vehicle> findByClient(Client client) {
        AtomicReference<List<Vehicle>> vehiclesFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Vehicle> vehicleQuery = HibernateUtil.getSession().createQuery("select distinct j.vehicle from " +
                    "Job j where j.client = :client", Vehicle.class);
            vehicleQuery.setParameter("client", client);

            vehiclesFromDb.set(vehicleQuery.getResultList());
        });

        return vehiclesFromDb.get();
    }

    @Override
    public void update(Vehicle vehicle) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().update(vehicle));
    }
}
