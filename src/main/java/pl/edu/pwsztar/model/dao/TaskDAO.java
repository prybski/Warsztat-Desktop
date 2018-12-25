package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Task;

public interface TaskDAO {

    void add(Job job, Task task);
}
