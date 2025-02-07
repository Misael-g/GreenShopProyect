package com.greenshop;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
/**
 * Clase que maneja el registro de nuevos usuarios en la tienda GreenShop.
 * Permite crear una nueva cuenta de usuario con su información personal y credenciales.
 */
public class RegistroGreen {
    private JTextField textonombre;
    private JTextField textoapellido;
    private JPasswordField contrasenian;
    private JPasswordField confirmarcontrasenia;
    private JButton crearusuarButton;
    private JTextField textousuario;
    public JPanel paginaregistro;
    private JButton iniciarSesionButton;
    /**
     * Constructor que inicializa la página de registro y configura los botones.
     */
    public RegistroGreen() {
        crearusuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textonombre.getText();
                String apellido = textoapellido.getText();
                String usuario = textousuario.getText();
                String contrasena = new String(contrasenian.getPassword());
                String confirmarContrasena = new String(confirmarcontrasenia.getPassword());

                // Validar que los campos no estén vacíos y las contraseñas coincidan
                if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                    return;
                }
                // Validar que las contraseñas coincidan
                if (!contrasena.equals(confirmarContrasena)) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
                    return;
                }
                // Validar la longitud mínima de la contraseña
                if (contrasena.length() < 8) {
                    JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 8 caracteres.");
                    return;
                }

                // Conexión a la base de datos e inserción
                try (Connection conn = LoginGreen.connectDatabase()) {
                    String sql = "INSERT INTO usuarios (nombre, apellido, usuario, contrasena, rol) VALUES (?, ?, ?, ?, 'cliente')";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, apellido);
                    pstmt.setString(3, usuario);
                    pstmt.setString(4, contrasena);

                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.");
                        // Limpiar campos
                        textonombre.setText("");
                        textoapellido.setText("");
                        textousuario.setText("");
                        contrasenian.setText("");
                        confirmarcontrasenia.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al registrar el usuario. Verifique los datos.");
                }

            }
        });
        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    // Regresar a la pantalla de login
                    JFrame loginFrame = new JFrame("Login");
                    loginFrame.setContentPane(new LoginGreen().Greenmainpanel);
                    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    loginFrame.setSize(500, 500);
                    loginFrame.setLocationRelativeTo(null); // Centrar ventana en la pantalla
                    loginFrame.setVisible(true);

                    // Cerrar la ventana actual (la de administrador)
                    ((JFrame) SwingUtilities.getWindowAncestor(paginaregistro)).dispose();

            }
        });
    }
}
