package co.edu.uptc.contrato;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IMapper<T> {
    void mapearObjeto(T objeto, PreparedStatement preparedStatement) throws SQLException;

    T mapearResultSet(ResultSet resultSet) throws SQLException;
}
