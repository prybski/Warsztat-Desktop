package pl.edu.pwsztar.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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

    private static final byte GENERATED_PASSWORD_LENGTH;

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

    static {
        GENERATED_PASSWORD_LENGTH = 6;
    }

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        propertyBindingsConfiguration();

        removeContextMenu();
    }

    public void addClient() {
        List<Client> clientsToCheck = singleton.getClientRepository().findAll();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String generatedPassword = RandomPasswordUtil.randomPassword(GENERATED_PASSWORD_LENGTH);
        String encodedPassword = bCryptPasswordEncoder.encode(generatedPassword);

        Client client = new Client(firstName.getText().trim(), lastName.getText().trim(),
                email.getText(), encodedPassword, phoneNumber.getText());

        if (ConstraintCheckUtil.checkForDuplicateEmail(clientsToCheck, email.getText())
                || ConstraintCheckUtil.checkForDuplicatePhoneNumber(clientsToCheck, phoneNumber.getText())) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", "Złamano zasadę " +
                    "intergralności dla kolumny 'email' lub 'phone_number'.");
        } else {
            singleton.getClientRepository().add(client);

            StageUtil.generateTextInputDialog("Informacja!", "Proszę zapisać hasło",
                    generatedPassword);
        }
    }

    private void propertyBindingsConfiguration() {
        BooleanBinding clientDataValid = Bindings.createBooleanBinding(() -> (!firstName.getText().isEmpty()
                        && !lastName.getText().isEmpty() && !email.getText().isEmpty() && !phoneNumber.getText()
                        .isEmpty()) && email.getText().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\." +
                        "[A-Za-z]{2,6}$") && phoneNumber.getText().matches("\\d{9,15}"), firstName
                        .textProperty(), lastName.textProperty(), email.textProperty(), phoneNumber.textProperty());

        add.disableProperty().bind(clientDataValid.not());
    }

    private void removeContextMenu() {
        ContextMenuUtil.remove(firstName, lastName, email, phoneNumber);
    }
}
