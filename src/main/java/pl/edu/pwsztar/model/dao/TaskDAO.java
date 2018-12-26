package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Task;

import java.util.List;

public interface TaskDAO {

    List<Task> findAllByJob(Job job);
    void add(Task task);
    void delete(Task task);
    void update(Task task);
}
