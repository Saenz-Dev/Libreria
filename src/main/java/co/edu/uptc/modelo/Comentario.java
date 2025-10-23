package co.edu.uptc.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un comentario realizado por un usuario sobre un libro.
 * Contiene información sobre el comentario, el usuario, el libro, la fecha y la calificación.
 */
public class Comentario {

    /** Texto del comentario realizado por el usuario. */
    private String comentario;
    /** ISBN del libro al que pertenece el comentario. */
    private String isbn;
    /** Correo del usuario que realizó el comentario. */
    private String correo;
    /** Nombre de usuario que realizó el comentario. */
    private String usuario;
    /** Fecha y hora en que se realizó el comentario. */
    private LocalDateTime fecha;
    /** Calificación otorgada al libro. */
    private int calificacion;
    /** Título del libro al que pertenece el comentario. */
    private String tituloLibro;

    /**
     * Constructor por defecto. Inicializa los campos con valores vacíos o cero.
     */
    public Comentario() {
        comentario = "";
        isbn = "";
        correo = "";
        usuario = "";
        calificacion = 0;
    }
    
    /**
     * Obtiene el título del libro al que pertenece el comentario.
     * @return Título del libro.
     */
    public String getTituloLibro() {
        return tituloLibro;
    }
    
    /**
     * Establece el título del libro al que pertenece el comentario.
     * @param tituloLibro Título del libro.
     */
    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    /**
     * Obtiene el texto del comentario.
     * @return Texto del comentario.
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Obtiene el ISBN del libro comentado.
     * @return ISBN del libro.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Obtiene el correo del usuario que realizó el comentario.
     * @return Correo del usuario.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Obtiene el nombre de usuario que realizó el comentario.
     * @return Nombre de usuario.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Obtiene la fecha y hora en que se realizó el comentario.
     * @return Fecha y hora del comentario.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Obtiene la calificación otorgada al libro.
     * @return Calificación del libro.
     */
    public int getCalificacion() {
        return calificacion;
    }

    /**
     * Establece el texto del comentario.
     * @param comentario Texto del comentario.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Establece el ISBN del libro comentado.
     * @param isbn ISBN del libro.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Establece el correo del usuario que realizó el comentario.
     * @param correo Correo del usuario.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Establece el nombre de usuario que realizó el comentario.
     * @param usuario Nombre de usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }    
    
    /**
     * Establece la fecha y hora en que se realizó el comentario.
     * @param fecha Fecha y hora del comentario.
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * Establece la calificación otorgada al libro.
     * @param calificacion Calificación del libro.
     */
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
