package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa el panel de gestión de libros en la interfaz gráfica.
 * Permite agregar, modificar y eliminar libros en el catálogo.
 */
public class PanelGestionLibro extends JPanel {

    /** Botón para registrar un libro. */
    private JButton botonRegistrar;
    /** Botón para modificar un libro. */
    private JButton botonModificar;
    /** Botón para eliminar un libro. */
    private JButton botonEliminar;

    /**
     * Constructor del panel de gestión de libros.
     * @param evento Manejador de eventos para los botones.
     */
    public PanelGestionLibro(Evento evento) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        initBotones();
        asignarAccionBoton(evento);
        personalizarBotones();

        add(botonRegistrar, gbc);
        gbc.gridy = 1;
        add(botonModificar, gbc);
        gbc.gridy = 2;
        add(botonEliminar, gbc);
    }

    /**
     * Inicializa los botones del panel.
     */
    private void initBotones() {
        botonRegistrar = new JButton("Registrar Libro");
        botonModificar = new JButton("Modificar Libro");
        botonEliminar = new JButton("Eliminar Libro");
    }

    /**
     * Asigna las acciones a los botones del panel.
     * @param evento Manejador de eventos para los botones.
     */
    private void asignarAccionBoton(Evento evento) {
        botonRegistrar.addActionListener(evento);
        botonRegistrar.setActionCommand(Evento.VENTANA_REGISTRAR_LIBRO);
        botonModificar.addActionListener(evento);
        botonModificar.setActionCommand(Evento.VENTANA_MODIFICAR_LIBRO);
        botonEliminar.addActionListener(evento);
        botonEliminar.setActionCommand(Evento.ELIMINAR_LIBRO);
    }

    /**
     * Personaliza la apariencia de los botones del panel.
     */
    private void personalizarBotones() {
        botonRegistrar.setPreferredSize(new Dimension(200, 40));
        botonModificar.setPreferredSize(new Dimension(200, 40));
        botonEliminar.setPreferredSize(new Dimension(200, 40));
        botonRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        botonModificar.setFont(new Font("Arial", Font.BOLD, 16));
        botonEliminar.setFont(new Font("Arial", Font.BOLD, 16));

        botonRegistrar.setBackground(new Color(76, 175, 80)); // Verde
        botonRegistrar.setForeground(Color.WHITE);
        botonModificar.setBackground(new Color(255, 193, 7)); // Amarillo
        botonModificar.setForeground(Color.BLACK);
        botonEliminar.setBackground(new Color(244, 67, 54)); // Rojo
        botonEliminar.setForeground(Color.WHITE);
    }
}
