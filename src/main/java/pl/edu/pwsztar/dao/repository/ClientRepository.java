package pl.edu.pwsztar.dao.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.dao.ClientDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.util.manager.SessionFactoryManager;

import javax.persistence.NoResultException;
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

    @Override
    public Client findByFirstAndLastName(String firstAndLastName) {
        sessionFactoryManager.openCurrentSession();

        Query<Client> clientQuery = sessionFactoryManager.getCurrentSession().createQuery("select c from Client c where concat(firstName, ' ', lastName) in (:firstAndLastName)", Client.class);
        clientQuery.setParameter("firstAndLastName", firstAndLastName);

        try {
            Client clientFromDb = clientQuery.getSingleResult();

            sessionFactoryManager.closeCurrentSession();

            return clientFromDb;
        } catch (NoResultException ex) {
            sessionFactoryManager.closeCurrentSession();

            return null;
        }
    }
}
