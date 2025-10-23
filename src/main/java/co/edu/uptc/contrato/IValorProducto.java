package co.edu.uptc.contrato;

import co.edu.uptc.modelo.Libro;

public interface IValorProducto {
    double calcularImpuesto(Libro libro);
    double calcularBase(Libro libro);
}
