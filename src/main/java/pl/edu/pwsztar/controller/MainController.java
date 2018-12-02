package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showAddTask(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/task-add.fxml"));

        try {
            stageConfiguration(loader, "Dodaj zadanie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddClient(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/client-add.fxml"));

        try {
            stageConfiguration(loader, "Dodaj klienta");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddVehicle(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/vehicle-add.fxml"));

        try {
            stageConfiguration(loader, "Dodaj pojazd");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddJob(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/job-add.fxml"));

        try {
            stageConfiguration(loader, "Dodaj zlecenie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stageConfiguration(FXMLLoader loader, String title) throws IOException {
        AnchorPane anchorPane = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(new Scene(anchorPane));
        stage.show();
    }
}
