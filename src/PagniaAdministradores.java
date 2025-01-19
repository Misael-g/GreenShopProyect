import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PagniaAdministradores {
    public JPanel paginaadmistradores;
    private JTabbedPane tabbedPane1;
    private JButton cambirolbotn;
    private JTextField textonombreproduct;
    private JTextField textoprecioproduct;
    private JTextField textonumerodeestuck;
    private JButton añadirProductoButton;
    private JButton actualizarProductoButton;
    private JButton eliminarProductoButton;
    private JTable tablaproductos;
    private JTable tablaclientesresgitrados;
    private JTable tablahistorialventas;

    public PagniaAdministradores() {
        // Botón para cambiar de rol (regresa al login)
        cambirolbotn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Login");
                frame.setContentPane(new LoginGreen().Greenmainpanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 800);
                frame.setVisible(true);
            }
        });

        // Añadir producto
        añadirProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textonombreproduct.getText();
                String precio = textoprecioproduct.getText();
                String stock = textonumerodeestuck.getText();

                if (nombre.isEmpty() || precio.isEmpty() || stock.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                    return;
                }

                try {
                    double precioDouble = Double.parseDouble(precio);
                    int stockInt = Integer.parseInt(stock);
                    // Lógica para añadir producto a la base de datos
                    añadirProducto(nombre, precioDouble, stockInt);
                    JOptionPane.showMessageDialog(null, "Producto añadido con éxito");
                    actualizarTablaProductos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Precio y stock deben ser números válidos");
                }
            }
        });

        // Actualizar producto
        actualizarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textonombreproduct.getText();
                String precio = textoprecioproduct.getText();
                String stock = textonumerodeestuck.getText();

                if (nombre.isEmpty() || precio.isEmpty() || stock.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                    return;
                }

                try {
                    double precioDouble = Double.parseDouble(precio);
                    int stockInt = Integer.parseInt(stock);
                    // Lógica para actualizar producto en la base de datos
                    actualizarProducto(nombre, precioDouble, stockInt);
                    JOptionPane.showMessageDialog(null, "Producto actualizado con éxito");
                    actualizarTablaProductos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Precio y stock deben ser números válidos");
                }
            }
        });

        // Eliminar producto
        eliminarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textonombreproduct.getText();

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar el nombre del producto a eliminar");
                    return;
                }

                // Lógica para eliminar producto de la base de datos
                eliminarProducto(nombre);
                JOptionPane.showMessageDialog(null, "Producto eliminado con éxito");
                actualizarTablaProductos();
            }
        });
    }

    private void añadirProducto(String nombre, double precio, int stock) {
        // Implementar lógica para añadir un producto a la base de datos
        System.out.println("Producto añadido: " + nombre + ", Precio: " + precio + ", Stock: " + stock);
    }

    private void actualizarProducto(String nombre, double precio, int stock) {
        // Implementar lógica para actualizar un producto en la base de datos
        System.out.println("Producto actualizado: " + nombre + ", Precio: " + precio + ", Stock: " + stock);
    }

    private void eliminarProducto(String nombre) {
        // Implementar lógica para eliminar un producto de la base de datos
        System.out.println("Producto eliminado: " + nombre);
    }

    private void actualizarTablaProductos() {
        // Lógica para actualizar los datos en la tabla de productos
        System.out.println("Tabla de productos actualizada");
    }
}
