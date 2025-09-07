package gui.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import trackerbot.TrackerBot;

import java.io.IOException;

public class Main extends Application {

    private TrackerBot trackerbot = new TrackerBot();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setTrackerBot(trackerbot);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
