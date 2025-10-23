package co.edu.uptc.negocio;

import co.edu.uptc.contrato.IValorProducto;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.TipoLibroEnum;

public class ValorProductoDigital implements IValorProducto {

    private static final double IMPUESTO = 0.19;

    @Override
    public double calcularImpuesto(Libro libro) {
        double base = libro.getPrecioVenta() / (1 + IMPUESTO);
        return libro.getPrecioVenta() - base;
    }

    @Override
    public double calcularBase(Libro libro) {
        return libro.getPrecioVenta() / (1 + IMPUESTO);
    }
}
