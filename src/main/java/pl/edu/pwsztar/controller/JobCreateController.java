package pl.edu.pwsztar.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.singleton.Singleton;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class JobCreateController implements Initializable {

    private Singleton singleton;

    @FXML
    private TextArea description;

    @FXML
    private DatePicker fixedDate;

    @FXML
    private ChoiceBox<Client> clients;

    @FXML
    private ChoiceBox<Vehicle> vehicles;

    @FXML
    private TextField brand;

    @FXML
    private TextField model;

    @FXML
    private Spinner<Integer> productionYear;

    @FXML
    private TextField vinNumber;

    @FXML
    private Spinner<Double> engineCapacity;

    @FXML
    private Button add;

    @FXML
    private Button addWithVehicle;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // sekcja ładowania konfiguracji niestandardowych nasłuchiwaczy
        customListenersConfiguration();
    }

    public void addJob() {
        Job job = new Job(description.getText(), Date.valueOf(fixedDate.getValue()));

        singleton.getJobRepository().addWithExistingVehicle(job, vehicles.getValue(), clients.getValue());
    }

    public void addJobAndVehicle() {
        Vehicle vehicle = new Vehicle(brand.getText(), model.getText(), productionYear.getValue().shortValue(), vinNumber.getText(), engineCapacity.getValue().floatValue());
        Job job = new Job(description.getText(), Date.valueOf(fixedDate.getValue()));

        singleton.getJobRepository().addWithNewVehicle(job, vehicle, clients.getValue());

        refreshOrLoadVehicles();
    }

    private void propertyBindingsConfiguration(List<Vehicle> clientVehicles) {
        add.disableProperty().bind(Bindings.createBooleanBinding(() -> !fixedDate.getEditor().getText().isEmpty()
                        && !description.getText().isEmpty() && !clientVehicles.isEmpty(), fixedDate.getEditor()
                .textProperty(), description.textProperty()).not());

        addWithVehicle.disableProperty().bind(Bindings.createBooleanBinding(() -> !brand.getText().isEmpty()
                        && !model.getText().isEmpty() && !productionYear.getEditor().getText().isEmpty()
                        && (!vinNumber.getText().isEmpty() && vinNumber.getText().length() == 17)
                        && !engineCapacity.getEditor().getText().isEmpty() && !fixedDate.getEditor().getText().isEmpty()
                        && !description.getText().isEmpty(), brand.textProperty(), model.textProperty(), productionYear
                        .getEditor().textProperty(), vinNumber.textProperty(), engineCapacity.getEditor().textProperty()
                , fixedDate.getEditor().textProperty(), description.textProperty()).not());
    }

    private void customListenersConfiguration() {
        SpinnerValueFactory<Integer> productionYearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, 2000);
        productionYear.setValueFactory(productionYearValueFactory);

        SpinnerValueFactory<Double> engineCapacityValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 6.0, 0.6, 0.1);
        engineCapacity.setValueFactory(engineCapacityValueFactory);

        List<Client> clientsFromDb = singleton.getClientRepository().findAll();
        clients.getItems().setAll(clientsFromDb);

        clients.getSelectionModel()
                .selectedItemProperty()
                .addListener((value, oldValue, newValue) -> {
                    List<Vehicle> vehiclesUpdated = singleton.getVehicleRepository().findByClient(newValue);

                    if (!vehiclesUpdated.isEmpty()) {
                        vehicles.setDisable(false);
                        vehicles.getItems().setAll(vehiclesUpdated);
                    }

                    propertyBindingsConfiguration(vehiclesUpdated);

                    if (!vehiclesUpdated.isEmpty()) {
                        vehicles.setDisable(false);
                    } else {
                        vehicles.setDisable(true);
                    }
                });
    }

    private void refreshOrLoadVehicles() {
        vehicles.getItems().setAll(singleton.getVehicleRepository().findByClient(clients.getSelectionModel().getSelectedItem()));
    }
}
