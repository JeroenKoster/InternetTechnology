import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Created by Jeroen on 18/11/2016.
 */
public class Server {

    ArrayList<SocketChannel> clients = new ArrayList();

    private final static int SERVER_PORT = 1500;
    ServerSocket serverSocket;

    public static void main(String[] args)
    {
        new Server().run();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            while (true) {
                System.out.println("Waiting for connection...");
                Socket socket = serverSocket.accept();
                System.out.println("Connection Accepted from: " + socket.getInetAddress());
                clients.add(socket.getChannel());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                String message = inputStream.readObject().toString();
                System.out.println(message);
            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

//    public class ClientThread extends Thread {
//        private Socket socket;
//
//        public ClientThread(Socket socket)
//        {
//            this.socket = socket;
//        }
//
//        public void run() {
//            while (true)
//                try {
//                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
//                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                    String message = inputStream.readObject().toString();
//                    System.out.println(message);
//                }
//                catch (Exception e) {
//                    System.out.println("Exception: " + e.getMessage());
//            }
//        }
//    }
}
