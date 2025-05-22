package co.edu.uptc.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comentario {

    private String comentario;
    private String isbn;
    private String correo;
    private String usuario;
    private LocalDateTime fecha;
    private int calificacion;
    private String tituloLibro;

    public Comentario() {
        comentario = "";
        isbn = "";
        correo = "";
        usuario = "";
        calificacion = 0;
    }
    
    public String getTituloLibro() {
        return tituloLibro;
    }
    
    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getComentario() {
        return comentario;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCorreo() {
        return correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }    
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
