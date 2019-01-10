package pl.edu.pwsztar.util.converter;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import pl.edu.pwsztar.entity.Task;

public class SimpleTaskConverter extends StringConverter<Task> {

    private final ComboBox<Task> tasks;

    public SimpleTaskConverter(ComboBox<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString(Task task) {
        return task.getActivity();
    }

    @Override
    public Task fromString(String activity) {
        Task task = tasks.getSelectionModel().getSelectedItem();
        task.setActivity(activity);

        return task;
    }
}
