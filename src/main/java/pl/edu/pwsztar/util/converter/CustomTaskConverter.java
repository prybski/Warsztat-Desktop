package pl.edu.pwsztar.util.converter;

import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import pl.edu.pwsztar.entity.Task;

public class CustomTaskConverter extends StringConverter<Task> {

    private final ListCell<Task> taskCell;

    public CustomTaskConverter(ListCell<Task> taskCell) {
        this.taskCell = taskCell;
    }

    @Override
    public String toString(Task task) {
        return task.getActivity();
    }

    @Override
    public Task fromString(String activity) {
        Task taskFromCell = taskCell.getItem();
        taskFromCell.setActivity(activity);

        return taskFromCell;
    }
}
