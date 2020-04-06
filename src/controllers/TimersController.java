package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import tracker.Note;

public class TimersController {

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

    @FXML
    void initialize() {
        Note note = new Note();
        note.setRole("test");
        note.setSummonerSpell("Flash");
        note.setTime(80);
        ObservableList<Note> items = FXCollections.observableArrayList(
                new Note("", 0, "F", 100),
                new Note("", 0, "T", 100),
                new Note("", 0, "T", 100));
        ObservableList<Note> items2 = FXCollections.observableArrayList(
                new Note("", 0, "F", 100),
                new Note("", 0, "H", 100));
        ObservableList<Note> items3 = FXCollections.observableArrayList(
                new Note("", 0, "F", 100),
                new Note("", 0, "E", 100));
        ObservableList<Note> items4 = FXCollections.observableArrayList(
                new Note("", 0, "F", 100),
                new Note("", 0, "T", 100));
        ObservableList<Note> items5 = FXCollections.observableArrayList(
                new Note("", 0, "F", 100),
                new Note("", 0, "B", 100));
        topListView.setItems(items);
        topListView.setMouseTransparent(true);
        topListView.setFocusTraversable(false);
        jungleListView.setItems(items2);
        jungleListView.setMouseTransparent(true);
        jungleListView.setFocusTraversable(false);
        midListView.setItems(items3);
        midListView.setMouseTransparent(true);
        midListView.setFocusTraversable(false);
        adcListView.setItems(items4);
        adcListView.setMouseTransparent(true);
        adcListView.setFocusTraversable(false);
        supportListView.setItems(items5);
        supportListView.setMouseTransparent(true);
        supportListView.setFocusTraversable(false);
        note.setSummonerSpell("Heal");
        System.out.println(topListView.getItems());
        System.out.println("-------");
    }

}
