import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
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
                    loginFrame.setSize(500, 500);
                    loginFrame.setLocationRelativeTo(null); // Centrar ventana en la pantalla
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
        if (carritotabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No hay productos en el carrito para comprar.");
            return;
        }

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

            // Actualizar el stock de productos
            String actualizarStock = "UPDATE productos " +
                    "SET stock = stock - (SELECT cantidad FROM detalle_ventas WHERE productos.id_producto = detalle_ventas.id_producto AND id_venta = ?) " +
                    "WHERE id_producto IN (SELECT id_producto FROM detalle_ventas WHERE id_venta = ?)";

            try (PreparedStatement pstmtStock = conn.prepareStatement(actualizarStock)) {
                pstmtStock.setInt(1, idVenta);
                pstmtStock.setInt(2, idVenta);
                pstmtStock.executeUpdate();
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
            PdfWriter.getInstance(document, new FileOutputStream("C:/Users/garci/Downloads/Factura_GreenShop" + idVenta + ".pdf"));
            document.open();

            // Encabezado principal
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            Paragraph title = new Paragraph("Green Shop - Factura", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("ID Venta: " + idVenta, bodyFont));
            document.add(new Paragraph("Cliente: " + usuario, bodyFont));
            document.add(new Paragraph("Fecha: " + new java.util.Date().toString(), bodyFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("------------------------------------------------------------"));
            document.add(new Paragraph(" "));

            // Crear la tabla con encabezados
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2f, 4f, 2f, 2f}); // Ajustar proporciones

            // Encabezados de la tabla
            PdfPCell cell;
            cell = new PdfPCell(new Phrase("ID Producto", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Nombre", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Cantidad", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Subtotal", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            // Obtener datos de la base de datos
            double total = 0;
            try (Connection conn = LoginGreen.connectDatabase();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "SELECT c.id_producto, p.nombre, c.cantidad, (c.cantidad * p.precio) AS subtotal " +
                                 "FROM detalle_ventas c INNER JOIN productos p ON c.id_producto = p.id_producto WHERE c.id_venta = ?")) {
                pstmt.setInt(1, idVenta);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    table.addCell(new Phrase(String.valueOf(rs.getInt("id_producto")), bodyFont));
                    table.addCell(new Phrase(rs.getString("nombre"), bodyFont));
                    table.addCell(new Phrase(String.valueOf(rs.getInt("cantidad")), bodyFont));
                    double subtotal = rs.getDouble("subtotal");
                    total += subtotal;
                    table.addCell(new Phrase(String.format("$%.2f", subtotal), bodyFont));
                }
            }

            // Agregar la tabla al documento
            document.add(table);

            // Agregar el total al final
            Paragraph totalParagraph = new Paragraph("Total: $" + String.format("%.2f", total), headerFont);
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagraph);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Gracias por su compra.", bodyFont));
            document.add(new Paragraph("Green Shop - Su tienda ecológica de confianza.", bodyFont));

            // Cerrar documento
            document.close();

            JOptionPane.showMessageDialog(null, "Factura generada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar la factura.");
        }
    }

}
