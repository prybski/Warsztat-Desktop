package pl.edu.pwsztar.service;

import pl.edu.pwsztar.dao.VehicleDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.repository.VehicleRepository;

import java.util.List;

public class VehicleService implements VehicleDAO {

    private static VehicleRepository vehicleRepository;

    public VehicleService() {
        vehicleRepository = new VehicleRepository();
    }

    @Override
    public List<Vehicle> findAll() {
        vehicleRepository.openCurrentSession();

        List<Vehicle> vehicles = vehicleRepository.findAll();

        vehicleRepository.closeCurrentSession();

        return vehicles;
    }

    @Override
    public List<Vehicle> findByClient(Client client) {
        vehicleRepository.openCurrentSession();

        List<Vehicle> vehicles = vehicleRepository.findByClient(client);

        vehicleRepository.closeCurrentSession();

        return vehicles;
    }
}
