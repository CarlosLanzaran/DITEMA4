import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.SQLException; 

public class altaarticulos extends JFrame {
    // Identificador único de la clase, requerido para la serialización.
    private static final long serialVersionUID = 1L;

    // Panel principal que contiene todos los elementos de la interfaz.
    private JPanel contentPane;
    
    // Campos de texto para ingresar datos del artículo.
    private JTextField textFieldDescripcion;
    private JTextField textFieldPrecio;
    private JTextField textFieldCantidad;

    // Credenciales de conexión a la base de datos MySQL.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tiendecitaCLR"; // URL de la base de datos.
    private static final String DB_USER = "root"; // Usuario de la base de datos.
    private static final String DB_PASSWORD = "Carlitos.1"; // Contraseña de la base de datos.

    /**
     * Método principal para lanzar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> { // Ejecuta la aplicación en el hilo de la GUI.
            try {
                altaarticulos frame = new altaarticulos(); // Crea la ventana principal.
                frame.setVisible(true); // Hace visible la ventana.
            } catch (Exception e) {
                e.printStackTrace(); // Imprime cualquier error que ocurra.
            }
        });
    }

    /**
     * Constructor que crea y configura la ventana principal.
     */
    public altaarticulos() {
        // Configuración básica de la ventana.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana, no toda la aplicación.
        setBounds(100, 100, 450, 300); // Establece el tamaño y la posición de la ventana.
        
        // Inicializa el panel principal y configura su borde.
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane); // Agrega el panel principal a la ventana.
        contentPane.setLayout(null); // Usar un diseño absoluto (posiciones fijas).

        // Etiqueta y campo de texto para la descripción.
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(50, 50, 100, 20); // Posición y tamaño.
        contentPane.add(lblDescripcion); // Agrega al panel.

        textFieldDescripcion = new JTextField();
        textFieldDescripcion.setBounds(160, 50, 200, 20);
        contentPane.add(textFieldDescripcion);

        // Etiqueta y campo de texto para el precio.
        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(50, 100, 100, 20);
        contentPane.add(lblPrecio);

        textFieldPrecio = new JTextField();
        textFieldPrecio.setBounds(160, 100, 200, 20);
        contentPane.add(textFieldPrecio);

        // Etiqueta y campo de texto para la cantidad.
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(50, 150, 100, 20);
        contentPane.add(lblCantidad);

        textFieldCantidad = new JTextField();
        textFieldCantidad.setBounds(160, 150, 200, 20);
        contentPane.add(textFieldCantidad);

        // Botón "Aceptar" para insertar un artículo.
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(50, 200, 100, 30);
        btnAceptar.addActionListener(new ActionListener() { // Listener para manejar el clic.
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtiene los valores ingresados por el usuario.
                String descripcion = textFieldDescripcion.getText();
                String precioStr = textFieldPrecio.getText();
                String cantidadStr = textFieldCantidad.getText();

                // Valida que no haya campos vacíos.
                if (descripcion.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, rellena todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Sale del método si hay campos vacíos.
                }

                try {
                    int precio = Integer.parseInt(precioStr); // Convierte el precio a entero.
                    int cantidad = Integer.parseInt(cantidadStr); // Convierte la cantidad a entero.

                    // Llama al método para insertar datos en la base de datos.
                    insertarArticulo(descripcion, precio, cantidad);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Precio y cantidad deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(btnAceptar);

        // Botón "Limpiar" para vaciar los campos de texto.
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(180, 200, 100, 30);
        btnLimpiar.addActionListener(e -> { // Usa una expresión lambda para manejar el clic.
            textFieldDescripcion.setText(""); // Limpia la descripción.
            textFieldPrecio.setText(""); // Limpia el precio.
            textFieldCantidad.setText(""); // Limpia la cantidad.
        });
        contentPane.add(btnLimpiar);

        // Botón "Volver" para cerrar la ventana actual.
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(310, 200, 100, 30);
        btnVolver.addActionListener(e -> dispose()); // Cierra la ventana.
        contentPane.add(btnVolver);
    }

    /**
     * Método para insertar un artículo en la base de datos.
     */
    private void insertarArticulo(String descripcion, int precio, int cantidad) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); 
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Articulos (descripcion, precio, cantidad) VALUES (?, ?, ?)")) {
            // Configura los valores para la consulta SQL.
            preparedStatement.setString(1, descripcion);
            preparedStatement.setInt(2, precio);
            preparedStatement.setInt(3, cantidad);

            // Ejecuta la consulta.
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Artículo agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime detalles del error en la consola.
            JOptionPane.showMessageDialog(null, "Error al agregar el artículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
