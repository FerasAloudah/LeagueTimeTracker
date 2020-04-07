package controllers;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import lombok.Data;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import tracker.Note;
import tracker.NoteTracker;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
public class TrackerController implements NativeKeyListener {

    @FXML
    private ListView<Note> topListView;

    @FXML
    private ListView<Note> jungleListView;

    @FXML
    private ListView<Note> midListView;

    @FXML
    private ListView<Note> adcListView;

    @FXML
    private ListView<Note> supportListView;

    private HomeController homeController;

    private NoteTracker tracker;

    private Timeline timeline;

    private Stage window;

    private static boolean tabPressed = false;

    private static int timerCounter = 0;

    @FXML
    void initialize() {
        setUpTrackerTimer();
        runListener();
    }

    public void refresh() {
        topListView.refresh();
        jungleListView.refresh();
        midListView.refresh();
        adcListView.refresh();
        supportListView.refresh();
    }

    public void setTracker(NoteTracker tracker) {
        this.tracker = tracker;

        topListView.setItems(tracker.getNotes()[0]);
        jungleListView.setItems(tracker.getNotes()[1]);
        midListView.setItems(tracker.getNotes()[2]);
        adcListView.setItems(tracker.getNotes()[3]);
        supportListView.setItems(tracker.getNotes()[4]);
    }

    public char[] getActiveWindow() {
        char[] buffer = new char[2048];
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowText(hwnd, buffer, 1024);
        return buffer;
    }

    private void setUpTrackerTimer() {
        timeline = new Timeline(new KeyFrame(Duration.millis(250), e -> {
            char[] currentWindowName = getActiveWindow();
            timerCounter++;

            if (timerCounter % 4 == 0) {
                tracker.decreaseSeconds();
                refresh();
            }

            if (Native.toString(currentWindowName).equals("League of Legends (TM) Client") && tabPressed) {
                window.show();
            } else {
                window.hide();
            }

            if (tracker.isFileModified()) {
                try {
                    Note note = tracker.readLastLine();
                    if (note != null && homeController.connected()) {
                        homeController.sendNote(note);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void runListener() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        try {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.registerNativeHook();
            }
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            return;
        }
        GlobalScreen.addNativeKeyListener(new TrackerController());
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        // Pressed Tab.
        if (nativeKeyEvent.getKeyCode() == 15) {
            tabPressed = true;
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        tabPressed = false;
    }
}
