package co.edu.uptc.persistencia.busqueda;

import co.edu.uptc.contrato.IBusquedaStrategy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BusquedaReciboPorCorreo implements IBusquedaStrategy {

    private String correo;

    public BusquedaReciboPorCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String getSQL() {
        return "SELECT * FROM recibos WHERE correo = ?";
    }

    @Override
    public void ajustarParametro(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, correo);
    }
}
