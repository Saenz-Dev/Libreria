package co.edu.uptc.persistencia;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.TipoUsuarioEnum;
import co.edu.uptc.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase DAO (Data Access Object) encargada de realizar operaciones CRUD
 * sobre la tabla 'usuarios' de la base de datos.
 * Extiende de {@code ConexionBD<Usuario>} para utilizar las utilidades de conexión.
 */
public class UsuarioDAO extends ConexionBD<Usuario> {

    /**
     * Inserta un nuevo registro de usuario en la base de datos.
     *
     * @param usuario Objeto {@code Usuario} con los datos a insertar.
     * @throws SQLException     Si ocurre un error al acceder a la base de datos.
     * @throws RuntimeException Si el objeto usuario es nulo.
     */
    @Override
    public void insertarDatos(Usuario usuario) throws SQLException, RuntimeException {
        if (usuario == null) {
            throw new RuntimeException("El usuario a guardar no tiene datos");
        }
        String sentencia = "INSERT INTO usuarios (nombre, dirección, telefono, cliente, descuento, correo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getDireccionEnvio());
            preparedStatement.setLong(3, usuario.getTelefono());
            preparedStatement.setString(4, usuario.getTipoCliente().toString());
            preparedStatement.setDouble(5, usuario.getDescuentoTipoUsuario());
            preparedStatement.setString(6, usuario.getCuenta().getCorreo());
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                RegistroLog.registrarInfo("✅ Usuario insertado correctamente: " + usuario.getNombre() + " - " + usuario.getCuenta().getCorreo());
            } else {
                RegistroLog.registrarInfo("⚠️ No se insertó el usuario: " + usuario.getNombre() + " - " + usuario.getCuenta().getCorreo());
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'usuarios': " + e.getMessage(), e);
            throw new SQLException("❌ Error al guardar el usuario. Intenta nuevamente más tarde.");
        }
    }

    /**
     * Actualiza los datos de un usuario existente en la base de datos.
     *
     * @param usuario Objeto {@code Usuario} con los datos a actualizar.
     * @throws SQLException     Si ocurre un error al acceder a la base de datos.
     * @throws RuntimeException Si el objeto usuario es nulo.
     */
    @Override
    public void actualizarDatos(Usuario usuario) throws SQLException, RuntimeException {
        if (usuario == null) {
            RegistroLog.registrarAdvertencia("No se puede guardar un usuario nulo.");
            throw new RuntimeException("No se proporcionaron datos del usuario para guardar. Por favor, complete toda la información requerida.");
        }
        String sentencia = "UPDATE usuarios SET nombre = ?, dirección = ?, telefono = ?, cliente = ?, descuento = ? WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getDireccionEnvio());
            preparedStatement.setLong(3, usuario.getTelefono());
            preparedStatement.setString(4, usuario.getTipoCliente().toString());
            preparedStatement.setDouble(5, usuario.getDescuentoTipoUsuario());
            preparedStatement.setString(6, usuario.getCuenta().getCorreo());
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                RegistroLog.registrarError("Se ha actualizado exitosamente el usuario " + usuario.getCuenta().getCorreo() + ", fueron afectadas " + filasAfectadas + " filas, ");
            } else {
                RegistroLog.registrarError("No se actualizó el usuario: " + usuario.getNombre() + " - " + usuario.getCuenta().getCorreo());
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al actualizar los datos en la tabla 'usuarios': " + e.getMessage(), e);
            throw new SQLException("No fue posible actualizar la cuenta, intentalo más tarde.");
        }
    }

    /**
     * Selecciona un registro de usuario por su correo electrónico.
     *
     * @param usuario Objeto {@code Usuario} con el correo a buscar.
     * @return Usuario encontrado o null si no existe.
     * @throws SQLException     Si ocurre un error al acceder a la base de datos.
     * @throws RuntimeException Si el correo del usuario es nulo.
     */
    @Override
    public Usuario seleccionarRegistro(Usuario usuario) throws SQLException, RuntimeException {
        if (usuario.getCuenta().getCorreo() == null) throw new RuntimeException("No ha ingresado algún usuario.");
        String sentencia = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, usuario.getCuenta().getCorreo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuarioEncontrado = new Usuario();
                    usuarioEncontrado.setNombre(resultSet.getString("nombre"));
                    usuarioEncontrado.setDireccionEnvio(resultSet.getString("dirección"));
                    usuarioEncontrado.setTelefono(resultSet.getLong("telefono"));
                    usuarioEncontrado.setTipoCliente(TipoUsuarioEnum.valueOf(resultSet.getString("cliente")));
                    usuarioEncontrado.setDescuentoTipoUsuario(resultSet.getDouble("descuento"));
                    usuarioEncontrado.getCuenta().setCorreo(resultSet.getString("correo"));
                    RegistroLog.registrarInfo("✅ Usuario encontrado: " + usuarioEncontrado.getCuenta().getCorreo());
                    return usuarioEncontrado;
                } else {
                    RegistroLog.registrarInfo("No se encontró usuario con correo: " + usuario.getCuenta().getCorreo());
                    return null;
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar los datos en la tabla 'usuarios': " + e.getMessage(), e);
            throw new SQLException("Ocurrió un problema al buscar el usuario. Por favor, intente más tarde.");
        }
    }

    /**
     * Selecciona todos los registros de usuarios en la base de datos.
     *
     * @return Lista de usuarios encontrados.
     * @throws SQLException     Si ocurre un error al acceder a la base de datos.
     * @throws RuntimeException Si ocurre un error al procesar los datos.
     */
    @Override
    public ArrayList<Usuario> seleccionarRegistros() throws SQLException, RuntimeException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sentencia = "SELECT * FROM usuarios ORDER BY nombre";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setDireccionEnvio(resultSet.getString("dirección"));
                usuario.setTelefono(resultSet.getLong("telefono"));
                usuario.setTipoCliente(TipoUsuarioEnum.valueOf(resultSet.getString("cliente")));
                usuario.setDescuentoTipoUsuario(resultSet.getDouble("descuento"));
                usuario.getCuenta().setCorreo(resultSet.getString("correo"));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los datos en la tabla 'cuentas': " + e.getMessage());
        }
    }

    /**
     * Elimina un registro de usuario por su correo electrónico.
     *
     * @param correo Correo del usuario a eliminar.
     * @throws SQLException     Si ocurre un error al acceder a la base de datos.
     * @throws RuntimeException Si el correo es nulo o vacío.
     */
    public void eliminarRegistro(String correo) throws SQLException {
        if (correo == null || correo.isEmpty()) {
            RegistroLog.registrarAdvertencia("No se puede eliminar un usuario con correo nulo o vacío.");
            throw new RuntimeException("No se proporcionó un correo válido para eliminar el usuario.");
        }
        String sentencia = "DELETE FROM usuarios WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, correo);
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                RegistroLog.registrarInfo("✅ Usuario eliminado correctamente: " + correo);
            } else {
                RegistroLog.registrarInfo("⚠️ No se encontró el usuario para eliminar: " + correo);
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al eliminar el usuario: " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo eliminar el usuario. Intenta nuevamente.");
        }
    }
}
