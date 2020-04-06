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
                System.out.println("ClientThread: " + note);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from server: " + e.getMessage());
            client.setConnected(false);
        }
    }
}
