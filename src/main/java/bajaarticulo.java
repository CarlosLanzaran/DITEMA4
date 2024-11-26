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

public class bajaarticulo extends JFrame {

    private static final long serialVersionUID = 1L; // Identificador único para la serialización.

    private JPanel contentPane; // Panel principal de la ventana.
    private JComboBox<String> comboBox; // Menú desplegable para mostrar los artículos disponibles.
    private DefaultComboBoxModel<String> comboBoxModel; // Modelo para gestionar los datos del ComboBox.

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tiendecitaCLR"; // URL de la base de datos.
    private static final String DB_USER = "root"; // Usuario de la base de datos.
    private static final String DB_PASSWORD = "Carlitos.1"; // Contraseña de la base de datos.

    /**
     * Método principal que lanza la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() { // Ejecuta la GUI en un hilo separado.
            public void run() {
                try {
                    bajaarticulo frame = new bajaarticulo(); // Crea una instancia de la ventana.
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
    public bajaarticulo() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configura la acción al cerrar la ventana.
        setBounds(100, 100, 450, 300); // Establece el tamaño estándar

        contentPane = new JPanel(); // Crea el panel principal.
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Agrega un borde vacío al panel.
        setContentPane(contentPane); // Configura el panel como contenido de la ventana.
        contentPane.setLayout(null); // Usa un diseño absoluto para posicionar componentes.

        // Etiqueta que indica al usuario qué hacer.
        JLabel lblNewLabel = new JLabel("Seleccione artículo a eliminar:");
        lblNewLabel.setBounds(125, 30, 200, 20); // Centrado
        contentPane.add(lblNewLabel); // Agrega la etiqueta al panel.

        // Modelo y ComboBox para mostrar los artículos disponibles.
        comboBoxModel = new DefaultComboBoxModel<>(); // Inicializa el modelo para el ComboBox.
        comboBox = new JComboBox<>(comboBoxModel); // Crea el ComboBox basado en el modelo.
        comboBox.setBounds(125, 70, 200, 25); // Centrado
        contentPane.add(comboBox); // Agrega el ComboBox al panel.

        cargarArticulos(); // Llama al método para cargar los artículos desde la base de datos.

        // Botón "Eliminar" para borrar el artículo seleccionado.
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(100, 150, 100, 30); // Botón izquierdo
        btnEliminar.addActionListener(new ActionListener() { // Listener para manejar el clic.
            public void actionPerformed(ActionEvent e) {
                // Obtiene el artículo seleccionado del ComboBox.
                String descripcionSeleccionada = (String) comboBox.getSelectedItem();
                if (descripcionSeleccionada != null) {
                    // Muestra un cuadro de confirmación antes de eliminar.
                    int confirm = JOptionPane.showConfirmDialog(
                            null, 
                            "¿Está seguro de que desea eliminar el artículo seleccionado?", 
                            "Confirmar eliminación", 
                            JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) { // Si el usuario confirma.
                        eliminarArticulo(descripcionSeleccionada); // Llama al método para eliminar.
                        cargarArticulos(); // Recarga los artículos en el ComboBox después de eliminar.
                    }
                } else {
                    // Muestra un mensaje de error si no hay un artículo seleccionado.
                    JOptionPane.showMessageDialog(
                            null, 
                            "Seleccione un artículo para eliminar.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(btnEliminar); // Agrega el botón "Eliminar" al panel.

        // Botón "Cancelar" para cerrar la ventana.
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() { // Listener para manejar el clic.
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual.
            }
        });
        btnCancelar.setBounds(250, 150, 100, 30); // Botón derecho
        contentPane.add(btnCancelar); // Agrega el botón "Cancelar" al panel.
    }

    /**
     * Método para cargar los artículos desde la base de datos al ComboBox.
     */
    private void cargarArticulos() {
        comboBoxModel.removeAllElements(); // Limpia el modelo antes de agregar nuevos datos.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT descripcion FROM Articulos"); // Consulta para obtener los artículos.
             ResultSet resultSet = preparedStatement.executeQuery()) { // Ejecuta la consulta.

            while (resultSet.next()) { // Itera sobre los resultados de la consulta.
                String descripcion = resultSet.getString("descripcion"); // Obtiene la descripción del artículo.
                comboBoxModel.addElement(descripcion); // Agrega la descripción al modelo del ComboBox.
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra detalles del error en la consola.
            JOptionPane.showMessageDialog(
                    null, 
                    "Error al cargar los artículos: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para eliminar un artículo de la base de datos.
     */
    private void eliminarArticulo(String descripcion) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Paso 1: Obtener el idArticulo basado en la descripción
            int idArticulo = -1;
            try (PreparedStatement selectStmt = connection.prepareStatement(
                    "SELECT idArticulo FROM Articulos WHERE descripcion = ?")) {
                selectStmt.setString(1, descripcion);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    idArticulo = rs.getInt("idArticulo");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el artículo seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Paso 2: Eliminar las relaciones de ArticulosTickets
            try (PreparedStatement deleteRelationsStmt = connection.prepareStatement(
                    "DELETE FROM ArticulosTickets WHERE idArticuloFk = ?")) {
                deleteRelationsStmt.setInt(1, idArticulo);
                deleteRelationsStmt.executeUpdate();
            }

            // Paso 3: Eliminar el artículo
            try (PreparedStatement deleteArticuloStmt = connection.prepareStatement(
                    "DELETE FROM Articulos WHERE idArticulo = ?")) {
                deleteArticuloStmt.setInt(1, idArticulo);
                int rowsDeleted = deleteArticuloStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Artículo eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el artículo seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el artículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
