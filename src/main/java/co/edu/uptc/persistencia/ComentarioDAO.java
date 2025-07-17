package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

import co.edu.uptc.contrato.IBusquedaStrategy;
import co.edu.uptc.contrato.IConexionBD;
import co.edu.uptc.contrato.IRepositorio;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Comentario;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con los comentarios de los libros.
 * Permite insertar, consultar y listar comentarios en la base de datos.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class ComentarioDAO implements IRepositorio<Comentario> {

    private IConexionBD iConexionBD;

    public ComentarioDAO(IConexionBD iConexionBD) {
        this.iConexionBD = iConexionBD;
    }

    /**
     * Inserta un nuevo comentario en la base de datos.
     *
     * @param comentario objeto Comentario a insertar
     * @throws SQLException si ocurre un error de base de datos
     */
    @Override
    public void guardar(Comentario comentario) throws RepositorioException {
        String sql = "INSERT INTO comentarios (isbn_libro, correo_usuario, comentario, calificacion, fecha) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            comentario.getFecha().format(dateFormat);
            preparedStatement.setLong(1, Long.parseLong(comentario.getIsbn()));
            preparedStatement.setString(2, comentario.getCorreo());
            preparedStatement.setString(3, comentario.getComentario());
            preparedStatement.setInt(4, comentario.getCalificacion());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(comentario.getFecha()));
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("Comentario insertado correctamente para ISBN " + comentario.getIsbn() + ", usuario: " + comentario.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'comentarios':" + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo guardar el comentario. Intenta nuevamente.");
        }
    }

    /**
     * Obtiene todos los comentarios registrados en la base de datos.
     *
     * @return lista de comentarios encontrados
     * @throws SQLException si ocurre un error de base de datos
     */
    @Override
    public List<Comentario> consultar() throws RepositorioException {
        String sql = "SELECT * FROM comentarios";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            ArrayList<Comentario> comentarios = new ArrayList<>();
            while (resultSet.next()) {
                Comentario comentario = new Comentario();
                comentario.setIsbn(resultSet.getString("isbn_libro"));
                comentario.setCorreo(resultSet.getString("correo_usuario"));
                comentario.setComentario(resultSet.getString("comentario"));
                comentario.setCalificacion(resultSet.getInt("calificacion"));
                comentario.setFecha(resultSet.getTimestamp("fecha").toLocalDateTime());
                comentarios.add(comentario);
            }
            if (comentarios.isEmpty()) {
                RegistroLog.registrarInfo("No se encontraron comentarios en el libro");
            } else {
                RegistroLog.registrarInfo("✅ Se encontraron " + comentarios.size() + " comentarios.");
            }
            return comentarios;
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar los registros en la tabla 'comentarios': " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudieron obtener los comentarios. Intenta nuevamente");
        }
    }

    /**
     * Obtiene los comentarios registrados en la base de datos para un libro específico.
     *
     * @param busquedaStrategy estrategia para la búsqueda del comentario.
     * @return lista de comentarios del libro especificado
     * @throws SQLException si ocurre un error de base de datos
     */
    public ArrayList<Comentario> seleccionarComentariosPorLibro(IBusquedaStrategy busquedaStrategy) throws SQLException {
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(busquedaStrategy.getSQL())) {
            busquedaStrategy.ajustarParametro(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ArrayList<Comentario> comentarios = new ArrayList<>();
                while (resultSet.next()) {
                    Comentario comentario = new Comentario();
                    comentario.setIsbn(resultSet.getString("isbn_libro"));
                    comentario.setCorreo(resultSet.getString("correo_usuario"));
                    comentario.setComentario(resultSet.getString("comentario"));
                    comentario.setCalificacion(resultSet.getInt("calificacion"));
                    comentario.setFecha(resultSet.getTimestamp("fecha").toLocalDateTime());
                    comentarios.add(comentario);
                }
                return comentarios;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar los comentarios de un libro." + e.getMessage(), e);
            throw new SQLException("❌ No se pudieron obtener los comentarios del libro. Intenta nuevamente.");
        }
    }

    @Override
    public void actualizar(Comentario comentario) throws RepositorioException {
        String sql = "UPDATE comentarios SET comentario = ?, calificacion = ? WHERE isbn_libro = ? AND correo_usuario = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, comentario.getComentario());
            preparedStatement.setInt(2, comentario.getCalificacion());
            preparedStatement.setLong(3, Long.parseLong(comentario.getIsbn()));
            preparedStatement.setString(4, comentario.getCorreo());
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("Comentario actualizado correctamente para ISBN " + comentario.getIsbn() + ", usuario: " + comentario.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'comentarios':" + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo guardar el comentario. Intenta nuevamente.");
        }
    }

    @Override
    public void eliminar(Comentario comentario) throws RepositorioException {
        String sql = "DELETE FROM comentarios WHERE isbn_libro = ? AND correo_usuario = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, Long.parseLong(comentario.getIsbn()));
            preparedStatement.setString(2, comentario.getCorreo());
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("Se ha eliminado EXITOSAMENTE el comentario del usuario: " + comentario.getCorreo() + " del libro: " + comentario.getIsbn());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar los comentarios del libro con ISBN " + comentario.getIsbn() + ": " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudieron ELIMINAR los comentarios del libro. Intenta nuevamente.");
        }
    }

    @Override
    public Comentario consultar(Comentario comentario) throws RepositorioException {
        String sql = "SELECT * FROM comentarios WHERE isbn_libro = ? AND correo_usuario = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            preparedStatement.setLong(1, Long.parseLong(comentario.getIsbn()));
            preparedStatement.setString(2, comentario.getCorreo());
            if (resultSet.next()) {
                comentario.setIsbn(resultSet.getString("isbn_libro"));
                comentario.setCorreo(resultSet.getString("correo_usuario"));
                comentario.setComentario(resultSet.getString("comentario"));
                comentario.setCalificacion(resultSet.getInt("calificacion"));
                comentario.setFecha(resultSet.getTimestamp("fecha").toLocalDateTime());
                RegistroLog.registrarInfo("✅ Se encontró el comentario del libro: " + comentario.getIsbn() + " del usuario: " + comentario.getCorreo() + ".");
                return comentario;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar los registros en la tabla 'comentarios': " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudieron obtener los comentarios. Intenta nuevamente");
        }
        return null;
    }
}
