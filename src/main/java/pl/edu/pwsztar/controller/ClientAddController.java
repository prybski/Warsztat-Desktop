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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.util.AlertUtil;
import pl.edu.pwsztar.util.RandomPasswordUtil;

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

                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                String generatedPassword = RandomPasswordUtil.randomPassword(6);
                String encodedPassword = bCryptPasswordEncoder.encode(generatedPassword);
                Client client = new Client(firstName.getText(), lastName.getText(), email.getText(), encodedPassword, phoneNumber.getText());

                session.save(client);

                session.getTransaction().commit();

                AlertUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Sukces!",
                        ButtonType.OK, "Nie zapomnij zapisać hasła!", "Wygenerowane hasło to: " + generatedPassword);
            }
        }
    }
}
