package co.edu.uptc.persistencia;

import co.edu.uptc.modelo.Cuenta;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.negocio.TipoLibro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LibroDAO extends ConexionBD<Libro> {

    @Override
    public void crearTabla() throws SQLException {
        String sentencia = "CREATE TABLE IF NOT EXISTS libros (isbn BIGINT PRIMARY KEY, titulo VARCHAR (50) NOT NULL, autor VARCHAR(40) NOT NULL, año_publicación INT, categoria VARCHAR(20) NOT NULL, editorial VARCHAR(30), páginas INT, precio INT NOT NULL, stockDisponible INT NOT NULL, stockReservado INT NOT NULL, tipo VARCHAR(20) NOT NULL, comprado BOOLEAN)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al crear la tabla 'libros': " + e.getMessage());
        }
    }

    @Override
    public void insertarDatos(Libro libro) throws SQLException, RuntimeException {
        if (libro == null) throw new RuntimeException("El cuenta a guardar no tiene datos");
        String sentencia = "INSERT INTO libros (isbn, titulo, autor, año_publicación, categoria, editorial, páginas, precio, stockDisponible, stockReservado, tipo, comprado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, libro.getIsbn());
            preparedStatement.setString(2, libro.getTitulo());
            preparedStatement.setString(3, libro.getAutor());
            preparedStatement.setInt(4, libro.getAnioPublicacion());
            preparedStatement.setString(5, libro.getCategoria());
            preparedStatement.setString(6, libro.getEditorial());
            preparedStatement.setInt(7, libro.getNumeroPaginas());
            preparedStatement.setDouble(8, libro.getPrecioVenta());
            preparedStatement.setInt(9, libro.getStockDisponible());
            preparedStatement.setInt(10, libro.getStockReservado());
            preparedStatement.setString(11, String.valueOf(libro.getTipoLibro()));
            preparedStatement.setBoolean(12, libro.getIsComprado());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al insertar los datos en la tabla 'libros': " + e.getMessage());
        }
    }

    @Override
    public void actualizarDatos(Libro libro) throws SQLException, RuntimeException {
        if (libro == null) throw new RuntimeException("Cuenta vacía");
        String sentencia = "UPDATE libros SET titulo = ?, autor = ?, año_publicación = ?, categoria = ?, editorial = ?, páginas = ?, precio = ?, stockDisponible = ?, stockReservado = ?, tipo = ?, comprado = ? WHERE isbn = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, libro.getTitulo());
            preparedStatement.setString(2, libro.getAutor());
            preparedStatement.setInt(3, libro.getAnioPublicacion());
            preparedStatement.setString(4, libro.getCategoria());
            preparedStatement.setString(5, libro.getEditorial());
            preparedStatement.setInt(6, libro.getNumeroPaginas());
            preparedStatement.setDouble(7, libro.getPrecioVenta());
            preparedStatement.setInt(8, libro.getStockDisponible());
            preparedStatement.setInt(9, libro.getStockReservado());
            preparedStatement.setString(10, String.valueOf(libro.getTipoLibro()));
            preparedStatement.setBoolean(11, libro.getIsComprado());
            preparedStatement.setString(12, libro.getIsbn());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al insertar los datos en la tabla 'libros': " + e.getMessage());
        }
    }

    @Override
    public Libro seleccionarRegistro(Libro libro) throws SQLException, RuntimeException {
        String sentencia = "SELECT * FROM libros WHERE isbn = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, libro.getIsbn());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Libro libroResult = new Libro();
                    libroResult.setIsbn(resultSet.getString("isbn"));
                    libroResult.setTitulo(resultSet.getString("titulo"));
                    libroResult.setAutor(resultSet.getString("autor"));
                    libroResult.setAnioPublicacion(resultSet.getInt("año_publicación"));
                    libroResult.setCategoria(resultSet.getString("categoria"));
                    libroResult.setEditorial(resultSet.getString("editorial"));
                    libroResult.setNumeroPaginas(resultSet.getInt("paginas"));
                    libroResult.setPrecioVenta(resultSet.getDouble("precio"));
                    libroResult.setStockDisponible(resultSet.getInt("stockDisponible"));
                    libroResult.setStockReservado(resultSet.getInt("stockReservado"));
                    libroResult.setTipoLibro(TipoLibro.valueOf(resultSet.getString("tipo")));
                    libroResult.setIsComprado(resultSet.getBoolean("comprado"));
                    return libroResult;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el dato en la tabla 'cuentas': " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Libro> seleccionarRegistros() throws SQLException, RuntimeException {
        ArrayList<Libro> libros = new ArrayList<>();
        String sentencia = "SELECT * FROM libros";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Libro libroResult = new Libro();
                libroResult.setIsbn(resultSet.getString("isbn"));
                libroResult.setTitulo(resultSet.getString("titulo"));
                libroResult.setAutor(resultSet.getString("autor"));
                libroResult.setAnioPublicacion(resultSet.getInt("año_publicación"));
                libroResult.setCategoria(resultSet.getString("categoria"));
                libroResult.setEditorial(resultSet.getString("editorial"));
                libroResult.setNumeroPaginas(resultSet.getInt("paginas"));
                libroResult.setPrecioVenta(resultSet.getDouble("precio"));
                libroResult.setStockDisponible(resultSet.getInt("stockDisponible"));
                libroResult.setStockReservado(resultSet.getInt("stockReservado"));
                libroResult.setTipoLibro(TipoLibro.valueOf(resultSet.getString("tipo")));
                libroResult.setIsComprado(resultSet.getBoolean("comprado"));
                libros.add(libroResult);
            }
            return libros;
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el dato en la tabla 'cuentas': " + e.getMessage());
        }
    }
}
