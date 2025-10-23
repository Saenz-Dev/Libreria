package co.edu.uptc.contrato;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IBusquedaStrategy {
    String getSQL();
    void ajustarParametro(PreparedStatement preparedStatement) throws SQLException;
}
