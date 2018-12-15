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
    public List<Job> findHistoryByClient(Client client) {
        sessionFactoryManager.openCurrentSession();

        Query<Job> jobQuery = sessionFactoryManager.getCurrentSession().createQuery("from Job where client = :client and endDate is not null", Job.class);
        jobQuery.setParameter("client", client);

        List<Job> jobsFromDb = jobQuery.getResultList();

        sessionFactoryManager.closeCurrentSession();

        return jobsFromDb;
    }

    @Override
    public List<Job> findHistoryByVehicle(Vehicle vehicle) {
        sessionFactoryManager.openCurrentSession();

        Query<Job> jobQuery = sessionFactoryManager.getCurrentSession().createQuery("from Job where vehicle = :vehicle and endDate is not null", Job.class);
        jobQuery.setParameter("vehicle", vehicle);

        List<Job> jobsFromDb = jobQuery.getResultList();

        sessionFactoryManager.closeCurrentSession();

        return jobsFromDb;
    }

    @Override
    public List<Job> findHistoryByVinNumber(String vinNumber) {
        sessionFactoryManager.openCurrentSession();

        Query<Job> jobQuery = sessionFactoryManager.getCurrentSession().createQuery("from Job where vehicle.vinNumber = :vinNumber and endDate is not null", Job.class);
        jobQuery.setParameter("vinNumber", vinNumber);

        List<Job> jobsFromDb = jobQuery.getResultList();

        sessionFactoryManager.closeCurrentSession();

        return jobsFromDb;
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
    public List<Date> findFixedDatesForNotStartedOnes() {
        sessionFactoryManager.openCurrentSession();

        Query jobQuery = sessionFactoryManager.getCurrentSession().createQuery("select distinct j.fixedDate from Job j where j.endDate is null and j.startDate is null");
        List datesFromDb = jobQuery.getResultList();

        sessionFactoryManager.closeCurrentSession();

        return convertToSqlDates(datesFromDb);
    }

    @Override
    public List<Date> findFixedDatesWithStartDate() {
        sessionFactoryManager.openCurrentSession();

        Query jobQuery = sessionFactoryManager.getCurrentSession().createQuery("select distinct j.fixedDate from Job j where j.endDate is null and j.startDate is not null");
        List datesFromDb = jobQuery.getResultList();

        sessionFactoryManager.closeCurrentSession();

        return convertToSqlDates(datesFromDb);
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

    private List<Date> convertToSqlDates(List dates) {
        List<Date> sqlDatesFromDb = new ArrayList<>();

        for (Object date : dates) {
            sqlDatesFromDb.add((Date) date);
        }

        return sqlDatesFromDb;
    }
}
