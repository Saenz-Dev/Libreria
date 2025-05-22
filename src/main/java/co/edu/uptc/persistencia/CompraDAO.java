package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import co.edu.uptc.modelo.Recibo;

public class CompraDAO extends ConexionBD<Recibo> {

    @Override
    public void insertarDatos(Recibo recibo) throws SQLException, RuntimeException {
	String sql = "INSERT INTO compras (numero_compra, correo, fecha) VALUES (?, ?, ?)";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	    preparedStatement.setInt(1, recibo.getNumeroRecibo());
	    preparedStatement.setString(2, recibo.getCorreo());
	    preparedStatement.setTimestamp(3, Timestamp.valueOf(recibo.getFecha()));
	    preparedStatement.executeUpdate();
	} catch (SQLException e) {
	    throw new SQLException("❌ Error al insertar los datos en la tabla 'compras': " + e.getMessage());
	}

    }
 
    @Override
    public void actualizarDatos(Recibo objeto) throws SQLException, RuntimeException {}

    @Override
    public Recibo seleccionarRegistro(Recibo recibo) throws SQLException, RuntimeException {
	String sql = "SELECT * FROM compras WHERE fecha = ? AND numero_compra = ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    preparedStatement.setTimestamp(1, Timestamp.valueOf(recibo.getFecha()));
	    preparedStatement.setInt(2, recibo.getNumeroRecibo());
	    try (ResultSet resultSet = preparedStatement.executeQuery()) {
		
		while (resultSet.next()) {
		    recibo.setNumeroRecibo(resultSet.getInt("numero_compra"));
		    recibo.setCorreo(resultSet.getString("correo"));
		    recibo.setFecha(resultSet.getTimestamp("fecha").toLocalDateTime());
		}
		return recibo;
	    }
	}  catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'compras': " + e.getMessage());
        }
    }

    public ArrayList<Recibo> seleccionarRegistros(Recibo recib) throws SQLException, RuntimeException {
	String sql = "SELECT * FROM compras WHERE correo = ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    preparedStatement.setString(1, recib.getCorreo());
	    try (ResultSet resultSet = preparedStatement.executeQuery()) {
		ArrayList<Recibo> listaRecibos = new ArrayList<>();
		if (!resultSet.next()) {
		    throw new RuntimeException("No se encontró la compra con los datos proporcionados.");
		}
		while (resultSet.next()) {
		    Recibo recibo = new Recibo();
		    recibo.setNumeroRecibo(resultSet.getInt("numero_compra"));
		    recibo.setCorreo(resultSet.getString("correo"));
		    recibo.setFecha(resultSet.getTimestamp("fecha").toLocalDateTime());
		    listaRecibos.add(recibo);
		}
		return listaRecibos;
	    }
	}  catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'compras': " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Recibo> seleccionarRegistros() throws SQLException, RuntimeException {
	String sql = "SELECT * FROM compras";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    try (ResultSet resultSet = preparedStatement.executeQuery()) {
		ArrayList<Recibo> listaRecibos = new ArrayList<>();
		while (resultSet.next()) {
		    Recibo recibo = new Recibo();
		    recibo.setNumeroRecibo(resultSet.getInt("numero_compra"));
		    recibo.setCorreo(resultSet.getString("correo"));
		    recibo.setFecha(resultSet.getTimestamp("fecha").toLocalDateTime());
		    listaRecibos.add(recibo);
		}
		return listaRecibos;
	    }
	}  catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'compras': " + e.getMessage());
        }
    }

}
