package co.edu.uptc.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Clase encargada de almacenar los libros disponibles en el catálogo.
 */
public class Catalogo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1097085679808862543L;
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
