package co.edu.uptc.modelo;

import co.edu.uptc.negocio.TipoPago;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductoCompra implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2814081768628314889L;

    private String isbn;
    private String titulo;
    private int numeroLibros;
    private double precioUnitario;
    private double precioTotal;

    public ProductoCompra() {}

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumeroLibros() {
        return numeroLibros;
    }

    public void setNumeroLibros(int numeroLibros) {
        this.numeroLibros = numeroLibros;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
