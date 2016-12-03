import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Jeroen on 18/11/2016.
 */
public class Client {

    private final static int SERVER_PORT = 1500;

    public void run()
    {
        String message, nickname, result;
        try {
            Socket socket;
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter your nickname: ");
            nickname = scanner.nextLine();
            socket = new Socket("localhost", SERVER_PORT);
            new ClientThread(socket).start();
            System.out.println("listener Thread created");
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                System.out.print("Message: ");
                message = scanner.nextLine();
                result = nickname + ": " + message;
                outputStream.writeObject(result);
                outputStream.flush();
            }
        }catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public class ClientThread extends Thread {
        private String message;
        private Socket socket;
        private ObjectInputStream ois;
        private boolean connected = true;

        public ClientThread(Socket socket)
        {
            this.socket = socket;
        }

        public void run()
        {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                while (connected) {
                    String message = ois.readObject().toString();
                    System.out.println("Message received from " + message);
                }
            }
            catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args)
    {
        new Client().run();
    }
}
