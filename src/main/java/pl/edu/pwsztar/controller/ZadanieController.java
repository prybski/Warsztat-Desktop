package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class ZadanieController implements Initializable {

    @FXML
    private TextField nazwaZadania;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nazwaZadania.setTooltip(new Tooltip("Wprowadź nazwę zadania"));
    }

    public void addZadanieOnAction(ActionEvent actionEvent) {
        Alert informationDialog = new Alert(Alert.AlertType.INFORMATION);
        Alert errorDialog = new Alert(Alert.AlertType.ERROR);

        if (!nazwaZadania.getText().isEmpty()) {
            try {
                Files.write(Paths.get(getClass().getResource("/BAZA_ZADAN.txt").toURI()), (nazwaZadania.getText() + "\n").getBytes(), StandardOpenOption.APPEND);

                informationDialog.setContentText("Udało się dodać nowe zadanie!");
                informationDialog.setTitle("Sukces!");
                informationDialog.setHeaderText("Sukces!");
                informationDialog.setResult(ButtonType.OK);
                informationDialog.showAndWait();
            } catch (IOException | URISyntaxException e) {
                errorDialog.setHeaderText("Błąd!");
                errorDialog.setTitle("Błąd!");
                errorDialog.setContentText("Nastąpił problem podczas odczytu z pliku!");
                errorDialog.setResult(ButtonType.OK);
                errorDialog.showAndWait();
            }
        } else {
            errorDialog.setHeaderText("Błąd!");
            errorDialog.setTitle("Błąd!");
            errorDialog.setContentText("Proszę wprowadzić nazwę zadania!");
            errorDialog.setResult(ButtonType.OK);
            errorDialog.showAndWait();
        }
    }
}
