package co.edu.uptc.gui;

import co.edu.uptc.modelo.ProductoCompra;
import co.edu.uptc.modelo.Recibo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Clase que representa el panel de compras en la interfaz gráfica.
 * Permite visualizar las compras realizadas por el usuario y gestionar su presentación.
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

    private JButton botonComentar;

    private GridBagConstraints gbc;

    private VentanaPrincipal ventanaPrincipal;

    /**
     * Constructor del panel de compras.
     */
    public PanelCompras(Evento evento, VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        setLayout(new GridBagLayout());
        removeAll();
        gbc = new GridBagConstraints();
        labelTitulo = new JLabel("Mis Compras");
        botonComentar = new JButton("Comentar");
        botonComentar.addActionListener(evento);
        botonComentar.setActionCommand(evento.REGISTRAR_COMENTARIO);
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        add(labelTitulo, gbc);
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
        gbc.fill = GridBagConstraints.HORIZONTAL;

        DefaultTableModel tableModel = getDefaultTableModel();

        if (listaRecibos == null || listaRecibos.isEmpty()) {
            add(new JLabel("No se encontraron compras realizadas..."), gbc);
            revalidate();
            repaint();
            return;
        }

        for (Recibo recibo : listaRecibos) {
            String fecha = String.valueOf(recibo.getFecha());
            String direccion = recibo.getDireccion();
            double total = recibo.getValorCompra().getTotal();
            String tipoPago = String.valueOf(recibo.getTipoPago());
            for (ProductoCompra productoCompra : recibo.getListaProductosComprados()) {
                String tituloLibro = productoCompra.getTitulo();
                String isbn = productoCompra.getIsbn();
                int cantidad = productoCompra.getNumeroLibros();
                tableModel.addRow(new Object[]{fecha, isbn, tituloLibro, direccion, cantidad, format.format(total), tipoPago});
            }
        }

        tablaCompras = new JTable(tableModel);
        tablaCompras.revalidate();
        tablaCompras.repaint();
        tablaCompras.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaCompras.getDefaultEditor(Boolean.class).addCellEditorListener(new EventoComentario(tablaCompras, ventanaPrincipal));


        tablaCompras.getColumnModel().getColumn(0).setPreferredWidth(130);
        tablaCompras.getColumnModel().getColumn(1).setPreferredWidth(170);
        tablaCompras.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaCompras.getColumnModel().getColumn(3).setPreferredWidth(200);
        tablaCompras.getColumnModel().getColumn(4).setPreferredWidth(40);
        tablaCompras.getColumnModel().getColumn(5).setPreferredWidth(100);
        tablaCompras.getColumnModel().getColumn(6).setPreferredWidth(100);

        JTableHeader tableHeader = tablaCompras.getTableHeader();
        tableHeader.setBackground(new Color(0x24242C));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        scroll = new JScrollPane(tablaCompras);
        scroll.setPreferredSize(new Dimension(500, 380));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        add(scroll, gbc);
        revalidate();
        repaint();
    }

    private static DefaultTableModel getDefaultTableModel() {
        String[] cabecera = {"Fecha y Hora", "ISBN", "Producto", "Dirección", "#", "Valor", "Tipo de pago", "Comentario"};
        DefaultTableModel tableModel = new DefaultTableModel() {
            public Class<?> getColumnClass(int column) {
                if (column == 7) {
                    return Boolean.class; // La columna de comentarios es un botón
                }
                return String.class; // Las demás columnas son de tipo String
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo la columna de comentarios es editable
            }
        };
        tableModel.setColumnIdentifiers(cabecera);
        return tableModel;
    }
}
