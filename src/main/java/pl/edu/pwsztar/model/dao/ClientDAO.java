package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Client;

import java.util.List;

public interface ClientDAO {

    void add(Client client);
    List<Client> findAll();
    Client findOneByVehicleVinNumber(String vinNumber);
    List<Client> findAllWithVehicles();
    List<Client> findAllByFirstAndLastName(String firstAndLastName);
}
