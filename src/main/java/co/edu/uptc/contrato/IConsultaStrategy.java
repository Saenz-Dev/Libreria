package co.edu.uptc.contrato;

import co.edu.uptc.modelo.Recibo;

import java.sql.SQLException;
import java.util.List;

public interface IConsultaStrategy<T> {
    List<T> consultar(IBusquedaStrategy iBusquedaStrategy) throws SQLException;
}
