package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class HomeController {

    @FXML
    private TextField locationField;

    @FXML
    private Button helpButton;

    @FXML
    private Button browseButton;

    @FXML
    private Button startButton;

    @FXML
    private Button resetButton;

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private Button joinButton;

    @FXML
    private Button hostButton;

    private File leagueLocation;

    @FXML
    void browseFile(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        File selectedFile = dc.showDialog(browseButton.getScene().getWindow());

        if (selectedFile != null) {
            locationField.setText(selectedFile.getAbsolutePath());
            leagueLocation = selectedFile;
        }
    }

    @FXML
    void initialize() {

    }

}
