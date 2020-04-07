package client;

import controllers.ServerController;
import tracker.Note;
import lombok.Data;
import lombok.NoArgsConstructor;
import tracker.NoteTracker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
public class Client {
    private ObjectOutputStream output;
    private Socket socket;
    private Thread connection;
    private ServerController controller;
    private NoteTracker tracker;
    private String name;

    public Client(ServerController controller, NoteTracker tracker, String name, String host, int port) {
        if (connection != null && connection.isAlive()) {
            return;
        }

        this.controller = controller;
        this.tracker = tracker;
        this.name = name;
        try {
            socket = new Socket(host, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            ClientThread clientThread = ClientThread.builder()
                    .socket(socket)
                    .client(this)
                    .build();
            connection = new Thread(clientThread);
            connection.start();

            Note note = new Note(name);
            output.writeObject(note);
            controller.setTitle("Connected to: " + host + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(ServerController controller, NoteTracker tracker, String name, String host, int port) {
        if (connection != null && connection.isAlive()) {
            return;
        }

        this.controller = controller;
        this.tracker = tracker;
        try {
            socket = new Socket(host, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            ClientThread clientThread = ClientThread.builder()
                    .socket(socket)
                    .client(this)
                    .build();
            connection = new Thread(clientThread);
            connection.start();

            Note note = new Note(name);
            output.writeObject(note);
            controller.setTitle("Connected to: " + host + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendNote(Note note) {
        try {
            note.setName(name);
            controller.println(note.getName() + ": " + note.getRole() + " - " + note.getSummonerSpell());
            output.writeObject(note);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
