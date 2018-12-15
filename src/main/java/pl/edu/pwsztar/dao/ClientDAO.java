package pl.edu.pwsztar.dao;

import pl.edu.pwsztar.entity.Client;

import java.util.List;

public interface ClientDAO {

    List<Client> findAll();
    Client findByFirstAndLastName(String firstAndLastName);
}
