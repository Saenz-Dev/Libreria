package co.edu.uptc.persistencia;

import java.sql.Connection;
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
import co.edu.uptc.modelo.CodigoPremium;
import co.edu.uptc.modelo.Cuenta;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con los códigos premium.
 * Permite insertar, actualizar, consultar y listar códigos premium en la base de datos.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class CodigoDAO implements IRepositorio<CodigoPremium> {

    private IConexionBD iConexionBD;
    private IMapper<CodigoPremium> codigoIMapper;

    public CodigoDAO(IConexionBD iConexionBD, IMapper<CodigoPremium> codigoIMapper) {
        this.iConexionBD = iConexionBD;
        this.codigoIMapper = codigoIMapper;
    }

    /**
     * Inserta un nuevo código premium en la base de datos.
     *
     * @param codigo objeto CodigoPremium a insertar
     * @throws RepositorioException si ocurre un error de base de datos o el código ya existe
     * @throws RuntimeException     si ocurre un error de lógica
     */
    @Override
    public void guardar(CodigoPremium codigo) throws RepositorioException {
        String sentencia = "INSERT INTO cod_premium (codigo, usado) VALUES (?, ?)";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            codigoIMapper.mapearObjeto(codigo, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositorioException("Este codigo ya esta registrado.");
        }
    }

    /**
     * Actualiza el estado de uso de un código premium en la base de datos.
     *
     * @param codigo objeto CodigoPremium a actualizar
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void actualizar(CodigoPremium codigo) throws RepositorioException {
        String sentencia = "UPDATE cod_premium SET codigo = ?, usado = ? WHERE codigo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            codigoIMapper.mapearObjeto(codigo, preparedStatement);
            preparedStatement.setString(3, codigo.getCodigo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositorioException("Ocurrió un problema técnico al actualizar el codigo: " + codigo);
        }
    }

    /**
     * Elimina un código premium de la base de datos.
     *
     * @param codigo código a eliminar.
     * @throws RepositorioException si ocurre un error al realizar la eliminación del código.
     */
    @Override
    public void eliminar(CodigoPremium codigo) throws RepositorioException {
        String sql = "DELETE FROM cod_premium WHERE codigo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, codigo.getCodigo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositorioException("Ocurrió un problema técnico al actualizar el codigo: " + codigo);
        }
    }

    /**
     * Consulta un código premium específico en la base de datos.
     *
     * @param objeto objeto CodigoPremium con el código a buscar
     * @return el objeto CodigoPremium encontrado o null si no existe
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public CodigoPremium consultar(CodigoPremium objeto) throws RepositorioException {
        String sql = "SELECT * FROM cod_premium WHERE codigo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, objeto.getCodigo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return codigoIMapper.mapearResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RepositorioException("❌ Ocurrio un error al buscar el código, intentalo más tarde.");
        }
        return null;
    }

    @Override
    public List<CodigoPremium> consultar() throws RepositorioException {
        String sql = "SELECT * FROM cod_premium";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ArrayList<CodigoPremium> codigos = new ArrayList<>();
                while (resultSet.next()) {
                    CodigoPremium codPremium = codigoIMapper.mapearResultSet(resultSet);
                    codigos.add(codPremium);
                }
                return codigos;
            }
        } catch (SQLException e) {
            throw new RepositorioException("❌ Ocurrio un error al buscar el código, intentalo más tarde.");
        }
    }
}
