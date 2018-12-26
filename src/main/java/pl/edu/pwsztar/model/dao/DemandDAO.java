package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Demand;
import pl.edu.pwsztar.entity.Task;

import java.util.List;

public interface DemandDAO {

    void add(Demand demand);
    List<Demand> findAllByTasks(List<Task> tasks);
    void delete(Demand demand);
}
