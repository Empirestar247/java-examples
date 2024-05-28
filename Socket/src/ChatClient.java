import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            new Thread(new ReceiveMessagesHandler(in)).start();

            Scanner scanner = new Scanner(System.in);
            String message;
            while (true) {
                System.out.print("Enter message: ");
                message = scanner.nextLine();
                out.println(message);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Server closed");
            System.exit(0);
        }
    }
}

class ReceiveMessagesHandler implements Runnable {
    private BufferedReader in;

    public ReceiveMessagesHandler(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println();
                System.out.println(message);
                System.out.print("Enter message: ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
