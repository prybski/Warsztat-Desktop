package pl.edu.pwsztar.model.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.entity.Part;
import pl.edu.pwsztar.model.dao.PartDAO;
import pl.edu.pwsztar.util.HibernateUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PartRepository implements PartDAO {

    @Override
    public void add(Part part) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().save(part));
    }

    @Override
    public List<Part> findAll() {
        AtomicReference<List<Part>> partsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Part> partQuery = HibernateUtil.getSession().createQuery("from Part", Part.class);

            partsFromDb.set(partQuery.getResultList());
        });

        return partsFromDb.get();
    }
}
