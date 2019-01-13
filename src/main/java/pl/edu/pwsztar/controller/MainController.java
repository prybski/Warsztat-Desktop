package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import pl.edu.pwsztar.util.ContextMenuUtil;
import pl.edu.pwsztar.util.StageUtil;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Singleton singleton;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ComboBox<Date> fixedDates;

    @FXML
    private ListView<Job> jobs;

    @FXML
    private ListView<Job> startedJobs;

    @FXML
    private ComboBox<Client> clients;

    @FXML
    private ListView<Job> clientHistory;

    @FXML
    private TextField firstAndLastName;

    @FXML
    private TextField vinNumber;

    @FXML
    private ListView<Job> vehicleHistoryByVinNumber;

    @FXML
    private ComboBox<Vehicle> vehicles;

    @FXML
    private ListView<Job> vehicleHistory;

    @FXML
    private Button searchVehicle;

    @FXML
    private Button showClient;

    @FXML
    private TextField vehicleDetailsVinNumber;

    @FXML
    private Button showVehicle;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshOrLoadFixedDates();
        refreshOrLoadClients();
        refreshOrLoadStartedJobs();
        refreshOrLoadVehicles();

        propertyBindingsConfiguration();

        customListenersConfiguration();

        removeContextMenu();
    }

    public void showAddClient() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/client-create.fxml"));

        try {
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();

            StageUtil.stageConfiguration(anchorPane, stage, "Dodaj klienta");

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

            StageUtil.stageConfiguration(anchorPane, stage, "Dodaj zlecenie");

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

            StageUtil.stageConfiguration(anchorPane, stage, "Modyfikuj pojazd");

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

            StageUtil.stageConfiguration(anchorPane, stage, "O aplikacji");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showClientData() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/client-details.fxml"));

        try {
            AnchorPane anchorPane = loader.load();

            Stage stage = new Stage();

            List<Client> clientsFoundInDb = singleton.getClientRepository().findAllByFirstAndLastName(firstAndLastName
                    .getText());

            if (clientsFoundInDb.isEmpty()) {
                throw new NoResultException();
            }

            ClientDetailsController clientDetailsController = loader.getController();
            clientDetailsController.setClients(clientsFoundInDb);

            StageUtil.stageConfiguration(anchorPane, stage, "Dane klienta");
        } catch (NoResultException ex) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!",
                    "Nie udało się odnaleźć klienta/klientów o podanym imieniu i nazwisku.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showVehicleData() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/vehicle-details.fxml"));

        try {
            AnchorPane anchorPane = loader.load();

            Stage stage = new Stage();

            Vehicle vehicleFoundInDB = singleton.getVehicleRepository().findOneByVehicleVinNumber(vehicleDetailsVinNumber.getText());
            Client clientFoundInDB = singleton.getClientRepository().findOneByVehicleVinNumber(vehicleDetailsVinNumber.getText());

            VehicleDetailsController vehicleDetailsController = loader.getController();
            vehicleDetailsController.setVehicle(vehicleFoundInDB);
            vehicleDetailsController.setClient(clientFoundInDB);

            StageUtil.stageConfiguration(anchorPane, stage, "Dane pojazdu");
        } catch (NoResultException ex) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!",
                    "Nie udało się odnaleźć danych pojazdu o podanym numerze VIN.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchHistoryByVin() {
        List<Job> jobsFoundByVehicleVin = singleton.getJobRepository().findHistoryByVinNumber(vinNumber
                .getText());

        vehicleHistoryByVinNumber.getItems().setAll(jobsFoundByVehicleVin);
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

                jobsListView.getSelectionModel().clearSelection();

                StageUtil.stageConfiguration(anchorPane, stage, "Szczegóły zlecenia");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshOrLoadClients() {
        List<Client> foundClients = singleton.getClientRepository().findAll();

        clients.getItems().clear();
        clients.getItems().setAll(foundClients);
    }

    private void refreshOrLoadFixedDates() {
        List<Date> foundFixedDates = singleton.getJobRepository().findNotStartedFixedDates();

        fixedDates.getItems().clear();
        fixedDates.getItems().setAll(foundFixedDates);
    }

    private void refreshOrLoadStartedJobs() {
        List<Job> foundStartedJobs = singleton.getJobRepository().findAllStarted();

        startedJobs.getItems().clear();
        startedJobs.getItems().setAll(foundStartedJobs);
    }

    private void refreshOrLoadVehicles() {
        List<Vehicle> foundVehicles = singleton.getVehicleRepository().findAll();

        vehicles.getItems().clear();
        vehicles.getItems().setAll(foundVehicles);
    }

    private void propertyBindingsConfiguration() {
        BooleanBinding vinNumberValid = Bindings.createBooleanBinding(() ->
                vinNumber.getText().matches("[A-HJ-NPR-Z\\d]{17}"), vinNumber.textProperty());
        BooleanBinding vehicleDetailsVinNumberValid = Bindings.createBooleanBinding(() ->
                vehicleDetailsVinNumber.getText().matches("[A-HJ-NPR-Z\\d]{17}"), vehicleDetailsVinNumber.textProperty());
        BooleanBinding firstAndLastNameValid = Bindings.createBooleanBinding(() -> !firstAndLastName.getText().isEmpty()
                && firstAndLastName.getText().contains(" "), firstAndLastName.textProperty());

        searchVehicle.disableProperty().bind(vinNumberValid.not());

        showClient.disableProperty().bind(firstAndLastNameValid.not());

        showVehicle.disableProperty().bind(vehicleDetailsVinNumberValid.not());
    }

    private void customListenersConfiguration() {
        fixedDates.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    List<Job> foundNotStartedJobs = singleton.getJobRepository().findNotStartedByFixedDate(newValue);

                    jobs.getItems().setAll(foundNotStartedJobs);
                });

        clients.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    List<Job> clientJobHistory = singleton.getJobRepository().findHistoryByClient(newValue);

                    clientHistory.getItems().setAll(clientJobHistory);
                });

        vehicles.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    List<Job> vehicleJobHistory = singleton.getJobRepository().findHistoryByVehicle(newValue);

                    vehicleHistory.getItems().setAll(vehicleJobHistory);
                });
    }

    private void removeContextMenu() {
        ContextMenuUtil.remove(vinNumber, firstAndLastName, vehicleDetailsVinNumber);
    }
}
