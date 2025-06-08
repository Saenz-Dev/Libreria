package co.edu.uptc.persistencia;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.TipoLibroEnum;
import co.edu.uptc.modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con los libros del catálogo.
 * Permite insertar, consultar, actualizar y eliminar libros en la base de datos, así como gestionar categorías.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class LibroDAO extends ConexionBD<Libro> {

    /**
     * Inserta un libro en la base de datos.
     *
     * @param libro libro a insertar
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si el libro es nulo
     */
    @Override
    public void insertarDatos(Libro libro) throws SQLException, RuntimeException {
        if (libro == null) throw new RuntimeException("No se puede guardar un libro vacío.");
        String sentencia = "INSERT INTO libros (isbn, titulo, autor, año_publicación, editorial, páginas, precio, stockDisponible, stockReservado, tipo, comprado, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, libro.getIsbn());
            preparedStatement.setString(2, libro.getTitulo());
            preparedStatement.setString(3, libro.getAutor());
            preparedStatement.setInt(4, libro.getAnioPublicacion());
            preparedStatement.setString(5, libro.getEditorial());
            preparedStatement.setInt(6, libro.getNumeroPaginas());
            preparedStatement.setDouble(7, libro.getPrecioVenta());
            preparedStatement.setInt(8, libro.getStockDisponible());
            preparedStatement.setInt(9, libro.getStockReservado());
            preparedStatement.setString(10, String.valueOf(libro.getTipoLibro()));
            preparedStatement.setBoolean(11, libro.getIsComprado());
            preparedStatement.setInt(12, seleccionarCateoriaNombre(libro.getCategoria().getNombre()).getIdCategoria());
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("✅ Libro insertado correctamente con ISBN: " + libro.getIsbn());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar el libro con ISBN: " + libro.getIsbn() + ". Detalles: " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo guardar el libro. Por favor revisa los datos o contacta soporte.");
        }
    }

    /**
     * Consulta una categoría por su nombre en la base de datos.
     *
     * @param nombreCategoria nombre de la categoría a buscar
     * @return la categoría encontrada o null si no existe
     * @throws SQLException si ocurre un error de base de datos
     */
    public Categoria seleccionarCateoriaNombre(String nombreCategoria) throws SQLException {
        String sqlCategoria = "SELECT * FROM categoria WHERE nombre = ?";
        try (Connection connection = crearConexion(); PreparedStatement psCat = connection.prepareStatement(sqlCategoria)) {
            psCat.setString(1, nombreCategoria);
            Categoria categoria = new Categoria();
            try (ResultSet rs = psCat.executeQuery()) {
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));
                    return categoria;
                }
                return null;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar la categoría: " + nombreCategoria + ". Detalles: " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo obtener la categoría. Verifica el nombre o intenta más tarde.");
        }
    }

    /**
     * Selecciona todas las categorías disponibles en la base de datos.
     *
     * @return lista de categorías encontradas
     * @throws SQLException si ocurre un error de base de datos
     */
    public ArrayList<Categoria> seleccionarCateorias() throws SQLException {
        String sqlCategoria = "SELECT * FROM categoria";
        try (Connection connection = crearConexion(); PreparedStatement psCat = connection.prepareStatement(sqlCategoria)) {
            ArrayList<Categoria> categorias = new ArrayList<>();
            try (ResultSet rs = psCat.executeQuery()) {
                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setIdCategoria(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));
                    categorias.add(categoria);
                }
                return categorias;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar la categoría. " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo obtener la categoría. Verifica el nombre o intenta más tarde.");
        }
    }

    /**
     * Inserta una nueva categoría en la base de datos.
     *
     * @param categoria nombre de la categoría a insertar
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si la categoría es nula o vacía
     */
    public void insertarCategoria(String categoria) throws SQLException, RuntimeException {
        if (categoria == null || categoria.isEmpty()) {
            throw new RuntimeException("No se puede insertar una categoría nula o sin nombre.");
        }
        String sqlInsert = "INSERT INTO categoria (nombre) VALUES (?)";
        try (Connection connection = crearConexion(); PreparedStatement psCat = connection.prepareStatement(sqlInsert)) {
            psCat.setString(1, categoria);
            psCat.executeUpdate();
            RegistroLog.registrarInfo("✅ Categoría insertada correctamente: " + categoria);
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar la categoría: " + categoria + ". Detalles: " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo insertar la categoría. Verifica el nombre o intenta más tarde.");
        }
    }

    /**
     * Selecciona una categoría por su ID en la base de datos.
     *
     * @param idCategoria ID de la categoría a buscar
     * @return la categoría encontrada o null si no existe
     * @throws SQLException si ocurre un error de base de datos
     */
    public Categoria seleccionarCateoriaId(int idCategoria) throws SQLException {
        String sqlCategoria = "SELECT * FROM categoria WHERE id_categoria = ?";
        try (Connection connection = crearConexion(); PreparedStatement psCat = connection.prepareStatement(sqlCategoria)) {
            psCat.setInt(1, idCategoria);
            Categoria categoria = new Categoria();
            try (ResultSet rs = psCat.executeQuery()) {
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));
                    return categoria;
                }
                return null;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar la categoría: " + idCategoria + ". Detalles: " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo obtener la categoría. Verifica el nombre o intenta más tarde.");
        }
    }

    /**
     * Actualiza los datos de un libro en la base de datos.
     *
     * @param libro libro con los nuevos datos
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si el libro es nulo
     */
    @Override
    public void actualizarDatos(Libro libro) throws SQLException, RuntimeException {
        if (libro == null) throw new RuntimeException("No se puede actualizar un libro nulo.");
        String sentencia = "UPDATE libros SET titulo = ?, autor = ?, año_publicación = ?, id_categoria = ?, editorial = ?, páginas = ?, precio = ?, stockDisponible = ?, tipo = ?, stockReservado = ?, comprado = ? WHERE isbn = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, libro.getTitulo());
            preparedStatement.setString(2, libro.getAutor());
            preparedStatement.setInt(3, libro.getAnioPublicacion());
            preparedStatement.setString(4, seleccionarCateoriaNombre(libro.getCategoria().getNombre()).getIdCategoria() + "");
            preparedStatement.setString(5, libro.getEditorial());
            preparedStatement.setInt(6, libro.getNumeroPaginas());
            preparedStatement.setDouble(7, libro.getPrecioVenta());
            preparedStatement.setInt(8, libro.getStockDisponible());
            preparedStatement.setString(9, String.valueOf(libro.getTipoLibro()));
            preparedStatement.setInt(10, libro.getStockReservado());
            preparedStatement.setBoolean(11, libro.getIsComprado());
            preparedStatement.setString(12, libro.getIsbn());

            int filasActualizadas = preparedStatement.executeUpdate();

            if (filasActualizadas > 0) {
                RegistroLog.registrarInfo("✅ Libro actualizado con éxito: " + libro.getIsbn());
            } else {
                RegistroLog.registrarInfo("⚠️ No se encontró el libro con ISBN: " + libro.getIsbn());
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al actualizar el libro con ISBN: " + libro.getIsbn() + ". Detalles: " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo actualizar el libro. Verifica los datos o intenta más tarde.");
        }
    }

    /**
     * Selecciona un libro por su ISBN en la base de datos.
     *
     * @param libro libro con el ISBN a buscar
     * @return el libro encontrado o null si no existe
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si el libro es nulo
     */
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
                    libroResult.setCategoria(seleccionarCateoriaId(resultSet.getInt("id_categoria")));
                    libroResult.setEditorial(resultSet.getString("editorial"));
                    libroResult.setNumeroPaginas(resultSet.getInt("páginas"));
                    libroResult.setPrecioVenta(resultSet.getDouble("precio"));
                    libroResult.setStockDisponible(resultSet.getInt("stockDisponible"));
                    libroResult.setStockReservado(resultSet.getInt("stockReservado"));
                    libroResult.setTipoLibro(TipoLibroEnum.valueOf(resultSet.getString("tipo")));
                    libroResult.setIsComprado(resultSet.getBoolean("comprado"));
                    RegistroLog.registrarInfo("✅ Libro encontrado: " + libro.getIsbn());
                    return libroResult;
                } else {
                    RegistroLog.registrarInfo("⚠️ No se encontró un libro con ISBN: " + libro.getIsbn());
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al buscar el libro con ISBN: " + libro.getIsbn() + ". Detalles: " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo obtener el libro. Intenta nuevamente más tarde.");
        }
        return null;
    }

    /**
     * Selecciona un libro por su título en la base de datos.
     *
     * @param titulo título del libro a buscar
     * @return el libro encontrado o null si no existe
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si el título es nulo o vacío
     */
    public Libro seleccionarRegistro(String titulo) throws SQLException, RuntimeException {
        String sentencia = "SELECT * FROM libros WHERE titulo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, titulo);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Libro libroResult = new Libro();
                    libroResult.setIsbn(resultSet.getString("isbn"));
                    libroResult.setTitulo(resultSet.getString("titulo"));
                    libroResult.setAutor(resultSet.getString("autor"));
                    libroResult.setAnioPublicacion(resultSet.getInt("año_publicación"));
                    libroResult.setCategoria(seleccionarCateoriaId(resultSet.getInt("id_categoria")));
                    libroResult.setEditorial(resultSet.getString("editorial"));
                    libroResult.setNumeroPaginas(resultSet.getInt("páginas"));
                    libroResult.setPrecioVenta(resultSet.getDouble("precio"));
                    libroResult.setStockDisponible(resultSet.getInt("stockDisponible"));
                    libroResult.setStockReservado(resultSet.getInt("stockReservado"));
                    libroResult.setTipoLibro(TipoLibroEnum.valueOf(resultSet.getString("tipo")));
                    libroResult.setIsComprado(resultSet.getBoolean("comprado"));
                    RegistroLog.registrarInfo("✅ Libro encontrado con título: " + titulo);
                    return libroResult;
                } else {
                    RegistroLog.registrarInfo("⚠️ No se encontró un libro con título: " + titulo);
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al buscar el libro con título: " + titulo + ". Detalles: " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo obtener el libro. Intenta nuevamente más tarde.");
        }
        return null;
    }

    /**
     * Elimina un libro del catálogo en la base de datos por su ISBN.
     *
     * @param libro libro a eliminar
     * @return true si el libro fue eliminado, false si no se encontró
     * @throws SQLException si ocurre un error de base de datos
     */
    public boolean eliminarRegistro(Libro libro) throws SQLException {
        String sql = "DELETE FROM libros WHERE isbn = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, libro.getIsbn());
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                RegistroLog.registrarInfo("✅ Libro eliminado: " + libro.getTitulo() + " (ISBN: " + libro.getIsbn() + ")");
                return true;
            } else {
                RegistroLog.registrarInfo("⚠️ No se encontró el libro para eliminar: " + libro.getTitulo() + " (ISBN: " + libro.getIsbn() + ")");
                return false;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al intentar borrar el libro '" + libro.getTitulo() + "': " + e.getMessage(), e);
            throw new SQLException("❌ No se pudo eliminar el libro. Intenta nuevamente más tarde.");
        }
    }

    /**
     * Obtiene todos los libros registrados en la base de datos, ordenados por título.
     *
     * @return lista de libros encontrados
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public ArrayList<Libro> seleccionarRegistros() throws SQLException, RuntimeException {
        ArrayList<Libro> libros = new ArrayList<>();
        String sentencia = "SELECT * FROM libros ORDER BY titulo ASC";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Libro libroResult = new Libro();
                libroResult.setIsbn(resultSet.getString("isbn"));
                libroResult.setTitulo(resultSet.getString("titulo"));
                libroResult.setAutor(resultSet.getString("autor"));
                libroResult.setAnioPublicacion(resultSet.getInt("año_publicación"));
                libroResult.setCategoria(seleccionarCateoriaId(resultSet.getInt("id_categoria")));
                libroResult.setEditorial(resultSet.getString("editorial"));
                libroResult.setNumeroPaginas(resultSet.getInt("páginas"));
                libroResult.setPrecioVenta(resultSet.getDouble("precio"));
                libroResult.setStockDisponible(resultSet.getInt("stockDisponible"));
                libroResult.setStockReservado(resultSet.getInt("stockReservado"));
                libroResult.setTipoLibro(TipoLibroEnum.valueOf(resultSet.getString("tipo")));
                libroResult.setIsComprado(resultSet.getBoolean("comprado"));
                libros.add(libroResult);
            }
            RegistroLog.registrarInfo("✅ Se obtuvieron " + libros.size() + " libros de la base de datos");
            return libros;
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar libros: " + e.getMessage(), e);
            throw new SQLException("❌ Error al obtener la lista de libros. Intenta nuevamente más tarde.");
        }
    }
}
