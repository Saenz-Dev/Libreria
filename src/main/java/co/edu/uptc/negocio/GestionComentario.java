package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

import co.edu.uptc.modelo.Comentario;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.persistencia.ComentarioDAO;

public class GestionComentario {

    private ManejoComentarioJSON manejoComentarioJSON;
    
    private ComentarioDAO comentarioDAO;

    public GestionComentario(Tienda tienda, ComentarioDAO comentarioDAO) throws SQLException {
        //manejoComentarioJSON = new ManejoComentarioJSON(tienda);
        this.comentarioDAO = comentarioDAO;
    }

    public void registrarComentario(Comentario comentario) throws IOException, RuntimeException, SQLException {	
	validarComentario(comentario);
	comentarioDAO.insertarDatos(comentario);
        //manejoComentarioJSON.escribirComentario(comentario.getIsbn(), comentario);
    }

    public Stack<Comentario> buscarComentario(String isbn) throws IOException, RuntimeException, SQLException {
	ArrayList<Comentario> listaComentarios = comentarioDAO.seleccionarComentariosPorLibro(isbn);
	if (listaComentarios == null || listaComentarios.isEmpty()) throw new RuntimeException("Este libro no tiene comentarios.");
	Stack<Comentario> stackComentarios = new Stack<>();
	stackComentarios.addAll(listaComentarios);
        return stackComentarios;
    }

    private static void validarComentario(Comentario comentario) throws RuntimeException {
        if (comentario.getComentario().isBlank() || comentario.getComentario().length() > 200) {
            throw new RuntimeException("El comentario no puede estar vacío o exceder los 200 caracteres.");
        }
        if (comentario.getCalificacion() < 1 || comentario.getCalificacion() > 5) {
            throw new RuntimeException("La calificación debe estar entre 1 y 5.");
        }
    }
}
