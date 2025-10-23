package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.contrato.IConexionBD;
import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.contrato.IRepositorio;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Cuenta;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con las cuentas de usuario.
 * Permite insertar, actualizar, consultar y eliminar cuentas en la base de datos.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class CuentaDAO implements IRepositorio<Cuenta> {

    private IConexionBD iConexionBD;
    private IMapper<Cuenta> cuentaIMapper;

    public CuentaDAO(IConexionBD iConexionBD, IMapper<Cuenta> cuentaIMapper) {
        this.iConexionBD = iConexionBD;
        this.cuentaIMapper = cuentaIMapper;
    }

    /**
     * Inserta una nueva cuenta en la base de datos.
     *
     * @param cuenta objeto Cuenta a insertar
     * @throws RepositorioException si ocurre un error de base de datos.
     * @throws RuntimeException     si la cuenta es nula.
     */
    @Override
    public void guardar(Cuenta cuenta) throws RepositorioException {
        if (cuenta == null) throw new RuntimeException("El cuenta a guardar no tiene datos");
        String sentencia = "INSERT INTO cuentas (correo, contraseña, conectado) VALUES (?, ?, ?)";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            cuentaIMapper.mapearObjeto(cuenta, preparedStatement);
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("✔ Cuenta insertada correctamente con correo: " + cuenta.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'cuentas': " + e.getMessage(), e);
            throw new RepositorioException("❌ Error al insertar los datos en la tabla 'cuentas': " + e.getMessage());
        }
    }

    /**
     * Selecciona un registro de cuenta en la base de datos según el correo proporcionado.
     *
     * @param cuenta objeto Cuenta con el correo a buscar
     * @return objeto Cuenta con los datos encontrados, o null si no se encuentra ninguna cuenta
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si el correo es nulo
     */
    @Override
    public Cuenta consultar(Cuenta cuenta) throws RepositorioException {
        String sentencia = "SELECT * FROM cuentas WHERE correo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, cuenta.getCorreo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return cuentaIMapper.mapearResultSet(resultSet);
                } else {
                    RegistroLog.registrarAdvertencia("Cuenta no encontrada con el correo: " + cuenta.getCorreo());
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error SQL al buscar cuenta con correo " + cuenta.getCorreo() + ": " + e.getMessage(), e);
            throw new RepositorioException("❌ Error al consultar la base de datos. Intente nuevamente.");
        }
        return null;
    }

    /**
     * Selecciona todos los registros de cuentas en la base de datos.
     *
     * @return lista de objetos Cuenta con todos los datos de cuentas
     * @throws SQLException si ocurre un error de base de datos
     */
    @Override
    public List<Cuenta> consultar() throws RepositorioException {
        ArrayList<Cuenta> cuentas = new ArrayList<>();
        String sentencia = "SELECT * FROM cuentas";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Cuenta cuenta = cuentaIMapper.mapearResultSet(resultSet);
                cuentas.add(cuenta);
            }
            return cuentas;
        } catch (SQLException e) {
            throw new RepositorioException("❌ Error al seleccionar los datos en la tabla 'cuentas': " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos de una cuenta existente en la base de datos.
     *
     * @param cuenta objeto Cuenta a actualizar
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si la cuenta es nula
     */
    @Override
    public void actualizar(Cuenta cuenta) throws RepositorioException {
        if (cuenta == null) throw new RuntimeException("Cuenta vacía");
        String sentencia = "UPDATE cuentas SET correo = ?, contraseña = ?, conectado = ? WHERE correo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            cuentaIMapper.mapearObjeto(cuenta, preparedStatement);
            preparedStatement.setString(4, cuenta.getCorreo());
            int filasActualizadas = preparedStatement.executeUpdate();
            if (filasActualizadas > 0) {
                RegistroLog.registrarInfo("✔ Cuenta actualizada correctamente con correo: " + cuenta.getCorreo());
            } else {
                RegistroLog.registrarAdvertencia("⚠ No se encontró ninguna cuenta con el correo: " + cuenta.getCorreo());
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("Error al actualizar los datos en la tabla 'cuentas': " + e.getMessage(), e);
            throw new RepositorioException("Ocurrió un problema técnico al actualizar la cuenta.");
        }
    }

    /**
     * Elimina un registro de cuenta en la base de datos según el correo proporcionado.
     *
     * @param cuenta la cuenta que contiene el correo de la cuenta a eliminar
     */
    @Override
    public void eliminar(Cuenta cuenta) throws RepositorioException {
        String sentencia = "DELETE FROM cuentas WHERE correo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, cuenta.getCorreo());
            int filasEliminadas = preparedStatement.executeUpdate();
            if (filasEliminadas > 0) {
                RegistroLog.registrarInfo("✔ Cuenta eliminada correctamente con correo: " + cuenta.getCorreo());
            } else {
                RegistroLog.registrarAdvertencia("⚠ No se encontró ninguna cuenta con el correo: " + cuenta.getCorreo());
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al eliminar la cuenta con correo " + cuenta.getCorreo() + ": " + e.getMessage(), e);
        }
    }
}
