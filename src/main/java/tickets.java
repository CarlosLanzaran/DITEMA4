import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class tickets extends JFrame {

    private static final long serialVersionUID = 1L; // Identificador único para la serialización de la clase.

    private JPanel contentPane; // Panel principal donde se colocarán los componentes.

    /**
     * Método principal para lanzar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() { // Ejecuta la GUI en un hilo separado.
            public void run() {
                try {
                    tickets frame = new tickets(); // Crea una instancia de la ventana `tickets`.
                    frame.setVisible(true); // Hace visible la ventana.
                } catch (Exception e) {
                    e.printStackTrace(); // Imprime cualquier excepción que ocurra.
                }
            }
        });
    }

    /**
     * Constructor que inicializa y configura la ventana.
     */
    public tickets() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configura la acción al cerrar la ventana (cierra solo esta ventana).
        setBounds(100, 100, 450, 300); // Define el tamaño y posición de la ventana.

        contentPane = new JPanel(); // Crea un panel principal que actuará como contenedor para los componentes.
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Configura un borde vacío de 5 píxeles alrededor del panel.

        setContentPane(contentPane); // Asigna el panel principal como contenido de la ventana.
    }
}
