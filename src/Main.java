import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Login");
        frame.setContentPane(new PaginaclienteGreen().paginaclientes);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.setPreferredSize(new Dimension(800,800));
        frame.pack();
        frame.setVisible(true);
    }
}