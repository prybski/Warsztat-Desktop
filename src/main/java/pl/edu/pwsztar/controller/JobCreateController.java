package pl.edu.pwsztar.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.hibernate.HibernateException;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.ContextMenuUtil;
import pl.edu.pwsztar.util.DataFieldsUtil;
import pl.edu.pwsztar.util.StageUtil;

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
    private ComboBox<Client> clients;

    @FXML
    private ComboBox<Vehicle> vehicles;

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
    private VBox vehicleDataVBox;

    @FXML
    private Button add;

    @FXML
    private Button addWithVehicle;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadClients();

        propertyBindingsConfiguration();

        customListenersConfiguration();

        removeContextMenu();
    }

    public void addJob() {
        Job job = new Job(description.getText().trim(), Date.valueOf(fixedDate.getValue()));
        job.setClient(clients.getSelectionModel().getSelectedItem());
        job.setVehicle(vehicles.getSelectionModel().getSelectedItem());

        try {
            singleton.getJobRepository().addWithExistingVehicle(job);

            StageUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Informacja!", "Udało się dodać " +
                    "nowe zlecenie.");
        } catch (HibernateException e) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", "Błąd prawdopodobnie " +
                    "został spowodowany przez: \n" +
                    "\n- wprowadzenie danych, które przekraczają zakresy ustawione dla poszczególnych kolumn " +
                    "w bazie danych" +
                    "\n- wprowadzenie zduplikowanych wartości do kolumn podlegających ograniczeniu unikalności");
        }
    }

    public void addJobAndVehicle() {
        Job job = new Job(description.getText().trim(), Date.valueOf(fixedDate.getValue()));
        Vehicle vehicle = new Vehicle(brand.getText().trim(), model.getText().trim(), productionYear.getValue().shortValue(),
                vinNumber.getText().trim(), engineCapacity.getValue().floatValue());
        vehicle.addJob(job, clients.getSelectionModel().getSelectedItem());

        try {
            singleton.getVehicleRepository().addVehicleWithJob(vehicle);

            StageUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Informacja!", "Udało się " +
                    "dodać nowe zlecenie z nowym pojazdem.");

            List<Vehicle> updatedVehicles = singleton.getVehicleRepository().findByClient(clients.getSelectionModel()
                    .getSelectedItem());

            refreshOrLoadVehicles(updatedVehicles);
        } catch (HibernateException e) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", "Błąd prawdopodobnie " +
                    "został spowodowany przez: \n" +
                    "\n- wprowadzenie danych, które przekraczają zakresy ustawione dla poszczególnych kolumn " +
                    "w bazie danych" +
                    "\n- wprowadzenie zduplikowanych wartości do kolumn podlegających ograniczeniu unikalności");
        }
    }

    private void loadClients() {
        List<Client> clientsFromDb = singleton.getClientRepository().findAll();

        clients.getItems().setAll(clientsFromDb);
    }

    private void refreshOrLoadVehicles(List<Vehicle> vehiclesToLoad) {
        vehicles.getItems().clear();
        vehicles.getItems().setAll(vehiclesToLoad);
    }

    private void propertyBindingsConfiguration() {
        BooleanBinding existingVehicleIsSelected = Bindings.createBooleanBinding(() -> !fixedDate.getEditor().getText()
                        .isEmpty() && !description.getText().isEmpty() && !vehicles.getSelectionModel().isEmpty()
                        && !clients.getSelectionModel().isEmpty(), fixedDate.getEditor()
                        .textProperty(), description.textProperty(), vehicles.getSelectionModel()
                        .selectedIndexProperty(), clients.getSelectionModel().selectedItemProperty());
        BooleanBinding clientIsSelected = Bindings.createBooleanBinding(() -> !clients.getSelectionModel().isEmpty(),
                        clients.getSelectionModel().selectedItemProperty());
        BooleanBinding existingVehicleIsSelectedOrNewVehicleIsDefined = Bindings.createBooleanBinding(() -> !vehicles
                        .getSelectionModel().isEmpty() || (!brand.getText().isEmpty() && !model.getText().isEmpty()
                        && !productionYear.getEditor().getText().isEmpty() && vinNumber.getText().matches(
                                "[A-HJ-NPR-Z\\d]{17}") && !engineCapacity.getEditor().getText().isEmpty()),
                        vehicles.getSelectionModel().selectedItemProperty(), brand.textProperty(), model
                        .textProperty(), productionYear.getEditor().textProperty(), vinNumber.textProperty(),
                        engineCapacity.getEditor().textProperty());
        BooleanBinding newVehicleIsDefinedAndClientIsSelected = Bindings.createBooleanBinding(() -> !brand.getText()
                        .isEmpty() && !model.getText().isEmpty() && !productionYear.getEditor().getText().isEmpty()
                        && vinNumber.getText().matches("[A-HJ-NPR-Z\\d]{17}") && !engineCapacity.getEditor()
                        .getText().isEmpty() && !fixedDate.getEditor().getText().isEmpty() && !description.getText()
                        .isEmpty() && !clients.getSelectionModel().isEmpty(), brand.textProperty(), model
                        .textProperty(), productionYear.getEditor().textProperty(), vinNumber.textProperty(),
                        engineCapacity.getEditor().textProperty(), fixedDate.getEditor().textProperty(), description
                        .textProperty(), clients.getSelectionModel().selectedItemProperty());

        add.disableProperty().bind(existingVehicleIsSelected.not());

        vehicles.disableProperty().bind(clientIsSelected.not());

        description.disableProperty().bind(existingVehicleIsSelectedOrNewVehicleIsDefined.not());

        fixedDate.disableProperty().bind(existingVehicleIsSelectedOrNewVehicleIsDefined.not());

        vehicleDataVBox.disableProperty().bind(clientIsSelected.not());

        addWithVehicle.disableProperty().bind(newVehicleIsDefinedAndClientIsSelected.not());
    }

    private void customListenersConfiguration() {
        clients.getSelectionModel()
                .selectedItemProperty()
                .addListener((value, oldValue, newValue) -> {
                    List<Vehicle> updatedVehicles = singleton.getVehicleRepository().findByClient(newValue);

                    DataFieldsUtil.resetFieldsToDefaults(productionYear, engineCapacity, brand, model,
                            vinNumber);
                    DataFieldsUtil.clearDatePicker(fixedDate);
                    DataFieldsUtil.clearTextArea(description);

                    refreshOrLoadVehicles(updatedVehicles);
                });
    }

    private void removeContextMenu() {
        ContextMenuUtil.remove(description, fixedDate, brand, model, productionYear, vinNumber, engineCapacity);
    }
}
