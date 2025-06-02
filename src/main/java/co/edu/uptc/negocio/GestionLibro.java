package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.persistencia.LibroDAO;

/**
 * Clase encargada de gestionar los libros del catálogo.
 */
public class GestionLibro {

    /**
     * Instancia Manejo de Libros con JSON
     */
    private ManejoLibroJSON manejoLibroJSON;

    private LibroDAO libroDAO;

    /**
     * Expresión regular
     */
    private Expresion expresion;

    /**
     * Constructor de la clase
     */
    public GestionLibro(Tienda tienda, LibroDAO libroDAO) {
        manejoLibroJSON = new ManejoLibroJSON(tienda);
        expresion = new Expresion();
        this.libroDAO = libroDAO;
    }

    /**
     * Método que devuelve la instancia Manejo de Libros con JSON
     *
     * @return instancia Manejo de Libros con JSON
     */
    public ManejoLibroJSON getManejoLibroJSON() {
        return manejoLibroJSON;
    }

    /**
     * Método que actualiza la instancia Manejo de Libros con JSON
     *
     * @param manejoLibroJSON instancia Manejo de Libros con JSON
     */
    public void setManejoLibroJSON(ManejoLibroJSON manejoLibroJSON) {
        this.manejoLibroJSON = manejoLibroJSON;
        this.libroDAO = new LibroDAO();
    }

    /**
     * Método que registra un libro en el catálogo
     *
     * @param libro libro a registrar
     * @throws IllegalArgumentException si alguno de los campos no cumple con las
     *                                  reglas
     * @throws IOException              si ocurre algún error cuando no se escribe
     *                                  el JSON
     * @throws SQLException
     */
    public void registrarLibro(Libro libro) throws IllegalArgumentException, IOException, SQLException {
        expresion.validarDatosObligatorios(libro);
        expresion.validarFormatoDatosLibro(libro);
        libroDAO.insertarDatos(libro);
    }

    /**
     * Método que modifica un libro en el catálogo
     *
     * @param libro libro a modificar
     * @throws IOException      si ocurre algún error cuando no se escribe el JSON
     * @throws RuntimeException
     * @throws SQLException
     */
    public void modificarLibro(Libro libro) throws IOException, SQLException, RuntimeException {
        expresion.validarDatosObligatorios(libro);
        expresion.validarFormatoDatosLibro(libro);
        libroDAO.actualizarDatos(libro);
        // manejoLibroJSON.modificarLibro(libro);
    }

    public void eliminarLibro(ArrayList<String> isbnLibros) throws IOException, SQLException, RuntimeException {

        ArrayList<Libro> catalogo = libroDAO.seleccionarRegistros();
        StringBuilder sb = new StringBuilder();
        if (isbnLibros.isEmpty()) throw new RuntimeException("No hay libros registrados para eliminar");
        for (String isbn : isbnLibros) {
            Libro libro = new Libro();
            libro.setIsbn(isbn);
            libro = libroDAO.seleccionarRegistro(libro);
            if (libro.getIsComprado()) {
                sb.append("\n- " + libro.getTitulo());
                continue;
            }
            if (libro != null) {
                Libro libroEliminar = new Libro();
                libroEliminar.setIsbn(isbn);
                libroDAO.eliminarRegistro(libro);
            }
        }
        if (!sb.isEmpty())
            throw new IllegalArgumentException("Estos libros no se pueden eliminar por que ya se han comprado: " + sb);
    }

    /**
     * Método que devuelve un array con los títulos de los libros que se encuentran
     * en el catálogo
     *
     * @return array con los títulos de los libros que se encuentran en el catálogo
     * @throws RuntimeException
     * @throws SQLException
     */
    public String[] obtenerLibros() throws SQLException, RuntimeException {
        ArrayList<Libro> libros = new ArrayList<>();
        String[] arrayLibros;
        libros = libroDAO.seleccionarRegistros();
        if (libros == null || libros.size() == 0) {
            throw new IllegalArgumentException("No hay libros registrados aun...");
        }
        arrayLibros = new String[libros.size()];
        for (int i = 0; i < arrayLibros.length; i++) {
            arrayLibros[i] = libros.get(i).getTitulo();
        }
        return arrayLibros;
    }

    /**
     * Método que devuelve el libro que se encuentra en el catálogo con el título
     * dado
     *
     * @param tituloLibro título del libro que se busca
     * @return libro del catálogo
     * @throws RuntimeException
     * @throws SQLException
     */
    public Libro buscarLibro(String tituloLibro) throws SQLException, RuntimeException {
        return libroDAO.seleccionarRegistro(tituloLibro);
    }

    /**
     * Verifica si el libro tiene stock disponible en el catálogo
     *
     * @param isbnLibro isbn para validar que el libro que lo contenga tenga
     *                  disponibilidad para ser vendido.
     * @return true si el stock disponible es mayor a 0
     * @throws RuntimeException
     * @throws SQLException
     */
    public boolean validarExistencia(String isbnLibro) throws SQLException, RuntimeException {
        Libro libro = new Libro();
        libro.setIsbn(isbnLibro);
        libro = libroDAO.seleccionarRegistro(libro);
        return libro.getStockDisponible() > 0;
    }
}
