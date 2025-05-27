package co.edu.uptc.persistencia;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.LibroCarrito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarritoDAO extends ConexionBD<LibroCarrito> {

    @Override
    public void insertarDatos(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
	if (libroCarrito == null) {
	    RegistroLog.registrarAdvertencia("El libro proporcionado es nulo en 'carritoDAO'");
	    throw new RuntimeException("El libro del carrito proporcionado es nulo.");
	}

	String sql = "INSERT INTO carrito (correo_usuario, isbn_libro, cantidad) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE cantidad = cantidad + ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    preparedStatement.setString(1, libroCarrito.getCorreo_usuario());
	    preparedStatement.setLong(2, libroCarrito.getIsbn_libro());
	    preparedStatement.setInt(3, libroCarrito.getCantidad());
	    preparedStatement.setInt(4, libroCarrito.getCantidad());
	    preparedStatement.executeUpdate();
	    RegistroLog.registrarInfo("Se agregaron: " + libroCarrito.getCantidad() + " libros con ISBN: "
		    + libroCarrito.getIsbn_libro() + ",  usuario: " + libroCarrito.getCorreo_usuario() + ".");
	} catch (NumberFormatException e) {
	    RegistroLog.registrarError("Formate de ISBN inválido para el libro: '" + libroCarrito.getIsbn_libro() + " ->" + e.getMessage(), e);
	    throw new RuntimeException("El formato del código ISBN es incorrecto, verifica el número ingresado.");
	} catch (SQLException e) {
	    RegistroLog.registrarError("Error SQL al insertar en la tabla 'carrito: '" + e.getMessage(), e);
	    throw new SQLException("❌ Ocurrió un error al agregar el libro al carrito. Por favor, intenta nuevamente.");
	}
    }

    @Override
    public void actualizarDatos(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
	String sql = "UPDATE carrito SET cantidad = ? WHERE correo_usuario = ? AND isbn_libro = ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    preparedStatement.setInt(1, libroCarrito.getCantidad());
	    preparedStatement.setString(2, libroCarrito.getCorreo_usuario());
	    preparedStatement.setString(3, String.valueOf(libroCarrito.getIsbn_libro()));
	    preparedStatement.executeUpdate();
	    RegistroLog.registrarInfo("✅ Actualización exitosa del libro con ISBN: " + libroCarrito.getIsbn_libro()
		    + ", usuario: " + libroCarrito.getCorreo_usuario());
	} catch (SQLException e) {
	    String errorMsg = "Error al actualizar en 'carrito' [ISBN: " + libroCarrito.getIsbn_libro() + ", Usuario: "
		    + libroCarrito.getCorreo_usuario() + "]";
	    RegistroLog.registrarError(errorMsg, e);
	    throw new SQLException("❌ No se pudo actualizar el libro en el carrito. Intenta nuevamente.");
	}
    }

    @Override
    public LibroCarrito seleccionarRegistro(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
	String sql = "SELECT * FROM carrito WHERE correo_usuario = ? AND isbn_libro = ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    preparedStatement.setString(1, libroCarrito.getCorreo_usuario());
	    preparedStatement.setLong(2, libroCarrito.getIsbn_libro());
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
	    RegistroLog.registrarError("❌ Error al consultar la tabla 'carrito' para ISBN "
		    + libroCarrito.getIsbn_libro() + " y usuario " + libroCarrito.getCorreo_usuario(), e);
	    throw new SQLException("❌ No se pudo consultar el carrito. Por favor, intenta nuevamente.");
	}
	return null;
    }

    @Override
    public ArrayList<LibroCarrito> seleccionarRegistros() throws SQLException, RuntimeException {
	ArrayList<LibroCarrito> librosCarrito = new ArrayList<>();
	String sql = "SELECT * FROM carrito";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery()) {
	    while (resultSet.next()) {
		LibroCarrito libroCarrito = new LibroCarrito();
		libroCarrito.setIsbn_libro(resultSet.getLong("isbn_libro"));
		libroCarrito.setCorreo_usuario(resultSet.getString("correo_usuario"));
		libroCarrito.setCantidad(resultSet.getInt("cantidad"));
		librosCarrito.add(libroCarrito);
	    }
	    RegistroLog.registrarInfo("✅ Se consultaron " + librosCarrito.size() + " libros del carrito.");
	    return librosCarrito;
	} catch (SQLException e) {
	    RegistroLog.registrarError("❌ Error al seleccionar todos los registros de la tabla 'carrito'", e);
	    throw new SQLException("❌ Error al seleccionar los datos en la tabla 'carrito': " + e.getMessage());
	}
    }

    public ArrayList<LibroCarrito> seleccionarRegistros(LibroCarrito libroCarrito)
	    throws SQLException, RuntimeException {
	ArrayList<LibroCarrito> librosCarrito = new ArrayList<>();
	String sql = "SELECT * FROM carrito WHERE correo_usuario = ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    preparedStatement.setString(1, libroCarrito.getCorreo_usuario());
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
	    RegistroLog.registrarError("❌ Error al consultar el carrito del usuario: " + libroCarrito.getCorreo_usuario(), e);
	    throw new SQLException("❌ No se pudo consultar el carrito del usuario. Por favor, intenta nuevamente.");
	}
    }

    public void restarCantidad(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
	if (libroCarrito == null) {
	    RegistroLog.registrarAdvertencia("❗ Se intentó restar cantidad con un libro o usuario nulo.");
	    throw new RuntimeException("No se proporcionó un libro válido para actualizar.");
	}
	if (libroCarrito.getCantidad() <= 0) {
	    RegistroLog.registrarAdvertencia(
		    "❗ Se intentó establecer una cantidad no válida: " + libroCarrito.getCantidad());
	    throw new RuntimeException("La cantidad de libros debe ser mayor a 0.");
	}

	String sql = "UPDATE carrito SET cantidad = ? WHERE correo_usuario = ? AND isbn_libro = ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    preparedStatement.setInt(1, libroCarrito.getCantidad());
	    preparedStatement.setString(2, libroCarrito.getCorreo_usuario());
	    preparedStatement.setLong(3, libroCarrito.getIsbn_libro());
	    int filasAfectadas = preparedStatement.executeUpdate();
	    if (filasAfectadas > 0) {
		RegistroLog.registrarInfo(
			"🔄 Cantidad actualizada en el carrito: " + libroCarrito.getCantidad() + " unidades para ISBN "
				+ libroCarrito.getIsbn_libro() + " del usuario " + libroCarrito.getCorreo_usuario());
	    } else {
		RegistroLog.registrarAdvertencia("⚠️ No se encontró registro para actualizar con ISBN "
			+ libroCarrito.getIsbn_libro() + " y usuario " + libroCarrito.getCorreo_usuario());
	    }
	} catch (SQLException e) {
	    RegistroLog.registrarError("❌ Error al restar cantidad para ISBN " + libroCarrito.getIsbn_libro()
		    + " y usuario " + libroCarrito.getCorreo_usuario(), e);
	    throw new SQLException("❌ No se pudo actualizar la cantidad. Intenta nuevamente.");
	}
    }

    public void eliminarRegistro(LibroCarrito libroCarrito) throws SQLException, RuntimeException {
	if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null) {
	    RegistroLog.registrarAdvertencia("❗ Se intentó eliminar un libro nulo o con correo nulo.");
	    throw new RuntimeException("⚠️ No se proporcionó un libro válido para eliminar.");
	}

	String sql = "DELETE FROM carrito WHERE correo_usuario = ? AND isbn_libro = ?";
	try (Connection connection = crearConexion();
		PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    preparedStatement.setString(1, libroCarrito.getCorreo_usuario());
	    preparedStatement.setLong(2, libroCarrito.getIsbn_libro());
	    int filasAfectadas = preparedStatement.executeUpdate();

	    if (filasAfectadas > 0) {
		RegistroLog.registrarInfo("🗑️ Libro eliminado del carrito (ISBN: " + libroCarrito.getIsbn_libro()
			+ ", Usuario: " + libroCarrito.getCorreo_usuario() + ").");
	    } else {
		RegistroLog.registrarAdvertencia("⚠️ No se encontró el libro para eliminar (ISBN: "
			+ libroCarrito.getIsbn_libro() + ", Usuario: " + libroCarrito.getCorreo_usuario() + ").");
	    }
	} catch (SQLException e) {
	    RegistroLog.registrarError("❌ Error al eliminar el libro del carrito (ISBN: " + libroCarrito.getIsbn_libro()
		    + ", Usuario: " + libroCarrito.getCorreo_usuario() + ").", e);
	    throw new SQLException("❌ No se pudo eliminar el libro del carrito. Intenta nuevamente.");
	}
    }
}
