import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Created by Jeroen on 18/11/2016.
 */
public class Server {

    ArrayList<Socket> sockets = new ArrayList<>();
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
                //if (!sockets.contains(socket)) {
                    System.out.println("New socket added");
                    sockets.add(socket);

                //}
                System.out.println("Connection Accepted from: " + socket.getInetAddress());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                String message = inputStream.readObject().toString();
                System.out.println(message);
                System.out.println(sockets.size());
                ClientThread thread = new ClientThread(socket, message);
                thread.start();

            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public class ClientThread extends Thread {
        private Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;

        public ClientThread(Socket socket, String message)
        {
            try {
                this.socket = socket;
                this.ois = new ObjectInputStream(socket.getInputStream());
                this.oos = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Creating thread for socket: " + socket.getInetAddress());
            }
            catch (IOException ioe) {
                System.out.println("IOException: " + ioe.getMessage());
            }

        }

        public void SendMessage(String message){
            try{
                oos.writeObject(message);
                oos.flush();
                oos.close();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }

        public void run()
        {
            try {
                while (true) {
                    String message = ois.readObject().toString();
                    SendMessage(message);
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }
}
