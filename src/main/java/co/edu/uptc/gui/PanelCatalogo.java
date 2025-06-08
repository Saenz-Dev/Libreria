package co.edu.uptc.gui;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import co.edu.uptc.modelo.Categoria;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import co.edu.uptc.modelo.Libro;

/**
 * Clase que representa el panel del catalogo en la interfaz gráfica. Permite
 * visualizar los productos disponibles en el catálogo y gestionar su
 * presentación y comprar.
 */
public class PanelCatalogo extends JPanel {

    /**
     * Etiqueta que muestra el título del panel.
     */
    private JLabel labelTitulo;

    /**
     * Panel que contiene la lista de libros.
     */
    private JPanel panelLibros;

    /**
     * Número de columnas en la disposición del panel de libros.
     */
    private int conteoColumnas;

    /**
     * Número de filas en la disposición del panel de libros.
     */
    private int conteoFilas;

    /**
     * Administrador de diseño basado en GridBagLayout para organizar los libros.
     */
    private GridBagLayout gbPanelLibros;

    /**
     * Restricciones para la disposición de los libros dentro del panel.
     */
    private GridBagConstraints gbcPanelLibros;

    private GridBagConstraints gbc;

    /**
     * Panel con barra de desplazamiento que contiene el panel de libros.
     */
    private JScrollPane scrollPanelLibros;

    /**
     * Formateador de números para mostrar precios u otros valores numéricos.
     */
    private NumberFormat numberFormat;

    /**
     * Referencia a la ventana principal de la aplicación.
     */
    private VentanaPrincipal ventanaPrincipal;

    /**
     * Etiqueta que muestra un mensaje cuando no hay libros registrados.
     */
    private JLabel labelSinLibros;

    /**
     * ComboBox para seleccionar categorías de libros.
     */
    private JComboBox<String> comboBoxCategorias;

    /**
     * ComboBox para seleccionar formatos de libros (físico o digital).
     */
    private JComboBox<String> comboBoxFomatos;

    /**
     * Color de fondo del panel del catálogo.
     */
    private final Color COLOR_FONDO = new Color(244, 246, 248);

    /**
     * Color del borde de las tarjetas de los libros.
     */
    private final Color BORDE_TARJETA = new Color(144, 164, 174);

    /**
     * Evento que maneja los filtros de búsqueda en el catálogo.
     */
    private EventoFiltro eventoFiltro;

    /**
     * Constructor del panel del catalogo.
     *
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     */
    public PanelCatalogo(VentanaPrincipal ventanaPrincipal, EventoFiltro eventoFiltro) {
        this.eventoFiltro = eventoFiltro;
        initAtributos(ventanaPrincipal, eventoFiltro);
        gbc = new GridBagConstraints();
        personalizarFont();
        setupPanel();
        personalizarFiltros();

        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 30, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(labelTitulo, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 1;
        JPanel panelFiltros = ajustarPanelFiltros();
        add(panelFiltros, gbc);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weighty = 0.9;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 1;
        ajustarScrollPanelLibros();

        scrollPanelLibros.setBorder(null);

        add(scrollPanelLibros, gbc);
        repaint();
    }

    /**
     * Ajusta las propiedades del panel de libros, incluyendo bordes y tamaño.
     */
    private void ajustarScrollPanelLibros() {
        panelLibros.setBorder(new LineBorder(BORDE_TARJETA, 2, true));
        scrollPanelLibros = new JScrollPane(panelLibros);
        scrollPanelLibros.getVerticalScrollBar().setUnitIncrement(15);
        scrollPanelLibros.setPreferredSize(new Dimension(800, 600));
        scrollPanelLibros.setOpaque(false);
        scrollPanelLibros.getViewport().setOpaque(false);
        scrollPanelLibros.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelLibros.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanelLibros.setViewportBorder(null);
    }

    /**
     * Ajusta el panel de filtros para que se muestre en la parte superior derecha
     * del panel del catálogo.
     *
     * @return Un JPanel que contiene los filtros de categoría y formato.
     */
    private JPanel ajustarPanelFiltros() {
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFiltros.setOpaque(false);
        panelFiltros.add(new JLabel("Categoría:"));
        panelFiltros.add(comboBoxCategorias);
        panelFiltros.add(new JLabel("Formato:"));
        panelFiltros.add(comboBoxFomatos);

        return panelFiltros;
    }

    /**
     * Configura el panel del catálogo con un color de fondo y otras propiedades.
     */
    private void setupPanel() {
        setBackground(COLOR_FONDO);
    }

    /**
     * Personaliza los filtros de búsqueda del catálogo, estableciendo tamaños y
     * fuentes.
     */
    private void personalizarFiltros() {
        comboBoxCategorias.setPreferredSize(new Dimension(200, 30));
        comboBoxFomatos.setPreferredSize(new Dimension(200, 30));
        comboBoxCategorias.setBackground(Color.WHITE);
        comboBoxFomatos.setBackground(Color.WHITE);
        comboBoxCategorias.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBoxFomatos.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    /**
     * Personaliza el formato de los textos del panel.
     */
    private void personalizarFont() {
        Font font = new Font("Arial", Font.BOLD, 30);
        labelTitulo = new JLabel("Catálogo de Libros");
        labelTitulo.setFont(font);
    }

    /**
     * Inicializa los atributos del panel del catalogo.
     *
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     */
    private void initAtributos(VentanaPrincipal ventanaPrincipal, EventoFiltro eventoFiltro) {
        setLayout(new GridBagLayout());
        gbcPanelLibros = new GridBagConstraints();
        gbPanelLibros = new GridBagLayout();
        gbPanelLibros.columnWeights = new double[]{1, 1, 1, 1};
        panelLibros = new JPanel(gbPanelLibros);
        //panelLibros.setPreferredSize(new Dimension(1100, 600));
        panelLibros.setOpaque(false);
        panelLibros.setOpaque(false);
        this.ventanaPrincipal = ventanaPrincipal;
        conteoFilas = 0;
        conteoColumnas = 0;
        numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMinimumFractionDigits(0);
        labelSinLibros = new JLabel("No hay libros registrados");
        comboBoxCategorias = new JComboBox<>();
        comboBoxCategorias.addItemListener(eventoFiltro);
        comboBoxFomatos = new JComboBox<>();
        comboBoxFomatos.addItemListener(eventoFiltro);
    }


    public void crearTablaLibros(ArrayList<Libro> catalogo) {
        if (catalogo == null || catalogo.isEmpty()) {
            repintarPanelLibros();
            return;
        }
        labelSinLibros.setVisible(false);
        DefaultTableModel tableModel = new DefaultTableModel();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMinimumFractionDigits(0);
        tableModel.setColumnIdentifiers(new Object[]{"ISBN", "Titulo", "Autor", "Año", "Categoria", "Editorial", "#Paginas", "Precio", "Disponible", "Reservado", "Tipo"});
        for (Libro libro : catalogo) {
            tableModel.addRow(new Object[]{libro.getIsbn(), libro.getTitulo(), libro.getAutor(), libro.getAnioPublicacion(), libro.getCategoria().getNombre(), libro.getEditorial(), libro.getNumeroPaginas(), numberFormat.format(libro.getPrecioVenta()), libro.getStockDisponible(), libro.getStockReservado(), libro.getTipoLibro()});
        }

        JTable tabla = new JTable(tableModel);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        personalizarTabla(tabla);
        JTableHeader tableHeader = tabla.getTableHeader();
        tableHeader.setBackground(new Color(0x24242C));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        remove(scrollPanelLibros);

        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.weighty = 0.9;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 1;
        scrollPanelLibros = new JScrollPane(tabla);
        scrollPanelLibros.getVerticalScrollBar().setUnitIncrement(15);
        scrollPanelLibros.setBorder(null);
        add(scrollPanelLibros, gbc);

        revalidate();
        repaint();
    }

    public void llenarFiltros(ArrayList<Categoria> categorias) {
        comboBoxFomatos.removeItemListener(eventoFiltro);
        comboBoxCategorias.removeItemListener(eventoFiltro);
        comboBoxCategorias.removeAllItems();
        comboBoxFomatos.removeAllItems();
        comboBoxCategorias.addItem("TODOS");
        comboBoxFomatos.addItem("TODOS");
        for (Categoria categoria : categorias) {
            comboBoxCategorias.addItem(categoria.getNombre());
        }
        comboBoxFomatos.addItem("FISICO");
        comboBoxFomatos.addItem("DIGITAL");
        comboBoxCategorias.addItemListener(eventoFiltro);
        comboBoxFomatos.addItemListener(eventoFiltro);
    }

    public void personalizarTabla(JTable tabla) {
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

    /**
     * Crea paneles de libros a partir de un mapa de libros que son los libros
     * disponibles en el catálogo.
     *
     * @param catalogo Libros en el catalogo.
     */
    public void crearPanelesLibros(ArrayList<Libro> catalogo) {

        remove(scrollPanelLibros);
        panelLibros.removeAll();
        gbc.gridwidth = 3;
        gbc.weighty = 0.9;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 1;
        revalidate();
        repaint();
        scrollPanelLibros = new JScrollPane(panelLibros);
        scrollPanelLibros.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanelLibros.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPanelLibros.getVerticalScrollBar().setUnitIncrement(15);
        scrollPanelLibros.setPreferredSize(new Dimension(800, 600));
        //scrollPanelLibros.setPreferredSize(panelLibros.getSize());

        scrollPanelLibros.setBorder(null);

        add(scrollPanelLibros, gbc);
        conteoColumnas = 0;
        conteoFilas = 0;

        gbcPanelLibros.weighty = 1;
        gbcPanelLibros.weightx = 1;
        gbcPanelLibros.anchor = GridBagConstraints.NORTHWEST;
        gbcPanelLibros.fill = GridBagConstraints.BOTH;

        for (Libro libro : catalogo) {
            anadirPanelLibro(libro);
        }
        /*if (panelLibros.getComponentCount() < 5) {
            gbcPanelLibros.fill = GridBagConstraints.NONE;
            gbcPanelLibros.gridy = conteoFilas++;
            gbcPanelLibros.gridx = 0;
            gbcPanelLibros.gridwidth = 1;
            gbcPanelLibros.gridheight = 1;
            gbcPanelLibros.weightx = 0;
            gbcPanelLibros.weighty = 0;

            JLabel espacio = new JLabel();
            espacio.setPreferredSize(new Dimension(1, 1));
            panelLibros.add(espacio, gbcPanelLibros);
            /*
            gbcPanelLibros.fill = GridBagConstraints.BOTH;
            gbcPanelLibros.gridy = conteoFilas++;
            gbcPanelLibros.gridx = 0;
            gbcPanelLibros.gridwidth = 4;
            gbcPanelLibros.gridheight = 2;
            gbcPanelLibros.weightx = 0 ;
            gbcPanelLibros.weighty = 0;
            panelLibros.add(new JLabel(""), gbcPanelLibros);
        }*/

        if (conteoFilas == 0 && conteoColumnas > 0 && conteoColumnas < 4) {
            gbcPanelLibros.fill = GridBagConstraints.HORIZONTAL;
            gbcPanelLibros.gridx = conteoColumnas++;
            gbcPanelLibros.gridwidth = 1;
            gbcPanelLibros.gridheight = 2;
            gbcPanelLibros.weightx = 1;
            gbcPanelLibros.weighty = 1;

            while (conteoColumnas < 4) {
                gbcPanelLibros.gridx = conteoColumnas++;
                JLabel espacio = new JLabel(); // NUEVO componente cada vez
                espacio.setPreferredSize(new Dimension(600, 220));
                panelLibros.add(espacio, gbcPanelLibros);
            }
        }

        if (conteoFilas < 2) {
            gbcPanelLibros.fill = GridBagConstraints.VERTICAL;
            gbcPanelLibros.gridy = conteoFilas++;
            gbcPanelLibros.gridwidth = 3;
            gbcPanelLibros.gridheight = 2;
            gbcPanelLibros.weightx = 1;
            gbcPanelLibros.weighty = 1;

            JLabel espacio = new JLabel(); // NUEVO componente cada vez
            espacio.setPreferredSize(new Dimension(600, 500));
            panelLibros.add(espacio, gbcPanelLibros);
        }


        if (panelLibros.getComponentCount() == 0) {
            repintarPanelLibros();
            panelLibros.revalidate();
            panelLibros.repaint();
        }

        revalidate();
        repaint();
    }

    private void anadirPanelLibro(Libro libro) {
        PanelLibro panelLibro = new PanelLibro(ventanaPrincipal, libro);
        panelLibro.setPreferredSize(new Dimension(300, 220));
        panelLibro.setBorder(new LineBorder(BORDE_TARJETA, 2, true));
        anadirLibrosPanel(panelLibro);
    }

    /**
     * Repinta el panel de libros.
     */
    public void repintarPanelLibros() {
        gbcPanelLibros.gridy = 0;
        gbcPanelLibros.gridx = 0;
        gbcPanelLibros.weightx = 1;
        gbcPanelLibros.weighty = 1;
        gbcPanelLibros.fill = GridBagConstraints.CENTER;
        conteoFilas = 0;
        labelSinLibros.setVisible(true);
        panelLibros.add(labelSinLibros, gbcPanelLibros);
    }

    public void activarMensajes(String mensaje) {
        panelLibros.removeAll();
        gbcPanelLibros.gridy = 0;
        gbcPanelLibros.gridx = 0;
        gbcPanelLibros.weightx = 1;
        gbcPanelLibros.weighty = 1;
        gbcPanelLibros.fill = GridBagConstraints.CENTER;
        conteoFilas = 0;
        labelSinLibros.setVisible(true);
        labelSinLibros.setText(mensaje);
        panelLibros.add(labelSinLibros, gbcPanelLibros);
        revalidate();
        repaint();
    }

    /**
     * Agrega un panel al panel del Catalogo que representa un libro al panel del
     * catalogo.
     *
     * @param panelLibro Panel del libro a agregar.
     */
    public void anadirLibrosPanel(PanelLibro panelLibro) {
        GridBagConstraints gbcPanelLibros = new GridBagConstraints();
        gbcPanelLibros.weightx = 1.0;
        gbcPanelLibros.weighty = 1;
        gbcPanelLibros.insets = new Insets(10, 10, 10, 10);
        gbcPanelLibros.fill = GridBagConstraints.NONE;
        gbcPanelLibros.gridwidth = 1;
        gbcPanelLibros.gridheight = 1;
        gbcPanelLibros.anchor = GridBagConstraints.NORTHWEST;

        gbcPanelLibros.gridx = conteoColumnas;
        gbcPanelLibros.gridy = conteoFilas;
        panelLibros.add(panelLibro, gbcPanelLibros);

        conteoColumnas++;
        if (conteoColumnas == 4) {
            conteoColumnas = 0;
            conteoFilas++;
        }
    }

    public String getCategoriaSeleccionada() {
        return (String) comboBoxCategorias.getSelectedItem();
    }

    public String getFormatoSeleccionado() {
        return (String) comboBoxFomatos.getSelectedItem();
    }
}
