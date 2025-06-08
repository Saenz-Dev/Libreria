package co.edu.uptc.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Clase que gestiona el evento de cierre de la ventana principal de la aplicación.
 * Al cerrar la ventana, se encarga de realizar el cierre de sesión del usuario.
 */
public class EventoCerrarFrame extends WindowAdapter {

    /** Referencia a la ventana principal de la aplicación. */
    public VentanaPrincipal ventanaPrincipal;

    /**
     * Constructor que recibe la ventana principal para gestionar el cierre de sesión.
     * @param ventanaPrincipal Instancia de la ventana principal.
     */
    public EventoCerrarFrame(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
    }

    /**
     * Método invocado al cerrar la ventana. Llama al método para cerrar sesión del usuario.
     * @param e Evento de ventana.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        ventanaPrincipal.cerrarSesionUsuario();
    }
}
