package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Clase que representa el panel de un libro en la interfaz gráfica de panel Catalogo.
 * Permite visualizar los datos de un libro y gestionar su presentación.
 */
public class PanelLibro extends JPanel {

    /** Etiqueta para el título del libro. */
    private JLabel labelTitulo;

    /** Etiqueta para mostrar el autor y la editorial del libro. */
    private JLabel labelAutorEditorial;

    /** Etiqueta para mostrar la categoría y el número de páginas del libro. */
    private JLabel labelCategoriaPaginas;

    /** Etiqueta para mostrar el precio del libro. */
    private JLabel labelPrecio;

    /** Botón para agregar el libro al carrito de compras. */
    private JButton botonAgregar;

    /** Objeto que gestiona los eventos del catálogo. */
    private EventoCatalogo eventoCatalogo;

    /** Formato para la presentación de valores numéricos, como el precio. */
    private NumberFormat format;

    /** Restricciones para la colocación de los componentes en el diseño de la interfaz gráfica. */
    private GridBagConstraints gbc;

    /**
     * Constructor del panel de un libro.
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     * @param libro Libro a visualizar en el panel.
     */
    public PanelLibro(VentanaPrincipal ventanaPrincipal, Libro libro) {
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        setBorder(new LineBorder(Color.WHITE, 1, true));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(20, 150));

        modificarAtributos(libro);
        personalizar();
        this.eventoCatalogo = new EventoCatalogo(ventanaPrincipal, libro.getIsbn(), this);

        botonAgregar.addActionListener(eventoCatalogo);
        botonAgregar.setActionCommand(eventoCatalogo.AGREGAR_LIBRO);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 0, 2, 0);

        add(labelTitulo, gbc);
        gbc.gridy = 1;
        add(labelAutorEditorial, gbc);
        gbc.gridy = 2;
        add(labelCategoriaPaginas, gbc);
        gbc.gridy = 3;
        add(labelPrecio, gbc);
        gbc.gridy = 4;
        habilitacionBoton(libro.getStockDisponible() > 0);
    }

    /**
     * Modifica los atributos del panel de un libro.
     * @param libro Libro a modificar.
     */
    private void modificarAtributos(Libro libro) {
        format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        labelTitulo = new JLabel(libro.getTitulo());
        labelAutorEditorial = new JLabel(libro.getAutor() + (!libro.getEditorial().isBlank() ? " - " + libro.getEditorial() : ""));
        labelCategoriaPaginas = new JLabel(libro.getCategoria() + ((libro.getNumeroPaginas() != 0 ? " - " + libro.getNumeroPaginas()+ " pags." : "")));
        labelPrecio = new JLabel(String.valueOf(format.format(libro.getPrecioVenta())));
        botonAgregar = new JButton("Agregar al carrito");
    }

    /**
     * Personaliza el formato de los textos del panel.
     */
    public void personalizar() {
        labelTitulo.setFont(new Font("Sunglasses", Font.BOLD, 20));
        labelPrecio.setFont(new Font("Sunglasses", Font.BOLD, 20));
        botonAgregar.setBorderPainted(false);
        botonAgregar.setBackground(new Color(98, 218, 93, 199));
    }

    /**
     * Habilita o deshabilita el botón de agregar al carrito.
     * @param valor Indica si el botón debe estar habilitado o no.
     */
    public void habilitacionBoton(boolean valor) {
        if (!valor) {
            botonAgregar.setVisible(false);
            add(new JLabel("Unidades no disponibles"), gbc);
        } else {
            add(botonAgregar, gbc);
        }
        repaint();
    }
}
