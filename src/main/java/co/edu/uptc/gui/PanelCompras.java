package co.edu.uptc.gui;

import co.edu.uptc.modelo.Recibo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Clase que representa el panel de compras en la interfaz gráfica. Permite
 * visualizar las compras realizadas por el usuario y gestionar su presentación.
 */
public class PanelCompras extends JPanel {

    /**
     * Etiqueta que muestra el título del panel.
     */
    private JTable tablaCompras;

    /**
     * Etiqueta que muestra el título del panel.
     */
    private JLabel labelTitulo;

    private JScrollPane scroll;

    private GridBagConstraints gbc;

    private VentanaPrincipal ventanaPrincipal;

    private JLabel labelSinCompras;

    /**
     * Constructor del panel de compras.
     */
    public PanelCompras(Evento evento, VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        setLayout(new GridBagLayout());
        removeAll();
        gbc = new GridBagConstraints();
        labelTitulo = new JLabel("Mis Compras");
        labelSinCompras = new JLabel("No se encontraron compras realizadas...");
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        add(labelTitulo, gbc);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(labelSinCompras, gbc);
    }

    public void llenarTabla(ArrayList<Recibo> listaRecibos) {

        if (scroll != null) {
            remove(scroll);
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        DefaultTableModel tableModel = getDefaultTableModel();

        if (listaRecibos == null || listaRecibos.isEmpty()) {
            labelSinCompras.setVisible(true);
            revalidate();
            repaint();
            return;
        }

        labelSinCompras.setVisible(false);
        int numCompra = 0;
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        for (Recibo recibo : listaRecibos) {
            if (numCompra != recibo.getNumeroRecibo()) {

                numCompra = recibo.getNumeroRecibo();
                String fecha = formater.format(recibo.getFechaCompra());
                int numeroRecibo = recibo.getNumeroRecibo();
                tableModel.addRow(new Object[]{fecha, numeroRecibo});
            }
        }

        tablaCompras = new JTable(tableModel);
        personalizarTabla(tablaCompras);
        tablaCompras.revalidate();
        tablaCompras.repaint();
        tablaCompras.getDefaultEditor(Boolean.class).addCellEditorListener(new EventoComentario(tablaCompras, ventanaPrincipal));
        tablaCompras.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            
        });

        JTableHeader tableHeader = tablaCompras.getTableHeader();
        tableHeader.setBackground(new Color(0x24242C));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        scroll = new JScrollPane(tablaCompras);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        add(scroll, gbc);
        revalidate();
        repaint();
    }

    private static DefaultTableModel getDefaultTableModel() {
        String[] cabecera = {"Fecha y Hora", "# Recibo", "Ver compra"};
        DefaultTableModel tableModel = new DefaultTableModel() {
            public Class<?> getColumnClass(int column) {
                if (column == 2) {
                    return Boolean.class; // La columna de comentarios es un botón
                }
                return String.class; // Las demás columnas son de tipo String
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la columna de comentarios es editable
            }
        };
        tableModel.setColumnIdentifiers(cabecera);
        return tableModel;
    }

    public void personalizarTabla(JTable tabla) {
        tablaCompras.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setRowHeight(30);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setSelectionBackground(new Color(0xE0E0E0));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setGridColor(Color.LIGHT_GRAY);
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(1, 1));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setPreferredSize(new Dimension(200, 30));
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
        }
    }
}
