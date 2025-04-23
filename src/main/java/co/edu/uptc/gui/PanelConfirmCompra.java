package co.edu.uptc.gui;

import co.edu.uptc.modelo.*;
import co.edu.uptc.negocio.TipoPago;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

public class PanelConfirmCompra extends JDialog {

    private JLabel labelTexto;
    private JRadioButton botonEfectivo;
    private JRadioButton botonTarjeta;
    private JButton botonContinuar;
    private JButton botonCancelar;
    private GridBagConstraints gbc;
    private ButtonGroup buttonGroup;
    private JTable tablaCompras;
    private JScrollPane scroll;

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public boolean seleccionEfectivo() {
        return botonEfectivo.isSelected();
    }

    public boolean seleccionTarjeta() {
        return botonTarjeta.isSelected();
    }

    public PanelConfirmCompra(Evento evento) {
        setTitle("Confirmar Compra");
        revalidate();
        repaint();
        preferenciasPanel();

        asignarAccionBoton(evento);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        add(labelTexto, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(botonEfectivo, gbc);

        gbc.gridx = 1;
        add(botonTarjeta, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(botonCancelar, gbc);

        gbc.gridx = 1;
        add(botonContinuar, gbc);
        setModal(true);
        setVisible(false);
        repaint();
    }

    private void asignarAccionBoton(Evento evento) {
        botonContinuar.addActionListener(evento);
        botonContinuar.setActionCommand(evento.ACEPTAR_CONFIRMAR_COMPRA);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(evento.CANCELAR_CONFIRMAR_COMPRA);
    }

    private void preferenciasPanel() {
        setLayout(new GridBagLayout());
        initAtributos();
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void initAtributos() {
        gbc = new GridBagConstraints();
        labelTexto = new JLabel("Seleccione el método de pago");
        botonCancelar = new JButton("Cancelar");
        botonContinuar = new JButton("Continuar");
        botonEfectivo = new JRadioButton(String.valueOf(TipoPago.EFECTIVO));
        botonTarjeta = new JRadioButton(String.valueOf(TipoPago.TARJETA));
        buttonGroup = new ButtonGroup();
        buttonGroup.add(botonEfectivo);
        buttonGroup.add(botonTarjeta);
    }

    public void visibilizar() {
        setVisible(true);
    }

    public void llenarTabla(ValorCompra valorCompra, ArrayList<ProductoCompra> listaCarrito) {

        if (scroll != null) {
            remove(scroll);
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridheight = 2;
        gbc.gridwidth = 0;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;

        String[] cabecera = {"Producto", "Cantidad", "Valor"};
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(cabecera);


        for (ProductoCompra productoCompra : listaCarrito) {
            String tituloLibro = productoCompra.getTitulo();
            int cantidad = productoCompra.getNumeroLibros();
            double valor = productoCompra.getPrecioTotal();
            tableModel.addRow(new Object[]{tituloLibro, cantidad, format.format(valor)});
        }


        tableModel.addRow(new Object[]{"", "Subtotal", format.format(valorCompra.getSubtotal())});
        tableModel.addRow(new Object[]{"", "Impuestos", "+ " +  format.format(valorCompra.getImpuestos())});
        tableModel.addRow(new Object[]{"", "Desc. Premium", "- " +  format.format(valorCompra.getDescuentoPremium())});
        tableModel.addRow(new Object[]{"", "Des. Frecuencia", "- " +  format.format(valorCompra.getDescuentoFrecuencia())});
        tableModel.addRow(new Object[]{"", "Total", format.format(valorCompra.getTotal())});

        tablaCompras = new JTable(tableModel);
        tablaCompras.revalidate();
        tablaCompras.repaint();
        tablaCompras.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //tablaCompras.setSize(300, 100);

        JTableHeader tableHeader = tablaCompras.getTableHeader();
        tableHeader.setBackground(new Color(0x24242C));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        scroll = new JScrollPane(tablaCompras);
        scroll.setPreferredSize(new Dimension(380, 100));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scroll, gbc);
        revalidate();
        repaint();
    }
}
