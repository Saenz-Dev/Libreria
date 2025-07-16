package co.edu.uptc.contrato;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ILibroBusquedaStrategy {
    String getSQL();
    void ajustarParametro(PreparedStatement preparedStatement) throws SQLException;
}
