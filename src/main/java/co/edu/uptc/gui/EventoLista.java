package co.edu.uptc.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Clase que gestiona los eventos del panel modificar usuario de la aplicación.
 * Implementa ActionListener para manejar las acciones del usuario en la interfaz gráfica.
 */
public class EventoLista implements ItemListener {

    /** Referencia de VentanaPrincipal. */
    public VentanaPrincipal ventanaPrincipal;

    /** Constructor de la clase
     * @param ventanaPrincipal Referencia de VentanaPrincipal.
     */
    public EventoLista(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
    }

    /** Maneja eventos generados por la interfaz gráfica de lista.
     * @param itemEvent el evento de acción que se ha disparado.
     */
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            String seleccion = itemEvent.getItem().toString();
            ventanaPrincipal.llenarCamposModificarLibros(seleccion);
        }
    }
}
