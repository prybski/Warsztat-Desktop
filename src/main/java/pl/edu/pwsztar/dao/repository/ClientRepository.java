package pl.edu.pwsztar.dao.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.dao.ClientDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.util.HibernateUtil;

import java.util.List;

public class ClientRepository implements ClientDAO {

    @Override
    public List<Client> findAll() {
        Query<Client> clientQuery = HibernateUtil.getOrOpenSession().createQuery("from Client", Client.class);
        List<Client> clientsFromDb = clientQuery.getResultList();

        HibernateUtil.closeSession();

        return clientsFromDb;
    }

    @Override
    public Client findByFirstAndLastName(String firstAndLastName) {
        Query<Client> clientQuery = HibernateUtil.getOrOpenSession().createQuery("select c from Client c where concat(firstName, ' ', lastName) in (:firstAndLastName)", Client.class);
        clientQuery.setParameter("firstAndLastName", firstAndLastName);

        Client clientFromDb = clientQuery.getSingleResult();

        HibernateUtil.closeSession();

        return clientFromDb;
    }
}
