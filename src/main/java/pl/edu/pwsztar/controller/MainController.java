package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    @FXML
    private BorderPane borderPane;

    @FXML
    private ChoiceBox<Date> fixedDates;

    @FXML
    private ListView<Job> jobs;

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

    @FXML
    private Button searchVehicle;

    @FXML
    private Button showClient;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // sekcja ładowania danych w kontrolkach FXML
        refreshOrLoadFixedDates();
        refreshOrLoadClients();
        refreshOrLoadStartedJobs();
        refreshOrLoadVehicles();

        // sekcja wiązania ze sobą dynamicznych walidatorów
        propertyBindingsConfiguration();

        // sekcja ładowania konfiguracji niestandardowych nasłuchiwaczy
        customListenersConfiguration();
    }

    public void showAddClient() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/client-create.fxml"));

        try {
            AnchorPane anchorPane = loader.load();
            Stage childStage = new Stage();

            StageUtil.stageConfiguration(anchorPane, "Dodaj klienta", childStage);

            refreshOrLoadClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddJob() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/job-create.fxml"));

        try {
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();

            StageUtil.stageConfiguration(anchorPane, "Dodaj zlecenie", stage);

            refreshOrLoadFixedDates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showModifyVehicle() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/vehicle-modify.fxml"));

        try {
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();

            StageUtil.stageConfiguration(anchorPane, "Modyfikuj pojazd", stage);

            refreshOrLoadVehicles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAboutApplication() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/about-application.fxml"));

        try {
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();

            StageUtil.stageConfiguration(anchorPane, "O aplikacji", stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showClientData() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/client-details.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();

            ClientDetailsController clientDetailsController = loader.getController();

            Client clientFoundInDb = singleton.getClientRepository().findOneByFirstAndLastName(firstAndLastName
                    .getText());

            clientDetailsController.setClient(clientFoundInDb);

            StageUtil.stageConfiguration(anchorPane, "Dane klienta", stage);
        } catch (NoResultException ex) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!",
                    "Nie udało się odnaleźć klienta o podanym imieniu i nazwisku.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchHistoryByVin() {
        vehicleHistoryByVinNumber.getItems().setAll(singleton.getJobRepository().findHistoryByVinNumber(vinNumber
                .getText()));
    }

    public void startManagingNotStartedJobs() {
        loadJobManagingScene(jobs);
    }

    public void startManagingStartedJobs() {
        loadJobManagingScene(startedJobs);
    }

    public void showJobDetailsForClient() {
        loadJobDetailsScene(clientHistory);
    }

    public void showJobDetailsForVehicleByVin() {
        loadJobDetailsScene(vehicleHistoryByVinNumber);
    }

    public void showJobDetailsForVehicle() {
        loadJobDetailsScene(vehicleHistory);
    }

    public void exit() {
        Platform.exit();
    }

    // prywatne metody pomocnicze
    private void loadJobManagingScene(ListView<Job> jobsListView) {
        if (!jobsListView.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/job-management.fxml"));

            try {
                BorderPane tempBorderPane = loader.load();
                tempBorderPane.setTop(borderPane.getTop());

                JobManagementController jobManagementController = loader.getController();
                jobManagementController.setJob(jobsListView.getSelectionModel().getSelectedItem());

                borderPane.getScene().setRoot(tempBorderPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadJobDetailsScene(ListView<Job> jobsListView) {
        if (!jobsListView.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/job-details.fxml"));

            try {
                AnchorPane anchorPane = loader.load();
                Stage stage = new Stage();

                JobDetailsController jobDetailsController = loader.getController();
                jobDetailsController.setJob(jobsListView.getSelectionModel().getSelectedItem());

                StageUtil.stageConfiguration(anchorPane, "Szczegóły zlecenia", stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshOrLoadClients() {
        clients.getItems().setAll(singleton.getClientRepository().findAll());
    }

    private void refreshOrLoadFixedDates() {
        fixedDates.getItems().setAll(singleton.getJobRepository().findNotStartedFixedDates());
    }

    private void refreshOrLoadStartedJobs() {
        startedJobs.getItems().setAll(singleton.getJobRepository().findAllStarted());
    }

    private void refreshOrLoadVehicles() {
        vehicles.getItems().setAll(singleton.getVehicleRepository().findAll());
    }

    private void propertyBindingsConfiguration() {
        searchVehicle.disableProperty().bind(Bindings.createBooleanBinding(() ->
                vinNumber.getText().matches("[A-HJ-NPR-Z\\d]{17}"), vinNumber.textProperty()).not());

        showClient.disableProperty().bind(Bindings.createBooleanBinding(() -> !firstAndLastName.getText().isEmpty()
                && firstAndLastName.getText().contains(" "), firstAndLastName.textProperty()).not());
    }

    private void customListenersConfiguration() {
        fixedDates.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue)
                        -> jobs.getItems().setAll(singleton.getJobRepository().findNotStartedByFixedDate(newValue)));

        clients.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> clientHistory.getItems().setAll(singleton.
                        getJobRepository().findHistoryByClient(clients.getSelectionModel().getSelectedItem())));

        vehicles.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> vehicleHistory.getItems().setAll(singleton.
                        getJobRepository().findHistoryByVehicle(vehicles.getSelectionModel().getSelectedItem())));
    }
}
