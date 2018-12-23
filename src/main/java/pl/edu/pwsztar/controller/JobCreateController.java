package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
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
    private Button addJob;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> productionYearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, 2000);
        productionYear.setValueFactory(productionYearValueFactory);

        SpinnerValueFactory<Double> engineCapacityValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 6.0, 0.6, 0.1);
        engineCapacity.setValueFactory(engineCapacityValueFactory);

        List<Client> clientsFromDb = singleton.getClientRepository().findAll();

        clients.getItems().setAll(clientsFromDb);
        clients.setValue(clientsFromDb.get(0));

        List<Vehicle> vehiclesFromDb = singleton.getVehicleRepository().findByClient(clientsFromDb.get(0));

        if (vehiclesFromDb.isEmpty()) {
            addJob.setDisable(true);
            vehicles.setDisable(true);
        } else {
            addJob.setDisable(false);
            vehicles.setDisable(false);
        }

        vehicles.getItems().setAll(vehiclesFromDb);
        if (!vehicles.getItems().isEmpty()) {
            vehicles.setValue(vehiclesFromDb.get(0));
        }

        clients.getSelectionModel()
                .selectedItemProperty()
                .addListener((value, oldValue, newValue) -> {
                    List<Vehicle> vehiclesUpdated = singleton.getVehicleRepository().findByClient(newValue);
                    vehicles.getItems().setAll(vehiclesUpdated);

                    if (!vehiclesUpdated.isEmpty()) {
                        vehicles.setDisable(false);
                        addJob.setDisable(false);

                        vehicles.setValue(vehiclesUpdated.get(0));
                    } else {
                        vehicles.setDisable(true);
                        addJob.setDisable(true);
                    }
                });
    }

    public void addJob(ActionEvent actionEvent) {
        Job job = new Job(description.getText(), Date.valueOf(fixedDate.getValue()));

        singleton.getJobRepository().addWithExistingVehicle(job, vehicles.getValue(), clients.getValue());
    }

    public void addJobAndVehicle(ActionEvent actionEvent) {
        Vehicle vehicle = new Vehicle(brand.getText(), model.getText(), productionYear.getValue().shortValue(), vinNumber.getText(), engineCapacity.getValue().floatValue());
        Job job = new Job(description.getText(), Date.valueOf(fixedDate.getValue()));

        singleton.getJobRepository().addWithNewVehicle(job, vehicle, clients.getValue());
    }
}
