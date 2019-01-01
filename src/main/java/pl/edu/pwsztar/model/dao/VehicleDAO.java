package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;

import java.util.List;

public interface VehicleDAO {

    void add(Vehicle vehicle);
    List<Vehicle> findAll();
    List<Vehicle> findByClient(Client client);
    void update(Vehicle vehicle);
}
