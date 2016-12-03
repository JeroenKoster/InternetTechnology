import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Jeroen on 18/11/2016.
 */
public class Server {

    ArrayList<ClientThread> clients = new ArrayList<>();
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
                ClientThread thread = new ClientThread(socket, this);
                clients.add(thread);
                thread.start();
            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public void broadcastMessage(String message) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread t = clients.get(i);
            t.sendMessage(message);
        }
    }


    public class ClientThread extends Thread {
        private String message;
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private boolean connected = true;
        Server server;

        public ClientThread(Socket socket, Server server)
        {
            this.server = server;
            this.socket = socket;
            try {

                this.ois = new ObjectInputStream(socket.getInputStream());
                this.oos = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Creating thread for socket: " + socket.getInetAddress());
                            }
            catch (IOException ioe) {
                System.out.println("IOException: " + ioe.getMessage());
            }
        }

        public void sendMessage(String message){
            try{
                oos.writeObject(message);
                oos.flush();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }

        public void run()
        {
            try {
                while (connected) {
                    message = ois.readObject().toString();
                    server.broadcastMessage(message);
                }
            }
            catch (Exception e) {
                System.out.println("Exception in run(): " + e.getMessage());
            }
        }
    }
}
