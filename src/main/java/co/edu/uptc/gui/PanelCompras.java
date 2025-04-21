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

    private GridBagConstraints gbc;

    /**
     * Constructor del panel de compras.
     */
    public PanelCompras() {
        setLayout(new GridBagLayout());
        removeAll();
        gbc = new GridBagConstraints();
        labelTitulo = new JLabel("Mis Compras");
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

        String[] cabecera = {"Fecha y Hora", "Producto", "Dirección", "Cantidad", "Valor", "Tipo de pago"};
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(cabecera);

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
                int cantidad = productoCompra.getNumeroLibros();
                tableModel.addRow(new Object[]{fecha, tituloLibro, direccion, cantidad, format.format(total), tipoPago});
            }
        }

        tablaCompras = new JTable(tableModel);
        tablaCompras.revalidate();
        tablaCompras.repaint();
        tablaCompras.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaCompras.setSize(300, 100);

        JTableHeader tableHeader = tablaCompras.getTableHeader();
        tableHeader.setBackground(new Color(0x24242C));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        scroll = new JScrollPane(tablaCompras);
        scroll.setPreferredSize(new Dimension(200, 380));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scroll, gbc);
        revalidate();
        repaint();
    }
}
