package co.edu.uptc.gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

import co.edu.uptc.modelo.LibroComprado;
import co.edu.uptc.modelo.TotalesCompra;

/**
 * Clase que representa el panel del carrito de compras en la interfaz gráfica.
 * Permite visualizar los productos agregados al carrito y gestionar su presentación.
 */
public class PanelCarrito extends JPanel {

    /**
     * Etiqueta que muestra el título del panel.
     */
    private JLabel labelTitulo;

    /**
     * Contenedor con barra de desplazamiento para los productos en el carrito.
     */
    private JScrollPane scrollPane;

    /**
     * Panel que contiene los productos agregados al carrito.
     */
    private JPanel panelProductos;

    private JButton botonVaciarCarrito;

    /**
     * Lista de paneles individuales para cada producto en el carrito.
     */
    private ArrayList<PanelProducto> listPanelesProductos;

    /**
     * Restricciones para la disposición general de los componentes en el panel.
     */
    private GridBagConstraints gbcGeneral;

    /**
     * Referencia a la ventana principal de la aplicación.
     */
    private VentanaPrincipal ventanaPrincipal;

    /**
     * Restricciones para la disposición de los productos dentro del panel.
     */
    private GridBagConstraints gbcPanelProductos;

    /**
     * Panel que muestra el resumen de la compra.
     */
    private PanelResumenCompra panelResumenCompra;

    /**
     * Obtiene la lista de paneles de productos en el carrito.
     *
     * @return Lista de paneles de productos.
     */
    public ArrayList<PanelProducto> getListPanelesProductos() {
        return listPanelesProductos;
    }

    /**
     * Elimina un panel de producto del carrito.
     *
     * @param panelProducto Panel del producto a eliminar.
     */
    public void eliminarPanelProducto(PanelProducto panelProducto) {
        panelProductos.remove(panelProducto);
    }

    /**
     * Constructor del panel del carrito.
     *
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     * @param evento           Manejador de eventos de la aplicación.
     */
    public PanelCarrito(VentanaPrincipal ventanaPrincipal, Evento evento) {
        listPanelesProductos = new ArrayList<>();
        gbcGeneral = new GridBagConstraints();
        gbcPanelProductos = new GridBagConstraints();
        agregarPaneles(evento);
        panelResumenCompra = new PanelResumenCompra(evento);
        this.ventanaPrincipal = ventanaPrincipal;
    }

    /**
     * Agrega los componentes iniciales al panel.
     */
    public void agregarPaneles(Evento evento) {
        setLayout(new GridBagLayout());

        gbcGeneral.gridy = 0;
        gbcGeneral.gridx = 0;
        gbcGeneral.weightx = 1.0;
        gbcGeneral.fill = GridBagConstraints.NONE;
        gbcGeneral.anchor = GridBagConstraints.NORTHWEST;
        gbcGeneral.insets = new Insets(10, 10, 10, 10);

        personalizarComponentes(evento);
        add(labelTitulo, gbcGeneral);
        gbcGeneral.anchor = GridBagConstraints.EAST;
        gbcGeneral.insets.right = 30;
        add(botonVaciarCarrito, gbcGeneral);
        gbcGeneral.insets.right = 10;
    }

    /**
     * Personaliza los componentes del panel del carrito.
     *
     * @param evento Evento que maneja las acciones de los botones.
     */
    private void personalizarComponentes(Evento evento) {
        labelTitulo = new JLabel("Mi carrito");
        botonVaciarCarrito = new JButton("Vaciar Carrito");
        botonVaciarCarrito.addActionListener(evento);
        botonVaciarCarrito.setActionCommand(Evento.VACIAR_CARRITO);
        botonVaciarCarrito.setToolTipText("Vaciar el carrito de compras");
        botonVaciarCarrito.setBackground(Color.RED);
        botonVaciarCarrito.setForeground(Color.WHITE);
        botonVaciarCarrito.setFont(new Font("Arial", Font.BOLD, 16));
        Font fontTitulo = new Font("Arial", Font.BOLD, 30);
        labelTitulo.setFont(fontTitulo);
    }

    /**
     * Agrega los productos al panel del carrito.
     *
     * @param librosCarrito Lista de libros que están en el carrito.
     */
    public void anadirProductosPanel(ArrayList<LibroComprado> librosCarrito) {
        listPanelesProductos = new ArrayList<>();

        if (panelProductos != null) {
            panelProductos.revalidate();
            panelProductos.repaint();
        }

        panelProductos = new JPanel(new GridBagLayout());
        panelProductos.setBorder(new LineBorder(Color.DARK_GRAY));
        gbcPanelProductos = new GridBagConstraints();
        gbcPanelProductos.insets = new Insets(5, 5, 5, 5);
        gbcPanelProductos.gridy = 0;

        agregarLibroPanel(librosCarrito);

        gbcGeneral.gridy = 1;
        gbcGeneral.gridheight = 1;
        gbcGeneral.weighty = 1;
        gbcGeneral.fill = GridBagConstraints.BOTH;
        gbcGeneral.insets.bottom = 0;


        agregarJScroll();

        gbcGeneral.weighty = 0.1;
        gbcGeneral.gridy = 2;
        gbcGeneral.gridx = 0;
        gbcGeneral.insets = new Insets(0, 200, 10, 10);
        gbcGeneral.anchor = GridBagConstraints.CENTER;
        gbcGeneral.fill = GridBagConstraints.HORIZONTAL;
        add(panelResumenCompra, gbcGeneral);
        panelProductos.revalidate();
        panelProductos.repaint();
        panelResumenCompra.revalidate();
        panelResumenCompra.repaint();

    }

    /**
     * Agrega un JScrollPane al panel de productos.
     * Si ya existe, lo reemplaza con el nuevo panel de productos.
     */
    private void agregarJScroll() {
        if (scrollPane != null) {
            gbcGeneral.insets.left = 5;
            remove(scrollPane);
        }

        scrollPane = new JScrollPane(panelProductos);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        add(scrollPane, gbcGeneral);
    }

    /**
     * Agrega los libros comprados al panel de productos.
     *
     * @param librosCarrito Lista de libros comprados que se mostrarán en el panel.
     */
    private void agregarLibroPanel(ArrayList<LibroComprado> librosCarrito) {
        if (librosCarrito.isEmpty()) {
            validarExistenciaProductos();
        } else {
            gbcPanelProductos.fill = GridBagConstraints.HORIZONTAL;
            gbcPanelProductos.weightx = 1.0;
            for (LibroComprado libroComprado : librosCarrito) {
                PanelProducto panelProducto = new PanelProducto(ventanaPrincipal, libroComprado);
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
        gbcPanelProductos.fill = GridBagConstraints.CENTER;
        gbcPanelProductos.anchor = GridBagConstraints.CENTER;
        JLabel label = new JLabel("No hay productos seleccionados");
        panelProductos.removeAll();
        panelProductos.add(label, gbcPanelProductos);
        panelProductos.repaint();
        revalidate();
        repaint();
    }

    /**
     * Actualiza la vista del panel del carrito.
     *
     * @param totalesCompra Información actualizada del valor de la compra.
     */
    public void repaintPanel(TotalesCompra totalesCompra) {
        modificarValores(totalesCompra);
        validarExistenciaProductos();
        panelResumenCompra.revalidate();
        panelResumenCompra.repaint();
        revalidate();
        repaint();
    }

    /**
     * Modifica los valores del resumen de compra.
     *
     * @param totalesCompra Información del valor de la compra.
     */
    public void modificarValores(TotalesCompra totalesCompra) {
        panelResumenCompra.modificarValor(totalesCompra);
        panelResumenCompra.repaint();
    }

    /**
     * Obtiene una lista de los ISBN de los libros en el carrito.
     *
     * @return Lista de ISBN de los libros en el carrito, o null si no hay productos.
     */
    public ArrayList<String> isbnLibrosCarrito() {
        ArrayList<String> titulosLibros = new ArrayList<>();
        if (listPanelesProductos.isEmpty()) return null;
        for (PanelProducto panelProducto : listPanelesProductos) {
            titulosLibros.add(panelProducto.getIsbnProducto());
        }
        return titulosLibros;
    }

    public void vaciarCarrito() {
        panelProductos.removeAll();
        listPanelesProductos = new ArrayList<>();
        validarExistenciaProductos();
        panelProductos.revalidate();
        panelProductos.repaint();
    }
}

