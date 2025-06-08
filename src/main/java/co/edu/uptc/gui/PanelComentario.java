package co.edu.uptc.gui;

import co.edu.uptc.modelo.Comentario;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

/**
 * PanelComentario es un JDialog que muestra los comentarios y calificaciones de un libro.
 */
public class PanelComentario extends JDialog {

    /**
     * Etiqueta para el título de la sección de comentarios
     */
    private JLabel labelComentario;
    /**
     * Panel con scroll para mostrar los comentarios
     */
    private JScrollPane scrollPane;
    /**
     * Restricciones de GridBagLayout para el layout principal
     */
    private GridBagConstraints gbc;
    /**
     * Botón para cerrar el diálogo
     */
    private JButton botonCerrar;

    /**
     * Constructor del panel de comentarios. Inicializa la ventana y sus componentes.
     */
    public PanelComentario() {
        setLayout(new GridBagLayout());
        setTitle("Calificaciones y comentarios");
        setSize(500, 550);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initAtributos();
    }

    /**
     * Inicializa los atributos y componentes gráficos del panel.
     */
    private void initAtributos() {
        labelComentario = new JLabel("Comentarios");
        labelComentario.setFont(new Font("Arial", Font.BOLD, 20));

        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(400, 300));
        botonCerrar = new JButton("Cerrar");
        botonCerrar.setPreferredSize(new Dimension(100, 30));
        botonCerrar.addActionListener(e -> dispose());
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(labelComentario, gbc);

        gbc.gridy = 2;
        add(botonCerrar, gbc);
    }

    /**
     * Repinta el panel con los comentarios recibidos en el stack.
     *
     * @param stackComentarios Pila de comentarios a mostrar
     */
    public void repintarComentarios(Stack<Comentario> stackComentarios) {
        labelComentario.setText("Comentarios: " + stackComentarios.getFirst().getTituloLibro());
        JPanel panelComentarios = new JPanel(new GridBagLayout());
        GridBagConstraints gbcComentarios = new GridBagConstraints();

        gbcComentarios.gridx = 0;
        gbcComentarios.weightx = 1;
        gbcComentarios.weighty = 1;
        gbcComentarios.fill = GridBagConstraints.HORIZONTAL;
        gbcComentarios.anchor = GridBagConstraints.NORTH;
        gbcComentarios.insets = new Insets(5, 5, 5, 5);

        int fila = 0;

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        llenarPanelComentarios(stackComentarios, format, gbcComentarios, fila, panelComentarios);
        if (scrollPane != null) {
            remove(scrollPane);
        }
        reubicarScroll(gbcComentarios, panelComentarios);
    }

    /**
     * Llena el panel de comentarios con los datos de cada comentario.
     *
     * @param stackComentarios Pila de comentarios a mostrar
     * @param format           Formato de fecha para mostrar
     * @param gbcComentarios   Restricciones de GridBag para los comentarios
     * @param fila             Fila actual en el GridBagLayout
     * @param panelComentarios Panel donde se agregarán los comentarios
     */
    private static void llenarPanelComentarios(Stack<Comentario> stackComentarios, DateTimeFormatter format, GridBagConstraints gbcComentarios, int fila, JPanel panelComentarios) {
        for (Comentario comentario : stackComentarios) {
            JPanel panelComentario = new JPanel(new GridBagLayout());
            panelComentario.setBorder(BorderFactory.createTitledBorder(comentario.getUsuario()));
            GridBagConstraints innerGbc = new GridBagConstraints();
            innerGbc.gridx = 0;
            innerGbc.gridy = 0;

            innerGbc.fill = GridBagConstraints.BOTH;
            innerGbc.anchor = GridBagConstraints.NORTHWEST;
            innerGbc.insets = new Insets(10, 10, 10, 10);

            JTextArea textComentario = new JTextArea("Comentario: " + comentario.getComentario());
            JLabel labelCalificacion = new JLabel("Calificación: " + comentario.getCalificacion());
            JLabel labelFecha = new JLabel("Fecha: " + comentario.getFecha().format(format));
            textComentario.setLineWrap(true);
            textComentario.setWrapStyleWord(true);
            textComentario.setEditable(false);
            textComentario.setBorder(new LineBorder(Color.WHITE));

            panelComentario.setPreferredSize(new Dimension(250, 200));
            panelComentario.add(labelCalificacion, innerGbc);

            innerGbc.gridy++;
            panelComentario.add(labelFecha, innerGbc);
            innerGbc.gridy++;
            innerGbc.weightx = 1;
            innerGbc.weighty = 1;
            JScrollPane scrollComentario = new JScrollPane(textComentario);
            scrollComentario.setBorder(new LineBorder(Color.WHITE));
            scrollComentario.setPreferredSize(new Dimension(100, 200));
            panelComentario.add(scrollComentario, innerGbc);
            panelComentario.setBackground(Color.WHITE);

            gbcComentarios.gridy = fila++;
            panelComentarios.add(panelComentario, gbcComentarios);
        }
    }

    /**
     * Reubica el JScrollPane con el panel de comentarios en el layout principal.
     *
     * @param gbcComentarios   Restricciones de GridBag para los comentarios
     * @param panelComentarios Panel que contiene los comentarios
     */
    private void reubicarScroll(GridBagConstraints gbcComentarios, JPanel panelComentarios) {
        gbcComentarios.gridy++;
        gbcComentarios.weighty = 1;
        panelComentarios.add(new JLabel(), gbcComentarios);
        scrollPane = new JScrollPane(panelComentarios);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        gbc.weightx = 1;
        gbc.gridy = 1;
        add(scrollPane, gbc);
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    /**
     * Agrega los comentarios al panel. Si no hay comentarios, muestra un mensaje.
     *
     * @param stackComentarios Pila de comentarios a mostrar
     */
    public void agregarComentarios(Stack<Comentario> stackComentarios) {
        if (stackComentarios == null || stackComentarios.isEmpty()) {
            if (scrollPane != null) {
                remove(scrollPane);
            }
            scrollPane = new JScrollPane(new JLabel("Este libro no tiene comentarios."));
            scrollPane.setPreferredSize(new Dimension(400, 300));
            scrollPane.getVerticalScrollBar().setUnitIncrement(20);
            gbc.weightx = 1;
            gbc.gridy = 1;
            add(scrollPane, gbc);
            scrollPane.revalidate();
            scrollPane.repaint();
        }
        repintarComentarios(stackComentarios);
    }
}
