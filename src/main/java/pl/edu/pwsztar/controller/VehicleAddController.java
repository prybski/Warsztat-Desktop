package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.service.ClientService;
import pl.edu.pwsztar.util.AlertUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VehicleAddController implements Initializable {

    private ClientService clientService;

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
    private ChoiceBox<Client> clients;

    {
        clientService = new ClientService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> productionYearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, 2000);
        productionYear.setValueFactory(productionYearValueFactory);

        SpinnerValueFactory<Double> engineCapacityValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 6.0, 0.6, 0.1);
        engineCapacity.setValueFactory(engineCapacityValueFactory);

        List<Client> clientsFromDb = clientService.findAll();

        clients.getItems().setAll(clientsFromDb);
        clients.setValue(clientsFromDb.get(0));
    }

    public void addVehicle(ActionEvent actionEvent) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.getCurrentSession()) {
                session.beginTransaction();

                Vehicle vehicle = new Vehicle(brand.getText(), model.getText(), productionYear.getValue().shortValue(), vinNumber.getText(), engineCapacity.getValue());
                vehicle.setClient(clients.getValue());

                session.save(vehicle);

                session.getTransaction().commit();

                AlertUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Sukces!", ButtonType.OK,
                        "Udało się dodać pojazd!", "Sukces!");
            }
        }
    }
}
