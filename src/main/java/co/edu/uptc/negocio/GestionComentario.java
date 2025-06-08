package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Comentario;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.persistencia.ComentarioDAO;

/**
 * Clase encargada de gestionar los comentarios de los libros en la tienda virtual.
 * Permite registrar y buscar comentarios asociados a un libro, validando su contenido y calificación.
 * Utiliza un DAO para la persistencia de los comentarios.
 */
public class GestionComentario {

    /**
     * DAO para operaciones de persistencia de comentarios.
     */
    private ComentarioDAO comentarioDAO;

    /**
     * Constructor que inicializa la gestión de comentarios con el DAO correspondiente.
     * @param tienda referencia a la tienda virtual (no se usa actualmente)
     * @param comentarioDAO DAO para comentarios
     * @throws SQLException si ocurre un error de base de datos
     */
    public GestionComentario(Tienda tienda, ComentarioDAO comentarioDAO) throws SQLException {
        this.comentarioDAO = comentarioDAO;
    }

    /**
     * Registra un nuevo comentario en el sistema, validando su contenido y calificación.
     *
     * @param comentario comentario a registrar
     * @throws IOException si ocurre un error de entrada/salida
     * @throws RuntimeException si el comentario no es válido
     * @throws SQLException si ocurre un error de base de datos
     */
    public void registrarComentario(Comentario comentario) throws IOException, RuntimeException, SQLException {
        validarComentario(comentario);
        comentarioDAO.insertarDatos(comentario);
    }

    /**
     * Busca y retorna los comentarios asociados a un libro por su ISBN.
     *
     * @param isbn ISBN del libro
     * @return pila de comentarios encontrados
     * @throws IOException si ocurre un error de entrada/salida
     * @throws RuntimeException si no hay comentarios o hay un error
     * @throws SQLException si ocurre un error de base de datos
     */
    public Stack<Comentario> buscarComentario(String isbn) throws IOException, RuntimeException, SQLException {
        ArrayList<Comentario> listaComentarios = comentarioDAO.seleccionarComentariosPorLibro(isbn);
        if (listaComentarios == null || listaComentarios.isEmpty()) {
            RegistroLog.registrarInfo("No se encontraron comentarios en el libro");
            throw new RuntimeException("Este libro no tiene comentarios.");
        } else {
            RegistroLog.registrarInfo("✅ Se encontraron " + listaComentarios.size() + " comentarios.");
        }
        Stack<Comentario> stackComentarios = new Stack<>();
        stackComentarios.addAll(listaComentarios);
        return stackComentarios;
    }

    /**
     * Valida el contenido y la calificación de un comentario.
     *
     * @param comentario comentario a validar
     * @throws RuntimeException si el comentario está vacío, excede los 400 caracteres o la calificación no está entre 1 y 5
     */
    private static void validarComentario(Comentario comentario) throws RuntimeException {
        if (comentario.getComentario().isBlank() || comentario.getComentario().length() > 400) {
            throw new RuntimeException("El comentario no puede estar vacío o exceder los 400 caracteres.");
        }
        if (comentario.getCalificacion() < 1 || comentario.getCalificacion() > 5) {
            throw new RuntimeException("La calificación debe estar entre 1 y 5.");
        }
    }
}
