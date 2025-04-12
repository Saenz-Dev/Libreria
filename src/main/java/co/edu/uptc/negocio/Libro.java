package co.edu.uptc.negocio;

import org.checkerframework.checker.units.qual.C;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Clase encargada de almacenar los datos de un libro.
 * Representa un libro en el catálogo.
 */
@Entity @Table(name = "LIBROS") public class Libro {

    /**
     * ISBN del libro
     */
    @Id @Column(name = "ISBN") private String isbn;

    /**
     * Título del libro
     */
    @Column(name = "TITULO") private String titulo;

    /**
     * Autor del libro
     */
    @Column(name = "AUTOR") private String autor;

    /**
     * Año de publicación
     */
    @Column(name = "AÑO PUBLICACION") private int anioPublicacion;

    /**
     * Categoría del libro
     */
    @Column(name = "CATEGORIA") private String categoria;

    /**
     * Editorial del libro
     */
    @Column(name = "EDITORIAL") private String editorial;

    /**
     * Número de páginas del libro
     */
    @Column(name = "PAGINAS") private int numeroPaginas;

    /**
     * Precio de venta del libro
     */
    @Column(name = "PRECIO") private double precioVenta;

    /**
     * Stock disponible del libro
     */
    @Column(name = "STOCK DISPONIBLE") private int stockDisponible;

    /**
     * Stock reservado del libro
     */
    @Column(name = "STOCK RESERVADO") private int stockReservado;

    /**
     * Tipo de libro
     */
    @Column(name = "FORMATO") private TipoLibro tipoLibro;

    /**
     * Constructor de la clase Libro
     */
    public Libro() {
    }

    /**
     * Constructor de la clase Libro
     * @param isbn ISBN del libro
     * @param titulo Título del libro
     * @param autor Autor del libro
     * @param anioPublicacion Año de publicación
     * @param categoria Categoría del libro
     * @param editorial Editorial del libro
     * @param numeroPaginas Número de páginas del libro
     * @param precioVenta Precio de venta del libro
     * @param stockDisponible Stock disponible del libro
     * @param tipoLibro Tipo de libro
     */
    public Libro(String isbn, String titulo, String autor, int anioPublicacion, String categoria, String editorial, int numeroPaginas, double precioVenta, int stockDisponible, TipoLibro tipoLibro) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.editorial = editorial;
        this.numeroPaginas = numeroPaginas;
        this.precioVenta = precioVenta;
        this.stockDisponible = stockDisponible;
        this.tipoLibro = tipoLibro;
    }

    /**
     * Método que devuelve el ISBN del libro
     * @return ISBN del libro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Método que actualiza el ISBN del libro
     * @param isbn ISBN del libro
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Método que devuelve el título del libro
     * @return título del libro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Método que actualiza el título del libro
     * @param titulo título del libro
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Método que devuelve el autor del libro
     * @return autor del libro
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Método que actualiza el autor del libro
     * @param autor autor del libro
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Método que devuelve el año de publicación del libro
     * @return año de publicación del libro
     */
    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    /**
     * Método que actualiza el año de publicación del libro
     * @param anioPublicacion año de publicación del libro
     */
    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    /**
     * Método que devuelve la categoría del libro
     * @return categoría del libro
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Método que actualiza la categoría del libro
     * @param categoria categoría del libro
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Método que devuelve el editorial del libro
     * @return editorial del libro
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Método que actualiza el editorial del libro
     * @param editorial editorial del libro
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     * Método que devuelve el número de páginas del libro
     * @return número de páginas del libro
     */
    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    /**
     * Método que actualiza el número de páginas del libro
     * @param numeroPaginas número de páginas del libro
     */
    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    /**
     * Método que devuelve el precio de venta del libro
     * @return precio de venta del libro
     */
    public double getPrecioVenta() {
        return precioVenta;
    }

    /**
     * Método que actualiza el precio de venta del libro
     * @param precioVenta precio de venta del libro
     */
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    /**
     * Método que devuelve el stock disponible del libro
     * @return stock disponible del libro
     */
    public int getStockDisponible() {
        return stockDisponible;
    }

    /**
     * Método que actualiza el stock disponible del libro
     * @param cantidadDisponible
     */
    public void aumentarCantidad(int cantidadDisponible) {
        this.stockReservado += cantidadDisponible;
    }

    /**
     * Método que devuelve el stock reservado del libro
     * @return stock reservado del libro
     */
    public void disminuirCantidadUnidad() {
        this.stockReservado--;
    }

    /**
     * Método que actualiza el stock reservado del libro
     * @param stockDisponible stock reservado del libro
     */
    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    /**
     * Método que devuelve el tipo de libro
     * @return tipo de libro
     */
    public TipoLibro getTipoLibro() {
        return tipoLibro;
    }

    /**
     * Método que actualiza el tipo de libro
     * @param tipoLibro tipo de libro
     */
    public void setTipoLibro(TipoLibro tipoLibro) {
        this.tipoLibro = tipoLibro;
    }

    /**
     * Método que devuelve el stock reservado del libro
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
    public void confirmarCompra() {
        if (stockReservado > 0) {
            stockReservado = 0;
        }
    }
}
