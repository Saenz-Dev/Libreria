package co.edu.uptc.contrato;

import co.edu.uptc.modelo.Libro;

public interface IValorProducto {
    boolean aplica(Libro libro);
    double calcularImpuesto(Libro libro);
    double calcularBase(Libro libro);
}
