package co.edu.uptc.modelo;

import java.io.Serializable;

public class LibroComprado extends Libro implements Serializable {

    /**
     * Serialización de la clase para persistencia
     */
    private static final long serialVersionUID = -2814081768628314889L;
    private int cantidadComprada;
    private double precioTotal;
    private double descuentoPremium;
    private double descuentoFrecuencia;
    private double impuestoUnitario;
    private double impuestoTotal;
    private double desPremiumTotal;


    public LibroComprado() {
        super();
        super.setIsComprado(true); //Indica si está reservada o comprada
        this.cantidadComprada = 0;
        this.precioTotal = 0.0;
        this.descuentoPremium = 0.0;
        this.descuentoFrecuencia = 0.0;
        this.impuestoUnitario = 0.0;
        this.impuestoTotal = 0.0;
        this.desPremiumTotal = 0.0;
    }

    public int getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(int cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

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

    public double getDesPremiumTotal() {
        return desPremiumTotal;
    }

    public void setDesPremiumTotal(double desPremiumTotal) {
        this.desPremiumTotal = desPremiumTotal;
    }
}
