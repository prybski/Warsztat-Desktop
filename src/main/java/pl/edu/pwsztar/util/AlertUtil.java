package pl.edu.pwsztar.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertUtil {

    public static void generateAlertDialog(Alert.AlertType alertType, String title,
                                            ButtonType resultButton, String contextText, String headerText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setResult(resultButton);
        alert.setContentText(contextText);
        alert.setHeaderText(headerText);

        alert.showAndWait();
    }
}
