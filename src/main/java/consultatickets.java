import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class consultatickets extends JFrame {

    private static final long serialVersionUID = 1L; // Identificador único para la serialización de la clase.

    private JPanel contentPane; // Panel principal de la ventana donde se colocan los componentes.

    // Credenciales para conectarse a la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tiendecitaCLR"; // URL de la base de datos.
    private static final String DB_USER = "root"; // Usuario de la base de datos.
    private static final String DB_PASSWORD = "Carlitos.1"; // Contraseña de la base de datos.

    /**
     * Método principal para lanzar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() { // Ejecuta la interfaz gráfica en un hilo separado.
            public void run() {
                try {
                    consultatickets frame = new consultatickets(); // Crea la ventana principal.
                    frame.setVisible(true); // Hace visible la ventana.
                } catch (Exception e) {
                    e.printStackTrace(); // Imprime los detalles de cualquier excepción.
                }
            }
        });
    }

    /**
     * Constructor que configura la ventana principal.
     */
    public consultatickets() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configura la acción de cerrar la ventana.
        setBounds(100, 100, 500, 400); // Establece el tamaño y posición de la ventana.

        contentPane = new JPanel(); // Crea el panel principal.
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Configura un borde vacío para el panel.
        setContentPane(contentPane); // Establece el panel principal como contenido de la ventana.
        contentPane.setLayout(null); // Usa un diseño absoluto para colocar los componentes.

        // Crear un JTextArea para mostrar los datos de los tickets.
        JTextArea textArea = new JTextArea();
        textArea.setBounds(20, 20, 445, 250); // Establece la posición y el tamaño del JTextArea.
        textArea.setEditable(false); // Desactiva la edición para que sea de solo lectura.
        contentPane.add(textArea); // Agrega el JTextArea al panel.

        // Crear un botón "Volver" para cerrar la ventana.
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() { // Agrega un listener para manejar clics.
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual.
            }
        });
        btnVolver.setBounds(190, 290, 100, 30); // Establece la posición y el tamaño del botón.
        contentPane.add(btnVolver); // Agrega el botón al panel.

        // Llama al método para cargar los datos de los tickets en el JTextArea.
        cargarTickets(textArea);
    }

    /**
     * Método para cargar los datos de la tabla `Tickets` y mostrarlos en el JTextArea.
     */
    private void cargarTickets(JTextArea textArea) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); // Conexión a la base de datos.
             Statement statement = connection.createStatement(); // Crea un Statement para ejecutar consultas.
             ResultSet resultSet = statement.executeQuery( // Ejecuta la consulta SQL.
                     "SELECT idTicket, codigo, fecha, totalprecio FROM Tickets")) {

            // Usa un StringBuilder para construir el texto a mostrar.
            StringBuilder data = new StringBuilder();

            // Itera sobre los resultados de la consulta.
            while (resultSet.next()) {
                int idTicket = resultSet.getInt("idTicket"); // Obtiene el ID del ticket.
                String codigo = resultSet.getString("codigo"); // Obtiene el código del ticket.
                String fecha = resultSet.getString("fecha"); // Obtiene la fecha del ticket.
                int totalPrecio = resultSet.getInt("totalprecio"); // Obtiene el total del precio del ticket.

                // Construye una línea de texto para cada ticket.
                data.append("ID: ").append(idTicket)
                    .append(" | Código: ").append(codigo)
                    .append(" | Fecha: ").append(fecha)
                    .append(" | Total Precio: ").append(totalPrecio)
                    .append("\n"); // Agrega un salto de línea para separar registros.
            }

            // Establece el texto construido en el JTextArea.
            textArea.setText(data.toString());

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime detalles del error en la consola.
            // Muestra un cuadro de diálogo con un mensaje de error.
            JOptionPane.showMessageDialog(
                    null, 
                    "Error al cargar los tickets: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
