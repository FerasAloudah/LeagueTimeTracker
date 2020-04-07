package client;

import tracker.Note;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

@Data
@Builder
public class ClientThread implements Runnable {
    private Socket socket;
    private Client client;
    private ObjectInputStream input;

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Note note = (Note) input.readObject();
                client.getTracker().addNote(note);
                client.getController().println(note.getName() + ": " + note.getRole() + " - " + note.getSummonerSpell());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
