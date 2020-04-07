package tracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

import java.io.*;
import java.util.Comparator;

@Data
public class NoteTracker {
    private ObservableList<Note> notes[] = new ObservableList[5];
    private NoteParser noteParser;
    private Long currentTime;
    private File myNotesFile;

    public NoteTracker() {
        currentTime = System.currentTimeMillis();
        noteParser = new NoteParser();
        for (int i = 0; i < notes.length; i++) {
            notes[i] = FXCollections.observableArrayList();
        }
    }

    public void addNote(Note note) {
        int index = note.getRoleIndex();
        notes[index].add(note);
        notes[index].sort(Comparator.comparingInt(Note::getTime));
    }

    public void decreaseSeconds() {
        for (ObservableList<Note> list : notes) {
            list.forEach(note -> note.decreaseSeconds());
            list.removeIf(Note::isFinished);
        }
    }

    public void clearNotes() {
        for (ObservableList<Note> list : notes) {
            list.clear();
        }
    }

    public boolean isFileModified() {
        long lastModified = myNotesFile.lastModified();
        if (currentTime < lastModified) {
            currentTime = lastModified;
            return true;
        }
        return false;
    }

    public Note readLastLine() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(myNotesFile.getAbsolutePath()));
        String currentLine, lastLine = "";
        while ((currentLine = br.readLine()) != null){
            lastLine = currentLine;
        }
        br.close();

        Note note = noteParser.parseNote(lastLine);
        if (note != null) {
            addNote(note);
        }
        return note;
    }
}
