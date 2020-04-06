import client.Client;
import controllers.TimersController;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import server.Server;
import tracker.Note;
import tracker.NoteParser;
import tracker.Tracker;
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
//        FXMLLoader loader = new FXMLLoader(new File("fxml/timers.fxml").toURI().toURL());
//        loader.load();
//        Parent root = loader.getRoot();
//        TimersController controller = loader.getController();
////        Parent root = FXMLLoader.load(new File("fxml/timers.fxml").toURI().toURL());
//        Scene scene = new Scene(root, 400, 385);
//        scene.setFill(Color.TRANSPARENT);
//        primaryStage.setX(915);
//        primaryStage.setY(310);
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.setScene(scene);
//        primaryStage.setAlwaysOnTop(true);
//        primaryStage.show();
        Parent root = FXMLLoader.load(new File("fxml/home.fxml").toURI().toURL());
        primaryStage.setScene(new Scene(root, 422, 120));
        primaryStage.setTitle("Time Tracker v1.0");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) throws InterruptedException {
        launch(args);
        NoteParser noteParser = new NoteParser();
        Tracker tracker = new Tracker();
        String tests[] = new String[] {
                "adc f",
                "jungle t 3",
                "top e r",
                "Mid f lb",
                "supp f lb r",
                "jg tp lb r 10",
                "qwer",
                "top",
                "f lb r"
        };

        int x = 4;

        if (x == 1) {
            Server server = new Server(8002);
            System.out.println("Started Server");
        }

        if (x == 2) {
            Client client = new Client();
            client.connect("localhost", 8002);
            System.out.println("Client 1: Connected");
        }

        System.out.println("---------------------------");

        if (x == 3) {
            Client client = new Client();
            client.connect("localhost", 8002);
            System.out.println("Client 2: Connected");
            Note note;
            for (String test : tests) {
                if ((note = noteParser.parseNote(test)) != null) {
                    tracker.addNote(note);
                    client.sendNote(note);
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }
    }
}
