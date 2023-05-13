import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Server()
    {
        try {
            server = new ServerSocket(10000);
            System.out.println("Server is ready to run");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void startReading(){
        Runnable r1=()->{

        };
    }
    public void startWriting(){
        Runnable r2=()->{

        }; 
    }
    public static void main(String[] args) {
        System.out.println("This is server");
        new Server();
    }
}