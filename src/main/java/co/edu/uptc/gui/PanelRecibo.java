package co.edu.uptc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.table.*;

import co.edu.uptc.modelo.LibroComprado;
import co.edu.uptc.modelo.Recibo;

public class PanelRecibo extends JDialog {

    private JLabel labelRecibo;
    private JLabel labelNombreCliente;
    private JLabel labelCorreoElectronico;
    private JLabel labelFechaHora;
    private JTable tablaCompras;
    private JLabel labelMetodoPago;
    private JLabel labelNombreLibreria;
    private JLabel labelTelefonoLibreria;
    private JLabel labelCorreoLibreria;
    private JLabel labelNumeroRecibo;
    private JLabel labelMensaje;
    private GridBagConstraints gbc;
    private JScrollPane scroll;
    private JScrollPane scrollPane;
    private VentanaPrincipal ventanaPrincipal;
    private JButton botonCerrar;

    public PanelRecibo(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        setLayout(new GridBagLayout());
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initAtributos();
        modificarRecibo();
        setSize(700, 600);

        setVisible(false);
    }

    private void modificarRecibo() {
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 10, 5, 10);
        add(labelRecibo, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 0;
        gbc.gridy = 1;
        add(labelNombreCliente, gbc);
        gbc.gridy = 2;
        add(labelCorreoElectronico, gbc);
        gbc.gridy = 3;
        add(labelFechaHora, gbc);
        gbc.gridy = 4;
        add(labelMetodoPago, gbc);
        gbc.gridy = 5;
        add(labelNumeroRecibo, gbc);
        gbc.gridy = 9;
        add(labelNombreLibreria, gbc);
        gbc.gridy = 10;
        add(labelTelefonoLibreria, gbc);
        gbc.gridy = 11;
        add(labelCorreoLibreria, gbc);
        gbc.gridy = 12;
        add(labelMensaje, gbc);
        gbc.gridy = 13;
        add(botonCerrar, gbc);
    }

    public void modificarLabels(Recibo recibo, boolean activarComentar) {
        if (getComponentCount() == 0) {
            modificarRecibo();
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        labelNombreCliente.setText("Nombre: " + recibo.getNombreUsuario());
        labelCorreoElectronico.setText("Correo: " + recibo.getCorreo());
        labelFechaHora.setText("Fecha y hora: " + recibo.getFechaCompra().format(dateTimeFormatter));
        labelMetodoPago.setText("M.Pago: " + recibo.getTipoPago());
        labelNumeroRecibo.setText("Recibo Nº: " + recibo.getNumeroRecibo());
        labelNombreLibreria.setText("Libreria Virtual");
        labelTelefonoLibreria.setText("3105432039");
        labelCorreoLibreria.setText("libreria.virtual@gmail.com");
        labelMensaje.setText("Gracias por tu compra, vuelve pronto :)");

        llenarTabla(recibo, activarComentar);
        revalidate();
        repaint();
    }

    public void initAtributos() {
        labelRecibo = new JLabel("RECIBO DE COMPRA");

        labelRecibo.setFont(new Font("", Font.BOLD, 18));
        labelNombreCliente = new JLabel();
        labelCorreoElectronico = new JLabel();
        labelFechaHora = new JLabel();
        labelMetodoPago = new JLabel();
        labelNombreLibreria = new JLabel();
        labelTelefonoLibreria = new JLabel();
        labelCorreoLibreria = new JLabel();
        labelNumeroRecibo = new JLabel();
        labelMensaje = new JLabel();
        botonCerrar = new JButton("Cerrar");
        botonCerrar.setPreferredSize(new Dimension(60, 30));
        botonCerrar.addActionListener(e -> dispose());
        gbc = new GridBagConstraints();
    }

    public void llenarTabla(Recibo recibo, boolean activarComentar) {
        if (scroll != null) {
            remove(scroll);
            remove(scrollPane);
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        gbc.gridy = 6;
        gbc.gridheight = 2;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        DefaultTableModel tableModel = getDefaultTableModel(recibo, format);

        JTable tablaTotales = tablaTotales(recibo, format);

        tablaCompras = new JTable(tableModel);
        personalizarTabla(tablaTotales);
        tablaTotales.setShowGrid(false);
        tablaTotales.revalidate();
        tablaTotales.repaint();
        scrollPane = new JScrollPane(tablaTotales);
        scrollPane.setPreferredSize(new Dimension(200, 100));

        personalizarTabla(tablaCompras);
        tablaCompras.getColumnModel().getColumn(0).setMinWidth(0);
        tablaCompras.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaCompras.getColumnModel().getColumn(0).setWidth(0);
        tablaCompras.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaCompras.revalidate();
        tablaCompras.repaint();
        if (!activarComentar) {
            TableColumnModel tcm = tablaCompras.getColumnModel();
            tcm.removeColumn(tcm.getColumn(8));
        } else {
            tablaCompras.getColumnModel().getColumn(8).setCellRenderer(new EventoRenderTable());
            tablaCompras.getDefaultEditor(Boolean.class).addCellEditorListener(new EventoRecibo(tablaCompras, ventanaPrincipal));
        }

        ajustarTableHeader();
        scroll = new JScrollPane(tablaCompras);
        scroll.setPreferredSize(new Dimension(200, 380));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scroll, gbc);
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridy = 8;

        add(scrollPane, gbc);
    }

    private static DefaultTableModel getDefaultTableModel(Recibo recibo, NumberFormat format) {
        DefaultTableModel tableModel = getDefaultTableModel();
        for (LibroComprado libroComprado : recibo.getListaProductosComprados()) {
            String isbn = libroComprado.getIsbn();
            String producto = libroComprado.getTitulo();
            int cantidad = libroComprado.getCantidadComprada();
            double precioUnitario = libroComprado.getPrecioVenta();
            double subtotal = libroComprado.getPrecioTotal();
            double impuestoUnitario = libroComprado.getImpuestoUnitario();
            double impuestoTotal = libroComprado.getImpuestoTotal();
            double valorBaseTotal = libroComprado.getPrecioTotalSinIva();
            tableModel.addRow(new Object[]{isbn, producto, format.format(precioUnitario), format.format(impuestoUnitario), cantidad, format.format(impuestoTotal), format.format(valorBaseTotal), format.format(subtotal)});
        }
        return tableModel;
    }

    private void ajustarTableHeader() {
        JTableHeader tableHeader = tablaCompras.getTableHeader();
        tableHeader.setBackground(new Color(0x24242C));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private JTable tablaTotales(Recibo recibo, NumberFormat format) {
        DefaultTableModel tablaTotalesModel = new DefaultTableModel();
        String[] titulos = {"Subtotal Base", "Impuesto Total", "Desc. Premium", "Desc. Frecuencia", "Total"};
        tablaTotalesModel.setColumnIdentifiers(titulos);

        tablaTotalesModel.addRow(new Object[]{format.format(recibo.getValorCompra().getPrecioBase()), "+ " + format.format(recibo.getValorCompra().getImpuestos()), "- " + format.format(recibo.getValorCompra().getDescuentoPremium()), "- " + format.format(recibo.getValorCompra().getDescuentoFrecuencia()), format.format(recibo.getValorCompra().getTotal())});
        return new JTable(tablaTotalesModel);
    }

    public static DefaultTableModel getDefaultTableModel() {
        String[] cabecera = {"ISBN", "Producto", "Vlr. Base", "IVA Unit.", "Cantidad", "IVA Total", "V.Base Total", "Vlr. Total", "Comentar"};
        DefaultTableModel tableModel = new DefaultTableModel() {
            public Class<?> getColumnClass(int indexColumna) {
                return indexColumna == 8 ? Boolean.class : String.class;
            }

            public boolean isCellEditable(int row, int column) {
                return column == 8 && tieneCheckBox(row);
            }

            private boolean tieneCheckBox(int fila) {
                String nombre = (String) getValueAt(fila, 0);
                return nombre != null && !nombre.isEmpty();
            }
        };
        tableModel.setColumnIdentifiers(cabecera);
        return tableModel;
    }

    public void personalizarTabla(JTable tabla) {
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setRowHeight(30);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(new Font("Arial", Font.PLAIN, 11));
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
            /*if (i == 1) {
                column.setPreferredWidth(250);
                continue;
            } else if (i == 4) {
                column.setPreferredWidth(56);
                continue;
            }*/
            column.setPreferredWidth(150);
        }
    }
}

