package server;

import tracker.Note;
import lombok.Builder;
import lombok.Data;

import java.io.*;
import java.net.Socket;

@Data
@Builder
public class ClientConnection implements Runnable {
    private Socket socket;
    private Server server;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                Note note = (Note) input.readObject();
                server.broadcastNote(this, note);
//                Platform.runLater(() -> {
                    System.out.println("ClientConnection run: " + note + "\n");
//                });
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ClientConnection Error");
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
        System.out.println("ClientConnection sendNote: " + note);
        output.writeObject(note);
        output.flush();
    }
}
