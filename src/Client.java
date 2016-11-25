import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Jeroen on 18/11/2016.
 */
public class Client {

    private final static int SERVER_PORT = 1500;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your nickname: ");
        String nickname = scanner.nextLine();
        try {
            Socket socket = new Socket("localhost", SERVER_PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.print("Message: ");
            String message = nickname + ": " + scanner.nextLine();
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }


    public static void main(String[] args)
    {
        new Client().run();
    }
}