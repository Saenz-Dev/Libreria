package co.edu.uptc.modelo;

import java.io.Serializable;
import java.util.Stack;

/**
 * Clase encargada de almacenar los datos de un libro.
 * Representa un libro en el catálogo.
 */
public class Libro implements Serializable {

    /**
     * Serialización de la clase para persistencia
     */
    private static final long serialVersionUID = 7932726996518039079L;

    /**
     * ISBN del libro
     */
    private String isbn;

    /**
     * Título del libro
     */
    private String titulo;

    /**
     * Autor del libro
     */
    private String autor;

    /**
     * Año de publicación
     */
    private int anioPublicacion;

    /**
     * Categoría del libro
     */
    private Categoria categoria;

    /**
     * Editorial del libro
     */
    private String editorial;

    /**
     * Número de páginas del libro
     */
    private int numeroPaginas;

    /**
     * Precio de venta del libro
     */
    private double precioVenta;

    /**
     * Stock disponible del libro
     */
    private int stockDisponible;

    /**
     * Stock reservado del libro
     */
    private int stockReservado;

    /**
     * Tipo de libro
     */
    private TipoLibroEnum tipoLibroEnum;

    private boolean isComprado;

    private Stack<Comentario> comentarios;

    public Stack<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Stack<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * Constructor de la clase Libro
     */
    public Libro() {
        isComprado = false;
        comentarios = new Stack<>();
        categoria = new Categoria();
    }
    public boolean getIsComprado() {
        return isComprado;
    }

    public void setIsComprado(boolean comprado) {
        isComprado = comprado;
    }

    /**
     * Metodo que devuelve el ISBN del libro
     * @return ISBN del libro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Metodo que actualiza el ISBN del libro
     * @param isbn ISBN del libro
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Metodo que devuelve el título del libro
     * @return título del libro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Metodo que actualiza el título del libro
     * @param titulo título del libro
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Metodo que devuelve el autor del libro
     * @return autor del libro
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Metodo que actualiza el autor del libro
     * @param autor autor del libro
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Metodo que devuelve el año de publicación del libro
     * @return año de publicación del libro
     */
    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    /**
     * Metodo que actualiza el año de publicación del libro
     * @param anioPublicacion año de publicación del libro
     */
    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    /**
     * Metodo que devuelve la categoría del libro
     * @return categoría del libro
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Metodo que actualiza la categoría del libro
     * @param categoria categoría del libro
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Metodo que devuelve el editorial del libro
     * @return editorial del libro
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Metodo que actualiza el editorial del libro
     * @param editorial editorial del libro
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     * Metodo que devuelve el número de páginas del libro
     * @return número de páginas del libro
     */
    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    /**
     * Metodo que actualiza el número de páginas del libro
     * @param numeroPaginas número de páginas del libro
     */
    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    /**
     * Metodo que devuelve el precio de venta del libro
     * @return precio de venta del libro
     */
    public double getPrecioVenta() {
        return precioVenta;
    }

    /**
     * Metodo que actualiza el precio de venta del libro
     * @param precioVenta precio de venta del libro
     */
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    /**
     * Metodo que devuelve el stock disponible del libro
     * @return stock disponible del libro
     */
    public int getStockDisponible() {
        return stockDisponible;
    }

    /**
     * Metodo que actualiza el stock disponible del libro
     * @param cantidadDisponible cantidad disponible
     */
    public void aumentarCantidad(int cantidadDisponible) {
        this.stockReservado += cantidadDisponible;
    }

    /**
     * Metodo que devuelve el stock reservado del libro
     */
    public void disminuirCantidadUnidad() {
        this.stockReservado--;
    }

    /**
     * Metodo que actualiza el stock reservado del libro
     * @param stockDisponible stock reservado del libro
     */
    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    /**
     * Metodo que devuelve el tipo de libro
     * @return tipo de libro
     */
    public TipoLibroEnum getTipoLibro() {
        return tipoLibroEnum;
    }

    /**
     * Metodo que actualiza el tipo de libro
     * @param tipoLibroEnum tipo de libro
     */
    public void setTipoLibro(TipoLibroEnum tipoLibroEnum) {
        this.tipoLibroEnum = tipoLibroEnum;
    }

    /**
     * Metodo que devuelve el stock reservado del libro
     * @return stock reservado del libro
     */
    public int getStockReservado() {
        return stockReservado;
    }

    /**
     * Actualiza el stock reservado
     * @param stockReservado stock reservado
     */
    public void setStockReservado(int stockReservado) {
        this.stockReservado = stockReservado;
    }

    /**
     * Aumenta un libro al stock reservado y decrementa el stock disponible
     * @return true si se pudo reservar el libro
     */
    public boolean reservarLibro() {
        if (stockDisponible > 0) {
            stockReservado++;
            stockDisponible--;
            return true;
        }
        return false;
    }

    /**
     * Disminuye el stock reservado y aumenta el stock disponible
     */
    public void cancelarReserva() {
        if (stockReservado > 0) {
            stockDisponible ++;
            stockReservado--;
        }
    }

    /**
     * Elimina la cantidad reservada del stock reservado y la agrega al stock disponible
     */
    public void eliminarReserva(int stockAgregar) {
        if (stockAgregar > 0) {
            stockDisponible += stockAgregar;
            stockReservado -= stockAgregar;
        }
    }

    /**
     * Elimina la cantidad reservada del stock reservado para confirmar la compra
     */
    public void confirmarCompra(int cantidadDisminuir) {
        if (stockReservado > 0) {
            stockReservado -= cantidadDisminuir;
        }
    }
}
