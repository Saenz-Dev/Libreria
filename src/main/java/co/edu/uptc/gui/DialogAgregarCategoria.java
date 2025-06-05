package co.edu.uptc.gui;

import jdk.jfr.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogAgregarCategoria extends JDialog {

    private JTextField campoCategoria;
    private JButton botonAgregar;
    private JLabel etiquetaMensaje;

    public DialogAgregarCategoria(Evento evento) {
        initComponents(evento);
    }

    private void initComponents(Evento evento) {
        setLayout(new BorderLayout(10, 10));

        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 5, 5));
        campoCategoria = new JTextField();
        panelCentral.add(new JLabel("Nombre de la categoría:"));
        panelCentral.add(campoCategoria);

        JPanel panelBoton = new JPanel();
        botonAgregar = new JButton("Agregar");
        panelBoton.add(botonAgregar);

        etiquetaMensaje = new JLabel("");
        etiquetaMensaje.setForeground(Color.RED);
        etiquetaMensaje.setHorizontalAlignment(SwingConstants.CENTER);

        add(panelCentral, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
        add(etiquetaMensaje, BorderLayout.NORTH);

        botonAgregar.addActionListener(evento);
        botonAgregar.setActionCommand(Evento.AGREGAR_CATEGORIA);

        setSize(350, 180);
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public String getCampoCategoria() {
        return campoCategoria.getText().trim();
    }
}
