package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Clase que representa el panel de eliminación de un libro en la  {@link PanelEliminarLibro}.
 */
public class PanelLibroEliminar extends JPanel {

    /** Etiqueta para el título del libro. */
    private JLabel labelTitulo;

    /** Etiqueta para mostrar el autor del libro. */
    private JLabel labelAutor;

    /** Etiqueta para mostrar el precio del libro. */
    private JLabel labelPrecio;

    /** Casilla de verificación para seleccionar el libro. */
    private JCheckBox checkBox;

    /** Formato para la presentación de valores numéricos, como el precio. */
    private NumberFormat format;

    /** Restricciones para la colocación de los componentes en el diseño de la interfaz gráfica. */
    private GridBagConstraints gbc;

    /** Objeto que representa el libro asociado a este panel. */
    private Libro libro;

    /**
     * Retorna el libro asociado a este panel.
     * @return libro asociado a este panel.
     */
    public Libro getLibro() {
        return libro;
    }

    /**
     * Indica si el libro asociado a este panel está seleccionado.
     * @return {@code true} si el libro está seleccionado, {@code false} en caso contrario.
     */
    public boolean isSelected() {
        boolean isSelected = checkBox.isSelected();
        return isSelected;
    }

    /**
     * Constructor del panel de eliminación de un libro.
     * @param ventanaPrincipal Referencia a la ventana principal de la aplicación.
     * @param libro Libro a eliminar.
     */
    public PanelLibroEliminar(VentanaPrincipal ventanaPrincipal, Libro libro) {
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        setBorder(new LineBorder(Color.WHITE));
        setBorder(new LineBorder(Color.black));

        initAtributos(libro);
        personalizar();

        gbc.fill = GridBagConstraints.CENTER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 0, 2, 0);
        add(labelTitulo, gbc);

        gbc.gridy = 1;
        add(labelAutor, gbc);

        gbc.gridy = 2;
        add(labelPrecio, gbc);

        gbc.gridy = 3;
        add(checkBox, gbc);
    }

    /**
     * Inicializa los atributos del panel de eliminación de un libro.
     * @param libro Libro a eliminar.
     */
    private void initAtributos(Libro libro) {
        format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        labelTitulo = new JLabel(libro.getTitulo());
        labelAutor = new JLabel(libro.getAutor());
        labelPrecio = new JLabel(String.valueOf(format.format(libro.getPrecioVenta())));
        checkBox = new JCheckBox();
        this.libro = libro;
    }

    /**
     * Personaliza el formato de los textos del panel.
     */
    public void personalizar() {
        labelTitulo.setFont(new Font("Sunglasses", Font.BOLD, 10));
        labelPrecio.setFont(new Font("Sunglasses", Font.BOLD, 10));
        labelAutor.setFont(new Font("Sunglasses", Font.BOLD, 10));
    }
}
