import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

    static Vector<ClientHandler> ar = new Vector<ClientHandler>();

    static int i = 0;

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(1234);

        Socket socket;

        while (true) {

            socket = server.accept();
            System.out.println("New client request recieved: " + socket);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            ClientHandler ntch = new ClientHandler(socket, "client " + i, dis, dos);

            Thread t = new Thread(ntch);

            System.out.println("Adding this client to active client list.");

            ar.add(ntch);

            t.start();

            i++;

        }
    }
}

class ClientHandler implements Runnable {

    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket socket;
    boolean isLoggedIn;

    public ClientHandler(Socket socket, String name, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.socket = socket;
        this.isLoggedIn = true;

    }

    @Override
    public void run() {

        String received;

        while (true) {
            try {
                received = dis.readUTF();
                System.out.println(received);

                if (received.equals("logout")) {
                    this.isLoggedIn = false;
                    this.socket.close();

                    break;
                }

                StringTokenizer st = new StringTokenizer(received, "#");
                String msgToSend = st.nextToken();
                String recipient = st.nextToken();

                for (ClientHandler mc : Server.ar) {
                    if (mc.name.equals(recipient) && mc.isLoggedIn) {
                        mc.dos.writeUTF(this.name + ": " + msgToSend);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                this.dis.close();
                this.dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


















