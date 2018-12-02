package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class TaskAddController implements Initializable {

    @FXML
    private TextField taskName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskName.setTooltip(new Tooltip("Wprowadź nazwę zadania"));
    }

    public void addTask(ActionEvent actionEvent) {
        Alert informationDialog = new Alert(Alert.AlertType.INFORMATION);
        Alert errorDialog = new Alert(Alert.AlertType.ERROR);

        if (!taskName.getText().isEmpty()) {
            try {
                Files.write(Paths.get(Paths.get(Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + "/txt/BAZA_ZADAN.txt").toUri()), (taskName.getText() + "\n").getBytes(), StandardOpenOption.APPEND);

                informationDialog.setContentText("Udało się dodać nowe zadanie!");
                informationDialog.setTitle("Sukces!");
                informationDialog.setHeaderText("Sukces!");
                informationDialog.setResult(ButtonType.OK);
                informationDialog.showAndWait();
            } catch (IOException e) {
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
