import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    JLabel header = new JLabel("Server");
    JTextArea textArea = new JTextArea();
    JTextField textField = new JTextField();
    Font font = new Font("Roboto",Font.PLAIN,20);
    public Server()
    {
        try {
            RenderGUI();

            server = new ServerSocket(10000);
            System.out.println("Server is ready to run");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            Eventhandles();

            startReading();
//            startWriting();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void Eventhandles() {
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println("Released key");
                if (e.getKeyCode()==10){
                    System.out.println("key pressed");
                    String sendingmsg = textField.getText();
                    textArea.append("Me: "+ sendingmsg +"\n");
                    out.println(sendingmsg);
                    out.flush();
                    textField.setText("");
                    textField.requestFocus();

                }
            }
        });
    }

    private void RenderGUI() {
        this.setTitle("Server Chat");
        this.setSize(500,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        header.setFont(font);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        textField.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        textArea.setFont(font);
        textField.setFont(font);
        this.setLayout(new BorderLayout());
        this.add(header,BorderLayout.NORTH);
        this.add(textArea,BorderLayout.CENTER);
        this.add(textField,BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading(){
        Runnable r1=()->{
            System.out.println("Read Start");
            while (true){
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")){
                        System.out.println("CLient chat terminated");
                        JOptionPane.showMessageDialog(this,"Client Terminated");
                        textField.setEnabled(false);
//                        socket.close();
                        break;
                    }
//                    System.out.println("Client: "+ msg);
                    textArea.append("Client: "+ msg);
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