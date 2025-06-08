package co.edu.uptc.gui;

import jdk.jfr.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diálogo para agregar una nueva categoría en la aplicación de la Librería Virtual.
 * Permite al usuario ingresar el nombre de la categoría y mostrar mensajes de estado.
 */
public class DialogAgregarCategoria extends JDialog {

    /** Etiqueta para el título del diálogo. */
    private JLabel labelTitulo;
    /** Campo de texto para ingresar el nombre de la categoría. */
    private JTextField campoCategoria;
    /** Botón para agregar la categoría. */
    private JButton botonAgregar;
    /** Etiqueta para mostrar mensajes de estado o error. */
    private JLabel etiquetaMensaje;

    /**
     * Constructor del diálogo para agregar una categoría.
     * @param evento Manejador de eventos para el botón de agregar.
     */
    public DialogAgregarCategoria(Evento evento) {
        initComponents(evento);
    }

    /**
     * Inicializa los componentes gráficos y configura el diálogo.
     * @param evento Manejador de eventos para el botón de agregar.
     */
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

    /**
     * Obtiene el texto ingresado en el campo de categoría.
     * @return Nombre de la categoría ingresada, sin espacios al inicio o final.
     */
    public String getCampoCategoria() {
        return campoCategoria.getText().trim();
    }
}
