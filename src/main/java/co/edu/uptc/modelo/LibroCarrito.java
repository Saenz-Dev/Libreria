package co.edu.uptc.modelo;

public class LibroCarrito {

    private String correo_usuario;
    private Long isbn_libro;
    private int cantidad;

    public LibroCarrito(String correo_usuario, Long isbn_libro, int cantidad) {
        this.correo_usuario = correo_usuario;
        this.isbn_libro = isbn_libro;
        this.cantidad = cantidad;
    }

    public LibroCarrito() {}

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public Long getIsbn_libro() {
        return isbn_libro;
    }

    public void setIsbn_libro(Long isbn_libro) {
        this.isbn_libro = isbn_libro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
