package pl.edu.pwsztar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/main.fxml"));

        BorderPane borderPane = loader.load();
        Scene scene = new Scene(borderPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Aplikacja warsztatowa");
        primaryStage.show();
    }
}
