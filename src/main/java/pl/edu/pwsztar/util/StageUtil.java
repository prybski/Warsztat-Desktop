package pl.edu.pwsztar.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageUtil {

    public static void generateAlertDialog(Alert.AlertType alertType, String title, String contextText) {
        Alert alert = new Alert(alertType);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(StageUtil.class.getResourceAsStream("/img/icon.png")));

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contextText);
        alert.getDialogPane().getStylesheets().add(StageUtil.class.getResource("/css/dialog-style.css").toExternalForm());
        alert.showAndWait();
    }

    public static void generateTextInputDialog(String title, String contentText, String textToTextField) {
        TextInputDialog textInputDialog = new TextInputDialog();
        Stage stage = (Stage) textInputDialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(StageUtil.class.getResourceAsStream("/img/icon.png")));

        textInputDialog.getEditor().setText(textToTextField);
        textInputDialog.getEditor().setEditable(false);
        textInputDialog.setTitle(title);
        textInputDialog.setContentText(contentText);
        textInputDialog.setHeaderText(null);
        ContextMenuUtil.remove(textInputDialog.getEditor());
        textInputDialog.getDialogPane().getButtonTypes().removeAll(ButtonType.CANCEL);
        textInputDialog.getDialogPane().getStylesheets().add(StageUtil.class.getResource("/css/dialog-style.css").toExternalForm());
        textInputDialog.showAndWait();
    }

    public static void stageConfiguration(Parent parent, Stage stage, String title) {
        Scene scene = new Scene(parent);

        stage.getIcons().add(new Image(StageUtil.class.getResourceAsStream("/img/icon.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
