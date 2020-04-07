package server;

import lombok.Builder;
import lombok.Data;
import tracker.Note;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Data
@Builder
public class ClientConnection implements Runnable {
    private String name;
    private Socket socket;
    private Server server;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());

            Note note = (Note) input.readObject();
            name = note.getName();

            server.getController().println(name + " connected!");

            while (true) {
                note = (Note) input.readObject();
                server.getTracker().addNote(note);
                server.broadcastNote(this, note);
            }
        } catch (IOException | ClassNotFoundException e) {
            server.getController().println(name + " left!");
            server.getConnections().remove(this);
            server.setTitle();
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendNote(Note note) throws IOException {
        output.writeObject(note);
        output.flush();
    }
}
