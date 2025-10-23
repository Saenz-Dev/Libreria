package co.edu.uptc.persistencia.busqueda;

import co.edu.uptc.contrato.IBusquedaStrategy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BusquedaComentarioPorCorreo implements IBusquedaStrategy {
    private String correo;

    public BusquedaComentarioPorCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String getSQL() {
        return "SELECT * FROM comentarios WHERE correo_usuario = ?";
    }

    @Override
    public void ajustarParametro(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, correo);
    }
}
