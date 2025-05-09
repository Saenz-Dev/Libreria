package co.edu.uptc.persistencia;

import co.edu.uptc.modelo.Cuenta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CuentaDAO extends ConexionBD<Cuenta> {

    @Override
    public void crearTabla() throws SQLException {
        String sentencia = "CREATE TABLE IF NOT EXISTS cuentas (correo VARCHAR(50) PRIMARY KEY, contraseña VARCHAR(30), conectado BOOLEAN)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al crear la tabla 'cuentas': " + e.getMessage());
        }
    }

    @Override
    public void insertarDatos(Cuenta cuenta) throws SQLException, RuntimeException {
        if (cuenta == null) throw new RuntimeException("El cuenta a guardar no tiene datos");
        String sentencia = "INSERT INTO cuentas (correo, contraseña, conectado) VALUES (?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, cuenta.getCorreo());
            preparedStatement.setString(2, cuenta.getContrasena());
            preparedStatement.setBoolean(3, cuenta.isLog());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
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
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al actualizar los datos en la tabla 'cuentas': " + e.getMessage());
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
                }
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el dato en la tabla 'cuentas': " + e.getMessage());
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
