import java.awt.Choice;
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;

public class altatickets extends JFrame {

    private static final long serialVersionUID = 1L; // Identificador único para la serialización de la clase.

    private JPanel contentPane; // Panel principal de la ventana.
    private JTextField textFieldCodigo; // Campo de texto para ingresar el código del ticket.
    private JTextField textFieldFecha; // Campo de texto para ingresar la fecha del ticket.
    private JTextField textFieldTotalPrecio; // Campo de texto para ingresar el total del precio del ticket.
    private Choice choiceArticulos; // Menú desplegable para mostrar las descripciones de los artículos disponibles.
    private HashMap<String, Integer> articulosMap; // Mapa para asociar las descripciones de los artículos con sus IDs.

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tiendecitaCLR"; // URL de la base de datos.
    private static final String DB_USER = "root"; // Usuario de la base de datos.
    private static final String DB_PASSWORD = "Carlitos.1"; // Contraseña de la base de datos.

    /**
     * Método principal que lanza la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> { // Ejecuta la GUI en un hilo separado.
            try {
                altatickets frame = new altatickets(); // Crea la ventana principal.
                frame.setVisible(true); // Hace visible la ventana.
            } catch (Exception e) {
                e.printStackTrace(); // Muestra detalles de cualquier excepción.
            }
        });
    }

    /**
     * Constructor que inicializa y configura la ventana principal.
     */
    public altatickets() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configura la acción al cerrar la ventana.
        setBounds(100, 100, 500, 400); // Establece el tamaño y posición de la ventana.

        contentPane = new JPanel(); // Crea el panel principal.
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Configura un borde vacío.
        setContentPane(contentPane); // Establece el panel principal como contenido.
        contentPane.setLayout(null); // Utiliza un diseño absoluto para los componentes.

        // Etiqueta y campo de texto para el código del ticket.
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(50, 50, 100, 20);
        contentPane.add(lblCodigo);

        textFieldCodigo = new JTextField();
        textFieldCodigo.setBounds(200, 50, 200, 20);
        contentPane.add(textFieldCodigo);

        // Etiqueta y campo de texto para la fecha del ticket.
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setBounds(50, 100, 150, 20);
        contentPane.add(lblFecha);

        textFieldFecha = new JTextField();
        textFieldFecha.setBounds(200, 100, 200, 20);
        contentPane.add(textFieldFecha);

        // Etiqueta y campo de texto para el total del precio del ticket.
        JLabel lblTotalPrecio = new JLabel("Total Precio:");
        lblTotalPrecio.setBounds(50, 150, 100, 20);
        contentPane.add(lblTotalPrecio);

        textFieldTotalPrecio = new JTextField();
        textFieldTotalPrecio.setBounds(200, 150, 200, 20);
        contentPane.add(textFieldTotalPrecio);

        // Etiqueta y menú desplegable para los artículos.
        JLabel lblArticulo = new JLabel("Artículo:");
        lblArticulo.setBounds(50, 200, 100, 20);
        contentPane.add(lblArticulo);

        choiceArticulos = new Choice(); // Inicializa el menú desplegable.
        choiceArticulos.setBounds(200, 200, 200, 20);
        contentPane.add(choiceArticulos);

        cargarArticulos(); // Carga los datos de los artículos desde la base de datos.

        // Botón "Aceptar" para insertar un nuevo ticket.
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(50, 300, 100, 30);
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtiene los datos ingresados por el usuario.
                String codigo = textFieldCodigo.getText();
                String fecha = textFieldFecha.getText();
                String totalPrecioStr = textFieldTotalPrecio.getText();
                String descripcionSeleccionada = choiceArticulos.getSelectedItem();

                // Valida que todos los campos estén llenos.
                if (codigo.isEmpty() || fecha.isEmpty() || totalPrecioStr.isEmpty() || descripcionSeleccionada == null) {
                    JOptionPane.showMessageDialog(null, "Por favor, rellena todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int totalPrecio = Integer.parseInt(totalPrecioStr); // Convierte el precio a número.
                    int idArticulo = articulosMap.get(descripcionSeleccionada); // Obtiene el ID del artículo seleccionado.

                    insertarTicket(codigo, fecha, totalPrecio, idArticulo); // Inserta los datos en la base.
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El campo 'Total Precio' debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(btnAceptar);

        // Botón "Limpiar" para resetear los campos.
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(180, 300, 100, 30);
        btnLimpiar.addActionListener(e -> {
            textFieldCodigo.setText("");
            textFieldFecha.setText("");
            textFieldTotalPrecio.setText("");
            choiceArticulos.select(0); // Restaura la selección inicial.
        });
        contentPane.add(btnLimpiar);

        // Botón "Volver" para cerrar la ventana.
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(310, 300, 100, 30);
        btnVolver.addActionListener(e -> dispose()); // Cierra la ventana.
        contentPane.add(btnVolver);
    }

    /**
     * Método para cargar las descripciones de los artículos desde la base de datos.
     */
    private void cargarArticulos() {
        articulosMap = new HashMap<>(); // Inicializa el mapa de artículos.
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT idArticulo, descripcion FROM Articulos")) {

            while (resultSet.next()) { // Itera sobre los resultados de la consulta.
                int idArticulo = resultSet.getInt("idArticulo");
                String descripcion = resultSet.getString("descripcion");

                choiceArticulos.add(descripcion); // Agrega la descripción al menú desplegable.
                articulosMap.put(descripcion, idArticulo); // Mapea la descripción al ID.
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra detalles del error en la consola.
            JOptionPane.showMessageDialog(null, "Error al cargar los artículos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para insertar datos en las tablas Tickets y ArticulosTickets.
     */
    private void insertarTicket(String codigo, String fecha, int totalPrecio, int idArticulo) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Inserta los datos en la tabla Tickets.
            String sqlTicket = "INSERT INTO Tickets (codigo, fecha, totalprecio) VALUES (?, ?, ?)";
            PreparedStatement preparedStatementTicket = connection.prepareStatement(sqlTicket, Statement.RETURN_GENERATED_KEYS);
            preparedStatementTicket.setString(1, codigo);
            preparedStatementTicket.setString(2, fecha);
            preparedStatementTicket.setInt(3, totalPrecio);
            preparedStatementTicket.executeUpdate();

            // Obtiene el ID del ticket recién insertado.
            ResultSet generatedKeys = preparedStatementTicket.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idTicket = generatedKeys.getInt(1);

                // Inserta los datos en la tabla ArticulosTickets.
                String sqlArticuloTicket = "INSERT INTO ArticulosTickets (idArticuloFk, idTicketFk) VALUES (?, ?)";
                PreparedStatement preparedStatementArticuloTicket = connection.prepareStatement(sqlArticuloTicket);
                preparedStatementArticuloTicket.setInt(1, idArticulo);
                preparedStatementArticuloTicket.setInt(2, idTicket);
                preparedStatementArticuloTicket.executeUpdate();

                JOptionPane.showMessageDialog(null, "Ticket agregado correctamente y asociado al artículo", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Muestra detalles del error en la consola.
            JOptionPane.showMessageDialog(null, "Error al agregar el ticket: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
