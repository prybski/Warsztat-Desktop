package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import pl.edu.pwsztar.util.StageUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class TaskCreateController implements Initializable {

    @FXML
    private TextField taskName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskName.setTooltip(new Tooltip("Wprowadź nazwę zadania"));
    }

    public void addTask(ActionEvent actionEvent) {
        if (!taskName.getText().isEmpty()) {
            try {
                Files.write(Paths.get(Paths.get(Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + "/txt/BAZA_ZADAN.txt").toUri()), (taskName.getText() + "\n").getBytes(), StandardOpenOption.APPEND);

                StageUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Sukces!", ButtonType.OK,
                        "Udało się dodać nowe zadanie!","Sukces!");
            } catch (IOException e) {
                StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", ButtonType.OK,
                        "Nastąpił problem podczas odczytu z pliku!", "Błąd!");
            }
        } else {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", ButtonType.OK,
                    "Proszę wprowadzić nazwę zadania!", "Błąd!");
        }
    }
}
