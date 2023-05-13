import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public  Client(){
        try {
            socket = new Socket("127.0.0.1",10000);

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
                        System.out.println("Server chat terminated");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: "+ msg);
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
        System.out.println("This is client");
        new Client();
    }
}
