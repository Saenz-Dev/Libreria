package co.edu.uptc.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EventoCerrarFrame extends WindowAdapter {

    public VentanaPrincipal ventanaPrincipal;

    public EventoCerrarFrame(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ventanaPrincipal.cerrarSesionUsuario();
    }
}
