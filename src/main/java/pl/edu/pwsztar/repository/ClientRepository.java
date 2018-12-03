package pl.edu.pwsztar.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.edu.pwsztar.entity.Client;

import java.util.List;

public class ClientRepository {

    public static List<Client> getAll(Session session) {
        Query<Client> clientQuery = session.createQuery("from Client", Client.class);

        return clientQuery.getResultList();
    }
}
