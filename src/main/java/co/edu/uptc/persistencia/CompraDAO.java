package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Recibo;

public class CompraDAO extends ConexionBD<Recibo> {

    @Override
    public void insertarDatos(Recibo recibo) throws SQLException, RuntimeException {
	String sql = "INSERT INTO compras (numero_compra, correo, fecha) VALUES (?, ?, ?)";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	    preparedStatement.setInt(1, recibo.getNumeroRecibo());
	    preparedStatement.setString(2, recibo.getCorreo());
	    preparedStatement.setTimestamp(3, Timestamp.valueOf(recibo.getFechaCompra()));
	    preparedStatement.executeUpdate();
	    RegistroLog.registrarInfo("Compra " + recibo.getNumeroRecibo()+ " registrada correctamente para el correo: " + recibo.getCorreo());
	} catch (SQLException e) {
	    RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'compras': " + e.getMessage(), e);
	    throw new SQLException("❌ No se pudo registrar la compra. Por favor, intenta de nuevo o contacta soporte.");
	}
    }
 
    @Override
    public void actualizarDatos(Recibo objeto) throws SQLException, RuntimeException {}

    @Override
    public Recibo seleccionarRegistro(Recibo recibo) throws SQLException, RuntimeException {
	String sql = "SELECT * FROM compras WHERE fecha = ? AND numero_compra = ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    preparedStatement.setTimestamp(1, Timestamp.valueOf(recibo.getFechaCompra()));
	    preparedStatement.setInt(2, recibo.getNumeroRecibo());
	    try (ResultSet resultSet = preparedStatement.executeQuery()) {
		
		while (resultSet.next()) {
		    recibo.setNumeroRecibo(resultSet.getInt("numero_compra"));
		    recibo.setCorreo(resultSet.getString("correo"));
		    recibo.setFechaCompra(resultSet.getTimestamp("fecha").toLocalDateTime());
		}
		RegistroLog.registrarInfo("✅ Registro de compra consultado correctamente. Número: " + recibo.getNumeroRecibo());
		return recibo;
	    }
	}  catch (SQLException e) {
	    RegistroLog.registrarError("❌ Error al consultar el registro de compra: " + e.getMessage(), e);
	    throw new SQLException("❌ No se pudo consultar el registro de la compra. Intenta de nuevo más tarde.");
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
		    recibo.setFechaCompra(resultSet.getTimestamp("fecha").toLocalDateTime());
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
		    recibo.setFechaCompra(resultSet.getTimestamp("fecha").toLocalDateTime());
		    listaRecibos.add(recibo);
		}
		 if (listaRecibos.isEmpty()) {
		    RegistroLog.registrarInfo("No se encontraron registros en la tabla 'compras'.");
		}

		return listaRecibos;
	    }
	}  catch (SQLException e) {
	    RegistroLog.registrarError("❌ Error al seleccionar los registros en la tabla 'compras': " + e.getMessage(), e);
	    throw new SQLException("❌ No se pudo buscar las compras: ");
        }
    }

}
