package co.edu.uptc.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Recibo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2814081768628314889L;
    private Usuario usuario;
    private List<Libro> libros;
    private int numeroRecibo;
    private LocalDateTime fecha;
    private int subtotal;
    //private Descuento descuento;
    private int impuestos;
    private int total;

    public Recibo() {}

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public int getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(int numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(int impuestos) {
        this.impuestos = impuestos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
