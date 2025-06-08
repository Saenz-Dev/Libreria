package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.CodigoPremium;
import co.edu.uptc.modelo.Cuenta;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con los códigos premium.
 * Permite insertar, actualizar, consultar y listar códigos premium en la base de datos.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class CodigoDAO extends ConexionBD<CodigoPremium> {

    /**
     * Inserta un nuevo código premium en la base de datos.
     *
     * @param codigo objeto CodigoPremium a insertar
     * @throws SQLException si ocurre un error de base de datos o el código ya existe
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void insertarDatos(CodigoPremium codigo) throws SQLException, RuntimeException {
        String sentencia = "INSERT INTO cod_premium (codigo, usado) VALUES (?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, codigo.getCodigo());
            preparedStatement.setBoolean(2, codigo.getUsado());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Este codigo ya esta registrado.");
        }

    }

    /**
     * Actualiza el estado de uso de un código premium en la base de datos.
     *
     * @param codigo objeto CodigoPremium a actualizar
     * @throws SQLException si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void actualizarDatos(CodigoPremium codigo) throws SQLException, RuntimeException {
        String sentencia = "UPDATE cod_premium SET usado = ? WHERE codigo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setBoolean(1, codigo.getUsado());
            preparedStatement.setString(2, codigo.getCodigo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ocurrió un problema técnico al actualizar el codigo: " + codigo);
        }

    }

    /**
     * Consulta un código premium específico en la base de datos.
     *
     * @param objeto objeto CodigoPremium con el código a buscar
     * @return el objeto CodigoPremium encontrado o null si no existe
     * @throws SQLException si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public CodigoPremium seleccionarRegistro(CodigoPremium objeto) throws SQLException, RuntimeException {
        String sql = "SELECT * FROM cod_premium WHERE codigo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, objeto.getCodigo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CodigoPremium codPremium = new CodigoPremium();
                    codPremium.setCodigo(resultSet.getString(1));
                    codPremium.setUsado(resultSet.getBoolean(2));
                    return codPremium;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Ocurrio un error al buscar el código, intentalo más tarde.");
        }
        return null;
    }

    @Override
    public ArrayList<CodigoPremium> seleccionarRegistros() throws SQLException, RuntimeException {
        String sql = "SELECT * FROM cod_premium";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ArrayList<CodigoPremium> codigos = new ArrayList<>();
                while (resultSet.next()) {
                    CodigoPremium codPremium = new CodigoPremium();
                    codPremium.setCodigo(resultSet.getString(1));
                    codPremium.setUsado(resultSet.getBoolean(2));
                    codigos.add(codPremium);
                }
                return codigos;
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Ocurrio un error al buscar el código, intentalo más tarde.");
        }
    }
}
