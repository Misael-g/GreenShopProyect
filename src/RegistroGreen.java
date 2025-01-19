import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroGreen {
    private JTextField textonombre;
    private JTextField textoapellido;
    private JPasswordField contrasenian;
    private JPasswordField confirmarcontrasenia;
    private JButton crearusuarButton;
    private JTextField textousuario;
    public JPanel paginaregistro;

    public RegistroGreen() {
        crearusuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textonombre.getText();
                String apellido = textoapellido.getText();
                String usuario = textousuario.getText();
                String contrasena = new String(contrasenian.getPassword());
                String confirmarContrasena = new String(confirmarcontrasenia.getPassword());

                if (contrasena.equals(confirmarContrasena)) {
                    // Implementar lógica para registrar usuario
                    registrarUsuario(nombre, apellido, usuario, contrasena);
                    JOptionPane.showMessageDialog(null, "Usuario registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
                }
            }
        });
    }

    private void registrarUsuario(String nombre, String apellido, String usuario, String contrasena) {
        // Lógica para registrar un nuevo usuario en la base de datos
        System.out.println("Registrando usuario: " + usuario);
    }
}
