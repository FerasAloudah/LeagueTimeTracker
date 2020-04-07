package tracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TimeTrackerApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(TimeTrackerApplication.class.getResource("/home.fxml"));
        primaryStage.setScene(new Scene(root, 422, 120));
        primaryStage.setTitle("Time Tracker v1.0");
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnHiding(e -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
