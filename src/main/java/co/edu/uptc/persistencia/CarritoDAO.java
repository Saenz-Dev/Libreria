package co.edu.uptc.persistencia;

import co.edu.uptc.contrato.IConexionBD;
import co.edu.uptc.contrato.IRepositorio;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Carrito;
import co.edu.uptc.modelo.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con el carrito de compras.
 * Permite insertar, actualizar, eliminar y consultar libros en el carrito de un usuario en la base de datos.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class CarritoDAO implements IRepositorio<Carrito> {

    private IConexionBD iConexionBD;

    public CarritoDAO(IConexionBD iConexionBD) {
        this.iConexionBD = iConexionBD;
    }

    /**
     * Inserta un libro en el carrito de un usuario. Si el libro ya existe, suma la cantidad.
     *
     * @param carrito carrito a guardar.
     * @throws RepositorioException si ocurre un error de base de datos.
     * @throws RuntimeException     si el libro es nulo o el ISBN es inválido.
     */
    @Override
    public void guardar(Carrito carrito) throws RepositorioException {
        if (carrito == null) {
            RegistroLog.registrarAdvertencia("El usuario a intentar guardar el carrito procede a dar un error'");
            throw new RuntimeException("El libro del carrito proporcionado es nulo.");
        }
        String sql = "INSERT INTO carrito (correo_usuario, isbn_libro, cantidad) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE cantidad = cantidad + ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, carrito.getUsuario().getCuenta().getCorreo());
            preparedStatement.setString(2, carrito.getLibros().getLast().getIsbn());
            preparedStatement.setInt(3, carrito.getLibros().getLast().getStockReservado());
            preparedStatement.setInt(4, carrito.getLibros().getLast().getStockReservado());
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("Se agregaron: " + carrito.getLibros().getLast().getStockReservado() + " libros con ISBN: " + carrito.getLibros().getLast().getIsbn() + ",  usuario: " + carrito.getUsuario().getCuenta().getCorreo() + ".");
        } catch (NumberFormatException e) {
            RegistroLog.registrarError("Formate de ISBN inválido para el libro: '" + carrito.getLibros().getLast().getIsbn() + " ->" + e.getMessage(), e);
            throw new RuntimeException("El formato del código ISBN es incorrecto, verifica el número ingresado.");
        } catch (SQLException e) {
            RegistroLog.registrarError("Error SQL al insertar en la tabla 'carrito: '" + e.getMessage(), e);
            throw new RepositorioException("❌ Ocurrió un error al agregar el libro al carrito. Por favor, intenta nuevamente.");
        }
    }

    @Override
    public Carrito consultar(Carrito carrito) throws RepositorioException {
        ArrayList<Libro> librosCarrito = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE correo_usuario = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, carrito.getUsuario().getCuenta().getCorreo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Libro libroCarritoEncontrado = new Libro();
                    libroCarritoEncontrado.setIsbn(resultSet.getString("isbn_libro"));
                    libroCarritoEncontrado.setStockReservado(resultSet.getInt("cantidad"));
                    librosCarrito.add(libroCarritoEncontrado);
                }
                Carrito carritoLocal = new Carrito();
                carritoLocal.setLibros(librosCarrito);
                return carritoLocal;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al consultar el carrito del usuario: " + carrito.getUsuario().getCuenta().getCorreo(), e);
            throw new RepositorioException("❌ No se pudo consultar el carrito del usuario. Por favor, intenta nuevamente.");
        }
    }

    @Override
    public List<Carrito> consultar() throws RepositorioException {
        ArrayList<Libro> librosCarrito = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE correo_usuario = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Libro libroCarritoEncontrado = new Libro();
                    libroCarritoEncontrado.setIsbn(resultSet.getString("isbn_libro"));
                    libroCarritoEncontrado.setStockReservado(resultSet.getInt("cantidad"));
                    librosCarrito.add(libroCarritoEncontrado);
                }
                List<Carrito> carritos = new ArrayList<>();
                Carrito carrito = new Carrito();
                carrito.setLibros(librosCarrito);
                carritos.add(carrito);
                return carritos;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al consultar los carrito");
            throw new RepositorioException("❌ No se pudo consultar el carrito del usuario. Por favor, intenta nuevamente.");
        }
    }

    /**
     * Actualiza la cantidad de un libro en el carrito de un usuario.
     *
     * @param carrito carrito a guardar.
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void actualizar(Carrito carrito) throws RepositorioException {
        String sql = "UPDATE carrito SET cantidad = ? WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, carrito.getUsuario().getCarrito().getLibros().getLast().getStockReservado());
            preparedStatement.setString(2, carrito.getUsuario().getCuenta().getCorreo());
            preparedStatement.setString(3, String.valueOf(carrito.getUsuario().getCarrito().getLibros().getLast().getIsbn()));
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("✅ Actualización exitosa del libro con ISBN: " + carrito.getUsuario().getCarrito().getLibros().getLast().getIsbn() + ", usuario: " + carrito.getUsuario().getCuenta().getCorreo());
        } catch (SQLException e) {
            String errorMsg = "Error al actualizar en 'carrito' [ISBN: " + carrito.getLibros().getLast().getIsbn() + ", Usuario: " + carrito.getUsuario().getCuenta().getCorreo() + "]";
            RegistroLog.registrarError(errorMsg, e);
            throw new RepositorioException("❌ No se pudo actualizar el libro en el carrito. Intenta nuevamente.");
        }
    }

    /**
     * Elimina un libro del carrito de un usuario.
     *
     * @param carrito carrito a eliminar.
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si el libro o el correo son nulos
     */

    @Override
    public void eliminar(Carrito carrito) throws RepositorioException {
        if (carrito == null) {
            RegistroLog.registrarAdvertencia("❗ Se intentó eliminar un libro nulo o con correo nulo.");
            throw new RuntimeException("⚠️ No se proporcionó un libro válido para eliminar.");
        }

        String sql = "DELETE FROM carrito WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Libro libro : carrito.getLibros()) {
                preparedStatement.setString(1, carrito.getUsuario().getCuenta().getCorreo());
                preparedStatement.setString(2, libro.getIsbn());
                int filasAfectadas = preparedStatement.executeUpdate();

                if (filasAfectadas > 0) {
                    RegistroLog.registrarInfo("🗑️ Libro eliminado del carrito (ISBN: " + libro.getIsbn() + ", Usuario: " + carrito.getUsuario().getCuenta().getCorreo() + ").");
                } else {
                    RegistroLog.registrarAdvertencia("⚠️ No se encontró el libro para eliminar (ISBN: " + libro.getIsbn() + ", Usuario: " + carrito.getUsuario().getCuenta().getCorreo() + ").");
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al eliminar el libro del carrito.", e);
            throw new RepositorioException("❌ No se pudo eliminar el libro del carrito. Intenta nuevamente.");
        }
    }
}
