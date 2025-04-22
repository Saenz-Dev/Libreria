package co.edu.uptc.gui;

import co.edu.uptc.modelo.ValorCompra;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Clase que representa el panel de resumen de la compra en el {@link PanelCarrito}.
 */
public class PanelResumenCompra extends JPanel {

    /**
     * Etiqueta que muestra el valor del subtotal de la compra.
     */
    private JLabel labelSubtotalValor;

    /**
     * Etiqueta que muestra el valor de los impuestos aplicados.
     */
    private JLabel labelImpuestosValor;

    /**
     * Etiqueta que muestra el valor total de la compra.
     */
    private JLabel labelTotalValor;

    /**
     * Etiqueta del título de la sección del resumen de compra.
     */
    private JLabel labelTitulo;

    /**
     * Etiqueta descriptiva para el subtotal.
     */
    private JLabel labelSubtotal;

    /**
     * Etiqueta descriptiva para los impuestos.
     */
    private JLabel labelImpuestos;

    private JLabel labelDescuento;

    private JLabel labelDescuentoValor;

    /**
     * Etiqueta descriptiva para el total.
     */
    private JLabel labelTotal;

    /**
     * Formateador de números para dar formato a los valores monetarios.
     */
    private NumberFormat format;

    /**
     * Botón para confirmar la compra.
     */
    private JButton botonComprar;

    private GridBagConstraints gbc;
    /**
     * Constructor del panel de resumen de la compra
     * @param evento Manejador de eventos principal de la app
     */
    public PanelResumenCompra(Evento evento) {
        setLayout(new GridBagLayout());

        setBackground(Color.pink);
        format = NumberFormat.getCurrencyInstance();

        initAtributos();
        modificarFont();

        botonComprar.addActionListener(evento);
        botonComprar.setActionCommand(evento.ACTIVAR_PANEL_CONFIRMAR);

        medidasLayout(gbc);
    }

    /**
     * Inicializa los atributos del panel de resumen de la compra.
     */
    public void initAtributos() {
        format = NumberFormat.getCurrencyInstance();
        labelTitulo = new JLabel("Resumen Compra");
        labelImpuestos = new JLabel("Impuestos");
        labelSubtotal = new JLabel("Subtotal");
        labelTotal = new JLabel("Total");
        labelDescuento = new JLabel("Descuento");
        labelImpuestosValor = new JLabel(format.format(0));
        labelSubtotalValor = new JLabel(format.format(0));
        labelTotalValor = new JLabel(format.format(0));
        labelDescuentoValor = new JLabel(format.format(0));
        botonComprar = new JButton("Comprar");
        gbc = new GridBagConstraints();
    }

    /**
     * Personaliza el formato de los textos del panel.
     */
    public void modificarFont() {
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTotalValor.setFont(new Font("Arial", Font.BOLD, 18));
        labelTotal.setFont(new Font("Arial", Font.BOLD, 18));
    }

    /**
     * Realiza las medidas de la disposición del panel.
     * @param gbc Restricciones de la disposición de los componentes.
     */
    public void medidasLayout(GridBagConstraints gbc) {
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(labelTitulo, gbc);
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(labelSubtotal, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        add(labelSubtotalValor, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 2;
        add(labelImpuestos, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        add(labelImpuestosValor, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 3;
        add(labelDescuento, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        add(labelDescuentoValor, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 4;
        add(labelTotal, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        add(labelTotalValor, gbc);
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        add(botonComprar, gbc);

        gbc.weighty = 1.0;
        gbc.weightx = 0;
        gbc.gridx++;

        add(new JLabel(), gbc);
    }

    /**
     * Modifica los valores del resumen de compra.
     * @param valorCompra Información del valor de la compra.
     */
    public void modificarValor(ValorCompra valorCompra) {
        labelImpuestosValor.setText(format.format(valorCompra.getImpuestos()));
        labelSubtotalValor.setText(format.format(valorCompra.getSubtotal()));
        labelTotalValor.setText(format.format(valorCompra.getTotal()));
        labelDescuentoValor.setText(format.format(valorCompra.getDescuento()));
        repaint();
    }
}
