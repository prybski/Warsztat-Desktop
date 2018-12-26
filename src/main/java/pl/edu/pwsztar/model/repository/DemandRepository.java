package pl.edu.pwsztar.model.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.entity.Demand;
import pl.edu.pwsztar.entity.Task;
import pl.edu.pwsztar.model.dao.DemandDAO;
import pl.edu.pwsztar.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DemandRepository implements DemandDAO {

    @Override
    public void add(Demand demand) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().save(demand));
    }

    @Override
    public List<Demand> findAllByTasks(List<Task> tasks) {
        AtomicReference<List<Demand>> demandsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            List<Demand> demands = new ArrayList<>();

            for (Task task : tasks) {
                Query demandQuery = HibernateUtil.getSession().createQuery("select t.demands from Task t where t.id = :id");
                demandQuery.setParameter("id", task.getId());

                demands.addAll(convertToDemands(demandQuery.getResultList()));
            }

            demandsFromDb.set(demands);
        });

        return demandsFromDb.get();
    }

    @Override
    public void delete(Demand demand) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().delete(demand));
    }

    private List<Demand> convertToDemands(List objectDemands) {
        List<Demand> demands = new ArrayList<>();

        for (Object demand : objectDemands) {
            demands.add((Demand) demand);
        }

        return demands;
    }
}
