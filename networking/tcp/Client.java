package networking.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    private final int serverPort;

    public Client(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), serverPort);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.write("GET / HTTP/1.1\n");
            writer.write("Host: developer.mozilla.org\n");
            writer.write("User-Agent: OSClient\n");
            writer.write("Accept-language: en-US;q=0.9, mk;q=0.8\n\n");

            writer.flush();

            String line;

            while (!(line = reader.readLine()).isEmpty()) {
                System.out.printf("Client received: %s\n", line);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
                reader.close();
                socket.close();
            } catch (IOException exc) {
                System.err.println(exc.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client(7000);
        client.start();

    }
}
