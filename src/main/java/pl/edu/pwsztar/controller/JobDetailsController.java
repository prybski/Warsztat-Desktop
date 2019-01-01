package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import pl.edu.pwsztar.entity.Demand;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Task;
import pl.edu.pwsztar.singleton.Singleton;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class JobDetailsController implements Initializable {

    private Job job;
    private Singleton singleton;

    @FXML
    private Text startDate;

    @FXML
    private Text endDate;

    @FXML
    private Text discount;

    @FXML
    private ListView<Task> doneTasks;

    @FXML
    private ListView<Demand> requiredDemands;

    @FXML
    private Text finalCost;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::readJobData);
    }

    private void readJobData() {
        String startDateString = job.getStartDate().toString().substring(0, job.getStartDate().toString()
                .lastIndexOf("."));
        String endDateString = job.getEndDate().toString().substring(0, job.getEndDate().toString()
                .lastIndexOf("."));

        startDate.setText(startDateString);
        endDate.setText(endDateString);

        if (job.getDiscount() != null) {
            discount.setText(job.getDiscount().toString() + " zł");
        } else {
            discount.setText("Brak zniżki");
        }

        List<Task> tasks = singleton.getTaskRepository().findAllByJob(job);

        doneTasks.getItems().setAll(tasks);

        List<Demand> demands = singleton.getDemandRepository().findAllByTasks(tasks);

        requiredDemands.getItems().setAll(demands);

        finalCost.setText(calculateFinalCost().toString() + " zł");
    }

    private BigDecimal calculateFinalCost() {
        BigDecimal finalCost = new BigDecimal(0);

        List<Task> tasks = doneTasks.getItems();
        List<Demand> demands = requiredDemands.getItems();

        for (Task task : tasks) {
            finalCost = finalCost.add(task.getCost());
        }

        for (Demand demand : demands) {
            finalCost = finalCost.add(demand.getPrice());
        }

        if (job.getDiscount() != null) {
            finalCost = finalCost.subtract(job.getDiscount());
        }

        return finalCost;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
