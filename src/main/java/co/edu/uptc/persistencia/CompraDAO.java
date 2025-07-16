package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.contrato.IConexionBD;
import co.edu.uptc.contrato.IRepositorio;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Recibo;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con las compras realizadas en la tienda virtual.
 * Permite insertar, consultar y actualizar registros de compras en la base de datos.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class CompraDAO implements IRepositorio<Recibo> {

    private IConexionBD iConexionBD;

    public CompraDAO(IConexionBD iConexionBD) {
        this.iConexionBD = iConexionBD;
    }

    /**
     * Inserta un nuevo registro de compra en la base de datos.
     *
     * @param recibo objeto Recibo con la información de la compra
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void guardar(Recibo recibo) throws RepositorioException {
        String sql = "INSERT INTO compras (numero_compra, correo, fecha) VALUES (?, ?, ?)";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, recibo.getNumeroRecibo());
            preparedStatement.setString(2, recibo.getCorreo());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(recibo.getFechaCompra()));
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("Compra " + recibo.getNumeroRecibo() + " registrada correctamente para el correo: " + recibo.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'compras': " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo registrar la compra. Por favor, intenta de nuevo o contacta soporte.");
        }
    }

    /**
     * Actualiza un registro de compra en la base de datos (no implementado).
     *
     * @param objeto objeto Recibo a actualizar
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void actualizar(Recibo objeto) throws RepositorioException {
    }


    /**
     * Consulta un registro de compra específico en la base de datos por fecha y número de compra.
     *
     * @param recibo objeto Recibo con los datos de búsqueda (fecha y número)
     * @return el objeto Recibo encontrado o null si no existe
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public Recibo consultar(Recibo recibo) throws RepositorioException {
        String sql = "SELECT * FROM compras WHERE fecha = ? AND numero_compra = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al consultar el registro de compra: " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo consultar el registro de la compra. Intenta de nuevo más tarde.");
        }
    }

    @Override
    public List<Recibo> seleccionarRegistros(Recibo recib) throws RepositorioException {
        String sql = "SELECT * FROM compras WHERE correo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            throw new RepositorioException("❌ Error al seleccionar el registro en la tabla 'compras': " + e.getMessage());
        }
    }

    @Override
    public List<Recibo> consultar() throws RepositorioException {
        String sql = "SELECT * FROM compras";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar los registros en la tabla 'compras': " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo buscar las compras: ");
        }
    }
}
