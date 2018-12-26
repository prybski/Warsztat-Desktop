package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;
import pl.edu.pwsztar.entity.Demand;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Part;
import pl.edu.pwsztar.entity.Task;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.scene.control.NoSelectionModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JobManagementController implements Initializable {

    private Job job;
    private Singleton singleton;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button start;

    @FXML
    private Button end;

    @FXML
    private TextField taskName;

    @FXML
    private ListView<Task> unfinishedTasks;

    @FXML
    private VBox demandVBox;

    @FXML
    private CheckBox arePartsRequired;

    @FXML
    private ChoiceBox<Part> parts;

    @FXML
    private ChoiceBox<Task> tasks;

    @FXML
    private Spinner<Integer> quantity;

    @FXML
    private TextField demandPrice;

    @FXML
    private ListView<Demand> demands;
    
    @FXML
    private ListView<Task> finishedTasks;

    @FXML
    private TextField taskCost;

    @FXML
    private CheckBox isDiscountIncluded;

    @FXML
    private TextField discount;

    @FXML
    private Text finalJobCost;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> taskNames = new ArrayList<>();
        String line;

        try (InputStream inputStream = getClass().getResourceAsStream("/txt/BAZA_ZADAN.txt")) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                while ((line = bufferedReader.readLine()) != null) {
                    taskNames.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextFields.bindAutoCompletion(taskName, taskNames);

        Platform.runLater(() -> {
            if (job.getStartDate() != null) {
                start.setDisable(true);
                end.setDisable(false);
            } else {
                start.setDisable(false);
                end.setDisable(true);
            }

            unfinishedTasks.getItems().setAll(singleton.getTaskRepository().findAllByJob(job));

            tasks.getItems().setAll(singleton.getTaskRepository().findAllByJob(job));
            parts.getItems().setAll(singleton.getPartRepository().findAll());
            finishedTasks.setSelectionModel(new NoSelectionModel<>());
            finishedTasks.getItems().setAll(singleton.getTaskRepository().findAllByJob(job));

            demands.getItems().setAll(singleton.getDemandRepository().findAllByTasks(singleton.getTaskRepository().findAllByJob(job)));

            if (!demands.getItems().isEmpty()) {
                arePartsRequired.setSelected(true);
                demandVBox.setDisable(false);
            }
        });

        arePartsRequired.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                demandVBox.setDisable(false);
            } else {
                demandVBox.setDisable(true);
            }
        });

        SpinnerValueFactory<Integer> quantityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 255, 1, 1);
        quantity.setValueFactory(quantityValueFactory);

        finishedTasks.setCellFactory(CheckBoxListCell.forListView(task -> {
            BooleanProperty property = new SimpleBooleanProperty();

            property.addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    task.setIsFinished(true);
                    task.setCost(new BigDecimal(taskCost.getText()));

                    singleton.getTaskRepository().update(task);
                }
            });

            return property;
        }));

        isDiscountIncluded.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                discount.setDisable(false);
            } else {
                discount.setDisable(true);
            }
        });
    }

    public void startJob(ActionEvent actionEvent) {
        singleton.getJobRepository().updateStartDate(job, Timestamp.valueOf(LocalDateTime.now()));

        start.setDisable(true);
    }

    public void backToMain(ActionEvent actionEvent) {
        try {
            borderPane.getScene().setRoot(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endJob(ActionEvent actionEvent) {
        BigDecimal finalCost = new BigDecimal(0);

        job.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
        job.setDiscount(new BigDecimal(discount.getText()));
        singleton.getJobRepository().update(job);

        for (Task task : finishedTasks.getItems()) {
            finalCost = finalCost.add(task.getCost());
        }

        for (Demand demand : demands.getItems()) {
            finalCost = finalCost.add(demand.getPrice());
        }

        finalCost = finalCost.subtract(job.getDiscount());

        finalJobCost.setText(finalCost.toString());
    }

    public void addTaskToJob(ActionEvent actionEvent) {
        Task task = new Task(taskName.getText());
        task.setJob(job);

        singleton.getTaskRepository().add(task);

        unfinishedTasks.getItems().setAll(singleton.getTaskRepository().findAllByJob(job));
    }

    public void deleteChoosenTask(ActionEvent actionEvent) {
        singleton.getTaskRepository().delete(unfinishedTasks.getSelectionModel().getSelectedItem());

        unfinishedTasks.getItems().setAll(singleton.getTaskRepository().findAllByJob(job));
    }

    public void addDemandToTask(ActionEvent actionEvent) {
        Demand demand = new Demand(quantity.getValue().byteValue(), new BigDecimal(demandPrice.getText()));
        demand.setTask(tasks.getValue());
        demand.setPart(parts.getValue());

        singleton.getDemandRepository().add(demand);

        demands.getItems().setAll(singleton.getDemandRepository().findAllByTasks(singleton.getTaskRepository().findAllByJob(job)));
    }

    public void deleteChoosenDemand(ActionEvent actionEvent) {
        singleton.getDemandRepository().delete(demands.getSelectionModel().getSelectedItem());

        demands.getItems().setAll(singleton.getDemandRepository().findAllByTasks(singleton.getTaskRepository().findAllByJob(job)));
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
