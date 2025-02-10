import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

public class consultatickets extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Campos de entrada de fecha
    private JTextField txtFechaDesde;
    private JTextField txtFechaHasta;
    
    // Tabla para mostrar los tickets
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Credenciales de la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tiendecitaCLR";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Carlitos.1";

    /**
     * Constructor de la clase. Inicializa la ventana y sus componentes.
     */
    public consultatickets() {
        setTitle("Consulta de Tickets");
        setBounds(100, 100, 600, 400); // Posición y tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Panel superior que contiene los filtros de fecha y el botón de informe
        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new FlowLayout());

        panelFiltros.add(new JLabel("Fecha Desde:"));
        txtFechaDesde = new JTextField(10);
        panelFiltros.add(txtFechaDesde);

        panelFiltros.add(new JLabel("Fecha Hasta:"));
        txtFechaHasta = new JTextField(10);
        panelFiltros.add(txtFechaHasta);

        JButton btnGenerarInforme = new JButton("Generar Informe");
        panelFiltros.add(btnGenerarInforme);
        
        getContentPane().add(panelFiltros, BorderLayout.NORTH);

        // Crear modelo de tabla para mostrar los tickets
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Ticket");
        tableModel.addColumn("Código");
        tableModel.addColumn("Fecha");
        tableModel.addColumn("Total Precio");

        // Crear tabla con el modelo y agregarla a la ventana
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Cargar todos los tickets al abrir la ventana
        cargarTodosLosTickets();

        // Acción del botón para generar informe (sin modificar la tabla)
        btnGenerarInforme.addActionListener(e -> generarInforme());
    }

    /**
     * Método que carga todos los tickets en la tabla desde la base de datos.
     */
    private void cargarTodosLosTickets() {
        tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT idTicket, codigo, fecha, totalprecio FROM tickets")) {

            // Recorrer los resultados y agregarlos a la tabla
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("idTicket"),
                        resultSet.getString("codigo"),
                        resultSet.getDate("fecha"),  // Se guarda como fecha
                        resultSet.getInt("totalprecio")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los tickets: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que genera el informe de JasperReports filtrando los datos por fecha.
     */
    private void generarInforme() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            HashMap<String, Object> parametros = new HashMap<>();

            // Convertir String a Date para asegurar compatibilidad con la base de datos
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDesde = dateFormat.parse(txtFechaDesde.getText());
            Date dateHasta = dateFormat.parse(txtFechaHasta.getText());

            // Convertir a java.sql.Date antes de pasarlo como parámetro
            parametros.put("FechaDesde", new java.sql.Date(dateDesde.getTime()));
            parametros.put("FechaHasta", new java.sql.Date(dateHasta.getTime()));

            // Cargar y generar el informe
            JasperReport reporte = JasperCompileManager.compileReport("src/main/resources/InformeTickets. jrxml");
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, connection);
            JasperViewer.viewReport(print, false); // Mostrar el informe

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Error al convertir la fecha: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException | JRException e) {
            JOptionPane.showMessageDialog(this, "Error al generar el informe: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método principal para ejecutar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                consultatickets frame = new consultatickets();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
