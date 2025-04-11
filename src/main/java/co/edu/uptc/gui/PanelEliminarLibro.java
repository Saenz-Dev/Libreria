package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Clase que representa el panel de eliminación de libros en la interfaz gráfica.
 * Permite visualizar los libros disponibles en el catálogo y gestionar su eliminación.
 */
public class PanelEliminarLibro extends JPanel {

    /** Etiqueta que muestra el título del panel. */
    private JLabel labelTitulo;

    /** Panel que contiene la lista de libros. */
    private JPanel panelLibros;

    /** Número de columnas en la disposición del panel de libros. */
    private int conteoColumnas;

    /** Número de filas en la disposición del panel de libros. */
    private int conteoFilas;

    /** Administrador de diseño basado en GridBagLayout para organizar los libros. */
    private GridBagLayout gbPanelLibros;

    /** Restricciones para la disposición de los libros dentro del panel. */
    private GridBagConstraints gbcPanelLibros;

    /** Panel con barra de desplazamiento que contiene el panel de libros. */
    private JScrollPane scrollPanelLibros;

    /** Formateador de números para mostrar precios u otros valores numéricos. */
    private NumberFormat numberFormat;

    /** Referencia a la ventana principal de la aplicación. */
    private VentanaPrincipal ventanaPrincipal;

    /** Botón para eliminar un libro seleccionado. */
    private JButton botonEliminar;

    /** Botón para cancelar la acción de eliminación. */
    private JButton botonCancelar;

    /** Lista de paneles que representan los libros a eliminar. */
    private ArrayList<PanelLibroEliminar> listPanelesLibros;

    /**
     * Obtiene la lista de paneles de libros a eliminar.
     * @return Lista de paneles de libros.
     */
    public ArrayList<PanelLibroEliminar> getListPanelesLibros() {
        return listPanelesLibros;
    }

    public ArrayList<String> isbnLibros() {
        ArrayList<String> titulosLibros = new ArrayList<>();
        if (listPanelesLibros.isEmpty()) return null;
        for (PanelLibroEliminar panelLibroEliminar: listPanelesLibros) {
            if (panelLibroEliminar.isSelected()) {
                titulosLibros.add(panelLibroEliminar.getLibro().getIsbn());
            }
        }
        return titulosLibros;
    }

    public void eliminarPanelesSeleccionados() {
        int index = 0;
        if (listPanelesLibros.isEmpty()) return;
        for (PanelLibroEliminar panelLibroEliminar: listPanelesLibros) {
            if (panelLibroEliminar.isSelected()) {
                listPanelesLibros.remove(index);
            }
            index++;
        }
    }

    /**
     * Elimina un panel de libro del panel de eliminación.
     * @param panelProducto Panel del libro a eliminar.
     */
    public void eliminarPanelProducto(PanelLibroEliminar panelProducto) {
        panelLibros.remove(panelProducto);
    }

    /**
     * Constructor del panel de eliminación de libros.
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     * @param evento Manejador de eventos de la aplicación.
     */
    public PanelEliminarLibro(VentanaPrincipal ventanaPrincipal, Evento evento) {
        initAtributos(ventanaPrincipal);
        GridBagConstraints gbc = new GridBagConstraints();
        ajustarPreferenciasPanel();
        asignarAccionBoton(evento);
        validarExistenciaProductos();

        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 30, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(labelTitulo, gbc);

        gbc.weighty = 1;
        gbc.gridheight = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 1;
        ajustarPanelLibros(gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.REMAINDER;
        add(new JLabel(), gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        add(botonEliminar, gbc);

        gbc.gridx = 1;
        add(botonCancelar, gbc);
    }

    private void ajustarPanelLibros(GridBagConstraints gbc) {
        panelLibros.setBorder(new LineBorder(Color.WHITE));
        panelLibros.setBackground(Color.WHITE);
        scrollPanelLibros = new JScrollPane(panelLibros);
        scrollPanelLibros.getVerticalScrollBar().setUnitIncrement(15);
        scrollPanelLibros.setBackground(Color.WHITE);
        scrollPanelLibros.setBorder(new LineBorder(Color.WHITE));
        add(scrollPanelLibros, gbc);
    }

    private void ajustarPreferenciasPanel() {
        personalizarFont();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 200));
    }

    private void asignarAccionBoton(Evento evento) {
        botonEliminar.addActionListener(evento);
        botonEliminar.setActionCommand(evento.FUNCION_ELIMINAR_LIBRO);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(evento.CANCELAR_ELIMINAR_LIBRO);
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
        conteoColumnas = 0;
        conteoFilas = 0;
        panelLibros.add(new JLabel("No hay libros registrados..."), gbcPanelLibros);
    }

    /**
     * Agrega un panel que representa un libro al panel Eliminar Libros.
     * @param panelLibro Panel del libro a agregar.
     */
    public void anadirLibrosPanel(PanelLibroEliminar panelLibro) {
        gbcPanelLibros.insets = new Insets(10, 10, 10, 10);
        gbcPanelLibros.fill = GridBagConstraints.NONE;

        gbcPanelLibros.gridx = conteoColumnas;
        gbcPanelLibros.gridy = conteoFilas;
        panelLibros.add(panelLibro, gbcPanelLibros);
        listPanelesLibros.add(panelLibro);

        conteoColumnas++;
        if (conteoColumnas == 3) {
            conteoColumnas = 0;
            conteoFilas++;
        }
    }

    /**
     * Crea los paneles de libros a partir de un mapa de libros.
     * @param mapLibros Libros en el catalogo.
     */
    public void crearPanelesLibros(Map<String, ArrayList<Libro>> mapLibros) {
        listPanelesLibros = new ArrayList<>();

        panelLibros.removeAll();
        conteoColumnas = 0;
        conteoFilas = 0;

        gbcPanelLibros.weightx = 1.0;
        gbcPanelLibros.weighty = 1.0;
        gbcPanelLibros.anchor = GridBagConstraints.NORTHWEST;

        for (ArrayList<Libro> libroArrayList : mapLibros.values()) {
            for (Libro libro : libroArrayList) {
                agregarPanelLibro(libro);
            }
        }

        if (panelLibros.getComponentCount() == 0) {
            repintarPanelLibros();
            panelLibros.revalidate();
            panelLibros.repaint();
        }

        revalidate();
        repaint();
    }

    private void agregarPanelLibro(Libro libro) {
        PanelLibroEliminar panelLibro = new PanelLibroEliminar(ventanaPrincipal, libro);
        panelLibro.setPreferredSize(new Dimension(180, 120));
        anadirLibrosPanel(panelLibro);
    }

    /**
     * Inicializa los atributos del panel de eliminación de libros.
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     */
    private void initAtributos(VentanaPrincipal ventanaPrincipal) {
        setLayout(new GridBagLayout());
        gbcPanelLibros = new GridBagConstraints();
        gbPanelLibros = new GridBagLayout();
        panelLibros = new JPanel(gbPanelLibros);
        this.ventanaPrincipal = ventanaPrincipal;
        conteoFilas = 0;
        conteoColumnas = 0;
        numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMinimumFractionDigits(0);
        botonEliminar = new JButton("Eliminar");
        botonCancelar = new JButton("Regresar");
        listPanelesLibros = new ArrayList<>();
    }

    /**
     * Personaliza el formato de los textos del panel.
     */
    private void personalizarFont() {
        Font font = new Font("Arial", Font.BOLD, 30);
        labelTitulo = new JLabel("Eliminar Libros");
        labelTitulo.setFont(font);
    }

    /**
     * Verifica si hay libros en el panel de eliminación y actualiza el panel en caso de estar vacío.
     */
    private void validarExistenciaProductos() {
        if (panelLibros != null) return;
        gbcPanelLibros.weighty = 1.0;
        gbcPanelLibros.fill = GridBagConstraints.BOTH;
        JLabel label = new JLabel("No hay productos seleccionados");
        panelLibros.add(label, gbcPanelLibros);
    }
}
