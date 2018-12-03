package pl.edu.pwsztar.service;

import pl.edu.pwsztar.dao.ClientDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.repository.ClientRepository;

import java.util.List;

public class ClientService implements ClientDAO {

    private static ClientRepository clientRepository;

    public ClientService() {
        clientRepository = new ClientRepository();
    }

    // gettery i settery
    public static ClientRepository getClientRepository() {
        return clientRepository;
    }

    @Override
    public List<Client> findAll() {
        clientRepository.openCurrentSession();

        List<Client> clients = clientRepository.findAll();

        clientRepository.closeCurrentSession();

        return clients;
    }
}
