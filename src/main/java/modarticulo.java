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

public class modarticulo extends JFrame {

    private static final long serialVersionUID = 1L; // Identificador único para la serialización de la clase.

    private JPanel contentPane; // Panel principal donde se colocan los componentes.
    private JTextField textFieldPrecio; // Campo de texto para ingresar o mostrar el precio del artículo.
    private JTextField textFieldCantidad; // Campo de texto para ingresar o mostrar la cantidad del artículo.
    private JTextField textFieldDescripcion; // Campo de texto para ingresar o mostrar la descripción del artículo.
    private JComboBox<String> comboBox; // Menú desplegable para seleccionar un artículo.
    private DefaultComboBoxModel<String> comboBoxModel; // Modelo asociado al ComboBox.

    // Credenciales de conexión a la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tiendecitaCLR";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "Carlitos.1"; 

    /**
     * Método principal para ejecutar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() { 
            public void run() {
                try {
                    modarticulo frame = new modarticulo(); // Crea la ventana principal.
                    frame.setVisible(true); // Hace visible la ventana.
                } catch (Exception e) {
                    e.printStackTrace(); // Muestra detalles de cualquier excepción.
                }
            }
        });
    }

    /**
     * Constructor que configura la ventana principal.
     */
    public modarticulo() {
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel(); 
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); 
        setContentPane(contentPane); 
        contentPane.setLayout(null); 

        // Etiqueta para indicar al usuario que seleccione un artículo.
        JLabel lblNewLabel = new JLabel("Seleccione artículo a modificar:");
        lblNewLabel.setBounds(140, 10, 200, 20);
        contentPane.add(lblNewLabel);

        // ComboBox para seleccionar un artículo.
        comboBoxModel = new DefaultComboBoxModel<>(); 
        comboBox = new JComboBox<>(comboBoxModel);
        comboBox.setBounds(125, 40, 200, 25); 
        comboBox.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                cargarDatosArticulo((String) comboBox.getSelectedItem()); // Carga los datos del artículo seleccionado.
            }
        });
        contentPane.add(comboBox);

        // Etiquetas para los campos de texto.
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(50, 90, 100, 20);
        contentPane.add(lblDescripcion);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(50, 130, 100, 20);
        contentPane.add(lblPrecio);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(50, 170, 100, 20);
        contentPane.add(lblCantidad);

        // Campos de texto para ingresar los datos del artículo.
        textFieldDescripcion = new JTextField();
        textFieldDescripcion.setBounds(170, 90, 200, 25);
        contentPane.add(textFieldDescripcion);

        textFieldPrecio = new JTextField();
        textFieldPrecio.setBounds(170, 130, 200, 25);
        contentPane.add(textFieldPrecio);

        textFieldCantidad = new JTextField();
        textFieldCantidad.setBounds(170, 170, 200, 25);
        contentPane.add(textFieldCantidad);

        // Botón "Aceptar" para actualizar el artículo.
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(50, 220, 100, 30);
        btnAceptar.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                actualizarArticulo((String) comboBox.getSelectedItem()); // Actualiza el artículo seleccionado.
            }
        });
        contentPane.add(btnAceptar);

        // Botón "Limpiar" para borrar los datos de los campos de texto.
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(170, 220, 100, 30);
        btnLimpiar.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                textFieldDescripcion.setText(""); 
                textFieldPrecio.setText("");
                textFieldCantidad.setText("");
            }
        });
        contentPane.add(btnLimpiar);

        // Botón "Volver" para cerrar la ventana.
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(290, 220, 100, 30);
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana.
            }
        });
        contentPane.add(btnVolver);

        cargarArticulos(); // Carga los artículos en el ComboBox al iniciar la ventana.
    }

    /**
     * Cargar los artículos desde la base de datos al ComboBox.
     */
    private void cargarArticulos() {
        comboBoxModel.removeAllElements(); // Limpia el ComboBox antes de cargar nuevos datos.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT descripcion FROM Articulos");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) { 
                String descripcion = resultSet.getString("descripcion");
                comboBoxModel.addElement(descripcion); // Agrega cada descripción al ComboBox.
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(null, "Error al cargar los artículos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cargar los datos de un artículo seleccionado en los campos de texto.
     */
    private void cargarDatosArticulo(String descripcion) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Articulos WHERE descripcion = ?")) {

            preparedStatement.setString(1, descripcion);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) { 
                textFieldDescripcion.setText(resultSet.getString("descripcion"));
                textFieldPrecio.setText(String.valueOf(resultSet.getInt("precio")));
                textFieldCantidad.setText(String.valueOf(resultSet.getInt("cantidad")));
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(null, "Error al cargar los datos del artículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualizar un artículo en la base de datos.
     */
    private void actualizarArticulo(String descripcionOriginal) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE Articulos SET descripcion = ?, precio = ?, cantidad = ? WHERE descripcion = ?")) {

            preparedStatement.setString(1, textFieldDescripcion.getText()); 
            preparedStatement.setInt(2, Integer.parseInt(textFieldPrecio.getText()));
            preparedStatement.setInt(3, Integer.parseInt(textFieldCantidad.getText()));
            preparedStatement.setString(4, descripcionOriginal); 

            int rowsUpdated = preparedStatement.executeUpdate(); 
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Artículo actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarArticulos(); 
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el artículo.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el artículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
