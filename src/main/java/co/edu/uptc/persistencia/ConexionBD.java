package co.edu.uptc.persistencia;

import co.edu.uptc.contrato.IConexionBD;
import co.edu.uptc.log.RegistroLog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Clase abstracta base para los DAOs que gestiona la conexión y operaciones genéricas con la base de datos MySQL.
 * Proporciona métodos para crear la conexión y define las operaciones CRUD básicas que deben implementar las subclases.
 */
public class ConexionBD implements IConexionBD {

    private final String NOMBRE_BD = "db_libreria";
    /**
     * URL de conexión a la base de datos MySQL.
     */
    private final String URL = "jdbc:mysql://localhost:3306/" + NOMBRE_BD;
    /**
     * Usuario de la base de datos.
     */
    private final String USUARIO = "root";
    /**
     * Contraseña de la base de datos.
     */
    private final String CONTRASENA = "Niosaenz123";

    /**
     * Conexión vinculada a la base de datos.
     */
    private Connection connection;

    public ConexionBD() {
        try {
            connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            RegistroLog.registrarError("Surgió un problema al conectar con la base de datos", e);
            throw new RuntimeException("Surgió un problema en el programa, contactate con el propietario.");
        }
    }

    /**
     * Crea y retorna una nueva conexión a la base de datos.
     *
     * @return conexión a la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
    @Override
    public Connection crearConexion() throws SQLException {
        if (connection == null || connection.isClosed()) {
            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        }
        return connection;
    }

    /**
     * Desconecta la conexión a la base de datos.
     */
    @Override
    public void cerrarConexion() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("Error al cerrar la base de datos", e);
        }
        connection = null;
    }
}
