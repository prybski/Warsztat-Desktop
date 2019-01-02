package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;

import java.sql.Date;
import java.util.List;

public interface JobDAO {

    void addWithExistingVehicle(Job job);
    List<Job> findAllStarted();
    List<Job> findHistoryByClient(Client client);
    List<Job> findHistoryByVehicle(Vehicle vehicle);
    List<Job> findHistoryByVinNumber(String vinNumber);
    List<Job> findNotStartedByFixedDate(Date date);
    List<Date> findNotStartedFixedDates();
    void update(Job job);
}
