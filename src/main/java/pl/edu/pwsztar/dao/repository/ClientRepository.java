package pl.edu.pwsztar.dao.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.dao.ClientDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.util.manager.SessionFactoryManager;

import java.util.List;

public class ClientRepository implements ClientDAO {

    private SessionFactoryManager sessionFactoryManager;

    {
        sessionFactoryManager = new SessionFactoryManager();
    }

    @Override
    public List<Client> findAll() {
        sessionFactoryManager.openCurrentSession();

        Query<Client> clientQuery = sessionFactoryManager.getCurrentSession().createQuery("from Client", Client.class);
        List<Client> clientsFromDb = clientQuery.getResultList();

        sessionFactoryManager.closeCurrentSession();

        return clientsFromDb;
    }
}
