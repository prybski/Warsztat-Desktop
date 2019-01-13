package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.pwsztar.entity.Client;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientDetailsController implements Initializable {

    private List<Client> clients;

    @FXML
    private Text firstAndLastName;

    @FXML
    private Text email;

    @FXML
    private Text phoneNumber;

    @FXML
    private ComboBox<Client> foundClients;

    @FXML
    private VBox clientDataVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::readClientData);

        customListenersConfiguration();
    }

    private void readClientData() {
        if (clients != null) {
            if (clients.size() == 1) {
                foundClients.setDisable(true);

                for (Client client : clients) {
                    setClientData(client);
                }
            } else {
                propertyBindingsConfiguration();

                foundClients.getItems().setAll(clients);
            }
        }
    }

    private void customListenersConfiguration() {
        foundClients.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> setClientData(newValue));
    }

    private void propertyBindingsConfiguration() {
        BooleanBinding foundClientsNotEmpty = Bindings.createBooleanBinding(() -> !foundClients.getSelectionModel()
                        .isEmpty(), foundClients.getSelectionModel().selectedItemProperty());

        clientDataVBox.disableProperty().bind(foundClientsNotEmpty.not());
    }

    private void setClientData(Client client) {
        String firstAndLastNameString = client.getFirstName() + " " + client.getLastName();

        firstAndLastName.setText(firstAndLastNameString);
        email.setText(client.getEmail());
        phoneNumber.setText(client.getPhoneNumber());
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
