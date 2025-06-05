package co.edu.uptc.persistencia;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.LibroCarrito;
import co.edu.uptc.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarritoDAO extends ConexionBD<Libro> {

    public void insertarDatos(Libro libroCarrito, String correo) throws SQLException, RuntimeException {
        if (libroCarrito == null) {
            RegistroLog.registrarAdvertencia("El libro proporcionado es nulo en 'carritoDAO'");
            throw new RuntimeException("El libro del carrito proporcionado es nulo.");
        }

        String sql = "INSERT INTO carrito (correo_usuario, isbn_libro, cantidad) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE cantidad = cantidad + ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, correo);
            preparedStatement.setString(2, libroCarrito.getIsbn());
            preparedStatement.setInt(3, libroCarrito.getStockReservado());
            preparedStatement.setInt(4, libroCarrito.getStockReservado());
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("Se agregaron: " + libroCarrito.getStockReservado() + " libros con ISBN: " + libroCarrito.getIsbn() + ",  usuario: " + correo + ".");
        } catch (NumberFormatException e) {
            RegistroLog.registrarError("Formate de ISBN inválido para el libro: '" + libroCarrito.getIsbn() + " ->" + e.getMessage(), e);
            throw new RuntimeException("El formato del código ISBN es incorrecto, verifica el número ingresado.");
        } catch (SQLException e) {
            RegistroLog.registrarError("Error SQL al insertar en la tabla 'carrito: '" + e.getMessage(), e);
            throw new SQLException("❌ Ocurrió un error al agregar el libro al carrito. Por favor, intenta nuevamente.");
        }
    }

    public void actualizarDatos(Libro libroCarrito, String correo) throws SQLException, RuntimeException {
        String sql = "UPDATE carrito SET cantidad = ? WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, libroCarrito.getStockReservado());
            preparedStatement.setString(2, correo);
            preparedStatement.setString(3, String.valueOf(libroCarrito.getIsbn()));
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("✅ Actualización exitosa del libro con ISBN: " + libroCarrito.getIsbn() + ", usuario: " + correo);
        } catch (SQLException e) {
            String errorMsg = "Error al actualizar en 'carrito' [ISBN: " + libroCarrito.getIsbn() + ", Usuario: " + correo + "]";
            RegistroLog.registrarError(errorMsg, e);
            throw new SQLException("❌ No se pudo actualizar el libro en el carrito. Intenta nuevamente.");
        }
    }

    public Libro seleccionarRegistro(Libro libroCarrito, String correo) throws SQLException, RuntimeException {
        String sql = "SELECT * FROM carrito WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, correo);
            preparedStatement.setLong(2, Long.parseLong(libroCarrito.getIsbn()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Libro libro = new Libro();
                    libro.setIsbn(resultSet.getString("isbn_libro"));
                    libro.setStockReservado(resultSet.getInt("cantidad"));
                    return libro;
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al consultar la tabla 'carrito' para ISBN " + libroCarrito.getIsbn() + " y usuario " + correo, e);
            throw new SQLException("❌ No se pudo consultar el carrito. Por favor, intenta nuevamente.");
        }
        return null;
    }

    @Override
    public ArrayList<Libro> seleccionarRegistros() throws SQLException, RuntimeException {
        ArrayList<Libro> librosCarrito = new ArrayList<>();
        String sql = "SELECT * FROM carrito";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Libro libroCarrito = new Libro();
                libroCarrito.setIsbn(resultSet.getString("isbn_libro"));
                libroCarrito.setStockReservado(resultSet.getInt("cantidad"));
                librosCarrito.add(libroCarrito);
            }
            RegistroLog.registrarInfo("✅ Se consultaron " + librosCarrito.size() + " libros del carrito.");
            return librosCarrito;
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar todos los registros de la tabla 'carrito'", e);
            throw new SQLException("❌ Error al seleccionar los datos en la tabla 'carrito': " + e.getMessage());
        }
    }

    public ArrayList<Libro> seleccionarRegistros(String correo) throws SQLException, RuntimeException {
        ArrayList<Libro> librosCarrito = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE correo_usuario = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, correo);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Libro libroCarritoEncontrado = new Libro();
                    libroCarritoEncontrado.setIsbn(resultSet.getString("isbn_libro"));
                    libroCarritoEncontrado.setStockReservado(resultSet.getInt("cantidad"));
                    librosCarrito.add(libroCarritoEncontrado);
                }
                return librosCarrito;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al consultar el carrito del usuario: " + correo, e);
            throw new SQLException("❌ No se pudo consultar el carrito del usuario. Por favor, intenta nuevamente.");
        }
    }

    public void eliminarRegistro(Libro libroCarrito, String correoUsuario) throws SQLException, RuntimeException {
        if (libroCarrito == null || correoUsuario == null) {
            RegistroLog.registrarAdvertencia("❗ Se intentó eliminar un libro nulo o con correo nulo.");
            throw new RuntimeException("⚠️ No se proporcionó un libro válido para eliminar.");
        }

        String sql = "DELETE FROM carrito WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, correoUsuario);
            preparedStatement.setString(2, libroCarrito.getIsbn());
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                RegistroLog.registrarInfo("🗑️ Libro eliminado del carrito (ISBN: " + libroCarrito.getIsbn() + ", Usuario: " + correoUsuario + ").");
            } else {
                RegistroLog.registrarAdvertencia("⚠️ No se encontró el libro para eliminar (ISBN: " + libroCarrito.getIsbn() + ", Usuario: " + correoUsuario + ").");
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al eliminar el libro del carrito (ISBN: " + libroCarrito.getIsbn() + ", Usuario: " + correoUsuario + ").", e);
            throw new SQLException("❌ No se pudo eliminar el libro del carrito. Intenta nuevamente.");
        }
    }

    @Override
    public void insertarDatos(Libro objeto) throws SQLException, RuntimeException {

    }

    @Override
    public void actualizarDatos(Libro objeto) throws SQLException, RuntimeException {

    }

    @Override
    public Libro seleccionarRegistro(Libro objeto) throws SQLException, RuntimeException {
        return null;
    }
}
