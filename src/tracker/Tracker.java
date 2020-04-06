package tracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class Tracker {
    private ObservableList<Note> notes[] = new ObservableList[5];

    public Tracker() {
        for (int i = 0; i < notes.length; i++) {
            notes[i] = FXCollections.observableArrayList();
        }
    }

    public void addNote(Note note) {
        notes[note.getRoleIndex()].add(note);
        notes[note.getRoleIndex()].sort(Comparator.comparingInt(Note::getTime));
    }

    public void decreaseSeconds() {
        for (ObservableList<Note> list : notes) {
            list.forEach(note -> note.decreaseSecond());
            list.removeIf(Note::isFinished);
        }

    }

    public void clearNotes() {
        for (ObservableList<Note> list : notes) {
            list.clear();
        }
    }
}
