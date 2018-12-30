package pl.edu.pwsztar.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.ConstraintCheckUtil;
import pl.edu.pwsztar.util.ContextMenuUtil;
import pl.edu.pwsztar.util.RandomPasswordUtil;
import pl.edu.pwsztar.util.StageUtil;

import java.net.URL;
import java.util.List;
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

        // usunięcie menu kontekstowego
        removeContextMenu();
    }

    public void addClient() {
        List<Client> clientsToCheck = singleton.getClientRepository().findAll();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String generatedPassword = RandomPasswordUtil.randomPassword(6);
        String encodedPassword = bCryptPasswordEncoder.encode(generatedPassword);

        Client client = new Client(firstName.getText(), lastName.getText(),
                email.getText(), encodedPassword, phoneNumber.getText());

        if (ConstraintCheckUtil.checkForDuplicateEmail(clientsToCheck, email.getText())
                || ConstraintCheckUtil.checkForDuplicatePhoneNumber(clientsToCheck, phoneNumber.getText())) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", "Złamano zasadę intergralności dla kolumny 'email' lub 'phone_number'.");
        } else {
            singleton.getClientRepository().add(client);

            StageUtil.generateTextInputDialog("Informacja!", "Proszę zapisać hasło", generatedPassword);
        }
    }

    // prywatne metody pomocnicze
    private void propertyBindingsConfiguration() {
        add.disableProperty().bind(Bindings.createBooleanBinding(() -> (!firstName.getText().isEmpty()
                        && !lastName.getText().isEmpty() && !email.getText().isEmpty() && !phoneNumber.getText().isEmpty())
                        && email.getText().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
                        && phoneNumber.getText().matches("\\d{9,15}"), firstName.textProperty(),
                lastName.textProperty(), email.textProperty(), phoneNumber.textProperty()).not());
    }

    private void removeContextMenu() {
        ContextMenuUtil.remove(firstName, lastName, email, phoneNumber);
    }
}
