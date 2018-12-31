package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import pl.edu.pwsztar.entity.Client;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientDetailsController implements Initializable {

    private Client client;

    @FXML
    private Text firstAndLastName;

    @FXML
    private Text email;

    @FXML
    private Text phoneNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::readClientData);
    }

    private void readClientData() {
        if (client != null) {
            firstAndLastName.setText(client.getFirstName() + " " + client.getLastName());
            email.setText(client.getEmail());
            phoneNumber.setText(client.getPhoneNumber());
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
