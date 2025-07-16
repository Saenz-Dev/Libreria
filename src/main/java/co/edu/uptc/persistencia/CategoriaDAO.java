package co.edu.uptc.persistencia;

import co.edu.uptc.contrato.ICategoriaRepositorio;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements ICategoriaRepositorio {

    private ConexionBD conexionBD;

    public CategoriaDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Inserta una nueva categoría en la base de datos.
     *
     * @param categoria nombre de la categoría a insertar
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si la categoría es nula o vacía
     */
    @Override
    public void guardar(Categoria categoria) throws RepositorioException {
        if (categoria == null) {
            throw new RuntimeException("No se puede insertar una categoría nula o sin nombre.");
        }
        String sql = "INSERT INTO categoria (nombre) VALUES (?)";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement psCat = connection.prepareStatement(sql)) {
            psCat.setString(1, categoria.getNombre());
            psCat.executeUpdate();
            RegistroLog.registrarInfo("✅ Categoría insertada correctamente: " + categoria);
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar la categoría: " + categoria + ". Detalles: " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo insertar la categoría. Verifica el nombre o intenta más tarde.");
        }
    }

    /**
     * Selecciona una categoría por su nombre en la base de datos.
     *
     * @param categoria de la categoría a buscar
     * @return la categoría encontrada o null si no existe
     * @throws SQLException si ocurre un error de base de datos
     */
    @Override
    public Categoria consultar(Categoria categoria) throws RepositorioException{
        String sql = "SELECT * FROM categoria WHERE nombre = ?";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement psCat = connection.prepareStatement(sql)) {
            psCat.setString(1, categoria.getNombre());
            try (ResultSet rs = psCat.executeQuery()) {
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));
                    return categoria;
                }
                return null;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar la categoría: " + categoria.getNombre() + ". Detalles: " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo obtener la categoría. Verifica el nombre o intenta más tarde.");
        }
    }

    /**
     * Selecciona todas las categorías disponibles en la base de datos.
     *
     * @return lista de categorías encontradas
     * @throws SQLException si ocurre un error de base de datos
     */
    @Override
    public List<Categoria> consultar() throws RepositorioException{
        String sqlCategoria = "SELECT * FROM categoria";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement psCat = connection.prepareStatement(sqlCategoria)) {
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
            throw new RepositorioException("❌ No se pudo obtener la categoría. Verifica el nombre o intenta más tarde.");
        }
    }

    /**
     * Actualiza una categoria con el id de la categoria.
     *
     * @param categoria categoria a actualizar.
     */
    @Override
    public void actualizar(Categoria categoria) {
        String sql = "UPDATE categoria SET nombre = ? WHERE id_categoria";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, categoria.getNombre());
            preparedStatement.setInt(2, categoria.getIdCategoria());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            RegistroLog.registrarError("No se pudo actualizar la información de la categoria.", e);
            throw new RuntimeException("Intenta nuevamente ");
        }
    }

    /**
     * Elimina una categoria de la base de datos.
     *
     * @param categoria categoria a eliminar.
     */
    @Override
    public void eliminar(Categoria categoria) throws RepositorioException{
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoria.getIdCategoria());
            RegistroLog.registrarInfo("Se eliminó la categoria: " + categoria.getIdCategoria());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al intentar borrar la categoria'" + categoria.getIdCategoria() + "': " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo eliminar la categoria. Intenta nuevamente más tarde.");
        }
    }

    @Override
    public Categoria consultarCategoriaID(int idCategoria) throws RepositorioException {
        String sqlCategoria = "SELECT * FROM categoria WHERE id_categoria = ?";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement psCat = connection.prepareStatement(sqlCategoria)) {
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
            throw new RepositorioException("❌ No se pudo obtener la categoría. Verifica el nombre o intenta más tarde.");
        }
    }

    @Override
    public Categoria consultarCategoriaNombre(String titulo) throws RepositorioException {
        String sqlCategoria = "SELECT * FROM categoria WHERE nombre = ?";
        try (Connection connection = conexionBD.crearConexion(); PreparedStatement psCat = connection.prepareStatement(sqlCategoria)) {
            psCat.setString(1, titulo);
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
            RegistroLog.registrarError("❌ Error al seleccionar la categoría: " + titulo + ". Detalles: " + e.getMessage(), e);
            throw new RepositorioException("❌ No se pudo obtener la categoría. Verifica el nombre o intenta más tarde.");
        }
    }
}
