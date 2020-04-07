package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class ServerController {

    @FXML
    private TextArea logArea;

    private Stage window;

    @FXML
    void initialize() {
    }

    public void setTitle(String title) {
        window.setTitle(title);
    }

    public void println(String message) {
        logArea.setText(logArea.getText() + "\n" + message);
    }
}
