package pl.edu.pwsztar.dao.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.dao.JobDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.util.manager.SessionFactoryManager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class JobRepository implements JobDAO {

    private SessionFactoryManager sessionFactoryManager;

    {
        sessionFactoryManager = new SessionFactoryManager();
    }

    @Override
    public List<Job> findAllByDate(Date date) {
        sessionFactoryManager.openCurrentSession();

        Query<Job> jobQuery = sessionFactoryManager.getCurrentSession().createQuery("from Job where fixedDate = :date", Job.class);
        jobQuery.setParameter("date", date);

        List<Job> jobsFromDb = jobQuery.getResultList();

        sessionFactoryManager.closeCurrentSession();

        return jobsFromDb;
    }

    @Override
    public List<Date> findJobsFixedDates() {
        sessionFactoryManager.openCurrentSession();

        Query<Job> jobQuery = sessionFactoryManager.getCurrentSession().createQuery("from Job", Job.class);

        List<Job> jobsFromDb = jobQuery.getResultList();
        List<Date> datesFromDb = new ArrayList<>();

        for (Job job : jobsFromDb) {
            datesFromDb.add(job.getFixedDate());
        }

        sessionFactoryManager.closeCurrentSession();

        return datesFromDb;
    }

    @Override
    public void addWithNewVehicle(Job job, Vehicle vehicle, Client client) {
        sessionFactoryManager.openCurrentSessionWithTransaction();

        sessionFactoryManager.getCurrentSession().save(vehicle);

        job.setVehicle(vehicle);
        job.setClient(client);

        sessionFactoryManager.getCurrentSession().save(job);

        sessionFactoryManager.closeCurrentSessionWithTransaction();
    }

    @Override
    public void addWithoutNewVehicle(Job job, Vehicle vehicle, Client client) {
        sessionFactoryManager.openCurrentSessionWithTransaction();

        job.setVehicle(vehicle);
        job.setClient(client);

        sessionFactoryManager.getCurrentSession().save(job);

        sessionFactoryManager.closeCurrentSessionWithTransaction();
    }
}
