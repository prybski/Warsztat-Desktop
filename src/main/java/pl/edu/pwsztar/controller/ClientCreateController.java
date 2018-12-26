package pl.edu.pwsztar.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.RandomPasswordUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCreateController implements Initializable {

    private Singleton singleton;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Button add;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // sekcja wiązania ze sobą dynamicznych walidatorów
        propertyBindingsConfiguration();
    }

    public void addClient() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String generatedPassword = RandomPasswordUtil.randomPassword(6);
        String encodedPassword = bCryptPasswordEncoder.encode(generatedPassword);

        Client client = new Client(firstName.getText(), lastName.getText(),
                email.getText(), encodedPassword, phoneNumber.getText());

        singleton.getClientRepository().add(client);
    }

    // prywatne metody pomocnicze
    private void propertyBindingsConfiguration() {
        add.disableProperty().bind(Bindings.createBooleanBinding(() -> (!firstName.getText().isEmpty()
                && !lastName.getText().isEmpty() && !email.getText().isEmpty() && !phoneNumber.getText().isEmpty())
                && (email.getText().contains("@") && email.getText().contains("."))
                && phoneNumber.getText().matches("^[0-9]*$"), firstName.textProperty(),
                lastName.textProperty(), email.textProperty(), phoneNumber.textProperty()).not());
    }
}
