package controllers;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.Server;
import tracker.Note;
import tracker.NoteTracker;
import tracker.TimeTrackerApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HomeController {

    @FXML
    private TextField locationField;

    @FXML
    private Button helpButton;

    @FXML
    private Button browseButton;

    @FXML
    private Button toggleButton;

    @FXML
    private Button resetButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private Button joinButton;

    @FXML
    private Button hostButton;

    private Stage serverStage;

    private TrackerController trackerController;

    private NoteTracker tracker;

    private Client client;

    private Server server;

    private boolean trackerStarted;

    @FXML
    void browseFile(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        File selectedFile = dc.showDialog(browseButton.getScene().getWindow());

        if (selectedFile != null) {
            locationField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void resetTracker(ActionEvent event) {
        tracker.clearNotes();
    }

    @FXML
    void connectToServer(ActionEvent event) throws IOException {
        if (connected()) {
            return;
        }

        String name = nameField.getText().isEmpty() ? "Anon" : nameField.getText();

        if (ipField.getText().isEmpty() || portField.getText().isEmpty()) {
            return;
        }

        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());

        ServerController controller = setUpServerWindow();
        controller.getLogArea().setText("Connected to " + ip + ":" + port + "...");
        client = new Client(controller, tracker, name, ip, port);
    }

    @FXML
    void showHelp(ActionEvent event) {

    }

    @FXML
    void startServer(ActionEvent event) throws IOException {
        if (connected()) {
            return;
        }

        String name = nameField.getText().isEmpty() ? "Host" : nameField.getText();

        if (portField.getText().isEmpty()) {
            return;
        }

        int port = Integer.parseInt(portField.getText());

        ServerController controller = setUpServerWindow();
        controller.getLogArea().setText("Waiting for connections...");
        server = new Server(controller, tracker, name, port);
    }

    @FXML
    void toggleTracker(ActionEvent event) throws IOException {
        if (trackerStarted) {
            trackerController.getTimeline().stop();
            trackerController.getWindow().hide();
            toggleButton.setText("Start");
        } else {
            trackerController.getTimeline().play();
            toggleButton.setText("Stop");

            Path myNotesPath = Paths.get(locationField.getText(), "MyNotes.txt");
            File myNotesFile = myNotesPath.toFile();

            if (!myNotesFile.exists()) {
                myNotesFile.createNewFile();
            }

            tracker.setMyNotesFile(myNotesFile);
        }
        trackerStarted = !trackerStarted;
    }

    @FXML
    void initialize() {
        tracker = new NoteTracker();
        setUpTrackerWindow();
    }

    private Parent loadTracker() throws IOException {
        FXMLLoader loader = new FXMLLoader(TimeTrackerApplication.class.getResource("/tracker.fxml"));
        loader.load();

        trackerController = loader.getController();
        trackerController.setTracker(tracker);

        return loader.getRoot();
    }

    private void setUpTrackerStage(Scene scene) {
        Stage trackerStage = new Stage();
        trackerStage.setX(915);
        trackerStage.setY(310);
        trackerStage.initStyle(StageStyle.TRANSPARENT);
        trackerStage.setScene(scene);
        trackerStage.setAlwaysOnTop(true);
        trackerController.setWindow(trackerStage);
        trackerController.setHomeController(this);
    }

    private void setUpTrackerWindow() {
        try {
            Parent root = loadTracker();
            Scene scene = new Scene(root, 400, 385);
            scene.setFill(Color.TRANSPARENT);
            setUpTrackerStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ServerController setUpServerWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(TimeTrackerApplication.class.getResource("/server.fxml"));
        loader.load();
        ServerController serverController = loader.getController();

        Parent root = loader.getRoot();
        Scene scene = new Scene(root, 425, 400);
        serverStage = new Stage();
        serverStage.setScene(scene);
        serverStage.setResizable(false);
        serverStage.setTitle("Time Tracker Server v1.0");
        serverStage.show();

        serverStage.setOnHiding(e -> {
            try {
                cleanUpConnection();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        serverController.setWindow(serverStage);
        return serverController;
    }

    private void cleanUpConnection() throws IOException {
        if (client != null && client.getConnection().isAlive()) {
            client.getSocket().close();
        } else if (server != null && server.getServer().isAlive()) {
            server.getServerSocket().close();
        }
    }

    public void sendNote(Note note) {
        if (client != null && client.getConnection().isAlive()) {
            client.sendNote(note);
        } else if (server != null && server.getServer().isAlive()) {
            server.broadcastNote(null, note);
        }
    }

    public boolean connected() {
        return serverStage != null && serverStage.isShowing();
    }
}
