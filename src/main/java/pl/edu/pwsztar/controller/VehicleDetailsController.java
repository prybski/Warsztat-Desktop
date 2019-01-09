package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;

import java.net.URL;
import java.util.ResourceBundle;

public class VehicleDetailsController implements Initializable {

    private Vehicle vehicle;
    private Client client;

    @FXML
    private Text clientOfJob;

    @FXML
    private Text brand;

    @FXML
    private Text model;

    @FXML
    private Text productionYear;

    @FXML
    private Text vinNumber;

    @FXML
    private Text engineCapacity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::readVehicleData);
    }

    private void readVehicleData() {
        if (vehicle != null && client != null) {
            String firstAndLastName = client.getFirstName() + " " + client.getLastName();

            clientOfJob.setText(firstAndLastName);

            brand.setText(vehicle.getBrand());
            model.setText(vehicle.getModel());
            productionYear.setText(String.valueOf(vehicle.getProductionYear()));
            vinNumber.setText(vehicle.getVinNumber());
            engineCapacity.setText(String.valueOf(vehicle.getEngineCapacity()));
        }
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
