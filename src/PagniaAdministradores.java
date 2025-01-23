import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PagniaAdministradores extends JFrame {
    public JPanel paginaadmistradores;
    private JTabbedPane tabbedPane1;
    private JButton cambirolbotn;
    private JTable tablaproductos;
    private JTable tablaclientesresgitrados;
    private JTable tablahistorialventas;
    private JTextField textonombreproduct;
    private JTextField textoprecioproduct;
    private JTextField textonumerodeestuck;
    private JButton anadirProductoButton;
    private JButton actualizarProductoButton;
    private JButton eliminarProductoButton;
    private JButton cerrarSesionButton;

    public PagniaAdministradores() {
        // Cargar los datos iniciales en las tablas
        cargarUsuarios();
        cargarProductos();
        cargarHistorialVentas();

        // Botón para cambiar el rol de un usuario
        cambirolbotn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tablaclientesresgitrados.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un usuario para cambiar el rol.");
                    return;
                }

                String usuario = tablaclientesresgitrados.getValueAt(selectedRow, 0).toString();
                cambiarRolUsuario(usuario);
            }
        });

        // Botón para añadir un producto
        anadirProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textonombreproduct.getText();
                String precio = textoprecioproduct.getText();
                String stock = textonumerodeestuck.getText();

                if (nombre.isEmpty() || precio.isEmpty() || stock.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos para añadir un producto.");
                    return;
                }

                anadirProducto(nombre, Double.parseDouble(precio), Integer.parseInt(stock));
            }
        });

        // Botón para actualizar un producto
        actualizarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tablaproductos.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un producto para actualizar.");
                    return;
                }

                String idProducto = tablaproductos.getValueAt(selectedRow, 0).toString();
                String nombre = textonombreproduct.getText();
                String precio = textoprecioproduct.getText();
                String stock = textonumerodeestuck.getText();

                if (nombre.isEmpty() || precio.isEmpty() || stock.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos para actualizar el producto.");
                    return;
                }

                actualizarProducto(Integer.parseInt(idProducto), nombre, Double.parseDouble(precio), Integer.parseInt(stock));
            }
        });

        // Botón para eliminar un producto
        eliminarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tablaproductos.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un producto para eliminar.");
                    return;
                }

                String idProducto = tablaproductos.getValueAt(selectedRow, 0).toString();
                eliminarProducto(Integer.parseInt(idProducto));
            }
        });
        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Está seguro de que desea cerrar sesión?",
                        "Confirmar cierre de sesión",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Regresar a la pantalla de login
                    JFrame loginFrame = new JFrame("Login");
                    loginFrame.setContentPane(new LoginGreen().Greenmainpanel);
                    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    loginFrame.setSize(800, 600);
                    loginFrame.setVisible(true);

                    // Cerrar la ventana actual (la de administrador)
                    ((JFrame) SwingUtilities.getWindowAncestor(paginaadmistradores)).dispose();
                }
            }
        });

    }

    // Métodos para cargar datos en las tablas
    private void cargarUsuarios() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Usuario", "Rol"}, 0);
        try (Connection conn = LoginGreen.connectDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT usuario, rol FROM usuarios")) {

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("usuario"), rs.getString("rol")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los usuarios.");
        }
        tablaclientesresgitrados.setModel(model);
    }

    private void cargarProductos() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock"}, 0);
        try (Connection conn = LoginGreen.connectDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM productos")) {

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los productos.");
        }
        tablaproductos.setModel(model);
    }

    private void cargarHistorialVentas() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID Venta", "Usuario", "Fecha", "Total"}, 0);
        try (Connection conn = LoginGreen.connectDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT v.id_venta, u.usuario, v.fecha_venta, v.total " +
                     "FROM ventas v INNER JOIN usuarios u ON v.id_usuario = u.id_usuario")) {

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id_venta"), rs.getString("usuario"), rs.getString("fecha_venta"), rs.getDouble("total")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar el historial de ventas.");
        }
        tablahistorialventas.setModel(model);
    }

    // Métodos para modificar la base de datos
    private void cambiarRolUsuario(String usuario) {
        try (Connection conn = LoginGreen.connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE usuarios SET rol = 'administrador' WHERE usuario = ?")) {

            pstmt.setString(1, usuario);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Rol cambiado con éxito.");
                cargarUsuarios();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cambiar el rol del usuario.");
        }
    }

    private void anadirProducto(String nombre, double precio, int stock) {
        try (Connection conn = LoginGreen.connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)")) {

            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            pstmt.setInt(3, stock);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Producto añadido con éxito.");
            cargarProductos();

            // Limpiar los campos de entrada
            textonombreproduct.setText("");
            textoprecioproduct.setText("");
            textonumerodeestuck.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al añadir el producto.");
        }
    }

    private void actualizarProducto(int idProducto, String nombre, double precio, int stock) {
        try (Connection conn = LoginGreen.connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE productos SET nombre = ?, precio = ?, stock = ? WHERE id_producto = ?")) {

            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            pstmt.setInt(3, stock);
            pstmt.setInt(4, idProducto);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Producto actualizado con éxito.");
            cargarProductos();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto.");
        }
    }

    private void eliminarProducto(int idProducto) {
        try (Connection conn = LoginGreen.connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM productos WHERE id_producto = ?")) {

            pstmt.setInt(1, idProducto);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Producto eliminado con éxito.");
            cargarProductos();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto.");
        }
    }
}
