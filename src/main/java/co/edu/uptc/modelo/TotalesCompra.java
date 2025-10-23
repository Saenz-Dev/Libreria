package co.edu.uptc.modelo;

import java.io.Serializable;

/**
 * Clase que representa los totales de una compra en la tienda virtual.
 * Almacena el subtotal, impuestos, descuentos y el total final de la compra.
 * Permite la gestión y consulta de los valores involucrados en el proceso de facturación.
 */
public class TotalesCompra implements Serializable{

    private static final long serialVersionUID = 7831877015225731172L;

    /**
     * Subtotal de la compra antes de impuestos y descuentos.
     */
    private double subtotal;

    /**
     * Valor de los impuestos aplicados a la compra.
     */
    private double impuestos;

    /**
     * Descuento aplicado por ser usuario premium.
     */
    private double descuentoPremium;

    /**
     * Descuento aplicado por frecuencia de compra.
     */
    private double descuentoFrecuencia;

    /**
     * Total final a pagar después de aplicar impuestos y descuentos.
     */
    private double total;

    /**
     * Constructor con parámetros para inicializar los valores de la compra.
     *
     * @param subtotal  El subtotal de la compra antes de impuestos.
     * @param impuestos El monto de los impuestos aplicados a la compra.
     * @param total     El total a pagar después de aplicar impuestos.
     */
    public TotalesCompra(double subtotal, double impuestos, double total, double descuentoPremium, double descuentoFrecuencia) {
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.total = total;
        this.descuentoPremium = descuentoPremium;
        this.descuentoFrecuencia = descuentoFrecuencia;
    }

    /**
     * Constructor vacío que inicializa la compra con valores por defecto.
     */
    public TotalesCompra() {}

    /**
     * Obtiene el subtotal de la compra.
     *
     * @return El subtotal antes de impuestos.
     */
    public double getPrecioBase() {
        return subtotal;
    }

    /**
     * Establece el subtotal de la compra.
     *
     * @param subtotal El nuevo valor del subtotal.
     */
    public void setPrecioBaseTotal(double subtotal) {
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

    /**
     * Obtiene el descuento aplicado por ser usuario premium.
     * @return descuento premium
     */
    public double getDescuentoPremium() {
        return descuentoPremium;
    }

    /**
     * Establece el descuento aplicado por ser usuario premium.
     * @param descuentoPremium descuento premium
     */
    public void setDescuentoPremium(double descuentoPremium) {
        this.descuentoPremium = descuentoPremium;
    }

    /**
     * Obtiene el descuento aplicado por frecuencia de compra.
     * @return descuento por frecuencia
     */
    public double getDescuentoFrecuencia() {
        return descuentoFrecuencia;
    }

    /**
     * Establece el descuento aplicado por frecuencia de compra.
     * @param descuentoFrecuencia descuento por frecuencia
     */
    public void setDescuentoFrecuencia(double descuentoFrecuencia) {
        this.descuentoFrecuencia = descuentoFrecuencia;
    }
}
