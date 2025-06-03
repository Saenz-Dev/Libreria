package co.edu.uptc.gui;

import co.edu.uptc.modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
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
    private VentanaPrincipal ventanaPrincipal;

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public boolean seleccionEfectivo() {
        return botonEfectivo.isSelected();
    }

    public boolean seleccionTarjeta() {
        return botonTarjeta.isSelected();
    }

    public PanelConfirmCompra(VentanaPrincipal ventanaPrincipal, Evento evento) {
	super(ventanaPrincipal, "Confirmar Compra", true);
        this.ventanaPrincipal = ventanaPrincipal;
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
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void initAtributos() {
        gbc = new GridBagConstraints();
        labelTexto = new JLabel("Seleccione el método de pago");
        botonCancelar = new JButton("Cancelar");
        botonContinuar = new JButton("Continuar");
        botonEfectivo = new JRadioButton(String.valueOf(TipoPagoEnum.EFECTIVO));
        botonTarjeta = new JRadioButton(String.valueOf(TipoPagoEnum.TARJETA));
        buttonGroup = new ButtonGroup();
        buttonGroup.add(botonEfectivo);
        buttonGroup.add(botonTarjeta);
    }

    public void llenarTabla(TotalesCompra totalesCompra, ArrayList<LibroComprado> listaCarrito) {

        if (scroll != null) {
            remove(scroll);
        }

        botonEfectivo.setSelected(true);

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

        DefaultTableModel tableModel = getDefaultTableModel();

        for (LibroComprado libroComprado : listaCarrito) {
            String isbn = libroComprado.getIsbn();
            String tituloLibro = libroComprado.getTitulo();
            int cantidad = libroComprado.getNumeroLibros();
            double valorUnitario = libroComprado.getPrecioUnitario();
            double impuestoTotalProducto = libroComprado.getImpuestoTotal();
            double impuesto = libroComprado.getImpuestoUnitario();
            double valor = libroComprado.getPrecioTotal();
            tableModel.addRow(new Object[]{isbn, tituloLibro, format.format(valorUnitario), format.format(impuesto), cantidad, format.format(impuestoTotalProducto), format.format(valor), false});
        }

        tableModel.addRow(new Object[]{"", "", "", "", "", "Subtotal", format.format(totalesCompra.getSubtotal())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "Impuestos", "+ " +  format.format(totalesCompra.getImpuestos())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "Desc. Premium", "- " +  format.format(totalesCompra.getDescuentoPremium())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "Des. Frecuencia", "- " +  format.format(totalesCompra.getDescuentoFrecuencia())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "Total", format.format(totalesCompra.getTotal())});

        tablaCompras = new JTable(tableModel);
        personalizarTabla(tablaCompras);
        tablaCompras.getColumnModel().getColumn(7).setCellRenderer(new EventoRenderTable());

        tablaCompras.getDefaultEditor(Boolean.class).addCellEditorListener(new EventoCelda(tablaCompras, ventanaPrincipal));
        tablaCompras.revalidate();
        tablaCompras.repaint();

        JTableHeader tableHeader = tablaCompras.getTableHeader();
        tableHeader.setBackground(new Color(0x24242C));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        scroll = new JScrollPane(tablaCompras);
        scroll.setPreferredSize(new Dimension(500, 300));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scroll, gbc);
        revalidate();
        repaint();
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

    private static DefaultTableModel getDefaultTableModel() {
        String[] cabecera = {"ISBN", "Producto", "V.Unitario", "V.Impuesto", "Cantidad","T.Impuesto", "V.Total", "Eliminar"};
        DefaultTableModel tableModel = new DefaultTableModel() {
            public Class<?> getColumnClass(int indexColumna) {
                return indexColumna == 7 ?  Boolean.class : String.class;
            }

            public boolean isCellEditable(int row, int column) {
                return column == 7 && tieneCheckBox(row);
            }

            public boolean tieneCheckBox(int fila) {
                String nombre = (String) getValueAt(fila, 0);
                return nombre != null && !nombre.isEmpty();
            }
        };
        tableModel.setColumnIdentifiers(cabecera);
        return tableModel;
    }


}
