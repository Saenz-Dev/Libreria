package co.edu.uptc.gui;

import co.edu.uptc.modelo.ProductoCompra;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.ValorCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

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
        setTitle("Factura de Compra");
        initAtributos();
    }

    private void modificarRecibo() {
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.CENTER;
        add(labelRecibo, gbc);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(labelNombreCliente, gbc);
        gbc.gridy = 2;
        add(labelCorreoElectronico, gbc);
        gbc.gridy = 3;
        add(labelFechaHora, gbc);
        gbc.gridy = 4;
        add(labelMetodoPago);
        gbc.gridy = 6;
        add(labelNumeroRecibo, gbc);
        gbc.gridy = 7;
        add(labelNombreLibreria, gbc);
        gbc.gridy = 8;
        add(labelTelefonoLibreria, gbc);
        gbc.gridy = 9;
        add(labelCorreoLibreria, gbc);
        gbc.gridy = 10;
        add(labelMensaje, gbc);
    }

    public void modificarLabels(ValorCompra valorCompra, Recibo recibo) {
        labelNombreCliente.setText("Nombre: " + recibo.getNombreUser());
        labelCorreoElectronico.setText("Correo: " + recibo.getUsuario());
        labelFechaHora.setText("Fecha y hora: " + recibo.getFecha());
        labelMetodoPago.setText("Método de pago: " + recibo.getTipoPago());
        labelNumeroRecibo.setText("Recibo Nº: " + recibo.getNumeroRecibo());
        labelNombreLibreria.setText("Libreria Virtual");
        labelTelefonoLibreria.setText("3105432039");
        labelCorreoElectronico.setText("libreria.virtual@gmail.com");
        labelMensaje.setText("Gracias por tu compra, vuelve pronto :)");
        modificarRecibo();
    }

    public void initAtributos() {
        labelRecibo = new JLabel("RECIBO DE COMPRA");
        labelNombreCliente = new JLabel();
        labelCorreoElectronico = new JLabel("Correo: ");
        labelFechaHora = new JLabel();
        labelMetodoPago = new JLabel("Método de pago: ");
        labelNombreLibreria = new JLabel("Librería El Saber");
        labelTelefonoLibreria = new JLabel("Tel: 123-4567890");
        labelCorreoLibreria = new JLabel("Correo: contacto@libreria.com");
        labelNumeroRecibo = new JLabel("Recibo Nº: ");
        labelMensaje = new JLabel("¡Gracias por tu compra!");
        gbc = new GridBagConstraints();
    }

    public void llenarTabla(Recibo recibo) {

        if (scroll != null) {
            remove(scroll);
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] cabecera = {"Producto", "Cantidad", "P. Unitario", "Subtotal"};
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(cabecera);

        //Quede aqui, modificar de aqui para abajo que datos van y cuales no van gracias :)
        for (ProductoCompra productoCompra : recibo.getListaProductosComprados()) {
            String fecha = String.valueOf(recibo.getFecha());
            String direccion = recibo.getDireccion();
            double total = recibo.getValorCompra().getTotal();
            String tipoPago = String.valueOf(recibo.getTipoPago());

            String tituloLibro = productoCompra.getTitulo();
            int cantidad = productoCompra.getNumeroLibros();
            tableModel.addRow(new Object[]{fecha, tituloLibro, direccion, cantidad, format.format(total), tipoPago});
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
