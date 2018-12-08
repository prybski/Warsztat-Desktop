package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.util.AlertUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientAddController implements Initializable {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private TextField phoneNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addClient(ActionEvent actionEvent) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.getCurrentSession()) {
                session.beginTransaction();

                Client client = new Client(firstName.getText(), lastName.getText(), email.getText(), phoneNumber.getText());

                session.save(client);

                session.getTransaction().commit();

                AlertUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Sukces!",
                        ButtonType.OK, "Udało się dodać klienta!", "Sukces!");
            }
        }
    }
}
