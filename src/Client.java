import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    JLabel header = new JLabel("Client");
    JTextArea textArea = new JTextArea();
    JTextField textField = new JTextField();
    Font font = new Font("Roboto",Font.PLAIN,20);
    public  Client(){
        try {
            socket = new Socket("127.0.0.1",10000);

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            RenderGUI();
            Eventhandles();
            startReading();
//            startWriting();

        }
        catch (IOException e) {
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
//                System.out.println("Key released");
                if (e.getKeyCode()==10){
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

    public void RenderGUI(){
        this.setTitle("CLient Chat");
        this.setSize(500,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        header.setFont(font);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
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
                        System.out.println("Server chat terminated");
                        JOptionPane.showMessageDialog(this,"Server Terminated");
                        textField.setEnabled(false);
//                        socket.close();
                        break;
                    }
//                    System.out.println();
                    textArea.append("Server: "+ msg + "\n");
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
