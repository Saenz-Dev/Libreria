package co.edu.uptc.gui;

import co.edu.uptc.modelo.Comentario;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Stack;

public class PanelComentario extends JDialog {

    private JLabel labelComentario;
    private JScrollPane scrollPane;
    private GridBagConstraints gbc;

    public PanelComentario() {
        setLayout(new GridBagLayout());
        setTitle("Calificaciones y comentarios");
        setSize(500, 450);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initAtributos();
    }

    private void initAtributos() {
        labelComentario = new JLabel("Comentarios");
        labelComentario.setFont(new Font("Arial", Font.BOLD, 20));

        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(400, 300));

        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(labelComentario, gbc);


    }

    public void repintarComentarios(Stack<Comentario> stackComentarios) {
        // Supongamos que tienes un panel principal dentro del scrollPane
        labelComentario.setText("Comentarios: " + stackComentarios.getFirst().getIsbn());
        JPanel panelComentarios = new JPanel(new GridBagLayout());
        GridBagConstraints gbcComentarios = new GridBagConstraints();

        gbcComentarios.gridx = 0;
        gbcComentarios.weightx = 1;
        gbcComentarios.weighty = 1;
        gbcComentarios.fill = GridBagConstraints.HORIZONTAL;
        gbcComentarios.anchor = GridBagConstraints.NORTH;
        gbcComentarios.insets = new Insets(5, 5, 5, 5);

        int fila = 0; // Control manual de filas

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
            JLabel labelFecha = new JLabel("Fecha: " + comentario.getFecha());
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
        if (scrollPane != null) {
            remove(scrollPane);
        }
        gbcComentarios.gridy++;
        gbcComentarios.weighty = 4;
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
