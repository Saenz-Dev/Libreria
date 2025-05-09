package co.edu.uptc.persistencia;

import co.edu.uptc.modelo.LibroCarrito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarritoDAO extends ConexionBD<LibroCarrito> {

    @Override
    public void crearTabla() throws SQLException {
        String sentencia = "CREATE TABLE IF NOT EXISTS carrito (id INT AUTO_INCREMENT PRIMARY KEY, correo_usuario VARCHAR(50), isbn_libro BIGINT, cantidad INT DEFAULT 1, FOREIGN KEY (correo_usuario) REFERENCES usuarios(correo), FOREIGN KEY (isbn_libro) REFERENCES libros(isbn), UNIQUE (correo_usuario, isbn_libro))";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al crear la tabla 'carrito': " + e.getMessage());
        }
    }

    @Override
    public void insertarDatos(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
        if (libroCarrito == null) throw new RuntimeException("El libro del carrito proporcionado es nulo.");

        String sql = "INSERT INTO carrito (correo_usuario, isbn_libro, cantidad) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE cantidad = cantidad + ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, libroCarrito.getCorreo_usuario());
            preparedStatement.setLong(2, libroCarrito.getIsbn_libro());
            preparedStatement.setInt(3, libroCarrito.getCantidad());
            preparedStatement.setInt(4, libroCarrito.getCantidad());
            preparedStatement.executeUpdate();
        } catch (NumberFormatException e) {
            throw new RuntimeException("El ISBN del libro no tiene un formato válido: " + libroCarrito.getIsbn_libro(), e);
        } catch (SQLException e) {
            throw new SQLException("❌ Error al insertar los datos en la tabla 'carrito': " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarDatos(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
        if (libroCarrito == null) throw new RuntimeException("El libro proporcionado es nulo.");
        if (libroCarrito.getCantidad() <= 0) throw new RuntimeException("La cantidad de libros debe ser mayor a 0.");

        String sql = "UPDATE carrito SET cantidad = ? WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, libroCarrito.getCantidad());
            preparedStatement.setString(2, libroCarrito.getCorreo_usuario());
            preparedStatement.setString(3, String.valueOf(libroCarrito.getIsbn_libro()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al actualizar los datos en la tabla 'carrito': " + e.getMessage());
        }
    }

    @Override
    public LibroCarrito seleccionarRegistro(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
        if (libroCarrito == null) {
            throw new RuntimeException("El parámetro 'libroCarrito' no puede ser nulo.");
        }
        String sql = "SELECT * FROM carrito WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, libroCarrito.getCorreo_usuario());
            preparedStatement.setString(2, String.valueOf(libroCarrito.getIsbn_libro()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    LibroCarrito libro = new LibroCarrito();
                    libro.setIsbn_libro(resultSet.getLong("isbn_libro"));
                    libro.setCorreo_usuario(resultSet.getString("correo_usuario"));
                    libro.setCantidad(resultSet.getInt("cantidad"));
                    return libro;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el dato en la tabla 'carrito': " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<LibroCarrito> seleccionarRegistros() throws SQLException, RuntimeException {
        ArrayList<LibroCarrito> librosCarrito = new ArrayList<>();
        String sql = "SELECT * FROM carrito";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                LibroCarrito libroCarrito = new LibroCarrito();
                libroCarrito.setIsbn_libro(resultSet.getLong("isbn_libro"));
                libroCarrito.setCorreo_usuario(resultSet.getString("correo_usuario"));
                libroCarrito.setCantidad(resultSet.getInt("cantidad"));
                librosCarrito.add(libroCarrito);
            }
            return librosCarrito;
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los datos en la tabla 'carrito': " + e.getMessage());
        }
    }

    public ArrayList<LibroCarrito> seleccionarRegistros(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
        ArrayList<LibroCarrito> librosCarrito = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, libroCarrito.getCorreo_usuario());
            preparedStatement.setLong(2, libroCarrito.getIsbn_libro());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    LibroCarrito libroCarritoEncontrado = new LibroCarrito();
                    libroCarritoEncontrado.setIsbn_libro(resultSet.getLong("isbn_libro"));
                    libroCarritoEncontrado.setCorreo_usuario(resultSet.getString("correo_usuario"));
                    libroCarritoEncontrado.setCantidad(resultSet.getInt("cantidad"));
                    librosCarrito.add(libroCarritoEncontrado);
                }
                return librosCarrito;
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los datos en la tabla 'carrito': " + e.getMessage());
        }
    }

    public void restarCantidad(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
        if (libroCarrito == null) throw new RuntimeException("El libro proporcionado es nulo.");
        if (libroCarrito.getCantidad() <= 0) throw new RuntimeException("La cantidad de libros debe ser mayor a 0.");

        String sql = "UPDATE carrito SET cantidad = ? WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, libroCarrito.getCantidad());
            preparedStatement.setString(2, libroCarrito.getCorreo_usuario());
            preparedStatement.setLong(3, libroCarrito.getIsbn_libro());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al restar la cantidad en la tabla 'carrito': " + e.getMessage());
        }
    }

    public void eliminarRegistro(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
        if (libroCarrito == null) throw new RuntimeException("El libro proporcionado es nulo.");

        String sql = "DELETE FROM carrito WHERE correo_usuario = ? AND isbn_libro = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, libroCarrito.getCorreo_usuario());
            preparedStatement.setLong(2, libroCarrito.getIsbn_libro());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al eliminar el registro en la tabla 'carrito': " + e.getMessage());
        }
    }
}
