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
            System.out.println("Read Start");
            while (true){
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")){
                        System.out.println("CLient chat terminated");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: "+ msg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        Runnable r2=()->{
            System.out.println("writer started");
            while (true){

                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("This is server");
        new Server();
    }
}