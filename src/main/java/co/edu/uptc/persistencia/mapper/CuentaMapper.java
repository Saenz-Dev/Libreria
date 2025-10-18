package co.edu.uptc.persistencia.mapper;

import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.modelo.Cuenta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuentaMapper implements IMapper<Cuenta> {
    @Override
    public void mapearObjeto(Cuenta cuenta, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, cuenta.getCorreo());
        preparedStatement.setString(2, cuenta.getContrasena());
        preparedStatement.setBoolean(3, cuenta.isLog());
    }

    @Override
    public Cuenta mapearResultSet(ResultSet resultSet) throws SQLException {
        Cuenta cuenta = new Cuenta();
        cuenta.setCorreo(resultSet.getString("correo"));
        cuenta.setContrasena(resultSet.getString("contraseña"));
        cuenta.setLog(resultSet.getBoolean("conectado"));
        return cuenta;
    }
}
