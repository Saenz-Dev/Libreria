package co.edu.uptc.modelo;

import java.io.Serializable;

/**
 * Clase que representa los descuentos por frecuencia de compras en la librería virtual.
 * Proporciona los valores de descuento aplicables según la cantidad de compras realizadas por un usuario.
 */
public class DescFrecuencia implements Serializable {

    /** Identificador de versión para la serialización. */
    private static final long serialVersionUID = 3060396762552110010L;

    /** Descuento aplicado al alcanzar 10 compras (5%). */
    private final double DESCUENTO_DIEZ_COMPRAS = 0.05;
    /** Descuento aplicado al alcanzar 50 compras (10%). */
    private final double DESCUENTO_CINCUENTA_COMPRAS = 0.1;
    /** Descuento aplicado al alcanzar 100 compras (25%). */
    private final double DESCUENTO_CIEN_COMPRAS = 0.25;

    /**
     * Obtiene el descuento por alcanzar 10 compras.
     * @return Valor del descuento (0.05).
     */
    public double getDESCUENTO_DIEZ_COMPRAS() {
        return DESCUENTO_DIEZ_COMPRAS;
    }

    /**
     * Obtiene el descuento por alcanzar 50 compras.
     * @return Valor del descuento (0.1).
     */
    public double getDESCUENTO_CINCUENTA_COMPRAS() {
        return DESCUENTO_CINCUENTA_COMPRAS;
    }

    /**
     * Obtiene el descuento por alcanzar 100 compras.
     * @return Valor del descuento (0.25).
     */
    public double getDESCUENTO_CIEN_COMPRAS() {
        return DESCUENTO_CIEN_COMPRAS;
    }
}
