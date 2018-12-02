package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.entity.Vehicle;
import pl.edu.pwsztar.util.AlertUtil;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class JobAddController implements Initializable {

    @FXML
    private TextArea description;

    @FXML
    private DatePicker fixedDate;

    @FXML
    private ChoiceBox<Client> clients;

    @FXML
    private ChoiceBox<Vehicle> vehicles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.getCurrentSession()) {
                session.beginTransaction();

                Query<Client> clientQuery = session.createQuery("from Client", Client.class);
                List<Client> clientsFromDb = clientQuery.getResultList();

                clients.getItems().setAll(clientsFromDb);
                clients.setValue(clientsFromDb.get(0));

                Query<Vehicle> vehicleQuery = session.createQuery("from Vehicle", Vehicle.class);
                List<Vehicle> vehiclesFromDb = vehicleQuery.getResultList();

                vehicles.getItems().setAll(vehiclesFromDb);
                vehicles.setValue(vehiclesFromDb.get(0));

                session.getTransaction().commit();
            }
        }
    }

    public void addJob(ActionEvent actionEvent) {
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
