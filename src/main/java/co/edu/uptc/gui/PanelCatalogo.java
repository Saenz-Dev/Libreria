package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Clase que representa el panel del catalogo en la interfaz gráfica.
 * Permite visualizar los productos disponibles en el catálogo y gestionar su presentación y comprar.
 */
public class PanelCatalogo extends JPanel {

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

    /**
     * Constructor del panel del catalogo.
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     */
    public PanelCatalogo(VentanaPrincipal ventanaPrincipal) {
        initAtributos(ventanaPrincipal);
        GridBagConstraints gbc = new GridBagConstraints();
        personalizarFont();

        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 30, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 0.9;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 1;
        panelLibros.setBorder(new LineBorder(Color.WHITE));
        scrollPanelLibros = new JScrollPane(panelLibros);
        scrollPanelLibros.getVerticalScrollBar().setUnitIncrement(15);
        scrollPanelLibros.setBorder(new LineBorder(Color.WHITE));

        add(scrollPanelLibros, gbc);
        repaint();
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
    }

    /**
     * Crea paneles de libros a partir de un mapa de libros que son los libros disponibles en el catálogo.
     * @param mapLibros Libros en el catalogo.
     */
    public void crearPanelesLibros(Map<String, ArrayList<Libro>> mapLibros) {
        panelLibros.removeAll();
        conteoColumnas = 0;
        conteoFilas = 0;

        gbcPanelLibros.weighty = 1;
        gbcPanelLibros.weightx = 1;
        gbcPanelLibros.anchor = GridBagConstraints.NORTHWEST;

        for (ArrayList<Libro> libroArrayList : mapLibros.values()) {
            for (Libro libro : libroArrayList) {
                anadirPanelLibro(libro);
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

    private void anadirPanelLibro(Libro libro) {
        PanelLibro panelLibro = new PanelLibro(ventanaPrincipal, libro);
        panelLibro.setPreferredSize(new Dimension(270, 150));
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
        conteoColumnas = 0;
        conteoFilas = 0;
        panelLibros.add(new JLabel("No hay libros registrados..."), gbcPanelLibros);
    }

    /**
     * Agrega un panel al panel del Catalogo que representa un libro al panel del catalogo.
     * @param panelLibro Panel del libro a agregar.
     */
    public void anadirLibrosPanel(PanelLibro panelLibro) {
        gbcPanelLibros.weightx = 1.0;
        gbcPanelLibros.insets = new Insets(10, 10, 10, 10);
        gbcPanelLibros.fill = GridBagConstraints.NONE;
        gbcPanelLibros.gridwidth = 1;

        gbcPanelLibros.gridx = conteoColumnas;
        gbcPanelLibros.gridy = conteoFilas;
        panelLibros.add(panelLibro, gbcPanelLibros);

        conteoColumnas++;
        if (conteoColumnas == 2) {
            conteoColumnas = 0;
            conteoFilas++;
        }
    }
}
