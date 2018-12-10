package pl.edu.pwsztar.dao.repository;

import org.hibernate.Criteria;
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
    public List<Date> findFixedDates() {
        sessionFactoryManager.openCurrentSession();

        Query jobQuery = sessionFactoryManager.getCurrentSession().createQuery("select distinct j.fixedDate from Job j where j.endDate is null");
        List datesFromDb = jobQuery.getResultList();

        List<Date> sqlDatesFromDb = new ArrayList<>();

        for (Object date : datesFromDb) {
            sqlDatesFromDb.add((Date) date);
        }

        sessionFactoryManager.closeCurrentSession();

        return sqlDatesFromDb;
    }

    @Override
    public void add(Job job, Vehicle vehicle, Client client, boolean isVehicleNew) {
        sessionFactoryManager.openCurrentSessionWithTransaction();

        if (isVehicleNew) {
            sessionFactoryManager.getCurrentSession().save(vehicle);

            job.setVehicle(vehicle);
            job.setClient(client);

            sessionFactoryManager.getCurrentSession().save(job);

            sessionFactoryManager.closeCurrentSessionWithTransaction();
        }

        job.setVehicle(vehicle);
        job.setClient(client);

        sessionFactoryManager.getCurrentSession().save(job);

        sessionFactoryManager.closeCurrentSessionWithTransaction();
    }
}
