package co.edu.uptc.modelo;

import java.io.Serializable;

/**
 * Representa un libro que ha sido comprado, extendiendo la clase {@link Libro}.
 * Incluye información sobre la cantidad comprada, descuentos, impuestos y precios relacionados con la compra.
 * Permite la persistencia mediante serialización.
 */
public class LibroComprado extends Libro implements Serializable {

    /**
     * Identificador de versión para la serialización de la clase.
     */
    private static final long serialVersionUID = -2814081768628314889L;
    /**
     * Cantidad de ejemplares comprados de este libro.
     */
    private int cantidadComprada;
    /**
     * Precio total pagado por la compra de los ejemplares.
     */
    private double precioTotal;
    /**
     * Descuento aplicado por ser usuario premium.
     */
    private double descuentoPremium;
    /**
     * Descuento aplicado por frecuencia de compra.
     */
    private double descuentoFrecuencia;
    /**
     * Valor del impuesto aplicado a cada ejemplar.
     */
    private double impuestoUnitario;
    /**
     * Valor total del impuesto aplicado a la compra.
     */
    private double impuestoTotal;
    /**
     * Total de descuento premium aplicado a la compra.
     */
    private double desPremiumTotal;
    /**
     * Precio total sin incluir el IVA.
     */
    private double precioTotalSinIva;

    /**
     * Constructor por defecto. Inicializa los valores y marca el libro como comprado.
     */
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

    /**
     * Obtiene la cantidad de ejemplares comprados.
     * @return cantidad comprada
     */
    public int getCantidadComprada() {
        return cantidadComprada;
    }

    /**
     * Establece la cantidad de ejemplares comprados.
     * @param cantidadComprada cantidad comprada
     */
    public void setCantidadComprada(int cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    /**
     * Obtiene el valor del impuesto unitario.
     * @return impuesto unitario
     */
    public double getImpuestoUnitario() {
        return impuestoUnitario;
    }

    /**
     * Establece el valor del impuesto unitario.
     * @param impuestoUnitario impuesto unitario
     */
    public void setImpuestoUnitario(double impuestoUnitario) {
        this.impuestoUnitario = impuestoUnitario;
    }

    /**
     * Obtiene el valor total del impuesto aplicado a la compra.
     * @return impuesto total
     */
    public double getImpuestoTotal() {
        return impuestoTotal;
    }

    /**
     * Establece el valor total del impuesto aplicado a la compra.
     * @param impuestoTotal impuesto total
     */
    public void setImpuestoTotal(double impuestoTotal) {
        this.impuestoTotal = impuestoTotal;
    }

    /**
     * Obtiene el precio total pagado por la compra.
     * @return precio total
     */
    public double getPrecioTotal() {
        return precioTotal;
    }

    /**
     * Establece el precio total pagado por la compra.
     * @param precioTotal precio total
     */
    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
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

    /**
     * Obtiene el total de descuento premium aplicado a la compra.
     * @return total descuento premium
     */
    public double getDesPremiumTotal() {
        return desPremiumTotal;
    }

    /**
     * Establece el total de descuento premium aplicado a la compra.
     * @param desPremiumTotal total descuento premium
     */
    public void setDesPremiumTotal(double desPremiumTotal) {
        this.desPremiumTotal = desPremiumTotal;
    }

    /**
     * Obtiene el precio total sin incluir el IVA.
     * @return precio total sin IVA
     */
    public double getPrecioTotalSinIva() {
        return precioTotalSinIva;
    }

    /**
     * Establece el precio total sin incluir el IVA.
     * @param precioTotalSinIva precio total sin IVA
     */
    public void setPrecioTotalSinIva(double precioTotalSinIva) {
        this.precioTotalSinIva = precioTotalSinIva;
    }
}
