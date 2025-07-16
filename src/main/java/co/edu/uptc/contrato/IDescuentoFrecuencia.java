package co.edu.uptc.contrato;

import co.edu.uptc.modelo.Recibo;

import java.util.List;

public interface IDescuentoFrecuencia {
    double calcularDescuentoFrecuencia(List<Recibo> listaRecibos, double total);
}
