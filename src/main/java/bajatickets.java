import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class bajatickets extends JFrame {

    private static final long serialVersionUID = 1L; // Identificador único para la serialización de la clase.

    private JPanel contentPane; // Panel principal donde se colocan los componentes.
    private JComboBox<String> comboBox; // Menú desplegable para mostrar los códigos de los tickets.
    private DefaultComboBoxModel<String> comboBoxModel; // Modelo que gestiona los datos del ComboBox.

    // Credenciales para la conexión con la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tiendecitaCLR"; // URL de la base de datos.
    private static final String DB_USER = "root"; // Usuario de la base de datos.
    private static final String DB_PASSWORD = "Carlitos.1"; // Contraseña de la base de datos.

    /**
     * Método principal para ejecutar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> { // Ejecuta la interfaz gráfica en un hilo separado.
            try {
                bajatickets frame = new bajatickets(); // Crea una instancia de la ventana `bajatickets`.
                frame.setVisible(true); // Hace visible la ventana.
            } catch (Exception e) {
                e.printStackTrace(); // Imprime cualquier excepción que ocurra.
            }
        });
    }

    /**
     * Constructor que configura la ventana principal.
     */
    public bajatickets() {
        // Configura el tamaño y la posición de la ventana.
        setBounds(100, 100, 450, 300);

        // Configuración del panel principal.
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Agrega un borde de 5 píxeles alrededor del panel.
        setContentPane(contentPane); // Establece el panel como contenido de la ventana.
        contentPane.setLayout(null); // Diseño absoluto para posicionar los componentes.

        // Etiqueta que indica al usuario que seleccione un ticket.
        JLabel lblNewLabel = new JLabel("Seleccione ticket a eliminar:");
        lblNewLabel.setBounds(125, 30, 200, 20); // Centrado horizontalmente.
        contentPane.add(lblNewLabel); // Agrega la etiqueta al panel.

        // ComboBox para seleccionar el ticket a eliminar.
        comboBoxModel = new DefaultComboBoxModel<>(); // Modelo para manejar los datos del ComboBox.
        comboBox = new JComboBox<>(comboBoxModel); // Crea el ComboBox basado en el modelo.
        comboBox.setBounds(125, 70, 200, 25); // Centrado horizontalmente.
        contentPane.add(comboBox); // Agrega el ComboBox al panel.

        // Botón "Eliminar" para eliminar el ticket seleccionado.
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(100, 150, 100, 30); // Botón alineado a la izquierda.
        btnEliminar.addActionListener(e -> { // Listener para manejar el clic en el botón.
            String codigoSeleccionado = (String) comboBox.getSelectedItem(); // Obtiene el ticket seleccionado.
            if (codigoSeleccionado != null) {
                // Muestra un cuadro de confirmación antes de eliminar.
                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Está seguro de que desea eliminar el ticket seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) { // Si el usuario confirma.
                    eliminarTicket(codigoSeleccionado); // Llama al método para eliminar el ticket.
                    cargarTickets(); // Recarga los tickets en el ComboBox.
                }
            } else {
                // Muestra un mensaje de error si no hay un ticket seleccionado.
                JOptionPane.showMessageDialog(null,
                        "Seleccione un ticket para eliminar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        contentPane.add(btnEliminar); // Agrega el botón al panel.

        // Botón "Cancelar" para cerrar la ventana.
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 150, 100, 30); // Botón alineado a la derecha.
        btnCancelar.addActionListener(e -> dispose()); // Cierra la ventana al hacer clic.
        contentPane.add(btnCancelar); // Agrega el botón al panel.

        cargarTickets(); // Carga los tickets disponibles al iniciar la ventana.
    }

    /**
     * Método para cargar los tickets desde la base de datos al ComboBox.
     */
    private void cargarTickets() {
        comboBoxModel.removeAllElements(); // Limpia el modelo antes de recargar.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT codigo FROM Tickets");
             ResultSet resultSet = preparedStatement.executeQuery()) { // Ejecuta la consulta.

            // Recorre los resultados de la consulta.
            while (resultSet.next()) {
                String codigo = resultSet.getString("codigo"); // Obtiene el código del ticket.
                comboBoxModel.addElement(codigo); // Agrega el código al modelo del ComboBox.
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime los detalles del error en la consola.
            // Muestra un cuadro de diálogo con el mensaje de error.
            JOptionPane.showMessageDialog(null, "Error al cargar los tickets: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para eliminar un ticket de la base de datos.
     */
    private void eliminarTicket(String codigo) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Paso 1: Obtener el idTicket basado en el código del ticket
            int idTicket = -1;
            try (PreparedStatement selectStmt = connection.prepareStatement(
                    "SELECT idTicket FROM Tickets WHERE codigo = ?")) {
                selectStmt.setString(1, codigo);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    idTicket = rs.getInt("idTicket");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el ticket seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Paso 2: Eliminar las relaciones en ArticulosTickets
            try (PreparedStatement deleteRelationsStmt = connection.prepareStatement(
                    "DELETE FROM ArticulosTickets WHERE idTicketFk = ?")) {
                deleteRelationsStmt.setInt(1, idTicket);
                deleteRelationsStmt.executeUpdate();
            }

            // Paso 3: Eliminar el ticket
            try (PreparedStatement deleteTicketStmt = connection.prepareStatement(
                    "DELETE FROM Tickets WHERE idTicket = ?")) {
                deleteTicketStmt.setInt(1, idTicket);
                int rowsDeleted = deleteTicketStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Ticket eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el ticket seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el ticket: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
