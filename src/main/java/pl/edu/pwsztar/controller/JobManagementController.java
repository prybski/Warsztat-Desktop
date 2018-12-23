package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import pl.edu.pwsztar.entity.Job;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JobManagementController implements Initializable {

    private Job job;

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            if (job != null) {
                System.out.println(job);
            }
        });
    }

    public void backToMain(ActionEvent actionEvent) {
        try {
            borderPane.getScene().setRoot(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
