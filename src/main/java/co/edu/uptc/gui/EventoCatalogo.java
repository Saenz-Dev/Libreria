package co.edu.uptc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que gestiona los eventos del panel de los paneles de libros de panel catalogo de la aplicación.
 * Implementa ActionListener para manejar las acciones del usuario en la interfaz gráfica.
 */
public class EventoCatalogo implements ActionListener {

    /** Acción para agregar un libro al carrito. */
    public static final String AGREGAR_LIBRO = "Agregar Libro";
    public static final String VER_COMENTARIOS = "Ver Comentarios";

    /** Referencia de VentanaPrincipal. */
    private VentanaPrincipal ventanaPrincipal;

    /** Referencia de Libro. */
    private String isbnLibro;

    /** Referencia de PanelLibro. */
    private PanelLibro panelLibro;

    /**
     * Constructor de la clase.
     * @param ventanaPrincipal referencia de VentanaPrincipal.
     * @param libro referencia de Libro.
     * @param panelLibro referencia de PanelLibro.
     */
    public EventoCatalogo(VentanaPrincipal ventanaPrincipal, String isbnLibro, PanelLibro panelLibro) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.isbnLibro = isbnLibro;
        this.panelLibro = panelLibro;
    }

    /**
     * Maneja eventos generados por la interfaz gráfica de catalogo.
     * @param e el evento de acción que se ha disparado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String evento = e.getActionCommand();
        switch (evento){
            case AGREGAR_LIBRO -> ventanaPrincipal.anadirProductosCarrito(isbnLibro, 1, panelLibro);
            case VER_COMENTARIOS -> ventanaPrincipal.activarMostrarComentario(isbnLibro);
        }
    }
}
