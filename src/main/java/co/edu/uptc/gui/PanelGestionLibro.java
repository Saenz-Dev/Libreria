package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa el panel de gestión de libros en la interfaz gráfica.
 * Permite agregar, modificar y eliminar libros en el catálogo.
 */
public class PanelGestionLibro extends JPanel {

    /** Botón para registrar un libro, modificar un libro o eliminar un libro. */
    private JButton botonRegistrar, botonModificar, botonEliminar;

    /** Constructor del panel de gestión de libros. */
    public PanelGestionLibro(Evento evento) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        initBotones();
        asignarAccionBoton(evento);

        add(botonRegistrar, gbc);
        gbc.gridy = 1;
        add(botonModificar, gbc);
        gbc.gridy = 2;
        add(botonEliminar, gbc);
    }

    private void initBotones() {
        botonRegistrar = new JButton("Registrar Libro");
        botonModificar = new JButton("Modificar Libro");
        botonEliminar = new JButton("Eliminar Libro");
    }

    private void asignarAccionBoton(Evento evento) {
        botonRegistrar.addActionListener(evento);
        botonRegistrar.setActionCommand(evento.VENTANA_REGISTRAR_LIBRO);
        botonModificar.addActionListener(evento);
        botonModificar.setActionCommand(evento.VENTANA_MODIFICAR_LIBRO);
        botonEliminar.addActionListener(evento);
        botonEliminar.setActionCommand(evento.ELIMINAR_LIBRO);
    }

}
