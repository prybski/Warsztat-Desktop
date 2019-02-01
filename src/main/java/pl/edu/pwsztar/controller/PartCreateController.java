package pl.edu.pwsztar.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import pl.edu.pwsztar.entity.Part;
import pl.edu.pwsztar.singleton.Singleton;
import pl.edu.pwsztar.util.ContextMenuUtil;
import pl.edu.pwsztar.util.StageUtil;

import java.net.URL;
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
        propertyBindingsConfiguration();

        removeContextMenu();
    }

    public void addPart() {
        Part part;

        if (details.getText().isEmpty()) {
            part = new Part(name.getText().trim(), null, developmentCode.getText().trim());
        } else  {
            part = new Part(name.getText().trim(), details.getText().trim(), developmentCode.getText().trim());
        }

        try {
            singleton.getPartRepository().add(part);

            StageUtil.generateAlertDialog(Alert.AlertType.INFORMATION, "Informacja!", "Udało " +
                    "się dodać nową część.");
        } catch (HibernateException e) {
            StageUtil.generateAlertDialog(Alert.AlertType.ERROR, "Błąd!", "Prawdopodobnie " +
                    "błąd dotyczy: \n" +
                    "\n- wprowadzenia danych, które przekraczają zakresy ustawione dla poszczególnych kolumn " +
                    "w bazie danych" +
                    "\n- wprowadzenia zduplikowanych wartości do kolumn podlegających ograniczeniu unikalności");
        }
    }

    private void propertyBindingsConfiguration() {
        BooleanBinding partDataValid = Bindings.createBooleanBinding(() -> !name.getText().isEmpty()
                        && !developmentCode.getText().isEmpty(), name.textProperty(), developmentCode.textProperty());

        addOnePart.disableProperty().bind(partDataValid.not());
    }

    private void removeContextMenu() {
        ContextMenuUtil.remove(developmentCode, details, name);
    }
}
