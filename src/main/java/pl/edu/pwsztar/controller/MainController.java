package pl.edu.pwsztar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.edu.pwsztar.dao.repository.JobRepository;
import pl.edu.pwsztar.entity.Job;
import pl.edu.pwsztar.util.StageUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private JobRepository jobRepository;

    @FXML
    private ChoiceBox<Date> fixedDates;

    @FXML
    private ListView<Job> jobs;

    {
        jobRepository = new JobRepository();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fixedDates.getItems().setAll(jobRepository.findJobsFixedDates());

        fixedDates.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> jobs.getItems().setAll(jobRepository.findAllByDate(newValue)));
    }

    public void showAddTask(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/task-add.fxml"));

        try {
            StageUtil.stageConfiguration(loader, "Dodaj zadanie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddClient(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/client-add.fxml"));

        try {
            StageUtil.stageConfiguration(loader, "Dodaj klienta");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddJob(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/job-add.fxml"));

        try {
            StageUtil.stageConfiguration(loader, "Dodaj zlecenie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void manageSelectedJob(ActionEvent actionEvent) {

    }
}
