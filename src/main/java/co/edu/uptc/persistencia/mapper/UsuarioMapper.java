package co.edu.uptc.persistencia.mapper;

import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.modelo.TipoUsuarioEnum;
import co.edu.uptc.modelo.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioMapper implements IMapper<Usuario> {
    @Override
    public void mapearObjeto(Usuario usuario, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, usuario.getNombre());
        preparedStatement.setString(2, usuario.getDireccionEnvio());
        preparedStatement.setLong(3, usuario.getTelefono());
        preparedStatement.setString(4, usuario.getTipoCliente().toString());
        preparedStatement.setDouble(5, usuario.getDescuentoTipoUsuario());
        preparedStatement.setString(6, usuario.getCuenta().getCorreo());
    }

    @Override
    public Usuario mapearResultSet(ResultSet resultSet) throws SQLException {
        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setNombre(resultSet.getString("nombre"));
        usuarioEncontrado.setDireccionEnvio(resultSet.getString("dirección"));
        usuarioEncontrado.setTelefono(resultSet.getLong("telefono"));
        usuarioEncontrado.setTipoCliente(TipoUsuarioEnum.valueOf(resultSet.getString("cliente")));
        usuarioEncontrado.setDescuentoTipoUsuario(resultSet.getDouble("descuento"));
        usuarioEncontrado.getCuenta().setCorreo(resultSet.getString("correo"));
        return usuarioEncontrado;
    }
}
