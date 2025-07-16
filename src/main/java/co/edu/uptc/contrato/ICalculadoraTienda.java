package co.edu.uptc.contrato;

import co.edu.uptc.modelo.Libro;

import java.util.List;

public interface ICalculadoraTienda extends IValorProducto {
    double calcularImpuestoTotalProducto(Libro libro);
    double calcularBaseTotalProducto(Libro libro);
    double calcularImpuestoTotalCompra(List<Libro> librosReservados);
    double calcularBaseTotalCompra(List<Libro> librosReservados);
}
