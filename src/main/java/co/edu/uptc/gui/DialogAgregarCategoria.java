package co.edu.uptc.gui;

import jdk.jfr.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogAgregarCategoria extends JDialog {

    private JLabel labelTitulo;
    private JTextField campoCategoria;
    private JButton botonAgregar;
    private JLabel etiquetaMensaje;

    public DialogAgregarCategoria(Evento evento) {
        initComponents(evento);
    }

    private void initComponents(Evento evento) {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        labelTitulo = new JLabel("Agregar Categoria");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 15));
        campoCategoria = new JTextField();
        botonAgregar = new JButton("Agregar");
        etiquetaMensaje = new JLabel("");

        panel.add(labelTitulo);
        panel.add(campoCategoria);
        panel.add(botonAgregar);
        panel.add(etiquetaMensaje);
        add(panel, BorderLayout.CENTER);

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
