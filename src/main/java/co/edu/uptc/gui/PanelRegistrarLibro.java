package co.edu.uptc.gui;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.negocio.TipoLibro;

import javax.swing.*;
import java.awt.*;

public class PanelRegistrarLibro extends JDialog {

    /**
     * Etiqueta que muestra el título de la ventana o panel.
     */
    private JLabel labelTitulo;

    /**
     * Etiqueta que muestra el ISBN del libro.
     */
    private JLabel labelISBN;

    /**
     * Etiqueta que muestra el nombre del libro.
     */
    private JLabel labelNombre;

    /**
     * Etiqueta que muestra el autor del libro.
     */
    private JLabel labelAutor;

    /**
     * Etiqueta que muestra el año de publicación del libro.
     */
    private JLabel labelAnoPublicacion;

    /**
     * Etiqueta que muestra la categoría del libro.
     */
    private JLabel labelCategoria;

    /**
     * Etiqueta que muestra la editorial del libro.
     */
    private JLabel labelEditorial;

    /**
     * Etiqueta que muestra el número de páginas del libro.
     */
    private JLabel labelNumeroPaginas;

    /**
     * Etiqueta que muestra el precio del libro.
     */
    private JLabel labelPrecio;

    /**
     * Etiqueta que muestra la cantidad de ejemplares disponibles.
     */
    private JLabel labelCantidad;

    /**
     * Etiqueta que muestra el formato del libro (físico o digital).
     */
    private JLabel labelFormato;

    /**
     * Campo de texto para ingresar el ISBN del libro.
     */
    private JTextField txtISBN;

    /**
     * Campo de texto para ingresar el nombre del libro.
     */
    private JTextField txtNombre;

    /**
     * Campo de texto para ingresar el nombre del autor del libro.
     */
    private JTextField txtAutor;

    /**
     * Campo de texto para ingresar el año de publicación del libro.
     */
    private JTextField txtAnoPublicacion;

    /**
     * Menú desplegable para seleccionar la categoría del libro.
     */
    private JComboBox txtCategoria;

    /**
     * Campo de texto para ingresar la editorial del libro.
     */
    private JTextField txtEditorial;

    /**
     * Campo de texto para ingresar el número de páginas del libro.
     */
    private JTextField txtNumeroPaginas;

    /**
     * Campo de texto para ingresar el precio del libro.
     */
    private JTextField txtPrecio;

    /**
     * Campo de texto para ingresar la cantidad de ejemplares disponibles.
     */
    private JTextField txtCantidad;

    /**
     * Menú desplegable para seleccionar el formato del libro (físico o digital).
     */
    private JComboBox txtFormato;

    /**
     * Botón para agregar el libro al sistema o carrito.
     */
    private JButton botonAgregar;

    /**
     * Botón para cancelar la operación actual.
     */
    private JButton botonCancelar;


    /**
     * Obtiene el ISBN ingresado en el campo de texto.
     *
     * @return ISBN como una cadena de texto.
     */
    public String getTxtIsbn() {
        return txtISBN.getText();
    }

    /**
     * Obtiene el nombre del libro ingresado.
     *
     * @return Nombre del libro como una cadena de texto.
     */
    public String getTxtNombre() {
        return txtNombre.getText();
    }

    /**
     * Obtiene el nombre del autor del libro.
     *
     * @return Autor del libro como una cadena de texto.
     */
    public String getTxtAutor() {
        return txtAutor.getText();
    }

    /**
     * Obtiene el año de publicación del libro.
     *
     * @return Año de publicación como una cadena de texto.
     */
    public String getTxtAnoPublicacion() {
        return txtAnoPublicacion.getText();
    }

    /**
     * Obtiene la categoría seleccionada del libro.
     *
     * @return Categoría del libro como una cadena de texto.
     */
    public String getTxtCategoria() {
        return (String) txtCategoria.getSelectedItem();
    }

    /**
     * Obtiene la editorial del libro.
     *
     * @return Editorial del libro como una cadena de texto.
     */
    public String getTxtEditorial() {
        return txtEditorial.getText();
    }

    /**
     * Obtiene el número de páginas del libro.
     *
     * @return Número de páginas como una cadena de texto.
     */
    public String getTxtNumeroPaginas() {
        return txtNumeroPaginas.getText();
    }

    /**
     * Obtiene el precio del libro.
     *
     * @return Precio del libro como una cadena de texto.
     */
    public String getTxtPrecio() {
        return txtPrecio.getText();
    }

    /**
     * Obtiene la cantidad disponible del libro.
     *
     * @return Cantidad del libro como una cadena de texto.
     */
    public String getTxtCantidad() {
        return txtCantidad.getText();
    }

    /**
     * Obtiene el formato del libro (Físico o Digital).
     *
     * @return Formato del libro como un objeto TipoLibro.
     */
    public TipoLibro getTxtFormato() {
        return txtFormato.getSelectedItem().toString().equals(String.valueOf(TipoLibro.FISICO)) ? TipoLibro.FISICO : TipoLibro.DIGITAL;
    }

    /**
     * Establece el ISBN en el campo de texto.
     *
     * @param texto ISBN del libro.
     */
    public void setTxtIsbn(String texto) {
        txtISBN.setText(texto);
    }

    /**
     * Establece el nombre del libro en el campo de texto.
     *
     * @param texto Nombre del libro.
     */
    public void setTxtNombre(String texto) {
        txtNombre.setText(texto);
    }

    /**
     * Establece el nombre del autor del libro.
     *
     * @param texto Nombre del autor.
     */
    public void setTxtAutor(String texto) {
        txtAutor.setText(texto);
    }

    /**
     * Establece el año de publicación en el campo de texto.
     *
     * @param texto Año de publicación.
     */
    public void setTxtAnoPublicacion(String texto) {
        txtAnoPublicacion.setText(texto);
    }

    /**
     * Establece la categoría del libro en el JComboBox.
     *
     * @param index Categoría del libro.
     */
    public void setTxtCategoria(String index) {
        txtCategoria.setSelectedItem(index);
    }

    /**
     * Establece la editorial del libro en el campo de texto.
     *
     * @param texto Nombre de la editorial.
     */
    public void setTxtEditorial(String texto) {
        txtEditorial.setText(texto);
    }

    /**
     * Establece el número de páginas del libro en el campo de texto.
     *
     * @param texto Número de páginas.
     */
    public void setTxtNumeroPaginas(String texto) {
        txtNumeroPaginas.setText(texto);
    }

    /**
     * Establece el precio del libro en el campo de texto.
     *
     * @param texto Precio del libro.
     */
    public void setTxtPrecio(String texto) {
        txtPrecio.setText(texto);
    }

    /**
     * Establece la cantidad disponible del libro en el campo de texto.
     *
     * @param texto Cantidad del libro.
     */
    public void setTxtCantidad(String texto) {
        txtCantidad.setText(texto);
    }

    /**
     * Establece el formato del libro (Físico o Digital) en el JComboBox.
     *
     * @param index Formato del libro.
     */
    public void setTxtFormato(String index) {
        txtFormato.setSelectedItem(index);
    }

    /**
     * Constructor del panel de registro de libros.
     *
     * @param evento Manejador de eventos de la aplicación.
     */
    public PanelRegistrarLibro(Evento evento) {
        setTitle("Registrar Libro");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 10, 5, 10);
        inicializarAtributos();
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 30));

        asignarEventoBoton(evento);

        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(labelTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        add(labelISBN, gbc);
        gbc.gridx = 1;
        add(txtISBN, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(labelNombre, gbc);
        gbc.gridx = 1;
        add(txtNombre, gbc);
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(labelAutor, gbc);
        gbc.gridx = 1;
        add(txtAutor, gbc);
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(labelAnoPublicacion, gbc);
        gbc.gridx = 1;
        add(txtAnoPublicacion, gbc);
        gbc.gridy = 5;
        gbc.gridx = 0;
        add(labelCategoria, gbc);
        gbc.gridx = 1;
        add(txtCategoria, gbc);
        gbc.gridy = 6;
        gbc.gridx = 0;
        add(labelEditorial, gbc);
        gbc.gridx = 1;
        add(txtEditorial, gbc);
        gbc.gridy = 7;
        gbc.gridx = 0;
        add(labelNumeroPaginas, gbc);
        gbc.gridx = 1;
        add(txtNumeroPaginas, gbc);
        gbc.gridy = 8;
        gbc.gridx = 0;
        add(labelPrecio, gbc);
        gbc.gridx = 1;
        add(txtPrecio, gbc);
        gbc.gridy = 9;
        gbc.gridx = 0;
        add(labelCantidad, gbc);
        gbc.gridx = 1;
        add(txtCantidad, gbc);
        gbc.gridy = 10;
        gbc.gridx = 0;
        add(labelFormato, gbc);
        gbc.gridx = 1;
        add(txtFormato, gbc);
        gbc.gridy = 11;
        gbc.gridx = 0;
        add(botonAgregar, gbc);
        gbc.gridx = 1;
        add(botonCancelar, gbc);

        setResizable(false);
        setModal(true);
        setSize(500, 500);
        setLocationRelativeTo(null);
    }

    private void asignarEventoBoton(Evento evento) {
        botonAgregar.addActionListener(evento);
        botonAgregar.setActionCommand(evento.REGISTRAR_LIBRO);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(evento.CANCELAR_REGISTRO_LIBRO);
    }

    /**
     * Inicializa los atributos del panel de registro de libros.
     */
    public void inicializarAtributos() {

        labelTitulo = new JLabel("Registrar Libro");
        labelISBN = new JLabel("ISBN*:");
        labelNombre = new JLabel("Nombre*:");
        labelAutor = new JLabel("Autor*:");
        labelAnoPublicacion = new JLabel("Año de Publicación:");
        labelCategoria = new JLabel("Categoría*:");
        labelEditorial = new JLabel("Editorial:");
        labelNumeroPaginas = new JLabel("Número de Páginas*:");
        labelPrecio = new JLabel("Precio*:");
        labelCantidad = new JLabel("Cantidad*:");
        labelFormato = new JLabel("Formato*:");

        txtISBN = new JTextField(20);
        txtNombre = new JTextField(20);
        txtAutor = new JTextField(20);
        txtAnoPublicacion = new JTextField(4);
        txtEditorial = new JTextField(20);
        txtNumeroPaginas = new JTextField(5);
        txtPrecio = new JTextField(10);
        txtCantidad = new JTextField(5);

        String[] categorias = {"Ficción", "No Ficción", "Misterio", "Ciencia"};
        txtCategoria = new JComboBox<>(categorias);

        String[] formatos = {String.valueOf(TipoLibro.DIGITAL), String.valueOf(TipoLibro.FISICO)};
        txtFormato = new JComboBox<>(formatos);

        botonAgregar = new JButton("Agregar");
        botonCancelar = new JButton("Cancelar");
    }

    /**
     * Vacia la información de los JTextFields.
     */
    public void limpiarTxtFieldsLibro() {
        setTxtNombre("");
        setTxtIsbn("");
        setTxtAutor("");
        setTxtAnoPublicacion("");
        setTxtEditorial("");
        setTxtNumeroPaginas("");
        setTxtPrecio("");
        setTxtCantidad("");
    }

    /**
     * Guarda los datos del libro en un objeto libro y lo retorna.
     *
     * @return Objeto Libro con los datos contenidos en los JTextFields.
     */
    public Libro obtenerDatos() {
        Libro libro = new Libro();

        libro.setIsbn(getTxtIsbn());
        libro.setTitulo(getTxtNombre());
        libro.setAutor(getTxtAutor());
        libro.setCategoria(getTxtCategoria());
        libro.setEditorial(getTxtEditorial());
        libro.setTipoLibro(getTxtFormato());
        libro.setAnioPublicacion(getTxtAnoPublicacion().matches("^[0-9]{4}$") ? Integer.parseInt(getTxtAnoPublicacion()) : 0);
        libro.setNumeroPaginas(getTxtNumeroPaginas().matches("^[0-9]{1,3}$") ? Integer.parseInt(getTxtNumeroPaginas()) : 0);
        libro.setPrecioVenta(getTxtPrecio().matches("^[0-9]+$") ? Integer.parseInt(getTxtPrecio()) : 0);
        libro.setStockDisponible(getTxtCantidad().matches("^[0-9]+$") ? Integer.parseInt(getTxtCantidad()) : 0);

        return libro;
    }
}
