package co.edu.uptc.negocio;

import co.edu.uptc.modelo.Comentario;
import co.edu.uptc.modelo.Tienda;

import java.io.IOException;
import java.util.Stack;

public class GestionComentario {

    private ManejoComentarioJSON manejoComentarioJSON;

    public GestionComentario(Tienda tienda) {
        manejoComentarioJSON = new ManejoComentarioJSON(tienda);
    }

    public void registrarComentario(Comentario comentario) throws IOException, RuntimeException {
        validarComentario(comentario);
        manejoComentarioJSON.escribirComentario(comentario.getIsbn(), comentario);
    }

    public Stack<Comentario> buscarComentario(String isbn) throws IOException, RuntimeException {
        return manejoComentarioJSON.buscarComentario(isbn);
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
