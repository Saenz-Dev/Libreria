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
    private VentanaPrincipal ventanaPrincipal;
    private JButton botonCerrar;

    public PanelRecibo(VentanaPrincipal ventanaPrincipal) {
        // super(ventanaPrincipal, "Factura de Compra", true);
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
        gbc.gridy = 8;
        add(labelNombreLibreria, gbc);
        gbc.gridy = 9;
        add(labelTelefonoLibreria, gbc);
        gbc.gridy = 10;
        add(labelCorreoLibreria, gbc);
        gbc.gridy = 11;
        add(labelMensaje, gbc);
        gbc.gridy = 12;
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
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        gbc.gridy = 6;
        gbc.gridheight = 2;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        DefaultTableModel tableModel = getDefaultTableModel();

        // Quede aqui, modificar de aqui para abajo que datos van y cuales no van
        // gracias :)
        for (LibroComprado libroComprado : recibo.getListaProductosComprados()) {
            String isbn = libroComprado.getIsbn();
            String producto = libroComprado.getTitulo();
            int cantidad = libroComprado.getCantidadComprada();
            double precioUnitario = libroComprado.getPrecioVenta();
            double subtotal = libroComprado.getPrecioTotal();
            double impuestoUnitario = libroComprado.getImpuestoUnitario();
            double impuestoTotal = libroComprado.getImpuestoTotal();

            tableModel.addRow(new Object[]{isbn, producto, format.format(precioUnitario), format.format(impuestoUnitario), cantidad, format.format(impuestoTotal), format.format(subtotal)});
        }

        tableModel.addRow(new Object[]{"", "", "", "", "", "Subtotal", format.format(recibo.getValorCompra().getSubtotal())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "Impuestos", "+ " + format.format(recibo.getValorCompra().getImpuestos())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "Desc. Premium", "- " + format.format(recibo.getValorCompra().getDescuentoPremium())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "Desc. Frecuencia", "- " + format.format(recibo.getValorCompra().getDescuentoFrecuencia())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "Total", format.format(recibo.getValorCompra().getTotal())});

        tablaCompras = new JTable(tableModel);
        personalizarTabla(tablaCompras);
        tablaCompras.revalidate();
        tablaCompras.repaint();
        if (!activarComentar) {
            TableColumnModel tcm = tablaCompras.getColumnModel();
            tcm.removeColumn(tcm.getColumn(7));
        } else {
            tablaCompras.getColumnModel().getColumn(7).setCellRenderer(new EventoRenderTable());
            tablaCompras.getDefaultEditor(Boolean.class).addCellEditorListener(new EventoRecibo(tablaCompras, ventanaPrincipal));
        }

        JTableHeader tableHeader = tablaCompras.getTableHeader();
        tableHeader.setBackground(new Color(0x24242C));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        scroll = new JScrollPane(tablaCompras);
        scroll.setPreferredSize(new Dimension(200, 380));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scroll, gbc);
    }

    public static DefaultTableModel getDefaultTableModel() {
        String[] cabecera = {"ISBN", "Producto", "V.Unitario", "V.Impuesto", "Cantidad", "T.Impuesto", "Subtotal", "Comentar"};
        DefaultTableModel tableModel = new DefaultTableModel() {
            public Class<?> getColumnClass(int indexColumna) {
                return indexColumna == 7 ? Boolean.class : String.class;
            }

            public boolean isCellEditable(int row, int column) {
                return column == 7 && tieneCheckBox(row);
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
