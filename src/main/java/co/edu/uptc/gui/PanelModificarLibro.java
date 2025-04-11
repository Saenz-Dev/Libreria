package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;
import co.edu.uptc.negocio.TipoLibro;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa el panel de modificación de un libro en la interfaz gráfica
 * Permite visualizar los datos de un libro y gestionar su modificación.
 */
public class PanelModificarLibro extends JDialog {

    /** Etiqueta para el título de la sección de modificación. */
    private JLabel labelTitulo;

    /** Etiqueta para seleccionar un libro. */
    private JLabel labelLibro;

    /** Etiqueta para mostrar o ingresar el ISBN del libro. */
    private JLabel labelISBN;

    /** Etiqueta para el nombre del libro. */
    private JLabel labelNombre;

    /** Etiqueta para el autor del libro. */
    private JLabel labelAutor;

    /** Etiqueta para el año de publicación del libro. */
    private JLabel labelAnoPublicacion;

    /** Etiqueta para la categoría del libro. */
    private JLabel labelCategoria;

    /** Etiqueta para la editorial del libro. */
    private JLabel labelEditorial;

    /** Etiqueta para el número de páginas del libro. */
    private JLabel labelNumeroPaginas;

    /** Etiqueta para el precio del libro. */
    private JLabel labelPrecio;

    /** Etiqueta para la cantidad disponible del libro. */
    private JLabel labelCantidad;

    /** Etiqueta para el formato del libro (físico o digital). */
    private JLabel labelFormato;

    /** Campo de texto para ingresar el ISBN del libro. */
    private JTextField txtISBN;

    /** ComboBox para seleccionar un libro existente. */
    private JComboBox cbLibros;

    /** Campo de texto para ingresar el nombre del libro. */
    private JTextField txtNombre;

    /** Campo de texto para ingresar el autor del libro. */
    private JTextField txtAutor;

    /** Campo de texto para ingresar el año de publicación del libro. */
    private JTextField txtAnoPublicacion;

    /** ComboBox para seleccionar la categoría del libro. */
    private JComboBox txtCategoria;

    /** Campo de texto para ingresar la editorial del libro. */
    private JTextField txtEditorial;

    /** Campo de texto para ingresar el número de páginas del libro. */
    private JTextField txtNumeroPaginas;

    /** Campo de texto para ingresar el precio del libro. */
    private JTextField txtPrecio;

    /** Campo de texto para ingresar la cantidad de unidades disponibles. */
    private JTextField txtCantidad;

    /** ComboBox para seleccionar el formato del libro (físico o digital). */
    private JComboBox txtFormato;

    /** Botón para confirmar la modificación del libro. */
    private JButton botonModificar;

    /** Botón para cancelar la modificación del libro. */
    private JButton botonCancelar;

    // Métodos GET (devuelven el texto ingresado)

    /**
     * Obtiene el ISBN ingresado.
     * @return ISBN del libro.
     */
    public String getISBN() {
        return txtISBN.getText();
    }

    /**
     * Obtiene el libro seleccionado.
     * @return Nombre del libro seleccionado.
     */
    public String getLibro() {
        return cbLibros.getSelectedItem().toString();
    }

    /**
     * Obtiene el nombre del libro ingresado.
     * @return Nombre del libro.
     */
    public String getNombre() {
        return txtNombre.getText();
    }

    /**
     * Obtiene el autor ingresado.
     * @return Autor del libro.
     */
    public String getAutor() {
        return txtAutor.getText();
    }

    /**
     * Obtiene el año de publicación ingresado.
     * @return Año de publicación.
     */
    public String getAnoPublicacion() {
        return txtAnoPublicacion.getText();
    }

    /**
     * Obtiene la categoría seleccionada.
     * @return Categoría del libro.
     */
    public String getCategoria() {
        return txtCategoria.getSelectedItem().toString();
    }

    /**
     * Obtiene la editorial ingresada.
     * @return Editorial del libro.
     */
    public String getEditorial() {
        return txtEditorial.getText();
    }

    /**
     * Obtiene el número de páginas ingresado.
     * @return Número de páginas.
     */
    public String getNumeroPaginas() {
        return txtNumeroPaginas.getText();
    }

    /**
     * Obtiene el precio ingresado.
     * @return Precio del libro.
     */
    public String getPrecio() {
        return txtPrecio.getText();
    }

    /**
     * Obtiene la cantidad ingresada.
     * @return Cantidad disponible del libro.
     */
    public String getCantidad() {
        return txtCantidad.getText();
    }

    /**
     * Obtiene el formato del libro seleccionado.
     * @return Tipo de libro (físico o digital).
     */
    public TipoLibro getFormato() {
        return txtFormato.getSelectedItem().toString() == String.valueOf(TipoLibro.FISICO) ? TipoLibro.FISICO : TipoLibro.DIGITAL;
    }

    /**
     * Asigna un nuevo ISBN al libro.
     * @param isbn Nuevo ISBN.
     */
    public void setISBN(String isbn) {
        txtISBN.setText(isbn);
    }

    /**
     * Asigna un nuevo libro seleccionado.
     * @param libro Nuevo libro.
     */
    public void setLibro(String libro) {
        cbLibros.setSelectedItem(libro);
    }

    /**
     * Asigna un nuevo nombre al libro.
     * @param nombre Nuevo nombre.
     */
    public void setNombre(String nombre) {
        txtNombre.setText(nombre);
    }

    /**
     * Asigna un nuevo autor al libro.
     * @param autor Nuevo autor.
     */
    public void setAutor(String autor) {
        txtAutor.setText(autor);
    }

    /**
     * Asigna un nuevo año de publicación al libro.
     * @param anoPublicacion Nuevo año de publicación.
     */
    public void setAnoPublicacion(String anoPublicacion) {
        txtAnoPublicacion.setText(anoPublicacion);
    }

    /**
     * Asigna una nueva categoría al libro.
     * @param categoria Nueva categoría.
     */
    public void setCategoria(String categoria) {
        txtCategoria.setSelectedItem(categoria);
    }

    /**
     * Asigna una nueva editorial al libro.
     * @param editorial Nueva editorial.
     */
    public void setEditorial(String editorial) {
        txtEditorial.setText(editorial);
    }

    /**
     * Asigna un nuevo número de páginas al libro.
     * @param numeroPaginas Nuevo número de páginas.
     */
    public void setNumeroPaginas(String numeroPaginas) {
        txtNumeroPaginas.setText(numeroPaginas);
    }

    /**
     * Asigna un nuevo precio al libro.
     * @param precio Nuevo precio.
     */
    public void setPrecio(String precio) {
        txtPrecio.setText(precio);
    }

    /**
     * Asigna una nueva cantidad disponible al libro.
     * @param cantidad Nueva cantidad.
     */
    public void setCantidad(String cantidad) {
        txtCantidad.setText(cantidad);
    }

    /**
     * Asigna un nuevo formato al libro.
     * @param tipoLibro Nuevo tipo de libro (físico o digital).
     */
    public void setFormato(TipoLibro tipoLibro) {
        txtFormato.setSelectedItem(String.valueOf(tipoLibro));
    }

    /**
     * Creación de lista de libros para el comboBox.
     * @param titulosLibros Lista de libros.
     */
    public void listarLibros(String[] titulosLibros) {
        cbLibros.removeAllItems();
        for (int i = 0; i < titulosLibros.length; i++) {
            cbLibros.addItem(titulosLibros[i]);
        }
    }

    /**
     * Constructor del panel de modificación de libros.
     * @param evento Manejador de eventos principal de la aplicación.
     * @param eventoLista Manejador de eventos de la lista de libros a modificar en el comboBox.
     */
    public PanelModificarLibro (Evento evento, EventoLista eventoLista) {
        setTitle("Modificar Libro");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        inicializarAtributos();
        asignarAccionBoton(evento, eventoLista);

        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(labelTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        add(labelLibro, gbc);
        gbc.gridx = 1;
        add(cbLibros, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(labelISBN, gbc);
        gbc.gridx = 1;
        add(txtISBN, gbc);
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(labelNombre, gbc);
        gbc.gridx = 1;
        add(txtNombre, gbc);
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(labelAutor, gbc);
        gbc.gridx = 1;
        add(txtAutor, gbc);
        gbc.gridy = 5;
        gbc.gridx = 0;
        add(labelAnoPublicacion, gbc);
        gbc.gridx = 1;
        add(txtAnoPublicacion, gbc);
        gbc.gridy = 6;
        gbc.gridx = 0;
        add(labelCategoria, gbc);
        gbc.gridx = 1;
        add(txtCategoria, gbc);
        gbc.gridy = 7;
        gbc.gridx = 0;
        add(labelEditorial, gbc);
        gbc.gridx = 1;
        add(txtEditorial, gbc);
        gbc.gridy = 8;
        gbc.gridx = 0;
        add(labelNumeroPaginas, gbc);
        gbc.gridx = 1;
        add(txtNumeroPaginas, gbc);
        gbc.gridy = 9;
        gbc.gridx = 0;
        add(labelPrecio, gbc);
        gbc.gridx = 1;
        add(txtPrecio, gbc);
        gbc.gridy = 10;
        gbc.gridx = 0;
        add(labelCantidad, gbc);
        gbc.gridx = 1;
        add(txtCantidad, gbc);
        gbc.gridy = 11;
        gbc.gridx = 0;
        add(labelFormato, gbc);
        gbc.gridx = 1;
        add(txtFormato, gbc);
        gbc.gridy = 12;
        gbc.gridx = 0;
        add(botonModificar, gbc);
        gbc.gridx = 1;
        add(botonCancelar, gbc);

        setResizable(false);
        setModal(true);
        setSize(450, 500);
        setLocationRelativeTo(null);
    }

    private void asignarAccionBoton(Evento evento, EventoLista eventoLista) {
        botonModificar.addActionListener(evento);
        botonModificar.setActionCommand(evento.MODIFICAR_LIBRO);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(evento.CANCELAR_MODIFICACION_LIBRO);
        cbLibros.addItemListener(eventoLista);
    }

    /**
     * Inicializa los atributos del panel de modificación de libros.
     */
    public void inicializarAtributos() {
        initJLabels();
        initTxt();
        initCb();
        initBotones();
    }

    private void initBotones() {
        botonModificar = new JButton("Modificar");
        botonCancelar = new JButton("Salir");
    }

    private void initCb() {
        cbLibros = new JComboBox<>(); //Lo agregue para el que se elija, los JTextField se llenen con la informacion de cada libro.
        String[] categorias = {"Ficción", "No Ficción", "Misterio", "Ciencia"}; //TODO hacer clase enum para separar que esto tenga sentido
        txtCategoria = new JComboBox<>(categorias);
        String[] formatos = {String.valueOf(TipoLibro.DIGITAL), String.valueOf(TipoLibro.FISICO)};
        txtFormato = new JComboBox<>(formatos);
    }

    private void initJLabels() {
        labelLibro = new JLabel("Libro: ");
        labelTitulo = new JLabel("Modificar Libro");
        labelISBN = new JLabel("ISBN*:");
        labelNombre = new JLabel("Nombre*:");
        labelAutor = new JLabel("Autor*:");
        labelAnoPublicacion = new JLabel("Año de Publicación:");
        labelCategoria = new JLabel("Categoría*:");
        labelEditorial = new JLabel("Editorial:");
        labelNumeroPaginas = new JLabel("Número de Páginas:");
        labelPrecio = new JLabel("Precio*:");
        labelCantidad = new JLabel("Cantidad*:");
        labelFormato = new JLabel("Formato*:");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 30));
    }

    private void initTxt() {
        txtISBN = new JTextField(20);
        txtISBN.setEditable(false);
        txtISBN.setBackground(Color.WHITE);
        txtNombre = new JTextField(20);
        txtAutor = new JTextField(20);
        txtAnoPublicacion = new JTextField(4);
        txtEditorial = new JTextField(20);
        txtNumeroPaginas = new JTextField(5);
        txtPrecio = new JTextField(10);
        txtCantidad = new JTextField(5);
        //cbLibros.setPreferredSize(new Dimension(80, 30));
    }

    public Libro obtenerDatos() throws RuntimeException{
        Libro libro = new Libro();
        try {
            libro.setIsbn(getISBN());
            libro.setTitulo(getNombre());
            libro.setAutor(getAutor());
            libro.setAnioPublicacion(txtAnoPublicacion.getText().isBlank() ? 0 : Integer.parseInt(txtAnoPublicacion.getText()));
            libro.setCategoria(getCategoria());
            libro.setEditorial(getEditorial());
            libro.setNumeroPaginas(txtNumeroPaginas.getText().isBlank() ? 0 : Integer.parseInt(txtNumeroPaginas.getText()));
            libro.setPrecioVenta(txtPrecio.getText().isBlank() ? 0 : Integer.parseInt(txtPrecio.getText()));
            libro.setStockDisponible(txtCantidad.getText().isBlank() ? 0 : Integer.parseInt(txtCantidad.getText()));
            libro.setTipoLibro(getFormato());
            return libro;
        } catch (RuntimeException e) {
            throw new RuntimeException("Verifica que los datos no tengan espacios de más ni caracteres especiales\nAño Publicacion, #Paginas, Precio y cantidad deben ser numero");
        }
    }

    public void llenarCampos(Libro libro) {
        setISBN(libro.getIsbn());
        setNombre(libro.getTitulo());
        setAutor(libro.getAutor());
        setAnoPublicacion(libro.getAnioPublicacion() == 0 ? "" : String.valueOf(libro.getAnioPublicacion()));
        setCategoria(libro.getCategoria());
        setEditorial(libro.getEditorial());
        setNumeroPaginas(libro.getNumeroPaginas() == 0 ? "" : String.valueOf(libro.getNumeroPaginas()));
        setPrecio(String.valueOf((int) libro.getPrecioVenta()));
        setCantidad(String.valueOf(libro.getStockDisponible()));
        setFormato(libro.getTipoLibro());
    }
}