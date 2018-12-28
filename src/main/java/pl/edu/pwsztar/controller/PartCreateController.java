package pl.edu.pwsztar.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import pl.edu.pwsztar.entity.Part;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.ConstraintCheckUtil;
import pl.edu.pwsztar.util.StageUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PartCreateController implements Initializable {

    private Singleton singleton;

    @FXML
    private Button addOnePart;

    @FXML
    private TextField developmentCode;

    @FXML
    private TextField details;

    @FXML
    private TextField name;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addOnePart.disableProperty().bind(Bindings.createBooleanBinding(() -> !name.getText().isEmpty() && !details.getText().isEmpty() && !developmentCode.getText().isEmpty(), name.textProperty(), details.textProperty(), developmentCode.textProperty()).not());
    }

    public void addPart() {
        Part part = new Part(name.getText(), details.getText(), developmentCode.getText());

        List<Part> partsToCheck = singleton.getPartRepository().findAll();

        if (ConstraintCheckUtil.checkForDuplicateDevelopmentCode(partsToCheck, developmentCode.getText())) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", null, "Złamano zasadę integralności dla kolumny 'development_code.'");
        } else {
            singleton.getPartRepository().add(part);

            StageUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Informacja!", null, "Udało się dodać nową część.");
        }
    }
}
