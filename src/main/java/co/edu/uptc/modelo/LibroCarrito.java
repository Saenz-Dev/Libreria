package co.edu.uptc.modelo;

public class LibroCarrito {

    private String correo_usuario;
    private Long isbn_libro;
    private int cantidad;
    private String nombre_libro;
    private double precioTotal;
    private double precioUnitario;

    public LibroCarrito(String correo_usuario, Long isbn_libro, int cantidad) {
        this.correo_usuario = correo_usuario;
        this.isbn_libro = isbn_libro;
        this.cantidad = cantidad;
    }

    public LibroCarrito() {}

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public String getNombre_libro() {
        return nombre_libro;
    }

    public void setNombre_libro(String nombre_libro) {
        this.nombre_libro = nombre_libro;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
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
    
    public void aumentarCantidad(int cantidadDisponible) {
        this.cantidad += cantidadDisponible;
    }
    
    public void disminuirCantidad(int cantidad) {
	this.cantidad -= cantidad;
    }
}
