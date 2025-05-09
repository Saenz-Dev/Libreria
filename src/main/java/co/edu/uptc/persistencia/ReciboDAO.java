package co.edu.uptc.persistencia;

import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.negocio.TipoPago;

import java.sql.*;
import java.util.ArrayList;

public class ReciboDAO extends ConexionBD<Recibo>{

    @Override
    public void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS recibos (" +
                "numero_recibo INT PRIMARY KEY" +
                "correo VARCHAR(50) NOT NULL, " +
                "usuario VARCHAR(50) NOT NULL, " +
                "fecha DATETIME NOT NULL, " +
                "tipo_pago VARCHAR(20) NOT NULL, " +
                "direccion VARCHAR(100) NOT NULL, " +
                "FOREIGN KEY (correo) REFERENCES usuarios(correo), " +
                "FOREIGN KEY (usuario) REFERENCES usuarios(nombre))";

        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al crear la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public void insertarDatos(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "INSERT INTO recibos (numero_recibo, correo, usuario, fecha, tipo_pago, direccion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, recibo.getNumeroRecibo());
            preparedStatement.setString(2, recibo.getCorreo());
            preparedStatement.setString(3, recibo.getNombreUser());
            preparedStatement.setDate(4, Date.valueOf(recibo.getFecha()));
            preparedStatement.setString(5, String.valueOf(recibo.getTipoPago()));
            preparedStatement.setString(6, recibo.getDireccion());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al insertar los datos en la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public void actualizarDatos(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "UPDATE recibos SET correo = ?, usuario = ?, fecha = ?, tipo_pago = ?, direccion = ? WHERE numero_recibo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, recibo.getCorreo());
            preparedStatement.setString(2, recibo.getNombreUser());
            preparedStatement.setDate(3, Date.valueOf(recibo.getFecha()));
            preparedStatement.setString(4, String.valueOf(recibo.getTipoPago()));
            preparedStatement.setString(5, recibo.getDireccion());
            preparedStatement.setInt(6, recibo.getNumeroRecibo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al actualizar los datos en la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public Recibo seleccionarRegistro(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "SELECT * FROM recibos WHERE numero_recibo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, recibo.getNumeroRecibo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Recibo reciboQuery = new Recibo();
                    reciboQuery.setNumeroRecibo(resultSet.getInt(1));
                    reciboQuery.setCorreo(resultSet.getString(2));
                    reciboQuery.setNombreUser(resultSet.getString(3));
                    reciboQuery.setFecha(resultSet.getDate(4).toString());
                    reciboQuery.setTipoPago(TipoPago.valueOf(resultSet.getString(5)));
                    reciboQuery.setDireccion(resultSet.getString(6));
                    return reciboQuery;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Recibo> seleccionarRegistros() throws SQLException, RuntimeException {

        String sql = "SELECT * FROM recibos";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            ArrayList<Recibo> recibos = new ArrayList<>();
            while (resultSet.next()) {
                Recibo recibo = new Recibo();
                recibo.setNumeroRecibo(resultSet.getInt(1));
                recibo.setCorreo(resultSet.getString(2));
                recibo.setNombreUser(resultSet.getString(3));
                recibo.setFecha(resultSet.getDate(4).toString());
                recibo.setTipoPago(TipoPago.valueOf(resultSet.getString(5)));
                recibo.setDireccion(resultSet.getString(6));
                recibos.add(recibo);
            }
            return recibos;
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los registros en la tabla 'recibos': " + e.getMessage());
        }
    }
}
