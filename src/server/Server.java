package server;

import tracker.Note;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {
    List<ClientConnection> connections;
    ServerSocket serverSocket;

    public Server(int port) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                connections = new ArrayList<>();

//                Platform.runLater(() -> System.out.println("New server started at " + new Date() + '\n'));
                System.out.println("New server started at " + new Date() + '\n');
                int i = 1;
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("client " + (i++) +  " connected");
                    ClientConnection connection = ClientConnection.builder()
                            .socket(socket)
                            .server(this)
                            .build();
                    connections.add(connection);

                    Thread thread = new Thread(connection);
                    thread.start();
                }
            } catch (IOException e) {
                System.out.println(e.toString() + '\n');
            }
        }).start();
    }

    public void broadcastNote(ClientConnection sender, Note note) {
        System.out.println("broadcastNote: " + note);
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
    }
}
