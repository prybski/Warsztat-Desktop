package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.textfield.TextFields;
import pl.edu.pwsztar.entity.Demand;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Part;
import pl.edu.pwsztar.entity.Task;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.StageUtil;

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
    private CheckListView<Task> tasksToFinish;

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

            refreshOrLoadFinishedTasks();

            demands.getItems().setAll(singleton.getDemandRepository().findAllByTasks(singleton.getTaskRepository().findAllByJob(job)));

            if (!demands.getItems().isEmpty()) {
                arePartsRequired.setSelected(true);
                demandVBox.setDisable(false);
            }

            end.disableProperty().bind(Bindings.createBooleanBinding(() -> !tasksToFinish.getCheckModel().getCheckedIndices().isEmpty() && (tasksToFinish.getCheckModel().getCheckedIndices().size() == tasksToFinish.getItems().size()), tasksToFinish.getCheckModel().getCheckedIndices()).not());
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

        BooleanBinding taskCostValid = Bindings.createBooleanBinding(() -> !taskCost.getText().isEmpty(), taskCost.textProperty());

        tasksToFinish.disableProperty().bind(taskCostValid.not());

        tasksToFinish.getCheckModel().getCheckedIndices().addListener((ListChangeListener<? super Integer>) changed -> {
            while (changed.next()) {
                if (changed.wasAdded()) {
                    for (int i : changed.getAddedSubList()) {
                        Task task = tasksToFinish.getItems().get(i);

                        if (!task.getIsFinished()) {
                            task.setIsFinished(true);
                            task.setCost(new BigDecimal(taskCost.getText()));

                            singleton.getTaskRepository().update(task);
                        }
                    }
                }
                if (changed.wasRemoved()) {
                    for (int i : changed.getRemoved()) {
                        Task task = tasksToFinish.getItems().get(i);

                        if (task.getIsFinished()) {
                            task.setIsFinished(false);
                            task.setCost(null);

                            singleton.getTaskRepository().update(task);
                        }
                    }
                }
            }
        });

        isDiscountIncluded.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                discount.setDisable(false);

                BooleanBinding discountValid = Bindings.createBooleanBinding(() -> {
                    try {
                        Double.parseDouble(discount.getText().trim());

                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }, discount.textProperty());

                end.disableProperty().bind(discountValid.not());
            } else {
                end.disableProperty().unbind();
                end.setDisable(false);

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

        try {
            if (tasksToFinish.getItems().isEmpty()) {
                throw new NullPointerException();
            }

            if (isDiscountIncluded.isSelected()) {
                job.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
                job.setDiscount(new BigDecimal(discount.getText()));
                singleton.getJobRepository().update(job);

                finalCost = calculateFinalCost(finalCost);

                finalCost = finalCost.subtract(job.getDiscount());
            } else {
                job.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
                job.setDiscount(null);
                singleton.getJobRepository().update(job);

                finalCost = calculateFinalCost(finalCost);
            }

            finalJobCost.setText(finalCost.toString() + " zł");

            end.disableProperty().unbind();
            end.setDisable(true);
        } catch (NullPointerException e) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", ButtonType.OK, "Musisz zakończyć wszystkie zadania przed podsumowaniem zlecenia!", "Błąd!");
        }
    }

    public void addTaskToJob(ActionEvent actionEvent) {
        Task task = new Task(taskName.getText());
        task.setJob(job);

        singleton.getTaskRepository().add(task);

        refreshOrLoadTasks();
        refreshOrLoadFinishedTasks();
        refreshOrLoadUnfinishedTasks();
    }

    public void deleteChoosenTask(ActionEvent actionEvent) {
        singleton.getTaskRepository().delete(unfinishedTasks.getSelectionModel().getSelectedItem());

        refreshOrLoadTasks();
        refreshOrLoadFinishedTasks();
        refreshOrLoadUnfinishedTasks();
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

        refreshOrLoadTasks();
        refreshOrLoadFinishedTasks();
        refreshOrLoadUnfinishedTasks();
    }

    private BigDecimal calculateFinalCost(BigDecimal costHolder) {
        for (Task task : tasksToFinish.getItems()) {
            costHolder = costHolder.add(task.getCost());
        }

        if (arePartsRequired.isSelected()) {
            for (Demand demand : demands.getItems()) {
                costHolder = costHolder.add(demand.getPrice());
            }
        }

        return costHolder;
    }

    private void refreshOrLoadUnfinishedTasks() {
        unfinishedTasks.getItems().setAll(singleton.getTaskRepository().findAllByJob(job));
    }

    private void refreshOrLoadFinishedTasks() {
        tasksToFinish.getItems().setAll(singleton.getTaskRepository().findAllByJob(job));

        for (Task task : singleton.getTaskRepository().findAllByJob(job)) {
            if (task.getIsFinished()) {
                tasksToFinish.getCheckModel().check(task);
            } else {
                tasksToFinish.getCheckModel().clearCheck(task);
            }
        }
    }

    private void refreshOrLoadTasks() {
        tasks.getItems().setAll(singleton.getTaskRepository().findAllByJob(job));
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
