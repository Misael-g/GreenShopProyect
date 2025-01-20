import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginGreen {
    public JPanel Greenmainpanel;
    private JButton botoninciarsecion;
    private JButton btonregistrarse;
    private JComboBox<String> selectroles;
    private JTextField textousuario;
    private JPasswordField textocotrase;

    public LoginGreen() {
        // Acción del botón para ir a la ventana de registro
        btonregistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Registro");
                frame.setContentPane(new RegistroGreen().paginaregistro);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setVisible(true);
            }
        });
        // Acción para iniciar sesión
        botoninciarsecion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = textousuario.getText();
                String contrasena = new String(textocotrase.getPassword());
                String rolSeleccionado = (String) selectroles.getSelectedItem();

                // Validar que los campos no estén vacíos
                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                    return;
                }

                // Verificar credenciales en la base de datos
                try (Connection conn = connectDatabase()) {
                    String sql = "SELECT rol FROM usuarios WHERE usuario = ? AND contrasena = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, usuario);
                    pstmt.setString(2, contrasena);

                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String rolBD = rs.getString("rol");
                        if (!rolBD.equals(rolSeleccionado)) {
                            JOptionPane.showMessageDialog(null, "El rol seleccionado no coincide con el rol registrado.");
                            return;
                        }

                        // Abrir la ventana correspondiente al rol
                        if (rolBD.equals("cliente")) {
                            JFrame frame = new JFrame("Cliente");
                            frame.setContentPane(new PaginaclienteGreen().paginaclientes);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.setSize(800, 800);
                            frame.setVisible(true);
                        } else if (rolBD.equals("administrador")) {
                            JFrame frame = new JFrame("Administrador");
                            frame.setContentPane(new PagniaAdministradores().paginaadmistradores);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.setSize(800, 800);
                            frame.setVisible(true);
                        }

                        JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                        ((JFrame) SwingUtilities.getWindowAncestor(Greenmainpanel)).dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciales incorrectas.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al verificar las credenciales.");
                }
            }
        });
    }

    // conexión a la base de datos
    public static Connection connectDatabase() {
        Connection connection = null;
        try {
            String url = "jdbc:sqlite:C:/Users/garci/Desktop/Base/holahl.db";
            connection = DriverManager.getConnection(url);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.");
        }
        return connection;
    }
}
