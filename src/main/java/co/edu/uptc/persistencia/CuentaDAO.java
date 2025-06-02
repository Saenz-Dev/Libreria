package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Cuenta;

public class CuentaDAO extends ConexionBD<Cuenta> {

    @Override
    public void insertarDatos(Cuenta cuenta) throws SQLException, RuntimeException {
        if (cuenta == null) throw new RuntimeException("El cuenta a guardar no tiene datos");
        String sentencia = "INSERT INTO cuentas (correo, contraseña, conectado) VALUES (?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, cuenta.getCorreo());
            preparedStatement.setString(2, cuenta.getContrasena());
            preparedStatement.setBoolean(3, cuenta.isLog());
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("✔ Cuenta insertada correctamente con correo: " + cuenta.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'cuentas': " + e.getMessage(), e);
            throw new SQLException("❌ Error al insertar los datos en la tabla 'cuentas': " + e.getMessage());
        }
    }

    @Override
    public void actualizarDatos(Cuenta cuenta) throws SQLException, RuntimeException {
        if (cuenta == null) throw new RuntimeException("Cuenta vacía");
        String sentencia = "UPDATE cuentas SET contraseña = ?, conectado = ? WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, cuenta.getContrasena());
            preparedStatement.setBoolean(2, cuenta.isLog());
            preparedStatement.setString(3, cuenta.getCorreo());
            int filasActualizadas = preparedStatement.executeUpdate();
            if (filasActualizadas > 0) {
                RegistroLog.registrarInfo("✔ Cuenta actualizada correctamente con correo: " + cuenta.getCorreo());
            } else {
                RegistroLog.registrarAdvertencia("⚠ No se encontró ninguna cuenta con el correo: " + cuenta.getCorreo());
                //throw new RuntimeException("No se encontró una cuenta asociada al correo ingresado.");
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("Error al actualizar los datos en la tabla 'cuentas': " + e.getMessage(), e);
            throw new SQLException("Ocurrió un problema técnico al actualizar la cuenta.");
        }
    }

    @Override
    public Cuenta seleccionarRegistro(Cuenta cuenta) throws SQLException, RuntimeException {

        String sentencia = "SELECT * FROM cuentas WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, cuenta.getCorreo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Cuenta cuentaQuery = new Cuenta();
                    cuentaQuery.setCorreo(resultSet.getString(1));
                    cuentaQuery.setContrasena(resultSet.getString(2));
                    cuentaQuery.setLog(resultSet.getBoolean(3));
                    return cuentaQuery;
                } else {
                    RegistroLog.registrarAdvertencia("Cuenta no encontrada con el correo: " + cuenta.getCorreo());
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error SQL al buscar cuenta con correo " + cuenta.getCorreo() + ": " + e.getMessage(), e);
            throw new SQLException("❌ Error al consultar la base de datos. Intente nuevamente.");
        }
        return null;
    }

    @Override
    public ArrayList<Cuenta> seleccionarRegistros() throws SQLException, RuntimeException {
        ArrayList<Cuenta> cuentas = new ArrayList<>();
        String sentencia = "SELECT * FROM cuentas";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setCorreo(resultSet.getString("correo"));
                cuenta.setContrasena(resultSet.getString("contraseña"));
                cuenta.setLog(resultSet.getBoolean("conectado"));
                cuentas.add(cuenta);
            }
            return cuentas;
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los datos en la tabla 'cuentas': " + e.getMessage());
        }
    }
}
