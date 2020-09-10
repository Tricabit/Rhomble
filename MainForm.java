import java.awt.*;
import javax.swing.*;
import java.net.URL;


public class MainForm {

    private JFrame frame;
    private Update update;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainForm window = new MainForm();
                    window.frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainForm() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Rhomble");
        update=new Update();
        View view= new View();
        URL iconURL = getClass().getResource("files/logo.png");
        ImageIcon icon = new ImageIcon(iconURL);
        frame.setIconImage(icon.getImage());


        frame.getContentPane().setBackground(Color.black);
        frame.getContentPane().add(view, BorderLayout.CENTER);


        /*  frame.setUndecorated(true);
            frame.setAlwaysOnTop(false);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.setSize(update.getXSize(), update.getYSize());
        */
        //System.out.print(update.mappa.area1.getBounds().getWidth());

            frame.setSize(update.getXSize(),update.getYSize());
            frame.setResizable(false);
            frame.setAlwaysOnTop(false);



        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
