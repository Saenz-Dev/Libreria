package co.edu.uptc.modelo;

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
    private double descuentoPremium;
    private double descuentoFrecuencia;
    private double impuestoUnitario;
    private double impuestoTotal;

    public ProductoCompra() {}

    public double getImpuestoUnitario() {
        return impuestoUnitario;
    }

    public void setImpuestoUnitario(double impuestoUnitario) {
        this.impuestoUnitario = impuestoUnitario;
    }

    public double getImpuestoTotal() {
        return impuestoTotal;
    }

    public void setImpuestoTotal(double impuestoTotal) {
        this.impuestoTotal = impuestoTotal;
    }

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

    public double getDescuentoPremium() {
	return descuentoPremium;
    }

    public void setDescuentoPremium(double descuentoPremium) {
	this.descuentoPremium = descuentoPremium;
    }

    public double getDescuentoFrecuencia() {
	return descuentoFrecuencia;
    }

    public void setDescuentoFrecuencia(double descuentoFrecuencia) {
	this.descuentoFrecuencia = descuentoFrecuencia;
    }
}
