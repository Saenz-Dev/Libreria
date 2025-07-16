package co.edu.uptc.negocio;

import co.edu.uptc.contrato.IDescuentoFrecuencia;
import co.edu.uptc.modelo.Recibo;

import java.util.List;

public class DescuentoFrecuenciaVeinte implements IDescuentoFrecuencia {
    private double descuento;

    public DescuentoFrecuenciaVeinte(double descuento) {
        this.descuento = descuento;
    }

    @Override
    public double calcularDescuentoFrecuencia(List<Recibo> listaRecibos, double total) {
        if (listaRecibos.size() == 20) {
            return total * descuento;
        }
        return 0;
    }
}
