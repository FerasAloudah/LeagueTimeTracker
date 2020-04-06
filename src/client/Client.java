package client;

import tracker.Note;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Data
@NoArgsConstructor
public class Client {
    private ObjectOutputStream output;
    private String host;
    private int port;
    private boolean connected;

    public void connect(String host, int port) {
        if (connected) {
            return;
        }

        try {
            Socket socket = new Socket(host, port);
            System.out.println("Connected. \n");
            output = new ObjectOutputStream(socket.getOutputStream());
            ClientThread clientThread = ClientThread.builder()
                    .socket(socket)
                    .client(this)
                    .build();
            Thread thread = new Thread(clientThread);
            thread.start();
            connected = true;
        } catch (IOException e) {
            System.out.println(e.toString() + '\n');
        }
    }

    public void sendNote(Note note) {
        try {
            System.out.println("Client sendNote: " + note);
            output.writeObject(note);
            output.flush();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
