package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.security.PublicKey;
import java.util.ArrayList;
import co.edu.uptc.modelo.CodigoPremium;

public class PanelAggCodigo extends JPanel {

    private JLabel labelTitulo, labelMensaje;
    private JTextField campoCodigo;
    private JButton botonGuardar;
    private JTable tablaCodigos;
    private JScrollPane scroll;

    private final Color COLOR_FONDO = new Color(240, 244, 248);
    private final Color COLOR_TITULO = new Color(44, 62, 80);
    private final Color COLOR_BOTON = new Color(41, 128, 185);
    private final Color COLOR_HEADER = new Color(52, 73, 94);
    private final Color COLOR_FILA_PAR = new Color(236, 240, 241);

    public PanelAggCodigo(Evento evento) {
        setLayout(new GridBagLayout());
        setBackground(COLOR_FONDO);
        construirComponentes(evento);
    }

    private void construirComponentes(Evento evento) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        // Título
        labelTitulo = crearLabel("AGREGAR CÓDIGOS", 28, COLOR_TITULO);
        gbc.gridy = 0;
        add(labelTitulo, gbc);

        // Campo de texto
        gbc.fill = GridBagConstraints.NONE;
        campoCodigo = new JTextField(20);
        campoCodigo.setFont(new Font("Arial", Font.PLAIN, 16));
        campoCodigo.setHorizontalAlignment(SwingConstants.CENTER);
        campoCodigo.setPreferredSize(new Dimension(100, 40));
        gbc.gridy++;
        add(campoCodigo, gbc);

        // Botón
        botonGuardar = new JButton("Agregar código");
        botonGuardar.setFont(new Font("Arial", Font.BOLD, 16));
        botonGuardar.setBackground(COLOR_BOTON);
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setFocusPainted(false);
        botonGuardar.setPreferredSize(new Dimension(200, 40));
        botonGuardar.setActionCommand(Evento.GUARDAR_CODIGO);
        botonGuardar.addActionListener(evento);
        gbc.gridy++;
        add(botonGuardar, gbc);

        // Label de mensaje
        labelMensaje = crearLabel("", 14, Color.WHITE);
        labelMensaje.setOpaque(true);
        labelMensaje.setVisible(false);
        gbc.gridy++;
        add(labelMensaje, gbc);

        // Tabla
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tablaCodigos = new JTable();
        tablaCodigos.setRowHeight(25);
        personalizarTabla();

        scroll = new JScrollPane(tablaCodigos);
        scroll.setPreferredSize(new Dimension(500, 200));
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(scroll, gbc);
    }

    private JLabel crearLabel(String texto, int tamaño, Color colorTexto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, tamaño));
        label.setForeground(colorTexto);
        return label;
    }

    private void personalizarTabla() {
        JTableHeader header = tablaCodigos.getTableHeader();
        header.setBackground(COLOR_HEADER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        tablaCodigos.setFont(new Font("Arial", Font.PLAIN, 14));

        // Alternar color de filas
        tablaCodigos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_FILA_PAR : Color.WHITE);
                }
                return c;
            }
        });
    }
    
    public String obtenerCodigo() {
        return campoCodigo.getText().trim();
    }

    public void construirTabla(ArrayList<CodigoPremium> codigos) {
        String[] columnas = {"Código", "Usado"};
        String[][] datos = llenarTabla(codigos);
        tablaCodigos.setModel(new DefaultTableModel(datos, columnas));
        personalizarTabla();
        tablaCodigos.revalidate();
        tablaCodigos.repaint();
    }

    private String[][] llenarTabla(ArrayList<CodigoPremium> codigos) {
        String[][] datos = new String[codigos.size()][2];
        for (int i = 0; i < codigos.size(); i++) {
            datos[i][0] = codigos.get(i).getCodigo();
            datos[i][1] = String.valueOf(codigos.get(i).getUsado());
        }
        return datos;
    }

    public void mostrarMensaje(String mensaje) {
        labelMensaje.setBackground(Color.RED);
        labelMensaje.setText(mensaje);
        labelMensaje.setVisible(true);
    }

    public void mostrarMensajeExito(String mensaje) {
        labelMensaje.setBackground(new Color(39, 174, 96)); // Verde
        labelMensaje.setText(mensaje);
        labelMensaje.setVisible(true);
    }

    public void ocultarLabelMensaje() {
        labelMensaje.setVisible(false);
    }
}

