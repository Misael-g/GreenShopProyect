import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGreen {
    public JPanel Greenmainpanel;
    private JButton botoninciarsecion;
    private JButton btonregistrarse;
    private JComboBox<String> selectroles;
    private JTextField textousuario;
    private JPasswordField textocotrase;

    public LoginGreen() {
        // Configurar roles en el JComboBox
        selectroles.addItem("Cliente");
        selectroles.addItem("Administrador");

        botoninciarsecion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para autenticar usuario
                String usuario = textousuario.getText();
                String contrasenia = new String(textocotrase.getPassword());
                String rolSeleccionado = (String) selectroles.getSelectedItem();

                // Llamar al método para verificar credenciales (pendiente implementar)
                if (autenticarUsuario(usuario, contrasenia, rolSeleccionado)) {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso como " + rolSeleccionado);

                    JFrame frame = new JFrame();
                    if (rolSeleccionado.equals("Cliente")) {
                        frame.setContentPane(new PaginaclienteGreen().paginaclientes);
                    } else if (rolSeleccionado.equals("Administrador")) {
                        frame.setContentPane(new PagniaAdministradores().paginaadmistradores);
                    }
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(800, 800);
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.");
                }
            }
        });

        btonregistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir pantalla de registro
                JFrame frame = new JFrame("Registro");
                frame.setContentPane(new RegistroGreen().paginaregistro);
                frame.setSize(800, 800);
                frame.setVisible(true);
            }
        });
    }

    private boolean autenticarUsuario(String usuario, String contrasenia, String rol) {
        // Simulación de autenticación (pendiente implementar conexión con la base de datos)
        // Retorna verdadero si el usuario y la contraseña coinciden con la base
        return usuario.equals("admin") && contrasenia.equals("12345") && rol.equals("Administrador");
    }
}
