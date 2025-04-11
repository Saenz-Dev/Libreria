package co.edu.uptc.negocio;

import java.util.ArrayList;
import java.util.Map;

/**
 * Clase encargada de almacenar los libros disponibles en el catálogo.
 */
public class Catalogo {

    /**
     * Mapa de libros
     */
    private Map<String, ArrayList<Libro>> mapLibros;

    /**
     * Constructor de la clase
     */
    public Catalogo() {}

    /**
     * Método que devuelve el mapa de libros
     * @return mapa de libros
     */
    public Map<String, ArrayList<Libro>> getMapLibros() {
        return mapLibros;
    }

    /**
     * Método que actualiza el mapa de libros
     * @param mapLibros mapa de libros
     */
    public void setMapLibros(Map<String, ArrayList<Libro>> mapLibros) {
        this.mapLibros = mapLibros;
    }
}
