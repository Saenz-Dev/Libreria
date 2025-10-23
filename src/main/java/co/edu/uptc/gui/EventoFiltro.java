package co.edu.uptc.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class EventoFiltro implements ItemListener {

    public VentanaPrincipal ventanaPrincipal;

    /** Constructor de la clase
     * @param ventanaPrincipal Referencia de VentanaPrincipal.
     */
    public EventoFiltro(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
    }

    /** Maneja eventos generados por la interfaz gráfica de lista.
     * @param itemEvent el evento de acción que se ha disparado.
     */
    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            ventanaPrincipal.activarFiltrarLibros();
        }
    }
}
