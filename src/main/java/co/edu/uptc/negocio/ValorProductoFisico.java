package co.edu.uptc.negocio;

import co.edu.uptc.contrato.IValorProducto;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.TipoLibroEnum;

public class ValorProductoFisico implements IValorProducto {
    @Override
    public boolean aplica(Libro libro) {
        return libro.getTipoLibro() == TipoLibroEnum.FISICO;
    }

    @Override
    public double calcularImpuesto(Libro libro) {
        double base = libro.getPrecioVenta() / 1.05;
        return libro.getPrecioVenta() - base;
    }

    @Override
    public double calcularBase(Libro libro) {
        return libro.getPrecioVenta() / 1.05;
    }
}
