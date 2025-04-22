package co.edu.uptc.modelo;

import java.io.Serializable;

/**
 * Representa el valor de una compra, incluyendo subtotal, impuestos y total.
 */
public class ValorCompra implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 7831877015225731172L;

    /**
     * Subtotal de la compra antes de impuestos.
     */
    private double subtotal;

    /**
     * Monto de los impuestos aplicados a la compra.
     */
    private double impuestos;

    /**
     * Total a pagar después de aplicar impuestos.
     */
    private double total;

    private double descuento;

    /**
     * Constructor con parámetros para inicializar los valores de la compra.
     *
     * @param subtotal  El subtotal de la compra antes de impuestos.
     * @param impuestos El monto de los impuestos aplicados a la compra.
     * @param total     El total a pagar después de aplicar impuestos.
     */
    public ValorCompra(double subtotal, double impuestos, double total, double descuento) {
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.total = total;
        this.descuento = descuento;
    }

    /**
     * Constructor vacío que inicializa la compra con valores por defecto.
     */
    public ValorCompra() {}

    /**
     * Obtiene el subtotal de la compra.
     *
     * @return El subtotal antes de impuestos.
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal de la compra.
     *
     * @param subtotal El nuevo valor del subtotal.
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Obtiene el valor de los impuestos aplicados a la compra.
     *
     * @return El monto de los impuestos.
     */
    public double getImpuestos() {
        return impuestos;
    }

    /**
     * Establece el valor de los impuestos aplicados a la compra.
     *
     * @param impuestos El nuevo monto de los impuestos.
     */
    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }

    /**
     * Obtiene el total a pagar de la compra.
     *
     * @return El total de la compra después de impuestos.
     */
    public double getTotal() {
        return total;
    }

    /**
     * Establece el total a pagar de la compra.
     *
     * @param total El nuevo valor total de la compra.
     */
    public void setTotal(double total) {
        this.total = total;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}
