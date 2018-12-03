package pl.edu.pwsztar.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;

import java.util.List;

public class VehicleRepository {

    public static List<Vehicle> getAll(Session session) {
        Query<Vehicle> vehicleQuery = session.createQuery("from Vehicle", Vehicle.class);

        return vehicleQuery.getResultList();
    }

    public static List<Vehicle> getByClient(Session session, Client client) {
        Query<Vehicle> vehicleQuery = session.createQuery("from Vehicle where client = :client", Vehicle.class);
        vehicleQuery.setParameter("client", client);

        return vehicleQuery.getResultList();
    }
}
