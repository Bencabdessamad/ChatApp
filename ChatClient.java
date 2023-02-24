import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String username;
            do {
                System.out.println("Enter your username:");
                username = System.console().readLine();
            } while (username.trim().isEmpty());

            new Thread(new ChatClientReader(socket)).start();

            String input;
            while ((input = System.console().readLine()) != null) {
                writer.println(username + ": " + input);
            }

            socket.close();
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    static class ChatClientReader implements Runnable {
        private Socket socket;

        public ChatClientReader(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}
