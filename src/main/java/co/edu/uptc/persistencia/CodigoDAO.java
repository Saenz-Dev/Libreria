package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.CodigoPremium;
import co.edu.uptc.modelo.Cuenta;

public class CodigoDAO extends ConexionBD<CodigoPremium> {

    @Override
    public void insertarDatos(CodigoPremium codigo) throws SQLException, RuntimeException {
	String sentencia = "INSERT INTO cod_premium (codigo, usado) VALUES (?, ?)";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
	    preparedStatement.setString(1, codigo.getCodigo());
	    preparedStatement.setBoolean(2, codigo.getUsado());
	    preparedStatement.executeUpdate();
	} catch (SQLException e) {
	    throw new SQLException("❌ Ocurrio un problema al agregar un codigo a la Base de Datos.");
	}

    }

    @Override
    public void actualizarDatos(CodigoPremium codigo) throws SQLException, RuntimeException {
	String sentencia = "UPDATE cod_premium SET usado = ? WHERE codigo = ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
	    preparedStatement.setBoolean(1, codigo.getUsado());
	    preparedStatement.setString(2, codigo.getCodigo());
	    preparedStatement.executeUpdate();
	} catch (SQLException e) {
	    throw new SQLException("Ocurrió un problema técnico al actualizar el codigo: " + codigo);
	}

    }

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
	// TODO Auto-generated method stub
	return null;
    }

}
