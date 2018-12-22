package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;

import java.sql.Date;
import java.util.List;

public interface JobDAO {

    List<Job> findHistoryByClient(Client client);
    List<Job> findHistoryByVehicle(Vehicle vehicle);
    List<Job> findHistoryByVinNumber(String vinNumber);
    List<Job> findAllByDate(Date date);
    List<Date> findFixedDatesForNotStartedOnes();
    List<Date> findFixedDatesWithStartDate();
    void add(Job job, Vehicle vehicle, Client client, boolean isVehicleNew);
}
