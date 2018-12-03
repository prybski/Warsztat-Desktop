package pl.edu.pwsztar.repository;

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
        Query<Client> clientQuery = sessionFactoryManager.getCurrentSession().createQuery("from Client", Client.class);

        return clientQuery.getResultList();
    }

    public SessionFactoryManager getSessionFactoryManager() {
        return sessionFactoryManager;
    }

    public void setSessionFactoryManager(SessionFactoryManager sessionFactoryManager) {
        this.sessionFactoryManager = sessionFactoryManager;
    }
}
