package co.edu.uptc.modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase encargada de almacenar los libros disponibles en el catálogo.
 */
public class Catalogo implements Serializable {

    /**
     * Serialización de la clase para persistencia
     */
    private static final long serialVersionUID = 1097085679808862543L;

    /**
     * Lista de libros que conforman el catálogo
     */
    private ArrayList<Libro> catalogoLibros;

    /**
     * Constructor de la clase
     */
    public Catalogo() {
        catalogoLibros = new ArrayList<>();
    }

    /**
     * Metodo que devuelve el mapa de libros
     * @return mapa de libros
     */
    public ArrayList<Libro> getCatalogoLibros() {
        return catalogoLibros;
    }

    /**
     * Metodo que actualiza el mapa de libros
     * @param catalogoLibros lista de libros a setear en el catálogo
     */
    public void setListaLibros(ArrayList<Libro> catalogoLibros) {
        this.catalogoLibros = catalogoLibros;
    }

    /**
     * Busca un libro en el catálogo local por su ISBN.
     * @param isbn ISBN del libro a buscar.
     * @return libro buscado.
     */
    public Libro buscarLibroLocalIsbn(String isbn) {
        for (Libro libro : getCatalogoLibros()) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }
    /**
     * Busca un libro en el catálogo local por su título.
     * @param titulo titulo del libro a buscar.
     * @return libro buscado.
     */
    public Libro buscarLibroLocalTitulo(String titulo) {
        for (Libro libro : getCatalogoLibros()) {
            if (libro.getTitulo().equals(titulo)) {
                return libro;
            }
        }
        return null;
    }
}
