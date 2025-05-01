package co.edu.uptc.gui;

import co.edu.uptc.modelo.Comentario;

import javax.swing.*;
import java.awt.*;

public class PanelCalificar extends JDialog {

    private JLabel labelTitulo;
    private JLabel labelLibro;
    private JButton botonAgregarCC;
    private JLabel labelComentario;
    private JPanel panelCalificacion;
    private JPanel panelComentarios;
    private GridBagConstraints gbc;
    private JTextArea textArea;
    private JSlider slider;
    private JLabel labelCalificacion;
    private String isbn;
    private Evento evento;

    public PanelCalificar(Evento evento) {

        setLayout(new GridBagLayout());
        setTitle("Calificación y Comentarios");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initAtributos();
        asignarAccionBotones(evento);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;


        Font fontTitulo = new Font("Arial", Font.BOLD, 20);
        labelTitulo.setFont(fontTitulo);
        gbc.insets = new Insets(10, 10, 10, 10);
        add(labelTitulo, gbc);

        gbc.gridy = 1;
        add(labelLibro, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 2;
        add(labelCalificacion, gbc);

        gbc.weighty = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.NONE;
        personalizacionSlider();
        add(slider, gbc);

        gbc.gridy = 4;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(labelComentario, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridy = 5;
        textArea.setPreferredSize(new Dimension(400, 100));
        add(textArea, gbc);

        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        add(botonAgregarCC, gbc);
        getContentPane().setBackground(new Color(0xF17431));
    }

    public void initAtributos() {
        labelTitulo = new JLabel("Calificaciones y comentarios ");
        labelLibro = new JLabel("9874563746543 - LIBRO");
        botonAgregarCC = new JButton("Agregar");
        panelCalificacion = new JPanel();
        panelComentarios = new JPanel();
        textArea = new JTextArea();
        slider = new JSlider(JSlider.HORIZONTAL, 0, 5, 0);
        gbc = new GridBagConstraints();
        labelCalificacion = new JLabel("Califica el libro: ");
        labelComentario = new JLabel("Escribe los comentarios del libro: ");
    }

    public void personalizacionSlider() {
        slider.setMinorTickSpacing(2);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setBackground(Color.ORANGE);
    }

    public void asignarAccionBotones(Evento evento) {
        botonAgregarCC.addActionListener(evento);
        botonAgregarCC.setActionCommand(evento.REGISTRAR_COMENTARIO);
    }

    public void setLabelLibro(String isbn, String nombreLibro) {
        this.isbn = isbn;
        labelLibro.setText(isbn + " - " + nombreLibro);
    }

    public void limpiarComentario() {
        textArea.setText("");
        slider.setValue(0);
    }

    public Comentario getComentario() {
        Comentario comentario = new Comentario();
        comentario.setIsbn(isbn);
        comentario.setComentario(textArea.getText());
        comentario.setCalificacion(slider.getValue());
        return comentario;
    }

    public String getTextoArea() {
        return textArea.getText();
    }

    public int getCalificacion() {
        return slider.getValue();
    }

    public String getIsbn() {
        return isbn;
    }
}
