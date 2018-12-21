package pl.edu.pwsztar.dao.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.dao.JobDAO;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.util.HibernateUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class JobRepository implements JobDAO {

    @Override
    public List<Job> findHistoryByClient(Client client) {
        Query<Job> jobQuery = HibernateUtil.getOrOpenSession().createQuery("from Job where client = :client and endDate is not null", Job.class);
        jobQuery.setParameter("client", client);

        List<Job> jobsFromDb = jobQuery.getResultList();

        HibernateUtil.closeSession();

        return jobsFromDb;
    }

    @Override
    public List<Job> findHistoryByVehicle(Vehicle vehicle) {
        Query<Job> jobQuery = HibernateUtil.getOrOpenSession().createQuery("from Job where vehicle = :vehicle and endDate is not null", Job.class);
        jobQuery.setParameter("vehicle", vehicle);

        List<Job> jobsFromDb = jobQuery.getResultList();

        HibernateUtil.closeSession();

        return jobsFromDb;
    }

    @Override
    public List<Job> findHistoryByVinNumber(String vinNumber) {
        Query<Job> jobQuery = HibernateUtil.getOrOpenSession().createQuery("from Job where vehicle.vinNumber = :vinNumber and endDate is not null", Job.class);
        jobQuery.setParameter("vinNumber", vinNumber);

        List<Job> jobsFromDb = jobQuery.getResultList();

        HibernateUtil.closeSession();

        return jobsFromDb;
    }

    @Override
    public List<Job> findAllByDate(Date date) {
        Query<Job> jobQuery = HibernateUtil.getOrOpenSession().createQuery("from Job where fixedDate = :date", Job.class);
        jobQuery.setParameter("date", date);

        List<Job> jobsFromDb = jobQuery.getResultList();

        HibernateUtil.closeSession();

        return jobsFromDb;
    }

    @Override
    public List<Date> findFixedDatesForNotStartedOnes() {
        Query jobQuery = HibernateUtil.getOrOpenSession().createQuery("select distinct j.fixedDate from Job j where j.endDate is null and j.startDate is null");
        List datesFromDb = jobQuery.getResultList();

        HibernateUtil.closeSession();

        return convertToSqlDates(datesFromDb);
    }

    @Override
    public List<Date> findFixedDatesWithStartDate() {
        Query jobQuery = HibernateUtil.getOrOpenSession().createQuery("select distinct j.fixedDate from Job j where j.endDate is null and j.startDate is not null");
        List datesFromDb = jobQuery.getResultList();

        HibernateUtil.closeSession();

        return convertToSqlDates(datesFromDb);
    }

    @Override
    public void add(Job job, Vehicle vehicle, Client client, boolean isVehicleNew) {
        HibernateUtil.getOrOpenSession();

        if (isVehicleNew) {
            HibernateUtil.withinTransaction(() -> {
                vehicle.addJob(job, client);
                HibernateUtil.getOrOpenSession().save(vehicle);
            });

            HibernateUtil.closeSession();
        }

        HibernateUtil.withinTransaction(() -> {
            job.setVehicle(vehicle);
            job.setClient(client);

            HibernateUtil.getOrOpenSession().save(job);
        });

        HibernateUtil.closeSession();
    }

    private List<Date> convertToSqlDates(List dates) {
        List<Date> sqlDatesFromDb = new ArrayList<>();

        for (Object date : dates) {
            sqlDatesFromDb.add((Date) date);
        }

        return sqlDatesFromDb;
    }
}
