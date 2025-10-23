package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.contrato.IConexionBD;
import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.contrato.IRepositorio;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Compra;
import co.edu.uptc.modelo.Recibo;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con las compras realizadas en la tienda virtual.
 * Permite insertar, consultar y actualizar registros de compras en la base de datos.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class CompraDAO implements IRepositorio<Compra> {

    private IConexionBD iConexionBD;
    private IMapper<Compra> compraIMapper;

    public CompraDAO(IConexionBD iConexionBD, IMapper<Compra> compraIMapper) {
        this.iConexionBD = iConexionBD;
        this.compraIMapper = compraIMapper;
    }

    /**
     * Inserta un nuevo registro de compra en la base de datos.
     *
     * @param compra objeto Recibo con la información de la compra
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void guardar(Compra compra) throws RepositorioException {
        String sql = "INSERT INTO compras (numero_compra, correo, fecha) VALUES (?, ?, ?)";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            compraIMapper.mapearObjeto(compra, preparedStatement);
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("Compra " + compra.getNumeroCompra() + " registrada correctamente para el correo: " + compra.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'compras': " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo registrar la compra. Por favor, intenta de nuevo o contacta soporte.");
        }
    }

    /**
     * Actualiza un registro de compra en la base de datos (no implementado).
     *
     * @param compra objeto {@code Compra} a actualizar
     * @throws RuntimeException si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void actualizar(Compra compra) throws RepositorioException {
        String sql = "UPDATE compras SET numero_compra = ?, correo = ?, fecha = ? WHERE numero_compra = ?";
        try (Connection con = iConexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            compraIMapper.mapearObjeto(compra, ps);
            ps.setInt(3, compra.getNumeroCompra());
            ps.executeUpdate();
            RegistroLog.registrarInfo("Compra " + compra.getNumeroCompra() + " ACTUALIZADA correctamente para el correo: " + compra.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al actualizar los datos en la tabla 'compras': " + e.getMessage(), e);
            throw new RepositorioException("No se pudo realizar la actualización del recibo. Por favor contacta a soporte.");
        }
    }

    /**
     * Elimina un recibo de compra de la base de datos.
     *
     * @param compra {@code Compra} a eliminar.
     * @throws RepositorioException excepción si ocurre un error al realizar la actualización en la base de datos.
     */
    @Override
    public void eliminar(Compra compra) throws RepositorioException {
        String sql = "DELETE FROM compras WHERE numero_compra = ?";
        try (Connection con = iConexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, compra.getNumeroCompra());
            ps.executeUpdate();
            RegistroLog.registrarInfo("Compra " + compra.getNumeroCompra() + " ELIMINADA correctamente para el correo: " + compra.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al actualizar los datos en la tabla 'compras': " + e.getMessage(), e);
            throw new RepositorioException("No se pudo realizar la actualización del recibo. Por favor contacta a soporte.");
        }
    }


    /**
     * Consulta un registro de compra específico en la base de datos por fecha y número de compra.
     *
     * @param compra objeto {@code Compra} con los datos de búsqueda (fecha y número)
     * @return el objeto Recibo encontrado o null si no existe
     * @throws RepositorioException si ocurre un error de base de datos
     * @throws RuntimeException     si ocurre un error de lógica
     */
    @Override
    public Compra consultar(Compra compra) throws RepositorioException {
        String sql = "SELECT * FROM compras WHERE fecha = ? AND numero_compra = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(compra.getFecha()));
            preparedStatement.setInt(2, compra.getNumeroCompra());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    compraIMapper.mapearResultSet(resultSet);
                }
                RegistroLog.registrarInfo("✅ Registro de compra consultado correctamente. Número: " + compra.getNumeroCompra());
                return compra;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al consultar el registro de compra: " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo consultar el registro de la compra. Intenta de nuevo más tarde.");
        }
    }

    /**
     * Consulta la lista de recibos generales de la base de datos.
     *
     * @return lista de recibos de la base de datos.
     * @throws RepositorioException excepción si surgen errores al realizar la consulta.
     */
    @Override
    public List<Compra> consultar() throws RepositorioException {
        String sql = "SELECT * FROM compras";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ArrayList<Compra> listaRecibos = new ArrayList<>();
                while (resultSet.next()) {
                    Compra compra = compraIMapper.mapearResultSet(resultSet);
                    listaRecibos.add(compra);
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
