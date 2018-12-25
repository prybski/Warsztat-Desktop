package pl.edu.pwsztar.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.TextFields;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.singleton.Singleton;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JobManagementController implements Initializable {

    private Job job;
    private Singleton singleton;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button start;

    @FXML
    private Button end;

    @FXML
    private TextField taskName;

    {
        singleton = Singleton.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> taskNames = new ArrayList<>();
        String line;

        try (InputStream inputStream = getClass().getResourceAsStream("/txt/BAZA_ZADAN.txt")) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                while ((line = bufferedReader.readLine()) != null) {
                    taskNames.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextFields.bindAutoCompletion(taskName, taskNames);

        Platform.runLater(() -> {
            if (job.getStartDate() != null) {
                start.setDisable(true);
                end.setDisable(false);
            } else {
                start.setDisable(false);
                end.setDisable(true);
            }
        });
    }

    public void startJob(ActionEvent actionEvent) {
        singleton.getJobRepository().updateStartDate(job, Timestamp.valueOf(LocalDateTime.now()));

        start.setDisable(true);
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

    public void endJob(ActionEvent actionEvent) {

    }

    public void addTaskToJob(ActionEvent actionEvent) {

    }
}
