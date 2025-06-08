package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Comentario;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con los comentarios de los libros.
 * Permite insertar, consultar y listar comentarios en la base de datos.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class ComentarioDAO extends ConexionBD<Comentario> {

    /**
     * Inserta un nuevo comentario en la base de datos.
     *
     * @param comentario objeto Comentario a insertar
     * @throws SQLException si ocurre un error de base de datos
     */
    @Override
    public void insertarDatos(Comentario comentario) throws SQLException {
        String sql = "INSERT INTO comentarios (isbn_libro, correo_usuario, comentario, calificacion, fecha) VALUES (?, ?, ?, ?, ?)";        
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            comentario.getFecha().format(dateFormat);
            preparedStatement.setLong(1, Long.parseLong(comentario.getIsbn()));
            preparedStatement.setString(2, comentario.getCorreo());
            preparedStatement.setString(3, comentario.getComentario());
            preparedStatement.setInt(4, comentario.getCalificacion());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(comentario.getFecha()));
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("Comentario insertado correctamente para ISBN " + comentario.getIsbn()
            + ", usuario: " + comentario.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'comentarios':" +  e.getMessage(), e);
            throw new SQLException("❌ No se pudo guardar el comentario. Intenta nuevamente.");
        }
    }

    /**
     * Obtiene todos los comentarios registrados en la base de datos.
     *
     * @return lista de comentarios encontrados
     * @throws SQLException si ocurre un error de base de datos
     */
    @Override
    public ArrayList<Comentario> seleccionarRegistros() throws SQLException {
        String sql = "SELECT * FROM comentarios";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
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
            throw new SQLException("❌ No se pudieron obtener los comentarios. Intenta nuevamente");
        }
    }

    /**
     * Obtiene los comentarios registrados en la base de datos para un libro específico.
     *
     * @param isbnLibro ISBN del libro cuyos comentarios se desean obtener
     * @return lista de comentarios del libro especificado
     * @throws SQLException si ocurre un error de base de datos
     */
    public ArrayList<Comentario> seleccionarComentariosPorLibro(String isbnLibro) throws SQLException {
        String sql = "SELECT * FROM comentarios WHERE isbn_libro = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, Long.parseLong(isbnLibro));
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
            RegistroLog.registrarError("❌ Error al seleccionar los comentarios del libro con ISBN " + isbnLibro + ": " + e.getMessage(), e);
            throw new SQLException("❌ No se pudieron obtener los comentarios del libro. Intenta nuevamente.");
        
        }
    }

    @Override
    public void actualizarDatos(Comentario objeto) throws SQLException, RuntimeException {
    }

    @Override
    public Comentario seleccionarRegistro(Comentario objeto) throws SQLException, RuntimeException {
        return null;
    }
}
