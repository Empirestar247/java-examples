import java.io.*;
import java.net.*;
import java.util.*;


public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    private static int clientId = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientId++;
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientId);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
                clientHandler.sendMessage(message);
            }
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int clientId;

    public ClientHandler(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            out.println("Welcome! You are client #" + clientId);
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from client #" + clientId + ": " + message);
                ChatServer.broadcastMessage("Client #" + clientId + ": " + message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
