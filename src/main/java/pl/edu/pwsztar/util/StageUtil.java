package pl.edu.pwsztar.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageUtil {

    public static void generateAlertDialog(Alert.AlertType alertType, String title,
                                            ButtonType resultButton, String contextText, String headerText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setResult(resultButton);
        alert.setContentText(contextText);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public static void stageConfiguration(Parent parent, String title, Stage stage) {
        Scene scene = new Scene(parent);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
