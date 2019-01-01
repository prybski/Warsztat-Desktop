package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Task;

import java.util.List;

public interface TaskDAO {

    void add(Task task);
    List<Task> findAllByJob(Job job);
    void update(Task task);
    void delete(Task task);
}
