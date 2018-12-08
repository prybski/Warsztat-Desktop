package pl.edu.pwsztar.service;

import pl.edu.pwsztar.dao.ClientDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.dao.repository.ClientRepository;

import java.util.List;

public class ClientService implements ClientDAO {

    private static ClientRepository clientRepository;

    public ClientService() {
        clientRepository = new ClientRepository();
    }

    @Override
    public List<Client> findAll() {
        clientRepository.getSessionFactoryManager().openCurrentSession();

        List<Client> clients = clientRepository.findAll();

        clientRepository.getSessionFactoryManager().closeCurrentSession();

        return clients;
    }
}
