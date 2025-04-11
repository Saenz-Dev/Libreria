package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que gestiona los eventos de la interfaz de carrito de la aplicación.
 * Implementa ActionListener para manejar las acciones del usuario en la interfaz gráfica.
 */
public class EventoCantidad implements ActionListener {

    /** Acción para sumar el cantidad de un producto al carrito. */
    public static final String SUMAR = "Sumar";

    /** Acción para disminuir el cantidad de un producto al carrito. */
    public static final String DISMINUIR = "Disminuir";

    /** Acción para eliminar el producto del carrito. */
    public static final String ELIMINAR = "Eliminar";

    /** Referencia de PanelProducto. */
    public PanelProducto panelProducto;

    /** Referencia de VentanaPrincipal. */
    private VentanaPrincipal ventanaPrincipal;

    /** Referencia de Libro. */
    private String isbnProducto;

    /**
     * Constructor de la clase.
     * @param ventanaPrincipal Instancia de VentanaPrincipal.
     * @param producto Instancia de Libro.
     * @param panelProducto Instancia de PanelProducto.
     */
    public EventoCantidad(VentanaPrincipal ventanaPrincipal, String isbnProducto, PanelProducto panelProducto) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.isbnProducto = isbnProducto;
        this.panelProducto = panelProducto;
    }

    /**
     * Maneja eventos generados por la interfaz gráfica de carrito.
     * @param e el evento de acción que se ha disparado.
     */
    public void actionPerformed(ActionEvent e) {
        String evento = e.getActionCommand();
        switch (evento) {
            case SUMAR -> ventanaPrincipal.sumarProductoCarrito(isbnProducto, panelProducto);
            case DISMINUIR -> ventanaPrincipal.disminuirProductoCarrito(isbnProducto, panelProducto);
            case ELIMINAR -> ventanaPrincipal.eliminarProductoCarrito(isbnProducto, panelProducto);
        }
    }
}
