import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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

    private String usuario; // Nombre del usuario actual
    private int idUsuario;  // ID del usuario actual en la base de datos
