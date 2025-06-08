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

/**
 * PanelConfirmCompra es un JDialog que permite al usuario confirmar la compra,
 * seleccionar el método de pago y visualizar el resumen de los productos a comprar.
 */
public class PanelConfirmCompra extends JDialog {

    /** Etiqueta con el texto de instrucciones o información. */
    private JLabel labelTexto;
    /** Botón de selección para pago en efectivo. */
    private JRadioButton botonEfectivo;
    /** Botón de selección para pago con tarjeta. */
    private JRadioButton botonTarjeta;
    /** Botón para continuar con la compra. */
    private JButton botonContinuar;
    /** Botón para cancelar la compra. */
    private JButton botonCancelar;
    /** Restricciones de GridBagLayout para el layout principal. */
    private GridBagConstraints gbc;
    /** Grupo de botones para los métodos de pago. */
    private ButtonGroup buttonGroup;
    /** Tabla que muestra los productos a comprar. */
    private JTable tablaCompras;
    /** Scroll para la tabla de compras. */
    private JScrollPane scroll;
    /** Referencia a la ventana principal. */
    private VentanaPrincipal ventanaPrincipal;

    /**
     * Devuelve el grupo de botones de método de pago.
     * @return ButtonGroup de métodos de pago
     */
    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    /**
     * Indica si el método de pago seleccionado es efectivo.
     * @return true si se seleccionó efectivo
     */
    public boolean seleccionEfectivo() {
        return botonEfectivo.isSelected();
    }

    /**
     * Indica si el método de pago seleccionado es tarjeta.
     * @return true si se seleccionó tarjeta
     */
    public boolean seleccionTarjeta() {
        return botonTarjeta.isSelected();
    }

    /**
     * Constructor del panel de confirmación de compra.
     * @param ventanaPrincipal Ventana principal de la aplicación
     * @param evento Evento asociado a los botones
     */
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

    /**
     * Asigna las acciones a los botones de continuar y cancelar.
     * @param evento Evento a asociar
     */
    private void asignarAccionBoton(Evento evento) {
        botonContinuar.addActionListener(evento);
        botonContinuar.setActionCommand(Evento.ACEPTAR_CONFIRMAR_COMPRA);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(Evento.CANCELAR_CONFIRMAR_COMPRA);
    }

    /**
     * Configura las preferencias visuales y de layout del panel.
     */
    private void preferenciasPanel() {
        setLayout(new GridBagLayout());
        initAtributos();
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Inicializa los atributos y componentes gráficos del panel.
     */
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

    /**
     * Llena la tabla con los productos del carrito y los totales de la compra.
     * @param totalesCompra Totales de la compra
     * @param listaCarrito Lista de productos en el carrito
     */
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

        llenarCeldas(totalesCompra, listaCarrito, tableModel, format);

        tablaCompras = new JTable(tableModel);
        personalizarTabla(tablaCompras);
        tablaCompras.getColumnModel().getColumn(8).setCellRenderer(new EventoRenderTable());

        tablaCompras.getDefaultEditor(Boolean.class).addCellEditorListener(new EventoCelda(tablaCompras, ventanaPrincipal));
        tablaCompras.revalidate();
        tablaCompras.repaint();
        tablaCompras.getColumnModel().getColumn(0).setMinWidth(0);
        tablaCompras.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaCompras.getColumnModel().getColumn(0).setWidth(0);
        tablaCompras.getColumnModel().getColumn(1).setPreferredWidth(200);
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

    /**
     * Llena las celdas del modelo de la tabla con los productos y totales.
     * @param totalesCompra Totales de la compra
     * @param listaCarrito Lista de productos en el carrito
     * @param tableModel Modelo de la tabla
     * @param format Formato de moneda
     */
    private static void llenarCeldas(TotalesCompra totalesCompra, ArrayList<LibroComprado> listaCarrito, DefaultTableModel tableModel, NumberFormat format) {
        for (LibroComprado libroComprado : listaCarrito) {
            String isbn = libroComprado.getIsbn();
            String tituloLibro = libroComprado.getTitulo();
            int cantidad = libroComprado.getCantidadComprada();
            double valorUnitario = libroComprado.getPrecioVenta();
            double impuestoTotalProducto = libroComprado.getImpuestoTotal();
            double impuesto = libroComprado.getImpuestoUnitario();
            double valorBaseTotal = libroComprado.getPrecioTotalSinIva();
            double valor = libroComprado.getPrecioTotal();
            tableModel.addRow(new Object[]{isbn, tituloLibro, format.format(valorUnitario), format.format(impuesto), cantidad, format.format(impuestoTotalProducto),format.format(valorBaseTotal), format.format(valor), false});
        }

        tableModel.addRow(new Object[]{"", "", "", "", "", "", "Subtotal Base", format.format(totalesCompra.getPrecioBase())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "", "Impuesto Total", "+ " +  format.format(totalesCompra.getImpuestos())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "", "Desc. Premium", "- " +  format.format(totalesCompra.getDescuentoPremium())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "", "Des. Frecuencia", "- " +  format.format(totalesCompra.getDescuentoFrecuencia())});
        tableModel.addRow(new Object[]{"", "", "", "", "", "", "Total", format.format(totalesCompra.getTotal())});
    }

    /**
     * Personaliza la apariencia de la tabla de compras.
     * @param tabla Tabla a personalizar
     */
    public void personalizarTabla(JTable tabla) {

        tabla.setRowHeight(30);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
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
            if (i == 0) {
                column.setPreferredWidth(1);
                continue;
            }
            column.setPreferredWidth(150);
        }
    }

    /**
     * Crea y retorna el modelo de tabla por defecto para la tabla de compras.
     * @return DefaultTableModel configurado para la tabla de compras
     */
    private static DefaultTableModel getDefaultTableModel() {
        String[] cabecera = {"ISBN", "Producto", "V.Base Unitario", "IVA Unit.", "Cantidad","IVA Total", "V.Base Total", "Vlr. Total", "Eliminar"};
        DefaultTableModel tableModel = new DefaultTableModel() {
            public Class<?> getColumnClass(int indexColumna) {
                return indexColumna == 8 ?  Boolean.class : String.class;
            }

            public boolean isCellEditable(int row, int column) {
                return column == 8 && tieneCheckBox(row);
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
