package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.persistencia.LibroDAO;

/**
 * Clase encargada de gestionar los libros del catálogo.
 */
public class GestionLibro {

    /**
     * Transferencia de datos entre la aplicación y la base de datos.
     */
    private LibroDAO libroDAO;

    /**
     * Validador de datos de los libros y usuarios.
     */
    private Expresion expresion;

    /**
     * Referencia a la tienda que contiene el catálogo de libros.
     */
    private Tienda tienda;

    /**
     * Constructor de la clase
     */
    public GestionLibro(Tienda tienda, LibroDAO libroDAO) throws SQLException {
        this.tienda = tienda;
        expresion = new Expresion();
        this.libroDAO = libroDAO;
        tienda.getCatalogo().setListaLibros(libroDAO.seleccionarRegistros());
    }

    /**
     * Registra un libro en el catálogo
     *
     * @param libro libro a registrar
     * @throws IllegalArgumentException si alguno de los campos no cumple con las reglas
     * @throws SQLException             si ocurre algún error al acceder a la base de datos
     */
    public void registrarLibro(Libro libro) throws IllegalArgumentException, SQLException {
        expresion.validarDatosObligatorios(libro);
        expresion.validarFormatoDatosLibro(libro);
        libroDAO.insertarDatos(libro);
        tienda.getCatalogo().getCatalogoLibros().add(libro);
    }

    /**
     * Método que modifica un libro en el catálogo
     *
     * @param libro libro a modificar
     * @throws IOException      si ocurre algún error cuando no se escribe el JSON
     * @throws RuntimeException
     * @throws SQLException
     */
    public void modificarLibro(Libro libro) throws SQLException, RuntimeException {
        expresion.validarDatosObligatorios(libro);
        expresion.validarFormatoDatosLibro(libro);
        Libro libroExistente = libroDAO.seleccionarRegistro(libro);
        if (libroExistente.getIsComprado()) {
            libro.setIsComprado(true);
        }
        libroDAO.actualizarDatos(libro);
        actualizarCatalogoMemoria(libro);
    }

    /**
     * Actualiza el catálogo en memoria con los datos del libro modificado
     *
     * @param libro libro modificado
     */
    private void actualizarCatalogoMemoria(Libro libro) {
        ArrayList<Libro> catalogo = tienda.getCatalogo().getCatalogoLibros();
        for (Libro libroBuscado : catalogo) {
            if (libroBuscado.getIsbn().equals(libro.getIsbn())) {
                libroBuscado.setStockDisponible(libro.getStockDisponible());
                libroBuscado.setStockReservado(libro.getStockReservado());
                libroBuscado.setTitulo(libro.getTitulo());
                libroBuscado.setAutor(libro.getAutor());
                libroBuscado.setAnioPublicacion(libro.getAnioPublicacion());
                libroBuscado.setNumeroPaginas(libro.getNumeroPaginas());
                libroBuscado.setPrecioVenta(libro.getPrecioVenta());
                libroBuscado.setCategoria(libro.getCategoria());
                libroBuscado.setTipoLibro(libro.getTipoLibro());
                libroBuscado.setEditorial(libro.getEditorial());
                libroBuscado.setIsComprado(libro.getIsComprado());
                break;
            }
        }
    }

    /**
     * Metodo que elimina un libro del catálogo
     * @param isbnLibros ArrayList de ISBN de los libros a eliminar
     * @throws SQLException si ocurre algún error al acceder a la base de datos
     * @throws RuntimeException si no hay libros registrados para eliminar o si hay libros comprados.
     */
    public void eliminarLibro(ArrayList<String> isbnLibros) throws SQLException, RuntimeException {
        StringBuilder sb = new StringBuilder();
        if (isbnLibros.isEmpty()) throw new RuntimeException("No hay libros registrados para eliminar");
        for (String isbn : isbnLibros) {
            Libro libro = tienda.getCatalogo().buscarLibroLocalIsbn(isbn);
            if (libro.getIsComprado()) { //Si el libro ya ha sido comprado, no se puede eliminar
                sb.append("\n- " + libro.getTitulo());
                continue;
            }
            libroDAO.eliminarRegistro(libro);
            tienda.getCatalogo().getCatalogoLibros().remove(libro); // Eliminar de la memoria
        }
        if (!sb.isEmpty())
            throw new IllegalArgumentException("Estos libros no se pueden eliminar por que ya se han comprado: " + sb);
    }

    /**
     * Metodo que devuelve un array con los títulos de los libros que se encuentran
     * en el catálogo
     *
     * @return array con los títulos de los libros que se encuentran en el catálogo
     * @throws RuntimeException si no hay libros registrados
     * @throws SQLException si ocurre algún error al acceder a la base de datos
     */
    public String[] obtenerLibros() throws RuntimeException {
        String[] arrayLibros;
        ArrayList<Libro> libros = tienda.getCatalogo().getCatalogoLibros();
        if (libros == null || libros.isEmpty()) {
            throw new IllegalArgumentException("No hay libros registrados aun...");
        }
        arrayLibros = new String[libros.size()];
        for (int i = 0; i < arrayLibros.length; i++) {
            arrayLibros[i] = libros.get(i).getTitulo();
        }
        return arrayLibros;
    }

    /**
     * Metodo que devuelve el libro que se encuentra en el catálogo con el título
     * dado
     *
     * @param tituloLibro título del libro que se busca
     * @return libro del catálogo
     * @throws RuntimeException si no se encuentra el libro con el título dado
     */
    public Libro buscarLibro(String tituloLibro) throws RuntimeException {
        //return libroDAO.seleccionarRegistro(tituloLibro);
        return tienda.getCatalogo().buscarLibroLocalTitulo(tituloLibro);
    }

    /**
     * Verifica si el libro tiene stock disponible en el catálogo
     *
     * @param isbnLibro isbn para validar que el libro que lo contenga tenga
     *                  disponibilidad para ser vendido.
     * @return true si el stock disponible es mayor a 0
     * @throws RuntimeException si el libro no se encuentra en el catálogo
     */
    public boolean validarExistencia(String isbnLibro) throws RuntimeException {
        Libro libro = tienda.getCatalogo().buscarLibroLocalIsbn(isbnLibro);
        if (libro == null) {
            throw new RuntimeException("El libro con ISBN " + isbnLibro + " no se encuentra en el catálogo.");
        }
        return libro.getStockDisponible() > 0;
    }
}
