package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.modelo.Catalogo;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.persistencia.LibroDAO;

/**
 * Clase encargada de gestionar el catálogo de libros.
 */
public class GestionCatalogo {

    /**
     * Instancia Catalogo
     */
    private Catalogo catalogo;

    private LibroDAO libroDAO;

    private Tienda tienda;

    /**
     * Constructor de la clase
     *
     * @throws SQLException
     */
    public GestionCatalogo(Tienda tienda, LibroDAO libroDAO) throws SQLException {
        this.tienda = tienda;
        this.libroDAO = libroDAO;
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
    public ArrayList<Libro> listarLibros() throws SQLException {
        return libroDAO.seleccionarRegistros();
    }
}
