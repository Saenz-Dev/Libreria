package co.edu.uptc.contrato;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.Usuario;

import java.util.List;

public interface ICalculadoraTienda {
    double descuentoPremiumTotal(double precioTotal, Usuario usuario);
    double calcularTotal(double precioBase, double impuestos);
    double calcularImpuestoTotalProducto(Libro libro);
    double calcularBaseTotalProducto(Libro libro);
    double calcularImpuestoTotalCompra(List<Libro> librosReservados);
    double calcularBaseTotalCompra(List<Libro> librosReservados);
    double calcularBase(Libro libro);
    double calcularImpuesto(Libro libro);
    double calcularDescuentoContinuidad(List<Recibo> listaRecibos, double total);
}
