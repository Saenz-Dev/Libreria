package co.edu.uptc.negocio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import co.edu.uptc.modelo.Catalogo;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Tienda;

/**
 * Clase encargada de gestionar el catálogo de libros.
 */
public class GestionCatalogo {

    /**
     * Instancia Catalogo
     */
    private Catalogo catalogo;

    /**
     * Instancia Manejo de Libros con JSON
     */
    private ManejoLibroJSON manejoLibroJSON;

    /**
     * Constructor de la clase
     */
    public GestionCatalogo(Tienda tienda) {
        manejoLibroJSON = new ManejoLibroJSON(tienda);
    }

    /**
     * Método que devuelve la instancia manejo de libros con JSON
     *
     * @return instancia manejo de libros con JSON
     */
    public ManejoLibroJSON getManejoLibroJSON() {
        return manejoLibroJSON;
    }

    /**
     * Método que actualiza la instancia manejo de libros con JSON
     *
     * @param manejoLibroJSON instancia manejo de libros con JSON
     */
    public void setManejoLibroJSON(ManejoLibroJSON manejoLibroJSON) {
        this.manejoLibroJSON = manejoLibroJSON;
    }

    /**
     * Método que devuelve el catalogo
     *
     * @return catalogo
     */
    public Catalogo getCatalogo() {
        return catalogo;
    }

    /**
     * Método que actualiza el catalogo
     *
     * @param catalogo catalogo
     */
    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    /**
     * Método que devuelve el catalogo de libros disponibles en la tienda
     *
     * @return catalogo de libros disponibles en la tienda
     * @throws IOException si ocurre algún error cuando no se lee el JSON
     */
    public Map<String, ArrayList<Libro>> listarLibros() throws IOException {
        return manejoLibroJSON.leerLibro();
    }
}
