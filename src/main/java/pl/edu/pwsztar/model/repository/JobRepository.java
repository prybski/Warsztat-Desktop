package pl.edu.pwsztar.model.repository;

import org.hibernate.query.Query;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.model.dao.JobDAO;
import pl.edu.pwsztar.util.HibernateUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class JobRepository implements JobDAO {

    @Override
    public void addWithExistingVehicle(Job job) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().save(job));
    }

    @Override
    public List<Job> findAllStarted() {
        AtomicReference<List<Job>> jobsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Job> jobQuery = HibernateUtil.getSession().createQuery("from Job j where j.startDate is not null and j.endDate is null order by j.id desc", Job.class);

            jobsFromDb.set(jobQuery.getResultList());
        });

        return jobsFromDb.get();
    }

    @Override
    public List<Job> findHistoryByClient(Client client) {
        AtomicReference<List<Job>> jobsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Job> jobQuery = HibernateUtil.getSession().createQuery("from Job where client = :client and endDate is not null order by id desc", Job.class);
            jobQuery.setParameter("client", client);

            jobsFromDb.set(jobQuery.getResultList());
        });

        return jobsFromDb.get();
    }

    @Override
    public List<Job> findHistoryByVehicle(Vehicle vehicle) {
        AtomicReference<List<Job>> jobsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Job> jobQuery = HibernateUtil.getSession().createQuery("from Job where vehicle = :vehicle and endDate is not null order by id desc", Job.class);
            jobQuery.setParameter("vehicle", vehicle);

            jobsFromDb.set(jobQuery.getResultList());
        });

        return jobsFromDb.get();
    }

    @Override
    public List<Job> findHistoryByVinNumber(String vinNumber) {
        AtomicReference<List<Job>> jobsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Job> jobQuery = HibernateUtil.getSession().createQuery("from Job where vehicle.vinNumber = :vinNumber and endDate is not null order by id desc", Job.class);
            jobQuery.setParameter("vinNumber", vinNumber);

            jobsFromDb.set(jobQuery.getResultList());
        });

        return jobsFromDb.get();
    }

    @Override
    public List<Job> findNotStartedByFixedDate(Date date) {
        AtomicReference<List<Job>> jobsFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query<Job> jobQuery = HibernateUtil.getSession().createQuery("from Job where fixedDate = :date and startDate is null and endDate is null order by id desc", Job.class);
            jobQuery.setParameter("date", date);

            jobsFromDb.set(jobQuery.getResultList());
        });

        return jobsFromDb.get();
    }

    @Override
    public List<Date> findNotStartedFixedDates() {
        AtomicReference<List> datesFromDb = new AtomicReference<>();

        HibernateUtil.withinSession(() -> {
            Query jobQuery = HibernateUtil.getSession().createQuery("select distinct j.fixedDate from Job j where j.endDate is null and j.startDate is null order by j.id desc");

            datesFromDb.set(jobQuery.getResultList());
        });


        return convertToSqlDates(datesFromDb.get());
    }

    @Override
    public void update(Job job) {
        HibernateUtil.withinTransaction(() -> HibernateUtil.getSession().update(job));
    }

    private List<Date> convertToSqlDates(List dates) {
        List<Date> sqlDatesFromDb = new ArrayList<>();

        for (Object date : dates) {
            sqlDatesFromDb.add((Date) date);
        }

        return sqlDatesFromDb;
    }
}
