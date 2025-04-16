package co.edu.uptc.gui;

import co.edu.uptc.modelo.Libro;

import javax.swing.*;
import java.awt.*;

import java.text.NumberFormat;

/**
 * Clase que representa el panel de un producto en la interfaz gráfica del panel carrito.
 */
public class PanelProducto extends JPanel {

    /** Etiqueta que muestra el nombre del producto. */
    private JLabel labelNombreProducto;

    /** Botón para aumentar la cantidad del producto en el carrito. */
    private JButton botonAumentar;

    /** Botón para disminuir la cantidad del producto en el carrito. */
    private JButton botonDisminuir;

    /** Etiqueta que muestra la cantidad actual del producto en el carrito. */
    private JLabel labelCantidad;

    /** Botón para eliminar el producto del carrito. */
    private JButton botonEliminar;

    /** Etiqueta que muestra el precio del producto. */
    private JLabel labelPrecio;

    /** Formato para mostrar los valores numéricos como el precio. */
    private NumberFormat format;

    /** Referencia al producto (libro) asociado a este panel. */
    private String isbnProducto;

    /** Evento que maneja las acciones de aumentar, disminuir y eliminar el producto. */
    private EventoCantidad eventoCantidad;

    /**
     * Constructor del panel de un producto.
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     * @param producto Referencia al producto (libro) asociado a este panel.
     */
    public PanelProducto(VentanaPrincipal ventanaPrincipal, Libro producto) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(Color.lightGray);

        /*No sé si sea necesario este Map entonces por eso lo comente porque solo puedo pasar el libro y después obtener sus atributos y valores*/
        //Map<String, ArrayList<Libro>> catalogo = librosDisp;
        format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(1);
        this.isbnProducto = producto.getIsbn();
        this.eventoCantidad = new EventoCantidad(ventanaPrincipal, producto.getIsbn(), this);

        initAtributos(producto);
        asignarAccionBoton();

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(labelNombreProducto, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        add(botonDisminuir, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = 1;
        add(labelCantidad, gbc);
        gbc.gridx = 5;
        gbc.gridwidth = 1;
        add(botonAumentar, gbc);
        gbc.gridx = 6;
        gbc.gridwidth = 1;
        add(labelPrecio, gbc);
        gbc.gridx = 7;
        gbc.gridwidth = 1;
        add(botonEliminar, gbc);
        botonDisminuir.setVisible(producto.getStockReservado() > 1);
        repaint();
    }

    private void initAtributos(Libro producto) {
        labelNombreProducto = new JLabel(producto.getTitulo());
        botonAumentar = new JButton("+");
        botonDisminuir = new JButton("-");
        labelCantidad = new JLabel(String.valueOf(producto.getStockReservado()));
        botonEliminar = new JButton("Eliminar");
        labelPrecio = new JLabel(format.format(producto.getPrecioVenta() * producto.getStockReservado()));
    }

    private void asignarAccionBoton() {
        botonAumentar.addActionListener(eventoCantidad);
        botonAumentar.setActionCommand(EventoCantidad.SUMAR);
        botonDisminuir.addActionListener(eventoCantidad);
        botonDisminuir.setActionCommand(EventoCantidad.DISMINUIR);
        botonEliminar.addActionListener(eventoCantidad);
        botonEliminar.setActionCommand(EventoCantidad.ELIMINAR);
    }

    /**
     * Actualiza el precio del producto en el panel.
     * @param precio Precio del producto.
     */
    public void actualizarPrecio(double precio) {
        labelPrecio.setText(format.format(precio));
    }

    /**
     * Actualiza la cantidad del producto en el panel.
     * @param cantidad Cantidad del producto.
     */
    public void actualizarCantidad(int cantidad) {
        labelCantidad.setText(String.valueOf(cantidad));
        botonDisminuir.setVisible(cantidad > 1);
    }
}
