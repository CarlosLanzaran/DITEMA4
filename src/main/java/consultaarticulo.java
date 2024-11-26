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

public class consultaarticulo extends JFrame {

    private static final long serialVersionUID = 1L; // Identificador único para la serialización de la clase.

    private JPanel contentPane; // Panel principal de la ventana donde se agregarán los componentes.

    // Credenciales para la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tiendecitaCLR"; // URL de la base de datos.
    private static final String DB_USER = "root"; // Usuario de la base de datos.
    private static final String DB_PASSWORD = "Carlitos.1"; // Contraseña de la base de datos.

    /**
     * Método principal para ejecutar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() { // Ejecuta la GUI en un hilo separado.
            public void run() {
                try {
                    consultaarticulo frame = new consultaarticulo(); // Crea la ventana.
                    frame.setVisible(true); // Hace visible la ventana.
                } catch (Exception e) {
                    e.printStackTrace(); // Muestra detalles de cualquier excepción.
                }
            }
        });
    }

    /**
     * Constructor que inicializa y configura la ventana principal.
     */
    public consultaarticulo() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configura la acción al cerrar la ventana.
        setBounds(100, 100, 450, 300); // Establece el tamaño y la posición de la ventana.

        contentPane = new JPanel(); // Crea el panel principal.
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Agrega un borde vacío al panel.
        setContentPane(contentPane); // Establece el panel principal como contenido de la ventana.
        contentPane.setLayout(null); // Usa un diseño absoluto para posicionar los componentes.

        // Crear un JTextArea para mostrar los datos de los artículos.
        JTextArea textArea = new JTextArea();
        textArea.setBounds(20, 20, 395, 180); // Define la posición y tamaño del JTextArea.
        textArea.setEditable(false); // Deshabilita la edición para que sea solo de lectura.
        contentPane.add(textArea); // Agrega el JTextArea al panel.

        // Crear un botón "Volver" para cerrar la ventana.
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() { // Listener para manejar el clic en el botón.
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual.
            }
        });
        btnVolver.setBounds(170, 220, 100, 30); // Define la posición y tamaño del botón.
        contentPane.add(btnVolver); // Agrega el botón al panel.

        // Llamar al método para cargar los datos de los artículos en el JTextArea.
        cargarArticulos(textArea);
    }

    /**
     * Método para cargar los datos de la tabla `Articulos` y mostrarlos en el JTextArea.
     */
    private void cargarArticulos(JTextArea textArea) {
        // Conexión y ejecución de consulta a la base de datos.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); // Establece la conexión.
             Statement statement = connection.createStatement(); // Crea un Statement para ejecutar la consulta.
             ResultSet resultSet = statement.executeQuery( // Ejecuta la consulta SQL.
                     "SELECT idArticulo, descripcion, precio, cantidad FROM Articulos")) {

            // Utiliza un StringBuilder para construir los datos a mostrar.
            StringBuilder data = new StringBuilder();

            // Recorre los resultados de la consulta.
            while (resultSet.next()) {
                int idArticulo = resultSet.getInt("idArticulo"); // Obtiene el ID del artículo.
                String descripcion = resultSet.getString("descripcion"); // Obtiene la descripción del artículo.
                int precio = resultSet.getInt("precio"); // Obtiene el precio del artículo.
                int cantidad = resultSet.getInt("cantidad"); // Obtiene la cantidad del artículo en inventario.

                // Construye la línea de texto para cada artículo en formato legible.
                data.append("ID: ").append(idArticulo)
                    .append(" | Descripción: ").append(descripcion)
                    .append(" | Precio: ").append(precio)
                    .append(" | Cantidad: ").append(cantidad)
                    .append("\n"); // Salto de línea para separar registros.
            }

            // Establece el texto construido en el JTextArea.
            textArea.setText(data.toString());

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra detalles del error en la consola.
            // Muestra un cuadro de diálogo con el error.
            JOptionPane.showMessageDialog(
                    null, 
                    "Error al cargar los artículos: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
