package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.StageUtil;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Singleton singleton;
    private Stage parentStage;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ChoiceBox<Date> fixedDates;

    @FXML
    private ListView<Job> jobs;

    @FXML
    private ChoiceBox<Date> fixedDatesWithStartDate;

    @FXML
    private ListView<Job> startedJobs;

    @FXML
    private ChoiceBox<Client> clients;

    @FXML
    private ListView<Job> clientHistory;

    @FXML
    private TextField firstAndLastName;

    @FXML
    private TextField vinNumber;

    @FXML
    private ListView<Job> vehicleHistoryByVinNumber;

    @FXML
    private ChoiceBox<Vehicle> vehicles;

    @FXML
    private ListView<Job> vehicleHistory;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fixedDates.getItems().setAll(singleton.getJobRepository().findNotStartedFixedDates());

        fixedDates.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue)
                        -> jobs.getItems().setAll(singleton.getJobRepository().findByFixedDate(newValue)));

        fixedDatesWithStartDate.getItems().setAll(singleton.getJobRepository().findStartedFixedDates());

        fixedDatesWithStartDate.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue)
                        -> startedJobs.getItems().setAll(singleton.getJobRepository().findByFixedDate(newValue)));

        clients.getItems().setAll(singleton.getClientRepository().findAll());

        clients.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> clientHistory.getItems().setAll(singleton.getJobRepository().findHistoryByClient(clients.getSelectionModel().getSelectedItem())));

        vinNumber.focusedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        vehicleHistoryByVinNumber.getItems().setAll(singleton.getJobRepository().findHistoryByVinNumber(vinNumber.getText()));
                    }
                });

        vehicles.getItems().setAll(singleton.getVehicleRepository().findAll());

        vehicles.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> vehicleHistory.getItems().setAll(singleton.getJobRepository().findHistoryByVehicle(vehicles.getSelectionModel().getSelectedItem())));
    }

    public void showAddTask(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/task-create.fxml"));

        try {
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();

            StageUtil.stageConfiguration(anchorPane, "Dodaj zadanie", stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddClient(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/client-create.fxml"));

        try {
            AnchorPane anchorPane = loader.load();
            Stage childStage = new Stage();

            ClientCreateController clientCreateController = loader.getController();
            clientCreateController.setChildStage(childStage);
            clientCreateController.setParentStage(parentStage);

            StageUtil.stageConfiguration(anchorPane, "Dodaj klienta", childStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddJob(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/job-create.fxml"));

        try {
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();

            StageUtil.stageConfiguration(anchorPane, "Dodaj zlecenie", stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showClientData(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/client-details.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();

            ClientDetailsController clientDetailsController = loader.getController();

            Client clientFoundInDb = singleton.getClientRepository().findOneByFirstAndLastName(firstAndLastName.getText());

            clientDetailsController.setClient(clientFoundInDb);

            StageUtil.stageConfiguration(anchorPane, "Dane klienta", stage);
        } catch (NoResultException ex) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", ButtonType.OK,
                    "Nie udało się odnaleźć klienta o podanym imieniu i nazwisku!", "Błąd!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startManaging(MouseEvent mouseEvent) {
        if (!jobs.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/job-management.fxml"));

            try {
                BorderPane tempBorderPane = loader.load();
                tempBorderPane.setTop(borderPane.getTop());

                JobManagementController jobManagementController = loader.getController();
                jobManagementController.setJob(jobs.getSelectionModel().getSelectedItem());

                borderPane.getScene().setRoot(tempBorderPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }
}
