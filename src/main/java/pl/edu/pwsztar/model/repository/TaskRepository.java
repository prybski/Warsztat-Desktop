package pl.edu.pwsztar.model.repository;

import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Task;
import pl.edu.pwsztar.model.dao.TaskDAO;
import pl.edu.pwsztar.util.HibernateUtil;

public class TaskRepository implements TaskDAO {

    @Override
    public void add(Job job, Task task) {
        HibernateUtil.withinTransaction(() -> {
            task.setJob(job);

            HibernateUtil.getSession().save(task);
        });
    }
}
