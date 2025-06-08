package co.edu.uptc.gui;

import co.edu.uptc.modelo.Libro;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Clase que representa el panel visual de un libro en el catálogo.
 * Permite mostrar información del libro y gestionar acciones como agregar al carrito o ver comentarios.
 */
public class PanelLibro extends JPanel {
    /**
     * Etiqueta para mostrar el título del libro.
     */
    private JLabel labelTitulo;
    /**
     * Etiqueta para mostrar el autor y editorial del libro.
     */
    private JLabel labelAutorEditorial;
    /**
     * Etiqueta para mostrar la categoría y número de páginas.
     */
    private JLabel labelCategoriaPaginas;
    /**
     * Etiqueta para mostrar el precio del libro.
     */
    private JLabel labelPrecio;
    /**
     * Etiqueta para indicar que el precio incluye IVA.
     */
    private JLabel labelIvaIncluido;

    private JLabel labelTipoLibroCantidad;

    /**
     * Botón para agregar el libro al carrito de compras.
     */
    private JButton botonAgregar;

    /**
     * Botón para ver los comentarios del libro.
     */
    private JButton botonComentario;

    /**
     * Objeto que gestiona los eventos del catálogo.
     */
    private EventoCatalogo eventoCatalogo;

    /**
     * Formato para la presentación de valores numéricos, como el precio.
     */
    private NumberFormat format;

    public void setLabelTipoLibroCantidad(Libro libro) {
        this.labelTipoLibroCantidad.setText(libro.getTipoLibro().toString() + " - " + libro.getStockDisponible() + " disponibles");
        revalidate();
        repaint();
    }

    /**
     * Restricciones para la colocación de los componentes en el diseño de la interfaz gráfica.
     */
    private GridBagConstraints gbc;

    private final Color COLOR_AGREGAR = new Color(102, 187, 106);

    private final Color COLOR_COMENTARIO = new Color(144, 202, 249);

    private final Color TEXTO_COMENTARIO = new Color(38, 50, 56);

    private final Color TEXTO_AGREGAR = new Color(38, 50, 56);

    private final Color TITULO_LIBRO = new Color(38, 50, 56);

    private final Color CONTENIDO = new Color(66, 66, 66);

    private final Color PRECIO = new Color(56, 142, 60);

    public JButton getBotonAgregar() {
        return botonAgregar;
    }

    public void enableBotonAgregar(boolean habilitar) {
        botonAgregar.setVisible(habilitar);
        botonComentario.setVisible(habilitar);
    }

    public JLabel getLabelTitulo() {
        return labelTitulo;
    }

    /**
     * Constructor del panel de un libro.
     *
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     * @param libro            Libro a visualizar en el panel.
     */
    public PanelLibro(VentanaPrincipal ventanaPrincipal, Libro libro) {
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        setBorder(new LineBorder(Color.WHITE, 1, true));
        setBackground(Color.WHITE);
        modificarAtributos(libro);
        personalizar();
        this.eventoCatalogo = new EventoCatalogo(ventanaPrincipal, libro.getIsbn(), this);

        botonAgregar.addActionListener(eventoCatalogo);
        botonAgregar.setActionCommand(EventoCatalogo.AGREGAR_LIBRO);
        botonComentario.addActionListener(eventoCatalogo);
        botonComentario.setActionCommand(EventoCatalogo.VER_COMENTARIOS);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelTitulo, gbc);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.gridwidth = 1;
        add(labelAutorEditorial, gbc);
        gbc.gridy = 2;
        add(labelCategoriaPaginas, gbc);
        gbc.gridy = 3;
        add(labelTipoLibroCantidad, gbc);
        gbc.gridy = 4;
        JPanel panel = panelPrecio();
        add(panel, gbc);
        gbc.gridy = 5;
        habilitacionBoton(libro.getStockDisponible() > 0);
        gbc.gridy = 6;
        add(botonComentario, gbc);
    }

    private JPanel panelPrecio() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.add(labelPrecio);
        panel.add(labelIvaIncluido);
        panel.setBackground(Color.WHITE);
        return panel;
    }

    /**
     * Modifica los atributos del panel de un libro.
     *
     * @param libro Libro a modificar.
     */
    private void modificarAtributos(Libro libro) {
        format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        labelTitulo = new JLabel(libro.getTitulo());
        labelAutorEditorial = new JLabel(libro.getAutor() + (!libro.getEditorial().isBlank() ? " - " + libro.getEditorial() : ""));
        labelCategoriaPaginas = new JLabel(libro.getCategoria().getNombre() + ((libro.getNumeroPaginas() != 0 ? " - " + libro.getNumeroPaginas() + " pags." : "")));
        labelTipoLibroCantidad = new JLabel(libro.getTipoLibro().toString() + " - " + libro.getStockDisponible() + " disponibles");
        labelPrecio = new JLabel(String.valueOf(format.format(libro.getPrecioVenta())));
        labelIvaIncluido = new JLabel("(IVA incluido)");
        botonAgregar = new JButton("Agregar al carrito");
        botonComentario = new JButton("Comentarios");
    }

    /**
     * Personaliza el formato de los textos y botones del panel.
     */
    public void personalizar() {
        labelTitulo.setFont(new Font("Sunglasses", Font.BOLD, 20));
        labelTitulo.setForeground(TITULO_LIBRO);
        labelPrecio.setFont(new Font("Sunglasses", Font.BOLD, 20));
        labelIvaIncluido.setFont(new Font("Sunglasses", Font.BOLD, 10));
        labelIvaIncluido.setForeground(PRECIO);
        labelPrecio.setForeground(PRECIO);
        labelAutorEditorial.setForeground(CONTENIDO);
        labelCategoriaPaginas.setForeground(CONTENIDO);
        botonAgregar.setBackground(COLOR_AGREGAR);
        botonAgregar.setFocusPainted(false);
        botonAgregar.setForeground(TEXTO_AGREGAR);
        botonComentario.setBackground(COLOR_COMENTARIO);
        botonComentario.setFocusPainted(false);
        botonComentario.setForeground(TEXTO_COMENTARIO);
    }

    /**
     * Habilita o deshabilita el botón de agregar al carrito.
     *
     * @param valor Indica si el botón debe estar habilitado o no.
     */
    public void habilitacionBoton(boolean valor) {
        if (!valor) {
            gbc.gridy = 5;
            botonAgregar.setVisible(false);
            add(new JLabel("Unidades no disponibles"), gbc);
            revalidate();
        } else {
            gbc.gridy = 5;
            add(botonAgregar, gbc);
            revalidate();
        }
        repaint();
    }
}
