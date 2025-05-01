package co.edu.uptc.gui;

import co.edu.uptc.modelo.ProductoCompra;
import co.edu.uptc.modelo.Recibo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;

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

    public void setLabelRecibo(JLabel labelRecibo) {
        this.labelRecibo = labelRecibo;
    }

    public void setLabelNombreCliente(JLabel labelNombreCliente) {
        this.labelNombreCliente = labelNombreCliente;
    }

    public void setLabelCorreoElectronico(JLabel labelCorreoElectronico) {
        this.labelCorreoElectronico = labelCorreoElectronico;
    }

    public void setLabelFechaHora(JLabel labelFechaHora) {
        this.labelFechaHora = labelFechaHora;
    }

    public void setTablaCompras(JTable tablaCompras) {
        this.tablaCompras = tablaCompras;
    }

    public void setLabelMetodoPago(JLabel labelMetodoPago) {
        this.labelMetodoPago = labelMetodoPago;
    }

    public void setLabelNombreLibreria(JLabel labelNombreLibreria) {
        this.labelNombreLibreria = labelNombreLibreria;
    }

    public void setLabelTelefonoLibreria(JLabel labelTelefonoLibreria) {
        this.labelTelefonoLibreria = labelTelefonoLibreria;
    }

    public void setLabelCorreoLibreria(JLabel labelCorreoLibreria) {
        this.labelCorreoLibreria = labelCorreoLibreria;
    }

    public void setLabelNumeroRecibo(JLabel labelNumeroRecibo) {
        this.labelNumeroRecibo = labelNumeroRecibo;
    }

    public void setLabelMensaje(JLabel labelMensaje) {
        this.labelMensaje = labelMensaje;
    }

    public PanelRecibo() {
        setLayout(new GridBagLayout());
        setTitle("Factura de Compra");
        setLocationRelativeTo(null);
        setSize(400, 600);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initAtributos();
        modificarRecibo();
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
    }

    public void modificarLabels(Recibo recibo) {
        if (getComponentCount() == 0) {
            modificarRecibo();
        }
        labelNombreCliente.setText("Nombre: " + recibo.getNombreUser());
        labelCorreoElectronico.setText("Correo: " + recibo.getUsuario());
        labelFechaHora.setText("Fecha y hora: " + recibo.getFecha());
        labelMetodoPago.setText("M.Pago: " + recibo.getTipoPago());
        labelNumeroRecibo.setText("Recibo Nº: " + recibo.getNumeroRecibo());
        labelNombreLibreria.setText("Libreria Virtual");
        labelTelefonoLibreria.setText("3105432039");
        labelCorreoLibreria.setText("libreria.virtual@gmail.com");
        labelMensaje.setText("Gracias por tu compra, vuelve pronto :)");

        llenarTabla(recibo);
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
        gbc = new GridBagConstraints();
    }

    public void llenarTabla(Recibo recibo) {

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

        String[] cabecera = {"Producto", "Cantidad", "P. Unitario", "Subtotal"};
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(cabecera);

        //Quede aqui, modificar de aqui para abajo que datos van y cuales no van gracias :)
        for (ProductoCompra productoCompra : recibo.getListaProductosComprados()) {
            String producto = productoCompra.getTitulo();
            int cantidad = productoCompra.getNumeroLibros();
            double precioUnitario = productoCompra.getPrecioUnitario();
            double subtotal = productoCompra.getPrecioTotal();

            tableModel.addRow(new Object[]{producto, cantidad, format.format(precioUnitario), format.format(subtotal)});
        }
        tableModel.addRow(new Object[]{"", "", "Subtotal", format.format(recibo.getValorCompra().getSubtotal())});
        tableModel.addRow(new Object[]{"", "", "Impuestos", "+ " + format.format(recibo.getValorCompra().getImpuestos())});
        tableModel.addRow(new Object[]{"", "", "Desc. Premium", "- " +  format.format(recibo.getValorCompra().getDescuentoPremium())});
        tableModel.addRow(new Object[]{"","", "Desc. Frecuencia", "- " +  format.format(recibo.getValorCompra().getDescuentoFrecuencia())});
        tableModel.addRow(new Object[]{"", "", "Total", format.format(recibo.getValorCompra().getTotal())});

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
    }
}
