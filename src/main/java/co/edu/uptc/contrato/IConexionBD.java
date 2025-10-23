package co.edu.uptc.contrato;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConexionBD {
    Connection crearConexion() throws SQLException;
    public void cerrarConexion() throws SQLException;
}
