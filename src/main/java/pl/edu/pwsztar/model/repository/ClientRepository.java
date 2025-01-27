package pl.edu.pwsztar.model.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.model.dao.ClientDAO;
import pl.edu.pwsztar.util.HibernateUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ClientRepository implements ClientDAO {

    @Override
    public void add(Client client) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().save(client));
    }

    @Override
    public List<Client> findAll() {
        AtomicReference<List<Client>> clientsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Client> clientQuery = HibernateUtil.getSession().createQuery("from Client", Client.class);

            clientsFromDb.set(clientQuery.getResultList());
        });

        return clientsFromDb.get();
    }

    @Override
    public Client findOneByVehicleVinNumber(String vinNumber) {
        AtomicReference<Client> clientFromDB = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Client> clientQuery = HibernateUtil.getSession().createQuery("select distinct j.client from Job j where j.vehicle.vinNumber = :vinNumber", Client.class);
            clientQuery.setParameter("vinNumber", vinNumber);

            clientFromDB.set(clientQuery.getSingleResult());
        });

        return clientFromDB.get();
    }

    @Override
    public List<Client> findAllWithVehicles() {
        AtomicReference<List<Client>> clientsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Client> clientQuery = HibernateUtil.getSession().createQuery("select distinct j.client from Job j where j.vehicle is not null", Client.class);

            clientsFromDb.set(clientQuery.getResultList());
        });

        return clientsFromDb.get();
    }

    @Override
    public List<Client> findAllByFirstAndLastName(String firstAndLastName) {
        AtomicReference<List<Client>> clientsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Client> clientQuery = HibernateUtil.getSession().createQuery("select c from Client c where concat(firstName, ' ', lastName) in (:firstAndLastName)", Client.class);
            clientQuery.setParameter("firstAndLastName", firstAndLastName);

            clientsFromDb.set(clientQuery.getResultList());
        });

        return clientsFromDb.get();
    }
}
