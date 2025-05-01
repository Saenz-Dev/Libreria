package co.edu.uptc.modelo;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Comentario {

    private String comentario;
    private String isbn;
    private String correo;
    private String usuario;
    private String fecha;
    private int calificacion;

    public Comentario() {
        comentario = "";
        isbn = "";
        correo = "";
        usuario = "";
        fecha = "";
        calificacion = 0;
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

    public String getFecha() {
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

    public String fechaActual() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        fecha = LocalDateTime.now().format(dateFormat);
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
