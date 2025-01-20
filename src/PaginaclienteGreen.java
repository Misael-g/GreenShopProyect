import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class PaginaclienteGreen extends JFrame {
    public JPanel paginaclientes;
    private JTabbedPane tabbedPane1;
    private JLabel mensajebienvenida;
    private JTextField textbuscar;
    private JButton botnbuscar;
    private JTable tablaproductos;
    private JTable carritotabla;
    private JButton botncomprar;
    private JTable tablaultimacompra;
    private JButton verFacturaButton;
    private JButton eliminarDelCarritoButton;

}
