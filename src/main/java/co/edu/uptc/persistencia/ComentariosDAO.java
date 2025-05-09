package co.edu.uptc.persistencia;

import co.edu.uptc.modelo.Comentario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComentariosDAO extends ConexionBD<Comentario> {

    @Override
    public void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS comentarios (" + "id INT PRIMARY KEY AUTO_INCREMENT, " + "isbn_libro BIGINT NOT NULL, " + "correo_usuario VARCHAR(50) NOT NULL, " + "comentario TEXT NOT NULL, " + "fecha DATETIME NOT NULL, " + "FOREIGN KEY (isbn_libro) REFERENCES libros(isbn), " + "FOREIGN KEY (correo_usuario) REFERENCES usuarios(correo))";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al crear la tabla 'comentarios': " + e.getMessage());
        }
    }

    @Override
    public void insertarDatos(Comentario comentario) throws SQLException {
        String sql = "INSERT INTO comentarios (isbn_libro, correo_usuario, comentario, fecha) VALUES (?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, Long.parseLong(comentario.getIsbn()));
            preparedStatement.setString(2, comentario.getCorreo());
            preparedStatement.setString(3, comentario.getComentario());
            preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(comentario.getFecha()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al insertar los datos en la tabla 'comentarios': " + e.getMessage());
        }
    }

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
                comentario.setFecha(resultSet.getTimestamp("fecha").toString());
                comentarios.add(comentario);
            }
            return comentarios;
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los registros en la tabla 'comentarios': " + e.getMessage());
        }
    }

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
                    comentario.setFecha(resultSet.getTimestamp("fecha").toString());
                    comentarios.add(comentario);
                }
                return comentarios;
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los comentarios del libro con ISBN " + isbnLibro + ": " + e.getMessage());
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
