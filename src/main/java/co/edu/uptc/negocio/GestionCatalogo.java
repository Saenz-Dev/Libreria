package co.edu.uptc.negocio;

import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.modelo.Catalogo;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.persistencia.LibroDAO;

/**
 * Clase encargada de gestionar el catálogo de libros de la tienda virtual.
 * Permite obtener y actualizar el catálogo, así como listar los libros disponibles en la tienda.
 * Utiliza un DAO para acceder a la persistencia de los libros.
 */
public class GestionCatalogo {

    /**
     * Instancia del catálogo de la tienda.
     */
    private Catalogo catalogo;

    /**
     * DAO para operaciones de persistencia de libros.
     */
    private LibroDAO libroDAO;

    /**
     * Referencia a la tienda virtual.
     */
    private Tienda tienda;

    /**
     * Constructor de la clase. Inicializa la gestión del catálogo con la tienda y el DAO de libros.
     *
     * @param tienda referencia a la tienda virtual
     * @param libroDAO DAO para libros
     * @throws SQLException si ocurre un error de base de datos
     */
    public GestionCatalogo(Tienda tienda, LibroDAO libroDAO) throws SQLException {
        this.tienda = tienda;
        this.libroDAO = libroDAO;
    }

    /**
     * Obtiene el catálogo de la tienda.
     *
     * @return catálogo de la tienda
     */
    public Catalogo getCatalogo() {
        return catalogo;
    }

    /**
     * Establece el catálogo de la tienda.
     *
     * @param catalogo catálogo a establecer
     */
    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    /**
     * Devuelve la lista de libros disponibles en la tienda.
     *
     * @return lista de libros disponibles
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    public ArrayList<Libro> listarLibros() throws SQLException {
        return libroDAO.seleccionarRegistros();
    }
}
