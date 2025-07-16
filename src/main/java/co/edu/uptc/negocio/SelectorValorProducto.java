package co.edu.uptc.negocio;

import co.edu.uptc.contrato.IValorProducto;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.TipoLibroEnum;

import java.util.Map;

public class SelectorValorProducto {

    private Map<TipoLibroEnum, IValorProducto> estrategias;

    public SelectorValorProducto(Map<TipoLibroEnum, IValorProducto> estrategias) {
        this.estrategias = estrategias;
    }

    public IValorProducto obtenerEstrategia(Libro libro) {
        IValorProducto estrategia = this.estrategias.get(libro.getTipoLibro());
        if (estrategia == null) throw new IllegalArgumentException("No se encontró esta estrategia");
        return estrategia;
    }
}
