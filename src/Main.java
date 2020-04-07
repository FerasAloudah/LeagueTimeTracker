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
        NoteParser noteParser = new NoteParser();
        NoteTracker noteTracker = new NoteTracker();
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

        int x = 2;

        if (x == 1) {
            Server server = new Server(null, null, "test", 8002);
            System.out.println("Started Server");
        }

        if (x == 2) {
            Client client = new Client();
            client.connect(null, null,"Feras","localhost", 5555);
            System.out.println("Client 1: Connected");
        }

        System.out.println("---------------------------");

        if (x == 3) {
            Client client = new Client();
            client.connect(null, null,"Badr", "localhost", 8002);
            System.out.println("Client 2: Connected");
            Note note;
            for (String test : tests) {
                if ((note = noteParser.parseNote(test)) != null) {
                    noteTracker.addNote(note);
                    client.sendNote(note);
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }
    }
}
