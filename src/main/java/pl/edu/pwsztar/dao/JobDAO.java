package pl.edu.pwsztar.dao;

import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;

import java.sql.Date;
import java.util.List;

public interface JobDAO {

    List<Job> findAllByDate(Date date);
    List<Date> findJobsFixedDates();
    void addWithNewVehicle(Job job, Vehicle vehicle, Client client);
    void addWithoutNewVehicle(Job job, Vehicle vehicle, Client client);
}
