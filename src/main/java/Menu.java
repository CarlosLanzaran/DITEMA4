import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L; // Identificador único para la serialización.

    /**
     * Método principal para lanzar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() { // Ejecuta la interfaz gráfica en un hilo separado.
            public void run() {
                try {
                    Menu frame = new Menu(); // Crea una instancia de la ventana principal (menú).
                    frame.setVisible(true); // Hace visible la ventana.
                } catch (Exception e) {
                    e.printStackTrace(); // Muestra detalles de cualquier excepción.
                }
            }
        });
    }

    /**
     * Constructor que configura la ventana principal del menú.
     */
    public Menu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura la acción al cerrar la ventana.
        setBounds(100, 100, 397, 235); // Define las dimensiones y posición de la ventana.

        // Crear una barra de menú.
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar); // Asocia la barra de menú a la ventana.

        // Crear un menú llamado "Artículos".
        JMenu mnNewMenu = new JMenu("Artículos");
        menuBar.add(mnNewMenu); // Agrega el menú a la barra de menú.

        // Crear una opción de menú para "Alta de artículos".
        JMenuItem mntmNewMenuItem = new JMenuItem("Alta");
        mntmNewMenuItem.addActionListener(new ActionListener() { // Listener para manejar el clic en la opción.
            public void actionPerformed(ActionEvent e) {
                altaarticulos articulosFrame = new altaarticulos(); // Crea una nueva ventana para "Alta de Artículos".
                articulosFrame.setVisible(true); // Hace visible la nueva ventana.
            }
        });
        mnNewMenu.add(mntmNewMenuItem); // Agrega la opción al menú "Artículos".

        // Crear una opción de menú para "Consulta de artículos".
        JMenuItem mntmNewMenuItem_2 = new JMenuItem("Consulta");
        mntmNewMenuItem_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultaarticulo articulosFrame = new consultaarticulo(); // Crea una nueva ventana para "Consulta de Artículos".
                articulosFrame.setVisible(true); // Hace visible la nueva ventana.
            }
        });
        mnNewMenu.add(mntmNewMenuItem_2); // Agrega la opción al menú "Artículos".

        // Crear una opción de menú para "Baja de artículos".
        JMenuItem mntmNewMenuItem_3 = new JMenuItem("Baja");
        mntmNewMenuItem_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bajaarticulo articulosFrame = new bajaarticulo(); // Crea una nueva ventana para "Baja de Artículos".
                articulosFrame.setVisible(true); // Hace visible la nueva ventana.
            }
        });
        mnNewMenu.add(mntmNewMenuItem_3); // Agrega la opción al menú "Artículos".

        // Crear una opción de menú para "Modificación de artículos".
        JMenuItem mntmNewMenuItem_4 = new JMenuItem("Modificación");
        mntmNewMenuItem_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modarticulo articulosFrame = new modarticulo(); // Crea una nueva ventana para "Modificación de Artículos".
                articulosFrame.setVisible(true); // Hace visible la nueva ventana.
            }
        });
        mnNewMenu.add(mntmNewMenuItem_4); // Agrega la opción al menú "Artículos".

        // Crear un menú llamado "Tickets".
        JMenu mnNewMenu_1 = new JMenu("Tickets");
        menuBar.add(mnNewMenu_1); // Agrega el menú a la barra de menú.

        // Crear una opción de menú para "Alta de tickets".
        JMenuItem mntmNewMenuItem_5 = new JMenuItem("Alta");
        mntmNewMenuItem_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                altatickets articulosFrame = new altatickets(); // Crea una nueva ventana para "Alta de Tickets".
                articulosFrame.setVisible(true); // Hace visible la nueva ventana.
            }
        });
        mnNewMenu_1.add(mntmNewMenuItem_5); // Agrega la opción al menú "Tickets".

        // Crear una opción de menú para "Consulta de tickets".
        JMenuItem mntmNewMenuItem_6 = new JMenuItem("Consulta");
        mntmNewMenuItem_6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultatickets articulosFrame = new consultatickets(); // Crea una nueva ventana para "Consulta de Tickets".
                articulosFrame.setVisible(true); // Hace visible la nueva ventana.
            }
        });
        mnNewMenu_1.add(mntmNewMenuItem_6); // Agrega la opción al menú "Tickets".
        
     // Opción "Baja" de tickets
        JMenuItem mntmNewMenuItem_7 = new JMenuItem("Baja");
        mntmNewMenuItem_7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bajatickets ticketsFrame = new bajatickets(); // Crear una nueva instancia
                ticketsFrame.setVisible(true); // Hacer visible la ventana
            }
        });
        mnNewMenu_1.add(mntmNewMenuItem_7); // Agregar al menú "Tickets" 
    }
}
