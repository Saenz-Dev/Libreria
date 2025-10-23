package co.edu.uptc.persistencia.mapper;

import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.modelo.Comentario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class ComentarioMapper implements IMapper<Comentario> {
    @Override
    public void mapearObjeto(Comentario comentario, PreparedStatement preparedStatement) throws SQLException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        comentario.getFecha().format(dateFormat);
        preparedStatement.setLong(1, Long.parseLong(comentario.getIsbn()));
        preparedStatement.setString(2, comentario.getCorreo());
        preparedStatement.setString(3, comentario.getComentario());
        preparedStatement.setInt(4, comentario.getCalificacion());
        preparedStatement.setTimestamp(5, Timestamp.valueOf(comentario.getFecha()));
    }

    @Override
    public Comentario mapearResultSet(ResultSet resultSet) throws SQLException {
        Comentario comentario = new Comentario();
        comentario.setIsbn(resultSet.getString("isbn_libro"));
        comentario.setCorreo(resultSet.getString("correo_usuario"));
        comentario.setComentario(resultSet.getString("comentario"));
        comentario.setCalificacion(resultSet.getInt("calificacion"));
        comentario.setFecha(resultSet.getTimestamp("fecha").toLocalDateTime());
        return comentario;
    }
}
