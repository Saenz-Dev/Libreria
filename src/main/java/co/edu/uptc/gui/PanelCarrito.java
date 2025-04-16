package co.edu.uptc.gui;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.ValorCompra;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Clase que representa el panel del carrito de compras en la interfaz gráfica.
 * Permite visualizar los productos agregados al carrito y gestionar su presentación.
 */
public class PanelCarrito extends JPanel {

    /** Etiqueta que muestra el título del panel. */
    private JLabel labelTitulo;

    /** Contenedor con barra de desplazamiento para los productos en el carrito. */
    private JScrollPane scrollPane;

    /** Panel que contiene los productos agregados al carrito. */
    private JPanel panelProductos;

    /** Lista de paneles individuales para cada producto en el carrito. */
    private ArrayList<PanelProducto> listPanelesProductos;

    /** Restricciones para la disposición general de los componentes en el panel. */
    private GridBagConstraints gbcGeneral;

    /** Referencia a la ventana principal de la aplicación. */
    private VentanaPrincipal ventanaPrincipal;

    /** Restricciones para la disposición de los productos dentro del panel. */
    private GridBagConstraints gbcPanelProductos;

    /** Panel que muestra el resumen de la compra. */
    private PanelResumenCompra panelResumenCompra;

    /**
     * Obtiene la lista de paneles de productos en el carrito.
     * @return Lista de paneles de productos.
     */
    public ArrayList<PanelProducto> getListPanelesProductos() {
        return listPanelesProductos;
    }

    /**
     * Elimina un panel de producto del carrito.
     * @param panelProducto Panel del producto a eliminar.
     */
    public void eliminarPanelProducto(PanelProducto panelProducto) {
        panelProductos.remove(panelProducto);
    }

    /**
     * Constructor del panel del carrito.
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     * @param evento Manejador de eventos de la aplicación.
     */
    public PanelCarrito(VentanaPrincipal ventanaPrincipal, Evento evento) {
        listPanelesProductos = new ArrayList<>();
        gbcGeneral = new GridBagConstraints();
        gbcPanelProductos = new GridBagConstraints();
        agregarPaneles();
        panelResumenCompra = new PanelResumenCompra(evento);
        this.ventanaPrincipal = ventanaPrincipal;
    }

    /**
     * Agrega los componentes iniciales al panel.
     */
    public void agregarPaneles() {
        setLayout(new GridBagLayout());

        gbcGeneral.gridy = 0;
        gbcGeneral.gridx = 0;
        gbcGeneral.weightx = 1.0;
        gbcGeneral.fill = GridBagConstraints.HORIZONTAL;
        gbcGeneral.anchor = GridBagConstraints.NORTHWEST;
        gbcGeneral.insets = new Insets(5, 5, 5, 5);

        labelTitulo = new JLabel("Mi carrito");
        Font fontTitulo = new Font("Arial", Font.BOLD, 30);
        labelTitulo.setFont(fontTitulo);
        add(labelTitulo, gbcGeneral);
    }

    /**
     * Agrega los productos al panel del carrito.
     * @param librosCarrito Lista de libros que están en el carrito.
     */
    public void anadirProductosPanel(ArrayList<Libro> librosCarrito) {
        listPanelesProductos = new ArrayList<>();

        if (panelProductos != null) {
            panelProductos.revalidate();
            panelProductos.repaint();
        }

        panelProductos = new JPanel(new GridBagLayout());
        gbcPanelProductos = new GridBagConstraints();
        gbcPanelProductos.insets = new Insets(5, 5, 5, 5);
        gbcPanelProductos.gridy = 0;

        agregarLibroPanel(librosCarrito);

        gbcGeneral.gridy = 1;
        gbcGeneral.gridheight = 1;
        gbcGeneral.weighty = 1;
        gbcGeneral.fill = GridBagConstraints.BOTH;

        agregarJScroll();

        gbcGeneral.weighty = 0.1;
        gbcGeneral.gridy = 2;
        gbcGeneral.gridx = 0;
        gbcGeneral.insets = new Insets(0, 10, 0, 10);
        gbcGeneral.anchor = GridBagConstraints.SOUTH;
        gbcGeneral.fill = GridBagConstraints.HORIZONTAL;
        add(panelResumenCompra, gbcGeneral);
        revalidate();
        repaint();
    }

    private void agregarJScroll() {
        if (scrollPane != null) {
            remove(scrollPane);
        }

        scrollPane = new JScrollPane(panelProductos);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        add(scrollPane, gbcGeneral);
    }

    private void agregarLibroPanel(ArrayList<Libro> librosCarrito) {
        if (librosCarrito.isEmpty()) {
            validarExistenciaProductos();
        } else {
            gbcPanelProductos.fill = GridBagConstraints.HORIZONTAL;
            gbcPanelProductos.weightx = 1.0;
            for (Libro libro : librosCarrito) {
                PanelProducto panelProducto = new PanelProducto(ventanaPrincipal, libro);
                gbcPanelProductos.gridy++;
                panelProductos.add(panelProducto, gbcPanelProductos);
                listPanelesProductos.add(panelProducto);
            }
            gbcPanelProductos.gridy++;
            gbcPanelProductos.weighty = 4;
            panelProductos.add(new JLabel(), gbcPanelProductos);
        }
    }

    /**
     * Verifica si hay productos en el carrito y actualiza el panel en caso de estar vacío.
     */
    private void validarExistenciaProductos() {
        if (!listPanelesProductos.isEmpty()) return;
        gbcPanelProductos.weighty = 1.0;
        gbcPanelProductos.fill = GridBagConstraints.BOTH;
        JLabel label = new JLabel("No hay productos seleccionados");
        panelProductos.add(label, gbcPanelProductos);
    }

    /**
     * Actualiza la vista del panel del carrito.
     * @param valorCompra Información actualizada del valor de la compra.
     */
    public void repaintPanel(ValorCompra valorCompra) {
        modificarValores(valorCompra);
        validarExistenciaProductos();
        panelResumenCompra.repaint();
        revalidate();
        repaint();
    }

    /**
     * Modifica los valores del resumen de compra.
     * @param valorCompra Información del valor de la compra.
     */
    public void modificarValores(ValorCompra valorCompra) {
        panelResumenCompra.modificarValor(valorCompra);
        repaint();
    }
}

