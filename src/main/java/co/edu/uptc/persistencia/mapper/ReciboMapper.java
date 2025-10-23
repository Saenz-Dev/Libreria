package co.edu.uptc.persistencia.mapper;

import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.TipoPagoEnum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class ReciboMapper implements IMapper<Recibo> {
    @Override
    public void mapearObjeto(Recibo recibo, PreparedStatement preparedStatement) throws SQLException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        recibo.getFechaCompra().format(dateFormat);
        preparedStatement.setInt(1, recibo.getNumeroRecibo());
        preparedStatement.setString(2, recibo.getCorreo());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(recibo.getFechaCompra()));
        preparedStatement.setString(4, String.valueOf(recibo.getTipoPago()));
        preparedStatement.setString(5, recibo.getDireccion());
        preparedStatement.setDouble(6, recibo.getValorCompra().getDescuentoPremium());
        preparedStatement.setDouble(7, recibo.getValorCompra().getDescuentoFrecuencia());
        preparedStatement.setDouble(12, recibo.getValorCompra().getPrecioBase());
        preparedStatement.setDouble(13, recibo.getValorCompra().getImpuestos());
        preparedStatement.setDouble(14, recibo.getValorCompra().getTotal());
    }

    @Override
    public Recibo mapearResultSet(ResultSet resultSet) throws SQLException {
        Recibo recibo = new Recibo();
        recibo.setNumeroRecibo(resultSet.getInt(2));
        recibo.setCorreo(resultSet.getString(3));
        recibo.setFechaCompra(resultSet.getTimestamp(4).toLocalDateTime());
        recibo.setTipoPago(TipoPagoEnum.valueOf(resultSet.getString(5)));
        recibo.setDireccion(resultSet.getString(6));
        recibo.getValorCompra().setDescuentoPremium(resultSet.getDouble(7));
        recibo.getValorCompra().setDescuentoFrecuencia(resultSet.getDouble(8));
        recibo.getValorCompra().setPrecioBaseTotal(resultSet.getDouble(13));
        recibo.getValorCompra().setImpuestos(resultSet.getDouble(14));
        recibo.getValorCompra().setTotal(resultSet.getDouble(15));
        return recibo;
    }
}
