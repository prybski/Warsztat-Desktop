package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Client;

import java.util.List;

public interface ClientDAO {

    List<Client> findAll();
    Client findOneByFirstAndLastName(String firstAndLastName);
}
