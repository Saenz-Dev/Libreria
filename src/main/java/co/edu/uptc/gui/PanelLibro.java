package co.edu.uptc.gui;

import co.edu.uptc.modelo.Libro;

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

    private JButton botonComentario;

    /** Objeto que gestiona los eventos del catálogo. */
    private EventoCatalogo eventoCatalogo;

    /** Formato para la presentación de valores numéricos, como el precio. */
    private NumberFormat format;

    /** Restricciones para la colocación de los componentes en el diseño de la interfaz gráfica. */
    private GridBagConstraints gbc;
    
    private final Color COLOR_AGREGAR = new Color(102, 187, 106);
    
    private final Color COLOR_COMENTARIO  = new Color(144, 202, 249);
    
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
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     * @param libro Libro a visualizar en el panel.
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
        botonAgregar.setActionCommand(eventoCatalogo.AGREGAR_LIBRO);
        botonComentario.addActionListener(eventoCatalogo);
        botonComentario.setActionCommand(eventoCatalogo.VER_COMENTARIOS);

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
        add(labelPrecio, gbc);
        gbc.gridy = 4;
        habilitacionBoton(libro.getStockDisponible() > 0);
        gbc.gridy = 5;
        add(botonComentario, gbc);
    }

    /**
     * Modifica los atributos del panel de un libro.
     * @param libro Libro a modificar.
     */
    private void modificarAtributos(Libro libro) {
        format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        labelTitulo = new JLabel("<html><div align='left'>" + libro.getTitulo() + "</div></html>" );
        labelAutorEditorial = new JLabel("<html><div align='center'>" +( libro.getAutor() + (!libro.getEditorial().isBlank() ? " - " + libro.getEditorial() : "")) + "</div></html>" );
        labelCategoriaPaginas = new JLabel(libro.getCategoria() + ((libro.getNumeroPaginas() != 0 ? " - " + libro.getNumeroPaginas()+ " pags." : "")));
        labelPrecio = new JLabel(String.valueOf(format.format(libro.getPrecioVenta())));
        botonAgregar = new JButton("Agregar al carrito");
        botonComentario = new JButton("Comentarios");
    }

    /**
     * Personaliza el formato de los textos del panel.
     */
    public void personalizar() {
        labelTitulo.setFont(new Font("Sunglasses", Font.BOLD, 20));
        labelTitulo.setForeground(TITULO_LIBRO);
        labelPrecio.setFont(new Font("Sunglasses", Font.BOLD, 20));
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
     * @param valor Indica si el botón debe estar habilitado o no.
     */
    public void habilitacionBoton(boolean valor) {
        if (!valor) {
            gbc.gridy = 4;
            botonAgregar.setVisible(false);
            add(new JLabel("Unidades no disponibles"), gbc);
            revalidate();
        } else {
            gbc.gridy = 4;
            add(botonAgregar, gbc);
            revalidate();
        }
        repaint();
    }
}
