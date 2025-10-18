package co.edu.uptc.persistencia.mapper;

import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.modelo.Compra;
import co.edu.uptc.modelo.Recibo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CompraMapper implements IMapper<Compra> {
    @Override
    public void mapearObjeto(Compra compra, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, compra.getNumeroCompra());
        preparedStatement.setString(2, compra.getCorreo());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(compra.getFecha()));
    }

    @Override
    public Compra mapearResultSet(ResultSet resultSet) throws SQLException {
        Compra compra = new Compra();
        compra.setNumeroCompra(resultSet.getInt("numero_compra"));
        compra.setCorreo(resultSet.getString("correo"));
        compra.setFecha(resultSet.getTimestamp("fecha").toLocalDateTime());
        return compra;
    }
}
