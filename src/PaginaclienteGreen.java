import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.*;


public class PaginaclienteGreen extends JFrame {
    public JPanel paginaclientes;
    private JTabbedPane tabbedPane1;
    private JLabel mensajebienvenida;
    private JTextField textbuscar;
    private JButton botnbuscar;
    private JTable tablaproductos;
    private JTable carritotabla;
    private JButton botncomprar;
    private JButton eliminarDelCarritoButton;
    private JButton anadiralcarrito;
    private JLabel valortotaltex;
    private JButton cerrarSesionButton;

    private String usuario; // Nombre del usuario actual
    private int idUsuario;  // ID del usuario actual en la base de datos

    public PaginaclienteGreen(String usuario, int idUsuario) {
        this.usuario = usuario;
        this.idUsuario = idUsuario;

        // Mensaje de bienvenida
        mensajebienvenida.setText("¡Bienvenido, " + usuario + "!");

        // Cargar datos iniciales
        cargarProductos("");
        cargarCarrito();

        // Buscar productos
        botnbuscar.addActionListener(e -> cargarProductos(textbuscar.getText()));

        // Añadir producto al carrito
        anadiralcarrito.addActionListener(e -> {
            int selectedRow = tablaproductos.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto para añadir al carrito.");
                return;
            }

            int idProducto = (int) tablaproductos.getValueAt(selectedRow, 0);
            int stock = (int) tablaproductos.getValueAt(selectedRow, 3);
            String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad:");

            if (cantidadStr == null || !cantidadStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Cantidad inválida.");
                return;
            }

            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0 || cantidad > stock) {
                JOptionPane.showMessageDialog(null, "Cantidad fuera de rango.");
                return;
            }

            try (Connection conn = LoginGreen.connectDatabase();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "INSERT INTO carrito (id_usuario, id_producto, cantidad) " +
                                 "VALUES (?, ?, ?) " +
                                 "ON CONFLICT(id_usuario, id_producto) DO UPDATE SET cantidad = cantidad + excluded.cantidad")) {
                pstmt.setInt(1, idUsuario);
                pstmt.setInt(2, idProducto);
                pstmt.setInt(3, cantidad);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Producto añadido al carrito.");
                cargarCarrito();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al añadir el producto al carrito.");
            }
        });

        // Eliminar producto del carrito
        eliminarDelCarritoButton.addActionListener(e -> {
            int selectedRow = carritotabla.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto para eliminar.");
                return;
            }

            int idProducto = (int) carritotabla.getValueAt(selectedRow, 0);

            try (Connection conn = LoginGreen.connectDatabase();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM carrito WHERE id_usuario = ? AND id_producto = ?")) {
                pstmt.setInt(1, idUsuario);
                pstmt.setInt(2, idProducto);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Producto eliminado del carrito.");
                cargarCarrito();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar el producto del carrito.");
            }
        });

        // Confirmar compra
        botncomprar.addActionListener(e -> confirmarCompra());
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
                    ((JFrame) SwingUtilities.getWindowAncestor(paginaclientes)).dispose();
                }
            }
        });
    }

    private void cargarProductos(String buscar) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock"}, 0);
        try (Connection conn = LoginGreen.connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM productos WHERE nombre LIKE ?")) {
            pstmt.setString(1, "%" + buscar + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los productos.");
        }
        tablaproductos.setModel(model);
    }

    private void cargarCarrito() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "Cantidad", "Subtotal"}, 0);
        double total = 0;

        try (Connection conn = LoginGreen.connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT c.id_producto, p.nombre, c.cantidad, (c.cantidad * p.precio) AS subtotal " +
                             "FROM carrito c INNER JOIN productos p ON c.id_producto = p.id_producto WHERE c.id_usuario = ?")) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal")
                });
                total += rs.getDouble("subtotal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar el carrito.");
        }

        carritotabla.setModel(model);
        valortotaltex.setText("Total: $" + total);
    }

    private void confirmarCompra() {
        try (Connection conn = LoginGreen.connectDatabase()) {
            conn.setAutoCommit(false);

            // Insertar en ventas
            String insertVenta = "INSERT INTO ventas (id_usuario, fecha_venta, total) VALUES (?, DATE('now'), ?)";
            double total = obtenerTotalCarrito();
            int idVenta;

            try (PreparedStatement pstmtVenta = conn.prepareStatement(insertVenta, Statement.RETURN_GENERATED_KEYS)) {
                pstmtVenta.setInt(1, idUsuario);
                pstmtVenta.setDouble(2, total);
                pstmtVenta.executeUpdate();

                ResultSet rs = pstmtVenta.getGeneratedKeys();
                if (rs.next()) {
                    idVenta = rs.getInt(1);
                } else {
                    throw new SQLException("Error al obtener ID de la venta.");
                }
            }

            // Insertar en detalle_ventas
            String insertDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio, subtotal) " +
                    "SELECT ?, c.id_producto, c.cantidad, p.precio, (c.cantidad * p.precio) AS subtotal " +
                    "FROM carrito c " +
                    "INNER JOIN productos p ON c.id_producto = p.id_producto WHERE c.id_usuario = ?";

            try (PreparedStatement pstmtDetalle = conn.prepareStatement(insertDetalle)) {
                pstmtDetalle.setInt(1, idVenta);
                pstmtDetalle.setInt(2, idUsuario);
                pstmtDetalle.executeUpdate();
            }

            // Vaciar carrito
            try (PreparedStatement pstmtCarrito = conn.prepareStatement("DELETE FROM carrito WHERE id_usuario = ?")) {
                pstmtCarrito.setInt(1, idUsuario);
                pstmtCarrito.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(null, "Gracias por preferirnos. Su factura se ha descargado.");
            generarFacturaPDF(idVenta);
            cargarCarrito();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al confirmar la compra.");
        }
    }

    private double obtenerTotalCarrito() {
        double total = 0;
        try (Connection conn = LoginGreen.connectDatabase();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT SUM(c.cantidad * p.precio) AS total " +
                             "FROM carrito c INNER JOIN productos p ON c.id_producto = p.id_producto WHERE c.id_usuario = ?")) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al calcular el total del carrito.");
        }
        return total;
    }

    private void generarFacturaPDF(int idVenta) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Factura_" + idVenta + ".pdf"));
            document.open();

            document.add(new Paragraph("Factura"));
            document.add(new Paragraph("ID Venta: " + idVenta));
            document.add(new Paragraph("Cliente: " + usuario));
            document.add(new Paragraph(""));

            PdfPTable table = new PdfPTable(4);
            table.addCell("ID Producto");
            table.addCell("Nombre");
            table.addCell("Cantidad");
            table.addCell("Subtotal");

            try (Connection conn = LoginGreen.connectDatabase();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "SELECT c.id_producto, p.nombre, c.cantidad, (c.cantidad * p.precio) AS subtotal " +
                                 "FROM detalle_ventas c INNER JOIN productos p ON c.id_producto = p.id_producto WHERE c.id_venta = ?")) {
                pstmt.setInt(1, idVenta);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    table.addCell(String.valueOf(rs.getInt("id_producto")));
                    table.addCell(rs.getString("nombre"));
                    table.addCell(String.valueOf(rs.getInt("cantidad")));
                    table.addCell(String.valueOf(rs.getDouble("subtotal")));
                }
            }

            document.add(table);
            document.close();

            JOptionPane.showMessageDialog(null, "Factura generada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar la factura.");
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
