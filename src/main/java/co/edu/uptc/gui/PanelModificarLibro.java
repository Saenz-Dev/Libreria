package co.edu.uptc.gui;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.TipoLibroEnum;
import co.edu.uptc.modelo.Categoria;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Clase que representa el panel de modificación de un libro en la interfaz gráfica
 * Permite visualizar los datos de un libro y gestionar su modificación.
 */
public class PanelModificarLibro extends JDialog {

    /**
     * Etiqueta para el título de la sección de modificación.
     */
    private JLabel labelTitulo;

    /**
     * Etiqueta para seleccionar un libro.
     */
    private JLabel labelLibro;

    /**
     * Etiqueta para mostrar o ingresar el ISBN del libro.
     */
    private JLabel labelISBN;

    /**
     * Etiqueta para el nombre del libro.
     */
    private JLabel labelNombre;

    /**
     * Etiqueta para el autor del libro.
     */
    private JLabel labelAutor;

    /**
     * Etiqueta para el año de publicación del libro.
     */
    private JLabel labelAnoPublicacion;

    /**
     * Etiqueta para la categoría del libro.
     */
    private JLabel labelCategoria;

    /**
     * Etiqueta para la editorial del libro.
     */
    private JLabel labelEditorial;

    /**
     * Etiqueta para el número de páginas del libro.
     */
    private JLabel labelNumeroPaginas;

    /**
     * Etiqueta para el precio del libro.
     */
    private JLabel labelPrecio;

    /**
     * Etiqueta para la cantidad disponible del libro.
     */
    private JLabel labelCantidad;

    /**
     * Etiqueta para el formato del libro (físico o digital).
     */
    private JLabel labelFormato;

    /**
     * Campo de texto para ingresar el ISBN del libro.
     */
    private JTextField txtISBN;

    /**
     * ComboBox para seleccionar un libro existente.
     */
    private JComboBox cbLibros;

    /**
     * Campo de texto para ingresar el nombre del libro.
     */
    private JTextField txtNombre;

    /**
     * Campo de texto para ingresar el autor del libro.
     */
    private JTextField txtAutor;

    /**
     * Campo de texto para ingresar el año de publicación del libro.
     */
    private JTextField txtAnoPublicacion;

    /**
     * ComboBox para seleccionar la categoría del libro.
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
     * Campo de texto para ingresar la cantidad de unidades disponibles.
     */
    private JTextField txtCantidad;

    /**
     * ComboBox para seleccionar el formato del libro (físico o digital).
     */
    private JComboBox txtFormato;

    /**
     * Botón para confirmar la modificación del libro.
     */
    private JButton botonModificar;

    /**
     * Botón para cancelar la modificación del libro.
     */
    private JButton botonCancelar;

    // Métodos GET (devuelven el texto ingresado)

    /**
     * Obtiene el ISBN ingresado.
     *
     * @return ISBN del libro.
     */
    public String getISBN() {
        return txtISBN.getText();
    }

    /**
     * Obtiene el libro seleccionado.
     *
     * @return Nombre del libro seleccionado.
     */
    public String getLibro() {
        return cbLibros.getSelectedItem().toString();
    }

    /**
     * Obtiene el nombre del libro ingresado.
     *
     * @return Nombre del libro.
     */
    public String getNombre() {
        return txtNombre.getText();
    }

    /**
     * Obtiene el autor ingresado.
     *
     * @return Autor del libro.
     */
    public String getAutor() {
        return txtAutor.getText();
    }

    /**
     * Obtiene el año de publicación ingresado.
     *
     * @return Año de publicación.
     */
    public int getAnoPublicacion() {
        if (txtAnoPublicacion == null || txtAnoPublicacion.getText().isBlank() || txtAnoPublicacion.getText().isEmpty()) {
            return 0;
        } else if (!txtAnoPublicacion.getText().matches("^[0-9]+$")) {
            return -1; // Indica un error en el formato del número de páginas
        } else {
            try {
                return Integer.parseInt(txtAnoPublicacion.getText());
            } catch (NumberFormatException e) {
                throw new RuntimeException("El número de páginas debe ser un número válido menor a 5000.");
            }
        }
    }

    /**
     * Obtiene la categoría seleccionada.
     *
     * @return Categoría del libro.
     */
    public Categoria getCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNombre(txtCategoria.getSelectedItem().toString());
        return categoria;
    }

    /**
     * Obtiene la editorial ingresada.
     *
     * @return Editorial del libro.
     */
    public String getEditorial() {
        return txtEditorial.getText();
    }

    /**
     * Obtiene el número de páginas ingresado.
     *
     * @return Número de páginas.
     */
    public int getNumeroPaginas() {
        if (txtNumeroPaginas == null || txtNumeroPaginas.getText().isBlank() || txtNumeroPaginas.getText().isEmpty()) {
            return 0;
        } else if (!txtNumeroPaginas.getText().matches("^[0-9]+$")) {
            return -999; // Indica un error en el formato del número de páginas
        } else {
            try {
                return Integer.parseInt(txtNumeroPaginas.getText());
            } catch (NumberFormatException e) {
                throw new RuntimeException("El precio debe ser un número válido menor a 10.000.000");
            }
        }
    }

    /**
     * Obtiene el precio ingresado.
     *
     * @return Precio del libro.
     */
    public double getPrecio() {
        if (txtPrecio == null || txtPrecio.getText().isBlank() || txtPrecio.getText().isEmpty()) {
            return 0;
        } else if (!txtPrecio.getText().matches("^[0-9]+$")) {
            return -0.1; // Indica un error en el formato del número de páginas
        } else {
            try {
                return Integer.parseInt(txtPrecio.getText());
            } catch (NumberFormatException e) {
                throw new RuntimeException("El precio debe ser un número válido menor a 5000.");
            }
        }
    }

    /**
     * Obtiene la cantidad ingresada.
     *
     * @return Cantidad disponible del libro.
     */
    public int getCantidad() {
        if (txtCantidad == null || txtCantidad.getText().isBlank() || txtCantidad.getText().isEmpty()) {
            return -999;
        } else if (!txtCantidad.getText().matches("^[0-9]+$")) {
            return -1; // Indica un error en el formato del número de páginas
        } else {
            try {
                return Integer.parseInt(txtCantidad.getText());
            } catch (NumberFormatException e) {
                throw new RuntimeException("La cantidad debe ser un número válido.");
            }
        }
    }

    /**
     * Obtiene el formato del libro seleccionado.
     *
     * @return Tipo de libro (físico o digital).
     */
    public TipoLibroEnum getFormato() {
        return TipoLibroEnum.valueOf(txtFormato.getSelectedItem().toString());
    }

    /**
     * Asigna un nuevo ISBN al libro.
     *
     * @param isbn Nuevo ISBN.
     */
    public void setISBN(String isbn) {
        txtISBN.setText(isbn);
    }

    /**
     * Asigna un nuevo libro seleccionado.
     *
     * @param libro Nuevo libro.
     */
    public void setLibro(String libro) {
        cbLibros.setSelectedItem(libro);
    }

    /**
     * Asigna un nuevo nombre al libro.
     *
     * @param nombre Nuevo nombre.
     */
    public void setNombre(String nombre) {
        txtNombre.setText(nombre);
    }

    /**
     * Asigna un nuevo autor al libro.
     *
     * @param autor Nuevo autor.
     */
    public void setAutor(String autor) {
        txtAutor.setText(autor);
    }

    /**
     * Asigna un nuevo año de publicación al libro.
     *
     * @param anoPublicacion Nuevo año de publicación.
     */
    public void setAnoPublicacion(String anoPublicacion) {
        txtAnoPublicacion.setText(anoPublicacion);
    }

    /**
     * Asigna una nueva categoría al libro.
     *
     * @param categoria Nueva categoría.
     */
    public void setCategoria(Categoria categoria) {
        txtCategoria.setSelectedItem(categoria.getNombre());
    }

    /**
     * Asigna una nueva editorial al libro.
     *
     * @param editorial Nueva editorial.
     */
    public void setEditorial(String editorial) {
        txtEditorial.setText(editorial);
    }

    /**
     * Asigna un nuevo número de páginas al libro.
     *
     * @param numeroPaginas Nuevo número de páginas.
     */
    public void setNumeroPaginas(String numeroPaginas) {
        txtNumeroPaginas.setText(numeroPaginas);
    }

    /**
     * Asigna un nuevo precio al libro.
     *
     * @param precio Nuevo precio.
     */
    public void setPrecio(String precio) {
        txtPrecio.setText(precio);
    }

    /**
     * Asigna una nueva cantidad disponible al libro.
     *
     * @param cantidad Nueva cantidad.
     */
    public void setCantidad(String cantidad) {
        txtCantidad.setText(cantidad);
    }

    /**
     * Asigna un nuevo formato al libro.
     *
     * @param tipoLibroEnum Nuevo tipo de libro (físico o digital).
     */
    public void setFormato(TipoLibroEnum tipoLibroEnum) {
        txtFormato.setSelectedItem(tipoLibroEnum);
    }

    /**
     * Creación de lista de libros para el comboBox.
     *
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
     *
     * @param evento      Manejador de eventos principal de la aplicación.
     * @param eventoLista Manejador de eventos de la lista de libros a modificar en el comboBox.
     */
    public PanelModificarLibro(Evento evento, EventoLista eventoLista) {
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
        setSize(450, 600);
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
        cbLibros = new JComboBox<>();//Lo agregue para el que se elija, los JTextField se llenen con la informacion de cada libro.
        cbLibros.setPreferredSize(new Dimension(15, 30));
        txtCategoria = new JComboBox<>();
        txtFormato = new JComboBox<>(TipoLibroEnum.values());
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
        Dimension dimension = new Dimension(15, 30);
        txtISBN = new JTextField(20);
        txtISBN.setPreferredSize(dimension);
        txtISBN.setEditable(false);
        txtISBN.setBackground(Color.WHITE);
        txtNombre = new JTextField(20);
        txtNombre.setPreferredSize(dimension);
        txtAutor = new JTextField(20);
        txtAutor.setPreferredSize(dimension);
        txtAnoPublicacion = new JTextField(4);
        txtAnoPublicacion.setPreferredSize(dimension);
        txtEditorial = new JTextField(20);
        txtEditorial.setPreferredSize(dimension);
        txtNumeroPaginas = new JTextField(5);
        txtNumeroPaginas.setPreferredSize(dimension);
        txtPrecio = new JTextField(10);
        txtPrecio.setPreferredSize(dimension);
        txtCantidad = new JTextField(5);
        txtCantidad.setPreferredSize(dimension);
    }

    public Libro obtenerDatos() throws RuntimeException {
        Libro libro = new Libro();
        try {
            libro.setIsbn(getISBN());
            libro.setTitulo(getNombre());
            libro.setAutor(getAutor());
            libro.setAnioPublicacion(getAnoPublicacion());
            libro.setCategoria(getCategoria());
            libro.setEditorial(getEditorial());
            libro.setNumeroPaginas(getNumeroPaginas());
            libro.setPrecioVenta(getPrecio());
            libro.setStockDisponible(getCantidad());
            libro.setTipoLibro(getFormato());
            return libro;
        } catch (RuntimeException e) {
            throw new RuntimeException("Verifica que los datos no tengan espacios de más ni caracteres especiales\nAño Publicacion, #Paginas, Precio y cantidad deben ser numero");
        }
    }

    public void llenarCbCategoria(ArrayList<Categoria> categorias) {
        txtCategoria.removeAllItems();
        for (Categoria categoria : categorias) {
            txtCategoria.addItem(categoria.getNombre());
        }
        txtCategoria.setSelectedIndex(0); // Selecciona la primera categoría por defecto

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
        revalidate();
        repaint();
    }
}