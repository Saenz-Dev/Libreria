package co.edu.uptc.persistencia;

import co.edu.uptc.contrato.*;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.TipoLibroEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con los libros del catálogo.
 * Permite insertar, consultar, actualizar y eliminar libros en la base de datos, así como gestionar categorías.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class LibroDAO implements IRepositorio<Libro>, ILibroBusquedaPorTitulo  {

    private IConexionBD conexionBD;
    private ICategoriaRepositorio categoriaRepo;

    public LibroDAO(IConexionBD conexionBD, ICategoriaRepositorio categoriaRepo) {
        this.conexionBD = conexionBD;
        this.categoriaRepo = categoriaRepo;
    }

    /**
     * Inserta un libro en la base de datos.
     *
     * @param libro libro a insertar
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si el libro es nulo
     */
    @Override
    public void guardar(Libro libro) throws RepositorioException {
        if (libro == null) throw new RuntimeException("No se puede guardar un libro vacío.");
        String sentencia = "INSERT INTO libros (isbn, titulo, autor, año_publicación, editorial, páginas, precio, stockDisponible, stockReservado, tipo, comprado, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
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
            preparedStatement.setInt(12, categoriaRepo.consultarCategoriaNombre(libro.getCategoria().getNombre()).getIdCategoria());
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("✅ Libro insertado correctamente con ISBN: " + libro.getIsbn());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar el libro con ISBN: " + libro.getIsbn() + ". Detalles: " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo guardar el libro. Por favor revisa los datos o contacta soporte.");
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
    public Libro consultar(Libro libro) throws RepositorioException {
        String sentencia = "SELECT * FROM libros WHERE isbn = ?";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, libro.getIsbn());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Libro libroResult = new Libro();
                    libroResult.setIsbn(resultSet.getString("isbn"));
                    libroResult.setTitulo(resultSet.getString("titulo"));
                    libroResult.setAutor(resultSet.getString("autor"));
                    libroResult.setAnioPublicacion(resultSet.getInt("año_publicación"));
                    libroResult.setCategoria(categoriaRepo.consultarCategoriaID(resultSet.getInt("id_categoria")));
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
            throw new RepositorioException("❌ No se pudo obtener el libro. Intenta nuevamente más tarde.");
        }
        return null;
    }

    /**
     * Obtiene todos los libros registrados en la base de datos, ordenados por título.
     *
     * @return lista de libros encontrados
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public List<Libro> consultar() throws RepositorioException {
        ArrayList<Libro> libros = new ArrayList<>();
        String sentencia = "SELECT * FROM libros ORDER BY titulo ASC";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Libro libroResult = new Libro();
                libroResult.setIsbn(resultSet.getString("isbn"));
                libroResult.setTitulo(resultSet.getString("titulo"));
                libroResult.setAutor(resultSet.getString("autor"));
                libroResult.setAnioPublicacion(resultSet.getInt("año_publicación"));
                libroResult.setCategoria(categoriaRepo.consultarCategoriaID(resultSet.getInt("id_categoria")));
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
            throw new RepositorioException("❌ Error al obtener la lista de libros. Intenta nuevamente más tarde.");
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
    public void actualizar(Libro libro) throws RepositorioException {
        if (libro == null) throw new RuntimeException("No se puede actualizar un libro nulo.");
        String sentencia = "UPDATE libros SET titulo = ?, autor = ?, año_publicación = ?, id_categoria = ?, editorial = ?, páginas = ?, precio = ?, stockDisponible = ?, tipo = ?, stockReservado = ?, comprado = ? WHERE isbn = ?";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {
            preparedStatement.setString(1, libro.getTitulo());
            preparedStatement.setString(2, libro.getAutor());
            preparedStatement.setInt(3, libro.getAnioPublicacion());
            preparedStatement.setString(4, categoriaRepo.consultarCategoriaNombre(libro.getCategoria().getNombre()).getIdCategoria() + "");
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
            throw new RepositorioException("❌ No se pudo actualizar el libro. Verifica los datos o intenta más tarde.");
        }
    }

    /**
     * Elimina un libro del catálogo en la base de datos por su ISBN.
     *
     * @param libro libro a eliminar
     * @throws SQLException si ocurre un error de base de datos
     */
    @Override
    public void eliminar(Libro libro) throws RepositorioException {
        String sql = "DELETE FROM libros WHERE isbn = ?";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, libro.getIsbn());
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                RegistroLog.registrarInfo("✅ Libro eliminado: " + libro.getTitulo() + " (ISBN: " + libro.getIsbn() + ")");
            } else {
                RegistroLog.registrarInfo("⚠️ No se encontró el libro para eliminar: " + libro.getTitulo() + " (ISBN: " + libro.getIsbn() + ")");
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al intentar borrar el libro '" + libro.getTitulo() + "': " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo eliminar el libro. Intenta nuevamente más tarde.");
        }
    }

    /**
     * Selecciona un libro por su título en la base de datos.
     *
     * @param estrategia estrategia a realizar la búsqueda.
     * @return el libro encontrado o null si no existe
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si el título es nulo o vacío
     */
    @Override
    public Libro buscarLibroPorTitulo(IBusquedaStrategy estrategia) throws RepositorioException {
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(estrategia.getSQL())) {
            estrategia.ajustarParametro(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Libro libroResult = new Libro();
                    libroResult.setIsbn(resultSet.getString("isbn"));
                    libroResult.setTitulo(resultSet.getString("titulo"));
                    libroResult.setAutor(resultSet.getString("autor"));
                    libroResult.setAnioPublicacion(resultSet.getInt("año_publicación"));
                    libroResult.setCategoria(categoriaRepo.consultarCategoriaID(resultSet.getInt("id_categoria")));
                    libroResult.setEditorial(resultSet.getString("editorial"));
                    libroResult.setNumeroPaginas(resultSet.getInt("páginas"));
                    libroResult.setPrecioVenta(resultSet.getDouble("precio"));
                    libroResult.setStockDisponible(resultSet.getInt("stockDisponible"));
                    libroResult.setStockReservado(resultSet.getInt("stockReservado"));
                    libroResult.setTipoLibro(TipoLibroEnum.valueOf(resultSet.getString("tipo")));
                    libroResult.setIsComprado(resultSet.getBoolean("comprado"));
                    RegistroLog.registrarInfo("✅ Libro encontrado con título: " + libroResult.getTitulo());
                    return libroResult;
                } else {
                    RegistroLog.registrarInfo("⚠️ No se encontró el libro");
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al buscar el libro", e);
            throw new RepositorioException("❌ No se pudo obtener el libro. Intenta nuevamente más tarde.");
        }
        return null;
    }
}
