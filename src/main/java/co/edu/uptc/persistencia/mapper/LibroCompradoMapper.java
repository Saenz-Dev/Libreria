package co.edu.uptc.persistencia.mapper;

import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.modelo.LibroComprado;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibroCompradoMapper implements IMapper<LibroComprado> {
    @Override
    public void mapearObjeto(LibroComprado libroComprado, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(8, libroComprado.getIsbn());
        preparedStatement.setInt(9, libroComprado.getCantidadComprada());
        preparedStatement.setDouble(10, libroComprado.getPrecioVenta());
        preparedStatement.setDouble(11, libroComprado.getPrecioTotal());
        preparedStatement.setDouble(15, libroComprado.getImpuestoUnitario());
        preparedStatement.setDouble(16, libroComprado.getImpuestoTotal());
    }

    @Override
    public LibroComprado mapearResultSet(ResultSet resultSet) throws SQLException {
        LibroComprado libroComprado = new LibroComprado();
        libroComprado.setIsbn(String.valueOf(resultSet.getLong(9)));
        libroComprado.setCantidadComprada(resultSet.getInt(10));
        libroComprado.setPrecioVenta(resultSet.getDouble(11));
        libroComprado.setPrecioTotal(resultSet.getDouble(12));
        libroComprado.setImpuestoUnitario(resultSet.getDouble(16));
        libroComprado.setImpuestoTotal(resultSet.getDouble(17));
        libroComprado.setPrecioTotalSinIva(libroComprado.getCantidadComprada() * libroComprado.getPrecioVenta());
        return libroComprado;
    }
}
