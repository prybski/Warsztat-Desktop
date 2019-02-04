package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import pl.edu.pwsztar.entity.Demand;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Part;
import pl.edu.pwsztar.entity.Task;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.ContextMenuUtil;
import pl.edu.pwsztar.util.NumericUtil;
import pl.edu.pwsztar.util.StageUtil;
import pl.edu.pwsztar.util.converter.CustomTaskConverter;
import pl.edu.pwsztar.util.converter.SimpleTaskConverter;

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
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class JobManagementController implements Initializable {

    private Job job;
    private Singleton singleton;
    private static final int MAX_TASK_COST_LENGTH;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button start;

    @FXML
    private Button end;

    @FXML
    private Button addOneTask;

    @FXML
    private Button deleteOneTask;

    @FXML
    private Button addOneDemand;

    @FXML
    private Button deleteOneDemand;

    @FXML
    private TextField taskName;

    @FXML
    private ListView<Task> unfinishedTasks;

    @FXML
    private VBox demandVBox;

    @FXML
    private HBox mainControlHBox;

    @FXML
    private CheckBox arePartsRequired;

    @FXML
    private ComboBox<Part> parts;

    @FXML
    private ComboBox<Task> tasks;

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

    @FXML
    private Label labelHeader;

    @FXML
    private TextField vehicleVinNumber;

    private ListChangeListener<Integer> tasksToFinishListener;

    static {
        MAX_TASK_COST_LENGTH = 8;
    }

    {
        singleton = Singleton.getInstance();

        tasksToFinishListener = changed -> {
            while (changed.next()) {
                if (changed.wasAdded()) {
                    for (int i : changed.getAddedSubList()) {
                        Task task = tasksToFinish.getItems().get(i);

                        if (!task.getIsFinished()) {
                            task.setIsFinished(true);
                            task.setCost(new BigDecimal(taskCost.getText().trim()));

                            singleton.getTaskRepository().update(task);

                            refreshOrLoadFinishedTasks();
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

                            refreshOrLoadFinishedTasks();
                        }
                    }
                }
            }
        };
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::configureForRunLater);

        readPredefinedTasks();

        propertyBindingsConfiguration();

        customListenersConfiguration();

        removeContextMenu();
    }

    public void startJob() {
        job.setStartDate(Timestamp.valueOf(LocalDateTime.now()));

        singleton.getJobRepository().update(job);

        start.setDisable(true);
        mainControlHBox.setDisable(false);
        isDiscountIncluded.setDisable(false);
    }

    public void showAddPart() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/part-create.fxml"));

        try {
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();

            StageUtil.stageConfiguration(anchorPane, stage, "Dodaj część");

            refreshOrLoadParts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTaskToJob() {
        Task task = new Task(taskName.getText().trim());
        task.setJob(job);

        try {
            singleton.getTaskRepository().add(task);

            taskName.clear();

            refreshOrLoadTasks();
            refreshOrLoadFinishedTasks();
            refreshOrLoadUnfinishedTasks();
        } catch (HibernateException e) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", "Błąd prawdopodobnie " +
                    "został spowodowany przez: \n" +
                    "\n- wprowadzenie danych, które przekraczają zakresy ustawione dla poszczególnych kolumn " +
                    "w bazie danych" +
                    "\n- wprowadzenie zduplikowanych wartości do kolumn podlegających ograniczeniu unikalności");
        }
    }

    public void deleteChoosenTask() {
        Task taskToDelete = unfinishedTasks.getSelectionModel().getSelectedItem();

        tasksToFinish.getCheckModel().clearCheck(taskToDelete);

        singleton.getTaskRepository().delete(taskToDelete);

        refreshOrLoadTasks();
        refreshOrLoadFinishedTasks();
        refreshOrLoadUnfinishedTasks();
        refreshOrLoadDemands();
    }

    public void addDemandToTask() {
        BigDecimal singlePrice = new BigDecimal(demandPrice.getText().trim());
        BigDecimal singlePriceMultiplier = new BigDecimal(quantity.getValue());

        Demand demand = new Demand(quantity.getValue().byteValue(), singlePrice.multiply(singlePriceMultiplier));
        demand.setTask(tasks.getValue());
        demand.setPart(parts.getValue());

        try {
            singleton.getDemandRepository().add(demand);

            refreshOrLoadDemands();
        } catch (HibernateException e) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", "Błąd prawdopodobnie " +
                    "został spowodowany przez: \n" +
                    "\n- wprowadzenie danych, które przekraczają zakresy ustawione dla poszczególnych kolumn " +
                    "w bazie danych" +
                    "\n- wprowadzenie zduplikowanych wartości do kolumn podlegających ograniczeniu unikalności");
        }
    }

    public void deleteChoosenDemand() {
        singleton.getDemandRepository().delete(demands.getSelectionModel().getSelectedItem());

        refreshOrLoadDemands();
    }

    public void endJob() {
        BigDecimal finalCost = new BigDecimal(0);

        if (isDiscountIncluded.isSelected()) {
            job.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
            job.setDiscount(new BigDecimal(discount.getText().trim()));

            try {
                singleton.getJobRepository().update(job);

                finalCost = calculateFinalCost(finalCost);

                finalCost = finalCost.subtract(job.getDiscount());

                finalJobCost.setText(finalCost.toString() + " zł");
            } catch (HibernateException e) {
                StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", "Błąd prawdopodobnie " +
                        "został spowodowany przez: \n" +
                        "\n- wprowadzenie danych, które przekraczają zakresy ustawione dla poszczególnych kolumn " +
                        "w bazie danych" +
                        "\n- wprowadzenie zduplikowanych wartości do kolumn podlegających ograniczeniu unikalności");
            }
        } else {
            job.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
            job.setDiscount(null);
            singleton.getJobRepository().update(job);

            finalCost = calculateFinalCost(finalCost);

            finalJobCost.setText(finalCost.toString() + " zł");
        }
    }

    public void backToMain() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/main.fxml"));

        try {
            borderPane.getScene().setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        List<Task> foundUnfinishedTasks = singleton.getTaskRepository().findAllByJob(job);

        unfinishedTasks.getItems().setAll(foundUnfinishedTasks);
    }

    private void refreshOrLoadFinishedTasks() {
        List<Task> foundTasks = singleton.getTaskRepository().findAllByJob(job);

        if (foundTasks.isEmpty()) {
            tasksToFinish.getCheckModel().getCheckedIndices().removeListener(tasksToFinishListener);
            tasksToFinish.getCheckModel().clearChecks();
            tasksToFinish.getCheckModel().getCheckedIndices().addListener(tasksToFinishListener);

            tasksToFinish.getItems().clear();
        } else {
            tasksToFinish.getItems().setAll(foundTasks);

            for (Task task : foundTasks) {
                if (task.getIsFinished()) {
                    tasksToFinish.getCheckModel().check(task);
                } else {
                    tasksToFinish.getCheckModel().clearCheck(task);
                }
            }
        }
    }

    private void refreshOrLoadTasks() {
        List<Task> foundTasks = singleton.getTaskRepository().findAllByJob(job);

        tasks.getSelectionModel().clearSelection();
        tasks.getItems().setAll(foundTasks);
    }

    private void refreshOrLoadParts() {
        List<Part> foundParts = singleton.getPartRepository().findAll();

        parts.getSelectionModel().clearSelection();
        parts.getItems().setAll(foundParts);
    }

    private void refreshOrLoadDemands() {
        List<Demand> foundDemands = singleton.getDemandRepository().findAllByTasks(singleton.getTaskRepository()
                .findAllByJob(job));

        demands.getItems().setAll(foundDemands);
    }

    private void readPredefinedTasks() {
        List<String> taskNames = new ArrayList<>();
        String line;

        try (InputStream inputStream = getClass().getResourceAsStream("/txt/BAZA_ZADAN.txt")) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
                    StandardCharsets.UTF_8))) {
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
    }

    private void configureForRunLater() {
        String jobShortInfo = String.format("Zlecenie (Opis: %s)", job.getDescription());

        labelHeader.setText(jobShortInfo);

        vehicleVinNumber.setText(job.getVehicle().getVinNumber());

        if (job.getStartDate() != null) {
            start.setDisable(true);
            mainControlHBox.setDisable(false);
            isDiscountIncluded.setDisable(false);
            end.setDisable(false);
        } else {
            start.setDisable(false);
            mainControlHBox.setDisable(true);
            isDiscountIncluded.setDisable(true);
            end.setDisable(true);
        }

        refreshOrLoadUnfinishedTasks();
        refreshOrLoadTasks();
        refreshOrLoadParts();
        refreshOrLoadFinishedTasks();
        refreshOrLoadDemands();

        propertyBindingsConfigurationForRunLater();

        if (!demands.getItems().isEmpty()) {
            arePartsRequired.setSelected(true);
        }
    }

    private void customListenersConfiguration() {
        unfinishedTasks.setCellFactory(listView -> configureCustomCellFactoryForTasks());

        tasks.setCellFactory(listView -> configureCustomCellFactoryForTasks());
        tasks.setConverter(new SimpleTaskConverter(tasks));

        tasksToFinish.getCheckModel().getCheckedIndices().addListener(tasksToFinishListener);
    }

    private void propertyBindingsConfigurationForRunLater() {
        BooleanBinding taskNameValid = Bindings.createBooleanBinding(() -> !taskName.getText().isEmpty(),
                taskName.textProperty());
        BooleanBinding demandDataValid = Bindings.createBooleanBinding(() -> parts.getSelectionModel()
                        .getSelectedItem() != null && tasks.getSelectionModel().getSelectedItem() != null &&
                        (!demandPrice.getText().isEmpty() && NumericUtil.isBigDecimal(demandPrice.getText())),
                parts.valueProperty(), tasks.valueProperty(), demandPrice.textProperty());
        BooleanBinding unfinishedTasksEmpty = Bindings.isEmpty(unfinishedTasks.getItems());
        BooleanBinding allRequiredDataValid = Bindings.createBooleanBinding(() -> (!tasksToFinish.getItems().isEmpty()
                        && (tasksToFinish.getCheckModel().getCheckedItems().stream().filter(Objects::nonNull)
                        .collect(Collectors.toList()).size() == tasksToFinish.getItems().size()) && !isDiscountIncluded
                        .isSelected()) || (!tasksToFinish.getItems().isEmpty() && (tasksToFinish.getCheckModel()
                        .getCheckedItems().stream().filter(Objects::nonNull).collect(Collectors.toList()).size()
                        == tasksToFinish.getItems().size()) && (isDiscountIncluded.isSelected() && (!discount
                        .getText().isEmpty() && NumericUtil.isBigDecimal(discount.getText())))),
                        tasksToFinish.getCheckModel().getCheckedItems(), discount.textProperty(), isDiscountIncluded
                        .selectedProperty());

        addOneTask.disableProperty().bind(taskNameValid.not());

        addOneDemand.disableProperty().bind(demandDataValid.not());

        taskCost.disableProperty().bind(unfinishedTasksEmpty);

        end.disableProperty().bind(allRequiredDataValid.not());
    }

    private void propertyBindingsConfiguration() {
        BooleanBinding unfinishedTaskSelected = Bindings.createBooleanBinding(() -> !unfinishedTasks.getSelectionModel()
                .isEmpty(), unfinishedTasks.getSelectionModel().selectedIndexProperty());
        BooleanBinding demandIsSelected = Bindings.createBooleanBinding(() -> !demands.getSelectionModel().isEmpty(),
                demands.getSelectionModel().selectedIndexProperty());
        BooleanBinding partsRequiredNotChecked = arePartsRequired.selectedProperty().not();
        BooleanBinding taskCostValid = Bindings.createBooleanBinding(() -> !taskCost.getText().isEmpty()
                        && NumericUtil.isBigDecimal(taskCost.getText()),
                taskCost.textProperty());
        BooleanBinding discountIncludedNotChecked = isDiscountIncluded.selectedProperty().not();

        deleteOneTask.disableProperty().bind(unfinishedTaskSelected.not());

        discount.disableProperty().bind(discountIncludedNotChecked);

        deleteOneDemand.disableProperty().bind(demandIsSelected.not());

        demandVBox.disableProperty().bind(partsRequiredNotChecked);

        tasksToFinish.disableProperty().bind(taskCostValid
                .not()
                .or(taskCost.lengthProperty().greaterThan(MAX_TASK_COST_LENGTH)));
    }

    private TextFieldListCell<Task> configureCustomCellFactoryForTasks() {
        TextFieldListCell<Task> taskCell = new TextFieldListCell<>();
        taskCell.setConverter(new CustomTaskConverter(taskCell));

        return taskCell;
    }

    private void removeContextMenu() {
        ContextMenuUtil.remove(taskName, quantity, demandPrice, taskCost, discount, vehicleVinNumber);
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
