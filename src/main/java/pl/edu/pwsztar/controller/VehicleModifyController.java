package pl.edu.pwsztar.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.hibernate.HibernateException;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.ContextMenuUtil;
import pl.edu.pwsztar.util.DataFieldsUtil;
import pl.edu.pwsztar.util.StageUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VehicleModifyController implements Initializable {

    private Singleton singleton;

    @FXML
    private ComboBox<Client> clients;

    @FXML
    private ComboBox<Vehicle> clientVehicles;

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
    private VBox editVehicleDataVBox;

    @FXML
    private Button modifyOneVehicle;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadClientsWithVehicles();

        propertyBindingsConfiguration();

        customListenersConfiguration();

        removeContextMenu();
    }

    public void modifyVehicle() {
        Vehicle vehicle = clientVehicles.getSelectionModel().getSelectedItem();
        vehicle.setBrand(brand.getText().trim());
        vehicle.setModel(model.getText().trim());
        vehicle.setProductionYear(productionYear.getValue().shortValue());
        vehicle.setVinNumber(vinNumber.getText().trim());
        vehicle.setEngineCapacity(engineCapacity.getValue().floatValue());

        try {
            singleton.getVehicleRepository().update(vehicle);

            StageUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Informacja!", "Dane " +
                    "wybranego pojazdu zostały zmodyfikowane.");

            List<Vehicle> updatedVehicles = singleton.getVehicleRepository().findByClient(clients.getSelectionModel()
                    .getSelectedItem());

            DataFieldsUtil.resetFieldsToDefaults(productionYear, engineCapacity, brand, model, vinNumber);

            clientVehicles.getSelectionModel().clearSelection();
            clientVehicles.getItems().setAll(updatedVehicles);
        } catch (HibernateException e) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", "Błąd prawdopodobnie " +
                    "został spowodowany przez: \n" +
                    "\n- wprowadzenie danych, które przekraczają zakresy ustawione dla poszczególnych kolumn " +
                    "w bazie danych" +
                    "\n- wprowadzenie zduplikowanych wartości do kolumn podlegających ograniczeniu unikalności");
        }
    }

    private void loadClientsWithVehicles() {
        List<Client> clientsFromDb = singleton.getClientRepository().findAllWithVehicles();

        clients.getItems().setAll(clientsFromDb);
    }

    private void propertyBindingsConfiguration() {
        BooleanBinding clientIsSelected = Bindings.createBooleanBinding(() -> !clients.getSelectionModel().isEmpty(),
                        clients.getSelectionModel().selectedItemProperty());
        BooleanBinding clientVehicleIsSelected = Bindings.createBooleanBinding(() -> !clientVehicles.getSelectionModel()
                        .isEmpty(), clientVehicles.getSelectionModel().selectedItemProperty());
        BooleanBinding modifiedVehicleDataValid = Bindings.createBooleanBinding(() ->
                        !brand.getText().isEmpty() && !model.getText().isEmpty() && !productionYear.getEditor()
                        .getText().isEmpty() && vinNumber.getText().matches("[A-HJ-NPR-Z\\d]{17}")
                        && !engineCapacity.getEditor().getText().isEmpty(), brand.textProperty(),
                        model.textProperty(), productionYear.getEditor().textProperty(),
                        vinNumber.textProperty(), engineCapacity.getEditor().textProperty());


        clientVehicles.disableProperty().bind(clientIsSelected.not());

        editVehicleDataVBox.disableProperty().bind(clientVehicleIsSelected.not());

        modifyOneVehicle.disableProperty().bind(modifiedVehicleDataValid.not());
    }

    private void customListenersConfiguration() {
        clients.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            List<Vehicle> vehiclesFromDb = singleton.getVehicleRepository().findByClient(newValue);

            DataFieldsUtil.resetFieldsToDefaults(productionYear, engineCapacity, brand, model, vinNumber);

            clientVehicles.getSelectionModel().clearSelection();
            clientVehicles.getItems().setAll(vehiclesFromDb);
        });

        clientVehicles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                brand.setText(newValue.getBrand());
                model.setText(newValue.getModel());
                productionYear.getValueFactory().setValue((int) newValue.getProductionYear());
                vinNumber.setText(newValue.getVinNumber());
                engineCapacity.getValueFactory().setValue((double) newValue.getEngineCapacity());
            }
        });
    }

    private void removeContextMenu() {
        ContextMenuUtil.remove(brand, model, productionYear, vinNumber, engineCapacity);
    }
}
