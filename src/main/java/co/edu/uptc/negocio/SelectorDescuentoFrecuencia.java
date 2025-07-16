package co.edu.uptc.negocio;

import co.edu.uptc.contrato.IDescuentoFrecuencia;
import co.edu.uptc.modelo.Recibo;

import java.util.List;

public class SelectorDescuentoFrecuencia {

    private List<IDescuentoFrecuencia> listaDescuentos;

    public SelectorDescuentoFrecuencia(List<IDescuentoFrecuencia> listaDescuentos) {
        this.listaDescuentos = listaDescuentos;
    }

    public double calcular(List<Recibo> listaRecibos, double total) {
        double descuento = 0;
        for (IDescuentoFrecuencia descuentoFrecuencia : listaDescuentos) {
            descuento += descuentoFrecuencia.calcularDescuentoFrecuencia(listaRecibos, total);
        }
        return descuento;
    }
}
