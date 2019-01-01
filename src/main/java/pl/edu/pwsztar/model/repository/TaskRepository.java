package pl.edu.pwsztar.model.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Task;
import pl.edu.pwsztar.model.dao.TaskDAO;
import pl.edu.pwsztar.util.HibernateUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TaskRepository implements TaskDAO {

    @Override
    public void add(Task task) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().save(task));
    }

    @Override
    public List<Task> findAllByJob(Job job) {
        AtomicReference<List<Task>> tasksFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Task> taskQuery = HibernateUtil.getSession().createQuery("from Task t where t.job = :job", Task.class);
            taskQuery.setParameter("job", job);

            tasksFromDb.set(taskQuery.getResultList());
        });

        return tasksFromDb.get();
    }

    @Override
    public void update(Task task) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().update(task));
    }

    @Override
    public void delete(Task task) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().delete(task));
    }
}
