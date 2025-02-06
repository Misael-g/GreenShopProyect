import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Aplicar FlatLaf antes de crear cualquier componente Swing
                UIManager.setLookAndFeel(new FlatDarkLaf());

                // Personalización de colores globales
                UIManager.put("Button.background", new Color(76, 175, 80)); // Verde brillante
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Panel.background", new Color(30, 30, 30)); // Gris oscuro
                UIManager.put("Label.foreground", new Color(200, 255, 200)); // Verde claro

                // Estilos adicionales
                UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
                UIManager.put("Button.arc", 15);
                UIManager.put("Component.arc", 12);


            } catch (Exception e) {
                e.printStackTrace();
            }

            // Crear y mostrar la ventana de login
            JFrame frame = new JFrame("Login");
            frame.setContentPane(new LoginGreen().Greenmainpanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setPreferredSize(new Dimension(500, 500));
            frame.pack();
            frame.setLocationRelativeTo(null); // Centrar la ventana en pantalla
            frame.setVisible(true);
        });
    }
}
