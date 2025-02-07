package com.greenshop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
/**
 * Esta clase representa la interfaz de administración para los administradores de la tienda GreenShop.
 * Permite gestionar productos, usuarios, historial de ventas y el cierre de sesión.
 */

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
    private JButton ELIMINARROLADMINISTRADORButton;

    /**
     * Constructor de la clase que inicializa la interfaz y los eventos.
     * Carga los datos iniciales de productos, usuarios y ventas.
     */

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
        // Botón para eliminar el rol de administrador y convertirlo en cliente
        ELIMINARROLADMINISTRADORButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tablaclientesresgitrados.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un administrador para cambiar su rol a cliente.");
                    return;
                }

                String usuario = tablaclientesresgitrados.getValueAt(selectedRow, 0).toString();
                eliminarRolAdministrador(usuario);
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
                    loginFrame.setSize(500, 500);
                    loginFrame.setLocationRelativeTo(null); // Centrar ventana en la pantalla
                    loginFrame.setVisible(true);

                    // Cerrar la ventana actual (la de administrador)
                    ((JFrame) SwingUtilities.getWindowAncestor(paginaadmistradores)).dispose();
                }
            }
        });

    }
    /**
     * Carga los usuarios registrados en la base de datos y los muestra en la tabla correspondiente.
     */
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
    /**
     * Carga los productos disponibles en la base de datos y los muestra en la tabla correspondiente.
     */
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
    /**
     * Carga el historial de ventas de la tienda y lo muestra en la tabla correspondiente.
     */
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
    /**
     * Cambia el rol de un usuario a "administrador".
     * @param usuario El nombre del usuario cuyo rol será modificado.
     */
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
    /**
     * Elimina el rol de administrador de un usuario y lo cambia a "cliente".
     * @param usuario El nombre del usuario cuyo rol será eliminado.
     */
    private void eliminarRolAdministrador(String usuario) {
        try (Connection conn = LoginGreen.connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE usuarios SET rol = 'cliente' WHERE usuario = ? AND rol = 'administrador'")) {

            pstmt.setString(1, usuario);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Rol eliminado con éxito. Ahora es un cliente.");
                cargarUsuarios(); // Refrescar la tabla
            } else {
                JOptionPane.showMessageDialog(null, "El usuario no es administrador o hubo un error.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cambiar el rol del usuario.");
        }
    }
    /**
     * Añade un nuevo producto a la base de datos.
     * @param nombre El nombre del producto.
     * @param precio El precio del producto.
     * @param stock La cantidad de stock disponible.
     */

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
    /**
     * Actualiza los datos de un producto existente en la base de datos.
     * @param idProducto El ID del producto a actualizar.
     * @param nombre El nuevo nombre del producto.
     * @param precio El nuevo precio del producto.
     * @param stock El nuevo stock del producto.
     */
    private void actualizarProducto(int idProducto, String nombre, double precio, int stock) {
        try (Connection conn = LoginGreen.connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE productos SET nombre = ?, precio = ?, stock = ? WHERE id_producto = ?")) {

            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            pstmt.setInt(3, stock);
            pstmt.setInt(4, idProducto);
            pstmt.executeUpdate();

            // Limpiar los campos de entrada
            textonombreproduct.setText("");
            textoprecioproduct.setText("");
            textonumerodeestuck.setText("");

            JOptionPane.showMessageDialog(null, "Producto actualizado con éxito.");
            cargarProductos();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto.");
        }
    }
    /**
     * Elimina un producto de la base de datos.
     * @param idProducto El ID del producto a eliminar.
     */
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
