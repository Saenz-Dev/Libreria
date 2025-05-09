package co.edu.uptc.persistencia;

import co.edu.uptc.modelo.Cuenta;
import co.edu.uptc.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDAO extends ConexionBD<Usuario> {

    @Override
    public void crearTabla() throws SQLException {
        String sentencia = "CREATE TABLE IF NOT EXISTS usuarios (nombre VARCHAR(50) NOT NULL, dirección VARCHAR(50) NOT NULL, telefono BIGINT NOT NULL, cliente VARCHAR(15) NOT NUL, descuento DOUBLE NOT NULL, correo VARCHAR(50), FOREIGN KEY (correo) REFERENCES cuentas(correo))";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al crear la tabla 'usuarios': " + e.getMessage());
        }
    }

    @Override
    public void insertarDatos(Usuario usuario) throws SQLException, RuntimeException {
        if (usuario == null) throw new RuntimeException("El usuario a guardar no tiene datos");
        String sentencia = "INSERT INTO usuarios (nombre, dirección, telefono, cliente, descuento) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getDireccionEnvio());
            preparedStatement.setLong(3, usuario.getTelefono());
            preparedStatement.setString(4, usuario.getTipoCliente());
            preparedStatement.setDouble(5, usuario.getDescuentoTipoUsuario());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al insertar los datos en la tabla 'usuarios': " + e.getMessage());
        }
    }

    @Override
    public void actualizarDatos(Usuario usuario) throws SQLException, RuntimeException {
        if (usuario == null) throw new RuntimeException("Usuario null");
        String sentencia = "UPDATE usuarios SET nombre = ?, dirección = ?, telefono = ?, cliente = ?, descuento = ? WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getDireccionEnvio());
            preparedStatement.setLong(3, usuario.getTelefono());
            preparedStatement.setString(4, usuario.getTipoCliente());
            preparedStatement.setDouble(5, usuario.getDescuentoTipoUsuario());
            preparedStatement.setString(6, usuario.getCuenta().getCorreo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al actualizar los datos en la tabla 'usuarios': " + e.getMessage());
        }
    }

    @Override
    public Usuario seleccionarRegistro(Usuario usuario) throws SQLException, RuntimeException {
        if (usuario == null) throw new RuntimeException("Usuario null");
        String sentencia = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, usuario.getCuenta().getCorreo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuarioEncontrado = new Usuario();
                    usuarioEncontrado.setNombre(resultSet.getString("nombre"));
                    usuarioEncontrado.setDireccionEnvio(resultSet.getString("dirección"));
                    usuarioEncontrado.setTelefono(resultSet.getLong("telefono"));
                    usuarioEncontrado.setTipoCliente(resultSet.getString("cliente"));
                    usuarioEncontrado.setDescuentoTipoUsuario(resultSet.getDouble("descuento"));
                    usuarioEncontrado.getCuenta().setCorreo(resultSet.getString("correo"));
                    return usuarioEncontrado;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los datos en la tabla 'usuarios': " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Usuario> seleccionarRegistros() throws SQLException, RuntimeException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sentencia = "SELECT * FROM usuarios";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setDireccionEnvio(resultSet.getString("dirección"));
                usuario.setTelefono(resultSet.getLong("telefono"));
                usuario.setTipoCliente(resultSet.getString("cliente"));
                usuario.setDescuentoTipoUsuario(resultSet.getDouble("descuento"));
                usuario.getCuenta().setCorreo(resultSet.getString("correo"));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los datos en la tabla 'cuentas': " + e.getMessage());
        }
    }
}
