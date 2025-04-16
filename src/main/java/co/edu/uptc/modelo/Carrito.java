package co.edu.uptc.modelo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase encargada de almacenar los libros del carrito de compras.
 * Contiene un ArrayList de Libro.
 */
public class Carrito implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2108683978684726974L;
    /**
     * ArrayList de libros del carrito
     */
    private ArrayList<Libro> libros;

    /**
     * Constructor de la clase
     */
    public Carrito() {
        libros = new ArrayList<>();
    }

    /*
     * Método que devuelve el arrayList de libros del carrito
     * @return arrayList de libros del carrito
     */
    public ArrayList<Libro> getLibros() {
        return libros;
    }

    /**
     * Agrega un libro al carrito
     * @param libro libro a agregar a la base de datos
     */
    public void agregarLibroCarrito(Libro libro) {
        Libro libroGuardar = new Libro();
        libroGuardar.setIsbn(libro.getIsbn());
        libroGuardar.setAutor(libro.getAutor());
        libroGuardar.setEditorial(libro.getEditorial());
        libroGuardar.setCategoria(libro.getCategoria());
        libroGuardar.setTipoLibro(libro.getTipoLibro());
        libroGuardar.setTitulo(libro.getTitulo());
        libroGuardar.setAnioPublicacion(libro.getAnioPublicacion());
        libroGuardar.setNumeroPaginas(libro.getNumeroPaginas());
        libroGuardar.setPrecioVenta(libro.getPrecioVenta());
        libroGuardar.setStockDisponible(0);
        libroGuardar.setStockReservado(1);
        libros.add(libroGuardar);
    }

    /**
     * Pasa los libros del user_default al usuario que inicia sesión
     * @param libro libro a asignar al usuario que se loguea
     */
    public void trasladarLibros(Libro libro) {
        if (buscarLibro(libro)) {
            return;
        }
        Libro libroGuardar = new Libro();
        libroGuardar.setIsbn(libro.getIsbn());
        libroGuardar.setAutor(libro.getAutor());
        libroGuardar.setEditorial(libro.getEditorial());
        libroGuardar.setCategoria(libro.getCategoria());
        libroGuardar.setTipoLibro(libro.getTipoLibro());
        libroGuardar.setTitulo(libro.getTitulo());
        libroGuardar.setAnioPublicacion(libro.getAnioPublicacion());
        libroGuardar.setNumeroPaginas(libro.getNumeroPaginas());
        libroGuardar.setPrecioVenta(libro.getPrecioVenta());
        libroGuardar.setStockDisponible(0);
        libroGuardar.setStockReservado(libro.getStockReservado());
        libros.add(libroGuardar);
    }

    /**
     * Retorna {@code true} si encuentra el libro en el carrito del usuario
     * @param libroParametro libro para buscar.
     * @return {@code true} si encuentra el libro, false si no lo encuentra.
     */
    public boolean buscarLibro(Libro libroParametro) {
        for (Libro libro : libros) {
            if (libro.getIsbn().equals(libroParametro.getIsbn())) {
                libro.aumentarCantidad(libroParametro.getStockReservado());
                return true;
            }
        }
        return false;
    }



    /**
     * Método que actualiza el los atributos del libro en el carrito
     * @param librosArrayList arrayList de libros del carrito
     */
    public void setLibros(ArrayList<Libro> librosArrayList) {
        this.libros = librosArrayList;
    }
}
