import java.net.*;
import java.io.*;
import java.util.*;


public class Client {

    final static int SERVERPORT = 1234;

    public static void main(String[] args) throws UnknownHostException, IOException {

        Scanner scn = new Scanner(System.in);

        InetAddress ip = InetAddress.getByName("localhost");

        Socket s = new Socket(ip, SERVERPORT);

        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        //send the message across the server
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String msg = scn.nextLine();

                    try {
                        dos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //read the message from the server by constantly listening
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = dis.readUTF();
                    System.out.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }

}
