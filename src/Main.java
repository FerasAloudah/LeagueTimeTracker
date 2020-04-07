import client.Client;
import server.Server;
import tracker.Note;
import tracker.NoteParser;
import tracker.NoteTracker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(new File("fxml/home.fxml").toURI().toURL());
        primaryStage.setScene(new Scene(root, 422, 120));
        primaryStage.setTitle("Time Tracker v1.0");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
