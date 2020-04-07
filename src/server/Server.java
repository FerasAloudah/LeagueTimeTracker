package server;

import controllers.ServerController;
import javafx.application.Platform;
import lombok.Data;
import tracker.Note;
import tracker.NoteTracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Data
public class Server {
    private List<ClientConnection> connections;
    private ServerController controller;
    private ServerSocket serverSocket;
    private Thread server;
    private NoteTracker tracker;
    private String name;
    private String title;

    public Server(ServerController controller, NoteTracker tracker, String name, int port) {
        this.controller = controller;
        this.tracker = tracker;
        this.name = name;
        title = "Running on port: " + port + " | Connected: ";
        server = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                connections = new ArrayList<>();
                setTitle();

                while (true) {
                    Socket socket = serverSocket.accept();
                    ClientConnection connection = ClientConnection.builder()
                            .socket(socket)
                            .server(this)
                            .build();
                    connections.add(connection);
                    setTitle();
                    Thread thread = new Thread(connection);
                    thread.start();
                }
            } catch (IOException e) {
                controller.getWindow().hide();
                e.printStackTrace();
            }
        });
        server.start();
    }

    public void broadcastNote(ClientConnection sender, Note note) {
        if (sender == null) {
            note.setName(name);
        }

        controller.println(note.getName() + ": " + note.getRole() + " - " + note.getSummonerSpell());
        List<ClientConnection> connectionsToRemove = new ArrayList<>();
        for (ClientConnection connection : connections) {
            if (connection != sender) {
                try {
                    connection.sendNote(note);
                } catch (IOException e) {
                    connectionsToRemove.add(connection);
                }
            }
        }
        connections.removeAll(connectionsToRemove);
        if (connectionsToRemove.size() > 0) {
            setTitle();
        }
    }

    public void setTitle() {
        Platform.runLater(() -> controller.setTitle(title + connections.size()));
    }
}
