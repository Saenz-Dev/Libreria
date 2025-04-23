package co.edu.uptc.modelo;

import java.io.Serializable;

public class DescFrecuencia implements Serializable {

    private static final long serialVersionUID = 3060396762552110010L;

    private final double DESCUENTO_DIEZ_COMPRAS = 0.05;
    private final double DESCUENTO_CINCUENTA_COMPRAS = 0.1;
    private final double DESCUENTO_CIEN_COMPRAS = 0.25;

    public double getDESCUENTO_DIEZ_COMPRAS() {
        return DESCUENTO_DIEZ_COMPRAS;
    }

    public double getDESCUENTO_CINCUENTA_COMPRAS() {
        return DESCUENTO_CINCUENTA_COMPRAS;
    }

    public double getDESCUENTO_CIEN_COMPRAS() {
        return DESCUENTO_CIEN_COMPRAS;
    }
}
