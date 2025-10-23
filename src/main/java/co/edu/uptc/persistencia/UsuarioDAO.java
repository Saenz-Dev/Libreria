package co.edu.uptc.persistencia;

import co.edu.uptc.contrato.IConexionBD;
import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.contrato.IRepositorio;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.TipoUsuarioEnum;
import co.edu.uptc.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clase DAO (Data Access Object) encargada de realizar operaciones CRUD
 * sobre la tabla 'usuarios' de la base de datos.
 * Extiende de {@code ConexionBD<Usuario>} para utilizar las utilidades de conexión.
 */
public class UsuarioDAO implements IRepositorio<Usuario> {

    private IConexionBD iConexionBD;
    private IMapper<Usuario> mapperUsuario;

    public UsuarioDAO(IConexionBD iConexionBD, IMapper<Usuario> mapperUsuario) {
        this.iConexionBD = iConexionBD;
        this.mapperUsuario = mapperUsuario;
    }

    /**
     * Inserta un nuevo registro de usuario en la base de datos.
     *
     * @param usuario Objeto {@code Usuario} con los datos a insertar.
     * @throws RuntimeException Si el objeto usuario es nulo.
     */
    @Override
    public void guardar(Usuario usuario) throws RepositorioException {
        if (usuario == null) {
            throw new RuntimeException("El usuario a guardar no tiene datos");
        }
        String sentencia = "INSERT INTO usuarios (nombre, dirección, telefono, cliente, descuento, correo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            mapperUsuario.mapearObjeto(usuario, preparedStatement);
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                RegistroLog.registrarInfo("✅ Usuario insertado correctamente: " + usuario.getNombre() + " - " + usuario.getCuenta().getCorreo());
            } else {
                RegistroLog.registrarInfo("⚠️ No se insertó el usuario: " + usuario.getNombre() + " - " + usuario.getCuenta().getCorreo());
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'usuarios': " + e.getMessage(), e);
            throw new RepositorioException("❌ Error al guardar el usuario. Intenta nuevamente más tarde.");
        }
    }

    /**
     * Selecciona un registro de usuario por su correo electrónico.
     *
     * @param usuario Objeto {@code Usuario} con el correo a buscar.
     * @return Usuario encontrado o null si no existe.
     * @throws RepositorioException Si ocurre un error al acceder a la base de datos.
     * @throws RuntimeException     Si el correo del usuario es nulo.
     */
    @Override
    public Usuario consultar(Usuario usuario) throws RepositorioException {
        if (usuario.getCuenta().getCorreo() == null) throw new RuntimeException("No ha ingresado algún usuario.");
        String sentencia = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, usuario.getCuenta().getCorreo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuarioEncontrado = mapperUsuario.mapearResultSet(resultSet);
                    RegistroLog.registrarInfo("✅ Usuario encontrado: " + usuarioEncontrado.getCuenta().getCorreo());
                    return usuarioEncontrado;
                } else {
                    RegistroLog.registrarInfo("No se encontró usuario con correo: " + usuario.getCuenta().getCorreo());
                    return null;
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar los datos en la tabla 'usuarios': " + e.getMessage(), e);
            throw new RepositorioException("Ocurrió un problema al buscar el usuario. Por favor, intente más tarde.");
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
    public List<Usuario> consultar() throws RepositorioException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sentencia = "SELECT * FROM usuarios ORDER BY nombre";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Usuario usuario = mapperUsuario.mapearResultSet(resultSet);
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RepositorioException("❌ Error al seleccionar los datos en la tabla 'cuentas': " + e.getMessage());
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
    public void actualizar(Usuario usuario) throws RepositorioException {
        if (usuario == null) {
            RegistroLog.registrarAdvertencia("No se puede guardar un usuario nulo.");
            throw new RuntimeException("No se proporcionaron datos del usuario para guardar. Por favor, complete toda la información requerida.");
        }
        String sentencia = "UPDATE usuarios SET nombre = ?, dirección = ?, telefono = ?, cliente = ?, descuento = ? WHERE correo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            mapperUsuario.mapearObjeto(usuario, preparedStatement);
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                RegistroLog.registrarError("Se ha actualizado exitosamente el usuario " + usuario.getCuenta().getCorreo() + ", fueron afectadas " + filasAfectadas + " filas, ");
            } else {
                RegistroLog.registrarError("No se actualizó el usuario: " + usuario.getNombre() + " - " + usuario.getCuenta().getCorreo());
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al actualizar los datos en la tabla 'usuarios': " + e.getMessage(), e);
            throw new RepositorioException("No fue posible actualizar la cuenta, intentalo más tarde.");
        }
    }

    /**
     * Elimina un registro de usuario por su correo electrónico.
     *
     * @param usuario usuario que contiene el correo del usuario a eliminar.
     * @throws SQLException     Si ocurre un error al acceder a la base de datos.
     * @throws RuntimeException Si el correo es nulo o vacío.
     */
    @Override
    public void eliminar(Usuario usuario) throws RepositorioException {
        if (usuario.getCuenta().getCorreo() == null || usuario.getCuenta().getCorreo().isEmpty()) {
            RegistroLog.registrarAdvertencia("No se puede eliminar un usuario con correo nulo o vacío.");
            throw new RuntimeException("No se proporcionó un correo válido para eliminar el usuario.");
        }
        String sentencia = "DELETE FROM usuarios WHERE correo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, usuario.getCuenta().getCorreo());
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                RegistroLog.registrarInfo("✅ Usuario eliminado correctamente: " + usuario.getCuenta().getCorreo());
            } else {
                RegistroLog.registrarInfo("⚠️ No se encontró el usuario para eliminar: " + usuario.getCuenta().getCorreo());
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al eliminar el usuario: " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo eliminar el usuario. Intenta nuevamente.");
        }
    }
}
