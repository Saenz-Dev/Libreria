package co.edu.uptc.gui;

import co.edu.uptc.modelo.Libro;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Clase que representa el panel de eliminación de libros en la interfaz
 * gráfica. Permite visualizar los libros disponibles en el catálogo y gestionar
 * su eliminación.
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
    /** Restricciones de GridBagLayout para el layout principal. */
    private GridBagConstraints gbc;
    /** Lista de paneles que representan los libros a eliminar. */
    private ArrayList<PanelLibroEliminar> listPanelesLibros;

    /**
     * Obtiene la lista de paneles de libros a eliminar.
     * @return Lista de paneles de libros.
     */
    public ArrayList<PanelLibroEliminar> getListPanelesLibros() {
        return listPanelesLibros;
    }

    /**
     * Obtiene la lista de ISBN de los libros seleccionados para eliminar.
     * @return Lista de ISBN de libros seleccionados o null si la lista está vacía.
     */
    public ArrayList<String> isbnLibros() {
        ArrayList<String> titulosLibros = new ArrayList<>();
        if (listPanelesLibros.isEmpty()) return null;
        for (PanelLibroEliminar panelLibroEliminar : listPanelesLibros) {
            if (panelLibroEliminar.isSelected()) {
                titulosLibros.add(panelLibroEliminar.getLibro().getIsbn());
            }
        }
        return titulosLibros;
    }

    /**
     * Elimina los paneles de libros seleccionados de la lista.
     */
    public void eliminarPanelesSeleccionados() {
        if (listPanelesLibros.isEmpty()) return;

        Iterator<PanelLibroEliminar> iterator = listPanelesLibros.iterator();
        while (iterator.hasNext()) {
            PanelLibroEliminar panelLibroEliminar = iterator.next();
            if (panelLibroEliminar.isSelected()) {
                iterator.remove();
            }
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
     * @param evento           Manejador de eventos de la aplicación.
     */
    public PanelEliminarLibro(VentanaPrincipal ventanaPrincipal, Evento evento) {
        initAtributos(ventanaPrincipal);
        gbc = new GridBagConstraints();
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

    /**
     * Ajusta el panel de libros y su barra de desplazamiento.
     * @param gbc Restricciones de GridBagLayout.
     */
    private void ajustarPanelLibros(GridBagConstraints gbc) {
        panelLibros.setBorder(new LineBorder(Color.WHITE));
        panelLibros.setBackground(Color.WHITE);
        scrollPanelLibros = new JScrollPane(panelLibros);
        scrollPanelLibros.getVerticalScrollBar().setUnitIncrement(15);
        scrollPanelLibros.setBackground(Color.WHITE);
        scrollPanelLibros.setBorder(new LineBorder(Color.WHITE));
        add(scrollPanelLibros, gbc);
    }

    /**
     * Ajusta las preferencias visuales y de layout del panel.
     */
    private void ajustarPreferenciasPanel() {
        personalizarFont();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 200));
    }

    /**
     * Asigna las acciones a los botones de eliminar y cancelar.
     * @param evento Evento a asociar
     */
    private void asignarAccionBoton(Evento evento) {
        botonEliminar.addActionListener(evento);
        botonEliminar.setActionCommand(Evento.FUNCION_ELIMINAR_LIBRO);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(Evento.CANCELAR_ELIMINAR_LIBRO);
        botonEliminar.setBackground(Color.RED);
        botonEliminar.setForeground(Color.WHITE);
        botonCancelar.setBackground(Color.lightGray);
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
     * @param panelLibroEliminar Panel del libro a agregar.
     */
    public void anadirLibrosPanel(PanelLibroEliminar panelLibroEliminar) {
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
        panelLibros.add(panelLibroEliminar, gbcPanelLibros);
        listPanelesLibros.add(panelLibroEliminar);

        conteoColumnas++;
        if (conteoColumnas == 4) {
            conteoColumnas = 0;
            conteoFilas++;
        }
    }

    /**
     * Crea y muestra los paneles de libros a partir del catálogo recibido.
     * @param catalogo Lista de libros del catálogo.
     */
    public void crearPanelesLibros(ArrayList<Libro> catalogo) {
        remove(scrollPanelLibros);
        panelLibros.removeAll();
        gbc.gridwidth = 3;
        gbc.weighty = 0.9;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        revalidate();
        repaint();
        ajustarScrolLibros();

        scrollPanelLibros.setBorder(null);

        add(scrollPanelLibros, gbc);
        conteoColumnas = 0;
        conteoFilas = 0;

        gbcPanelLibros.weighty = 1;
        gbcPanelLibros.weightx = 1;
        gbcPanelLibros.anchor = GridBagConstraints.NORTHWEST;
        gbcPanelLibros.fill = GridBagConstraints.BOTH;

        for (Libro libro : catalogo) {
            agregarPanelLibro(libro);
        }

        ajustarFilasColumnasScroll();

        validarFilasColumnas();

        validarPanelLibrosVacio();

        revalidate();
        repaint();
    }

    /**
     * Valida si el panel de libros está vacío y lo repinta si es necesario.
     */
    private void validarPanelLibrosVacio() {
        if (panelLibros.getComponentCount() == 0) {
            repintarPanelLibros();
            panelLibros.revalidate();
            panelLibros.repaint();
        }
    }

    /**
     * Valida la cantidad de filas y columnas para ajustar el layout.
     */
    private void validarFilasColumnas() {
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
    }

    /**
     * Ajusta filas y columnas del panel de libros para el scroll.
     */
    private void ajustarFilasColumnasScroll() {
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
    }

    /**
     * Ajusta el JScrollPane que contiene los libros.
     */
    private void ajustarScrolLibros() {
        scrollPanelLibros = new JScrollPane(panelLibros);
        scrollPanelLibros.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanelLibros.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPanelLibros.getVerticalScrollBar().setUnitIncrement(15);
        scrollPanelLibros.setPreferredSize(new Dimension(800, 600));
    }

    /**
     * Agrega un panel de libro al panel de libros.
     * @param libro Libro a agregar.
     */
    private void agregarPanelLibro(Libro libro) {
        PanelLibroEliminar panelLibro = new PanelLibroEliminar(ventanaPrincipal, libro);
        panelLibro.setPreferredSize(new Dimension(300, 220));

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
     * Verifica si hay libros en el panel de eliminación y actualiza el panel en
     * caso de estar vacío.
     */
    private void validarExistenciaProductos() {
        if (panelLibros != null) return;
        gbcPanelLibros.weighty = 1.0;
        gbcPanelLibros.fill = GridBagConstraints.BOTH;
        JLabel label = new JLabel("No hay productos seleccionados");
        panelLibros.add(label, gbcPanelLibros);
    }
}