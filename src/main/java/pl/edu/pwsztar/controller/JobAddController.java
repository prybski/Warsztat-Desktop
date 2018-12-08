package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.edu.pwsztar.dao.repository.ClientRepository;
import pl.edu.pwsztar.dao.repository.VehicleRepository;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.util.AlertUtil;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class JobAddController implements Initializable {

    private ClientRepository clientRepository;
    private VehicleRepository vehicleRepository;

    @FXML
    private TextArea description;

    @FXML
    private DatePicker fixedDate;

    @FXML
    private ChoiceBox<Client> clients;

    @FXML
    private ChoiceBox<Vehicle> vehicles;

    {
        clientRepository = new ClientRepository();
        vehicleRepository = new VehicleRepository();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Client> clientsFromDb = clientRepository.findAll();

        clients.getItems().setAll(clientsFromDb);
        clients.setValue(clientsFromDb.get(0));

        List<Vehicle> vehiclesFromDb = vehicleRepository.findByClient(clientsFromDb.get(0));

        vehicles.getItems().setAll(vehiclesFromDb);
        vehicles.setValue(vehiclesFromDb.get(0));

        clients.getSelectionModel()
                .selectedItemProperty()
                .addListener((value, oldValue, newValue) -> {
                    List<Vehicle> vehiclesUpdated = vehicleRepository.findByClient(newValue);

                    vehicles.getItems().setAll(vehiclesUpdated);
                    vehicles.setValue(vehiclesUpdated.get(0));
                });
    }

    public void addJob(ActionEvent actionEvent) {
        System.out.println(Date.valueOf(fixedDate.getValue()));
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.getCurrentSession()) {
                session.beginTransaction();

                Job job = new Job(description.getText(), Date.valueOf(fixedDate.getValue()));
                job.setClient(clients.getValue());
                job.setVehicle(vehicles.getValue());

                session.save(job);

                session.getTransaction().commit();

                AlertUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Sukces!", ButtonType.OK,
                        "Udało się dodać zlecenie!", "Sukces!");
            }
        }
    }
}
