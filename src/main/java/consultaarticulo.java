import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class consultaarticulo extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tiendecitaCLR";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Carlitos.1";

    private JPanel contentPane;
    private JTextArea textArea;

    public consultaarticulo() {
        setTitle("Consulta de Artículos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 400);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textArea = new JTextArea();
        textArea.setBounds(20, 20, 445, 250);
        textArea.setEditable(false);
        contentPane.add(textArea);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> dispose());
        btnVolver.setBounds(330, 300, 100, 30);
        contentPane.add(btnVolver);

        JButton btnGenerarInforme = new JButton("Generar Informe");
        btnGenerarInforme.addActionListener(e -> generarInformeArticulos());
        btnGenerarInforme.setBounds(50, 300, 150, 30);
        contentPane.add(btnGenerarInforme);

        cargarArticulos(textArea);
    }

    /**
     * Método para cargar los datos de la tabla `Articulos` y mostrarlos en el JTextArea.
     */
    private void cargarArticulos(JTextArea textArea) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT idArticulo, descripcion, precio, cantidad FROM Articulos")) {

            StringBuilder data = new StringBuilder();

            while (resultSet.next()) {
                int idArticulo = resultSet.getInt("idArticulo");
                String descripcion = resultSet.getString("descripcion");
                int precio = resultSet.getInt("precio");
                int cantidad = resultSet.getInt("cantidad");

                data.append("ID: ").append(idArticulo)
                    .append(" | Descripción: ").append(descripcion)
                    .append(" | Precio: ").append(precio)
                    .append(" | Cantidad: ").append(cantidad)
                    .append("\n");
            }

            textArea.setText(data.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los artículos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para generar el informe de artículos en JasperReports.
     */
    private void generarInformeArticulos() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Cargar el informe compilado desde src/main/resources
            JasperReport reporte = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("InformeArticulos .jasper"));

            HashMap<String, Object> parametros = new HashMap<>();

            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, connection);

            JasperViewer.viewReport(print, false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error al generar el informe: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método principal para lanzar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                consultaarticulo frame = new consultaarticulo();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
