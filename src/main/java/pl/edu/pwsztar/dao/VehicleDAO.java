package pl.edu.pwsztar.dao;

import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;

import java.util.List;

public interface VehicleDAO {

    List<Vehicle> findAll();
    List<Vehicle> findByClient(Client client);
}
