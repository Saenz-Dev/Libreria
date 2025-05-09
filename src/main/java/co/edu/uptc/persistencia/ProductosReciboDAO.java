package co.edu.uptc.persistencia;

import co.edu.uptc.modelo.ProductoCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductosReciboDAO extends ConexionBD<ProductoCompra> {

    @Override
    public void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS productos_recibo (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "numero_recibo INT NOT NULL, " +
                "isbn_libro BIGINT NOT NULL, " +
                "correo_usuario VARCHAR(50) NOT NULL, " +
                "cantidad INT NOT NULL, " +
                "precio_unitario DOUBLE NOT NULL, " +
                "precio_total DOUBLE NOT NULL, " +
                "FOREIGN KEY (numero_recibo) REFERENCES recibos(numero_recibo), " +
                "FOREIGN KEY (isbn_libro) REFERENCES libros(isbn), " +
                "FOREIGN KEY (correo_usuario) REFERENCES usuarios(correo))";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al crear la tabla 'productos_recibo': " + e.getMessage());
        }
    }

    public void insertarDatos(ProductoCompra productoCompra, int numeroRecibo, String correoUsuario) throws SQLException, RuntimeException {
        String sql = "INSERT INTO productos_recibo (numero_recibo, isbn_libro, correo_usuario, cantidad, precio_unitario, precio_total) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, numeroRecibo);
            preparedStatement.setLong(2, Long.parseLong(productoCompra.getIsbn()));
            preparedStatement.setString(3, correoUsuario);
            preparedStatement.setInt(4, productoCompra.getNumeroLibros());
            preparedStatement.setDouble(5, productoCompra.getPrecioUnitario());
            preparedStatement.setDouble(6, productoCompra.getPrecioTotal());
            preparedStatement.executeUpdate();
        } catch (NumberFormatException e) {
            throw new RuntimeException("❌ Error al convertir el ISBN a un número válido: " + e.getMessage());
        } catch (SQLException e) {
            throw new SQLException("❌ Error al insertar los datos en la tabla 'productos_recibo': " + e.getMessage());
        }
    }

    public void actualizarDatos(ProductoCompra productoCompra, int numeroRecibo) throws SQLException, RuntimeException {
        String sql = "UPDATE productos_recibo SET isbn_libro = ?, cantidad = ?, precio_unitario = ?, precio_total = ? WHERE numero_recibo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, Long.parseLong(productoCompra.getIsbn()));
            preparedStatement.setInt(2, productoCompra.getNumeroLibros());
            preparedStatement.setDouble(3, productoCompra.getPrecioUnitario());
            preparedStatement.setDouble(4, productoCompra.getPrecioTotal());
            preparedStatement.setInt(5, numeroRecibo);
            preparedStatement.executeUpdate();
        } catch (NumberFormatException e) {
            throw new RuntimeException("❌ Error al convertir el ISBN a un número válido: " + e.getMessage());
        } catch (SQLException e) {
            throw new SQLException("❌ Error al actualizar los datos en la tabla 'productos_recibo': " + e.getMessage());
        }
    }

    public ProductoCompra seleccionarRegistro(ProductoCompra objeto, int numeroRecibo) throws SQLException, RuntimeException {
        String sql = "SELECT * FROM productos_recibo WHERE numero_recibo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, numeroRecibo);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ProductoCompra productoCompra = new ProductoCompra();
                    productoCompra.setIsbn(resultSet.getString("isbn_libro"));
                    productoCompra.setNumeroLibros(resultSet.getInt("cantidad"));
                    productoCompra.setPrecioUnitario(resultSet.getDouble("precio_unitario"));
                    productoCompra.setPrecioTotal(resultSet.getDouble("precio_total"));
                    return productoCompra;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'productos_recibo': " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<ProductoCompra> seleccionarRegistros() throws SQLException, RuntimeException {
        String sql = "SELECT * FROM productos_recibo";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            ArrayList<ProductoCompra> productosRecibo = new ArrayList<>();
            while (resultSet.next()) {
                ProductoCompra productoCompra = new ProductoCompra();
                productoCompra.setIsbn(resultSet.getString("isbn_libro"));
                productoCompra.setNumeroLibros(resultSet.getInt("cantidad"));
                productoCompra.setPrecioUnitario(resultSet.getDouble("precio_unitario"));
                productoCompra.setPrecioTotal(resultSet.getDouble("precio_total"));
                productosRecibo.add(productoCompra);
            }
            return productosRecibo;
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los registros en la tabla 'productos_recibo': " + e.getMessage());
        }
    }

    @Override
    public void insertarDatos(ProductoCompra objeto) throws SQLException, RuntimeException {}

    @Override
    public void actualizarDatos(ProductoCompra objeto) throws SQLException, RuntimeException {}

    @Override
    public ProductoCompra seleccionarRegistro(ProductoCompra objeto) throws SQLException, RuntimeException {return null;}
}
