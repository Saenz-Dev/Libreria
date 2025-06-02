package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Carrito;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.LibroCarrito;
import co.edu.uptc.modelo.ProductoCompra;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.ResumenProductoDTO;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.ValorCompra;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.CuentaDAO;
import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.ReciboDAO;
import co.edu.uptc.persistencia.UsuarioDAO;

/**
 * Clase encargada de gestionar el carrito de compras del usuario.
 */
public class GestionCarrito {

    /**
     * Carrito del usuario
     */
    private Carrito carrito;

    private CarritoDAO carritoDAO;

    private UsuarioDAO usuarioDAO;

    private CuentaDAO cuentaDAO;

    private LibroDAO libroDAO;

    private GestionUsuario gestionUsuario;
    /**
     * Instancia de Manejo de libros con JSON
     */
    private ManejoLibroJSON manejoLibroJSON;

    /**
     * Instancia de Manejo de usuarios con JSON
     */
    private ManejoUsuarioJSON manejoUsuarioJSON;

    /**
     * Calculadora de IVA
     */
    private CalculadoraIVA calculadoraIVA;

    /**
     * Constructor de la clase
     *
     * @throws SQLException
     */
    public GestionCarrito(Tienda tienda, CarritoDAO carritoDAO,
	    UsuarioDAO usuarioDAO, CuentaDAO cuentaDAO, LibroDAO libroDAO, GestionUsuario gestionUsuario)
	    throws SQLException {
	carrito = new Carrito();
	this.carritoDAO = carritoDAO;
	this.usuarioDAO = usuarioDAO;
	this.cuentaDAO = cuentaDAO;
	this.libroDAO = libroDAO;
	this.gestionUsuario = gestionUsuario;
	manejoLibroJSON = new ManejoLibroJSON(tienda);
	this.manejoUsuarioJSON = manejoUsuarioJSON;
	calculadoraIVA = new CalculadoraIVA();
    }

    /**
     * Método que devuelve el carrito del usuario
     *
     * @return carrito del usuario
     */
    public Carrito getCarrito() {
	return carrito;
    }

    /**
     * Método que actualiza el carrito del usuario
     *
     * @param carrito carrito del usuario
     */
    public void setCarrito(Carrito carrito) {
	this.carrito = carrito;
    }

    /**
     * Método que devuelve la instancia de Manejo de usuarios con JSON
     *
     * @return instancia de Manejo de usuarios con JSON
     */
    public ManejoUsuarioJSON getManejoUsuarioJSON() {
	return manejoUsuarioJSON;
    }

    /*
     * public void agregarProductos(Carrito carrito) { for (Libro libro :
     * carrito.getLibros()) { this.carrito.agregarLibroCarrito(libro); } }
     */

    /**
     * Método que agrega los libros al carrito del usuario
     *
     * @param isbnLibro libro a agregar al carrito
     * @param cantidad  cantidad de libros a agregar al carrito
     * @throws IOException      si ocurre algún error cuando no se escribe el
     *                          usuario en el JSON
     * @throws RuntimeException
     * @throws SQLException
     */
    public void anadirLibrosCarrito(String isbnLibro, int cantidad) throws IOException, SQLException, RuntimeException {
	Usuario usuarioLog = gestionUsuario.userLog();
	Libro libroCatalogo = validarDisponibilidadLibros(isbnLibro, cantidad);
	if (libroCatalogo == null) {
	    throw new IllegalArgumentException("No se pudo realizar la acción de añadir libros al carrito");
	}
	LibroCarrito libroCarrito = existeProductoCarrito(isbnLibro, usuarioLog);
	libroCatalogo.setIsComprado(true);
	if (libroCarrito != null) {
	    anadirProductoExistente(libroCarrito, libroCatalogo, usuarioLog);
	} else {
	    anadirProductoNuevo(usuarioLog, libroCatalogo);
	}
    }

    /**
     * Suma productos si ya existen en el carrito del usuario.
     * 
     * @param libroCarrito  libro del carrito existente en el carrito para aumentar
     *                      su cantidad.
     * @param libroCatalogo libro del catalogo a modificar.
     * @param usuarioLogin  usuario logueado.
     * @throws IOException      si llega a ocurrir algun error al serializar los
     *                          datos.
     * @throws RuntimeException
     * @throws SQLException
     */
    public void anadirProductoExistente(LibroCarrito libroCarrito, Libro libroCatalogo, Usuario usuarioLogin)
	    throws IOException, SQLException, RuntimeException {
	if (libroCatalogo.getStockDisponible() == 0)
	    throw new IllegalArgumentException("Libro Agotado");
	libroCatalogo.reservarLibro();
	libroCarrito.aumentarCantidad(1);
	actualizarDatos(usuarioLogin, libroCatalogo, libroCarrito);
    }

    /**
     * Agrega un libro al carrito.
     * 
     * @param usuarioLogin  usuario logueado.
     * @param libroCatalogo libro del catalogo para agregar al carrito.
     * @throws IOException      si ocurre algún error al serializar los datos.
     * @throws RuntimeException
     * @throws SQLException
     */
    public void anadirProductoNuevo(Usuario usuarioLogin, Libro libroCatalogo)
	    throws IOException, SQLException, RuntimeException {
	agregarLibroCarrito(libroCatalogo, usuarioLogin);
	libroCatalogo.reservarLibro();
	usuarioDAO.actualizarDatos(usuarioLogin);
	libroDAO.actualizarDatos(libroCatalogo);
    }

    /**
     * Agrega un libro al carrito
     * 
     * @param libro libro a agregar a la base de datos
     * @throws RuntimeException
     * @throws SQLException
     */
    public void agregarLibroCarrito(Libro libro, Usuario usuarioLog) throws SQLException, RuntimeException {
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setIsbn_libro(Long.parseLong(libro.getIsbn()));
	libroCarrito.setCorreo_usuario(usuarioLog.getCuenta().getCorreo());
	libroCarrito.setCantidad(1);
	carritoDAO.insertarDatos(libroCarrito);
    }

    /**
     * Actualiza los dato en la lista de los usuarios, serializando los datos.
     * 
     * @param usuarioLogin  usuario logueado.
     * @param libroCatalogo libro del catalogo.
     * @throws IOException      si ocurre algún error al serializar los datos.
     * @throws RuntimeException
     * @throws SQLException
     */
    public void actualizarDatos(Usuario usuarioLogin, Libro libroCatalogo, LibroCarrito libroCarrito)
	    throws IOException, SQLException, RuntimeException {
	validarLibroCarrito(libroCarrito);
	usuarioDAO.actualizarDatos(usuarioLogin);
	libroDAO.actualizarDatos(libroCatalogo);
	carritoDAO.actualizarDatos(libroCarrito);
    }

    private void validarLibroCarrito(LibroCarrito libroCarrito) {
	if (libroCarrito == null) {
	    RegistroLog.registrarAdvertencia("El libro proporcionado es nulo.");
	    throw new RuntimeException("No se proporcionó un libro válido.");
	}
	if (libroCarrito.getCantidad() <= 0) {
	    RegistroLog.registrarAdvertencia(
		    "Intento de actualizar con cantidad no válida: " + libroCarrito.getCantidad());
	    throw new RuntimeException("La cantidad de libros debe ser mayor a 0.");
	}
    }

    /**
     * Metodo que valida si el libro está disponible en el catálogo.
     * 
     * @param isbn               isbn del libro.
     * @param cantidadSolicitada cantidad solicitada.
     * @return libro disponible.
     * @throws RuntimeException
     * @throws SQLException
     */
    public Libro validarDisponibilidadLibros(String isbn, int cantidadSolicitada)
	    throws SQLException, RuntimeException {
	Libro libro = new Libro();
	libro.setIsbn(isbn);
	libro = libroDAO.seleccionarRegistro(libro);
	if (libro.getStockDisponible() == 0)
	    throw new IllegalArgumentException("Libro Agotado");
	return libro;
    }

    /**
     * Método que verifica si el libro ya está en el carrito
     *
     * @param isbn isbn del libro
     * @return libro encontrado en el carrito
     * @throws IOException      si ocurre algún error cuando no se lee el usuario en
     *                          el JSON
     * @throws RuntimeException
     * @throws SQLException
     */
    public LibroCarrito existeProductoCarrito(String isbn, Usuario usuarioLogin)
	    throws IOException, SQLException, RuntimeException {
	if (usuarioLogin == null)
	    return null;
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setIsbn_libro(Long.parseLong(isbn));
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	libroCarrito = carritoDAO.seleccionarRegistro(libroCarrito);
	return libroCarrito;
    }

    /**
     * Método que devuelve el arrayList de libros del carrito del usuario
     *
     * @return arrayList de libros del carrito del usuario
     * @throws RuntimeException 
     * @throws SQLException 
     */
    public ArrayList<LibroCarrito> listarLibros() throws SQLException, RuntimeException {
	return carritoDAO.seleccionarRegistros();
    }

    /**
     * Método que suma la cantidad de un libro en el carrito
     *
     * @param isbnProducto libro a sumar
     * @return subtotal del producto
     * @throws IOException si ocurre algún error cuando no se lee el usuario en el
     *                     JSON
     * @throws RuntimeException 
     * @throws SQLException 
     */
    public ResumenProductoDTO sumarProducto(String isbnProducto) throws IOException, SQLException, RuntimeException {
	LibroCarrito libroCarrito = consultaLibroCarrito(isbnProducto);
	Libro libroCatalogo = consultaLibroCatalogo(isbnProducto);
	
	validarDisponibilidad(isbnProducto, libroCarrito, libroCatalogo);
	
	libroCatalogo.reservarLibro();
	libroCarrito.aumentarCantidad(1);
	validarLibroCarrito(libroCarrito);
	carritoDAO.actualizarDatos(libroCarrito);
	libroDAO.actualizarDatos(libroCatalogo);
	
	return actualizarProductoCarrito(isbnProducto);
    }

    private void validarDisponibilidad(String isbnProducto, LibroCarrito libroCarrito, Libro libroCatalogo) {
	if (libroCarrito == null || libroCatalogo == null) {
	    throw new IllegalArgumentException("No se pudo sumar el producto con ISBN:" + isbnProducto);
	}
	
	if (libroCatalogo.getStockDisponible() == 0) {
	    throw new IllegalArgumentException("El libo agotado.");
	}
    }

    /**
     * Retorna el subtotal y la cantidad reservada de un producto en el carrito.
     * 
     * @param productoCarrito producto actualizar.
     * @param librosCarrito   lista de libros del carrito del usuario.
     * @param catalogo        catalogo disponible en la tienda.
     * @param index           posición en la que se encuentra el producto en el
     *                        carrito del usuario.
     * @return El resumen del producto.
     * @throws IOException si al serializar los datos ocurre algún error.
     * @throws RuntimeException 
     * @throws SQLException 
     */
    public ResumenProductoDTO actualizarProductoCarrito(String isbn) throws IOException, SQLException, RuntimeException {

	ResumenProductoDTO resumenProductoDTO = new ResumenProductoDTO();
	LibroCarrito libroCarrito = consultaLibroCarrito(isbn);
	Libro libroCatalogo = consultaLibroCatalogo(isbn);
	
	resumenProductoDTO.setSubtotal(calculadoraIVA.subtotalProducto(libroCarrito, libroCatalogo));
	resumenProductoDTO.setCantidadReservada(libroCarrito.getCantidad());
	
	return resumenProductoDTO;
    }

    private void validarConsulta(LibroCarrito libroCarrito) {
	if (libroCarrito.getCorreo_usuario().isEmpty()) {
	    RegistroLog.registrarInfo("✅ Libro encontrado en el carrito: ISBN " + libroCarrito.getIsbn_libro());
	} else {
	    RegistroLog.registrarInfo("🔍 No se encontró el libro en el carrito: ISBN "
		    + libroCarrito.getIsbn_libro() + ", usuario " + libroCarrito.getCorreo_usuario());
	}
    }

    private void validarLibroCarritoNull(LibroCarrito libroCarrito) {
	if (libroCarrito == null) {
	    RegistroLog.registrarAdvertencia("Intento de seleccionar un libro con valor nulo.");
	    throw new RuntimeException("No se proporcionó un libro válido.");
	}
    }

    /**
     * Método que disminuye la cantidad de un libro en el carrito
     *
     * @param isbnProducto libro a disminuir
     * @return subtotal del producto
     * @throws IOException si ocurre algún error cuando no se lee el usuario en el
     *                     JSON
     * @throws RuntimeException 
     * @throws SQLException 
     */
    public ResumenProductoDTO disminuirProducto(String isbnProducto) throws IOException, SQLException, RuntimeException {
	LibroCarrito libroCarrito = consultaLibroCarrito(isbnProducto);
	Libro libroCatalogo = consultaLibroCatalogo(isbnProducto);
	
	libroCatalogo.cancelarReserva();
	libroCarrito.disminuirCantidad(1);
	
	validarLibroCarrito(libroCarrito);
	carritoDAO.actualizarDatos(libroCarrito);
	libroDAO.actualizarDatos(libroCatalogo);
	
	return actualizarProductoCarrito(isbnProducto);
    }

    /**
     * Método que elimina el libro del carrito
     *
     * @param isbnProducto libro a eliminar
     * @return subtotal del producto
     * @throws IOException si ocurre algún error cuando no se lee el usuario en el
     *                     JSON
     * @throws RuntimeException 
     * @throws SQLException 
     */
    public void eliminarProducto(String isbnProducto) throws IOException, SQLException, RuntimeException {
	LibroCarrito libroCarrito = consultaLibroCarrito(isbnProducto);
	Libro libroCatalogo = consultaLibroCatalogo(isbnProducto);
	
	if (libroCarrito == null || libroCatalogo == null) {
	    throw new IllegalArgumentException("No se pudo sumar el producto con ISBN:" + isbnProducto);
	}
	
	ArrayList<LibroCarrito> librosCarrito = listarLibros();
	libroCatalogo.setIsComprado(validarComprado(librosCarrito, isbnProducto));
	libroCatalogo.eliminarReserva(libroCarrito.getCantidad());
	
	carritoDAO.eliminarRegistro(libroCarrito);
	libroDAO.actualizarDatos(libroCatalogo);
    }

    private Libro consultaLibroCatalogo(String isbnProducto) throws SQLException {
	Libro libroCatalogo = new Libro();
	libroCatalogo.setIsbn(isbnProducto);
	libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
	return libroCatalogo;
    }

    private LibroCarrito consultaLibroCarrito(String isbnProducto) throws SQLException {
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setIsbn_libro(Long.parseLong(isbnProducto));
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	validarLibroCarritoNull(libroCarrito);
	libroCarrito = carritoDAO.seleccionarRegistro(libroCarrito);
	validarConsulta(libroCarrito);
	return libroCarrito;
    }
    
    
    public boolean validarComprado(ArrayList<LibroCarrito> librosCarrito, String isbn) {
	int numeroCompras = 0;//Variable de control para validar si el producto lo tienen dos usuarios
	String correoUsuario = "";
	if (librosCarrito.isEmpty()) return false; //Si la lista de libros de la tabla carrito esta vacia devuelve null
	for (LibroCarrito libroCarrito : librosCarrito) { //Por cada libroCarrito que hay en la lista de libros en el carrito
	    
	    if (libroCarrito.getIsbn_libro().equals(Long.parseLong(isbn))) { //Compara el isbn del libro del carrito con el isbn del libro a buscar y si es igual entra al if
		numeroCompras++; //Aumenta el numero de compras
		if (!libroCarrito.getCorreo_usuario().equals(correoUsuario) && numeroCompras > 1) { //Si el correo del usuario asignado al libro en el carrito no es igual al correo
		    //guardado en la variable local correoUsuario y el numero de compras es mayor a 2 devuelve true
		    return true;
		}
		correoUsuario = libroCarrito.getCorreo_usuario(); // En este caso la primera vez no devuelve true, asigna el correo a la variable local y despues si lo valida.
	    }
	}
	return false; //En el caso de que no este comprado o apartado en otro carrito de otro usuario devuelve false.
    }

    /**
     * Método que busca un libro en el catálogo
     *
     * @param isbnProducto libro a buscar
     * @param catalogo     catálogo de libros para buscar
     * @return libro encontrado
     */
    public Libro encontrarLibro(String isbnProducto, Map<String, ArrayList<Libro>> catalogo) {
	for (ArrayList<Libro> libros : catalogo.values()) {
	    for (Libro libroCatalogo : libros) {
		if (libroCatalogo.getIsbn().equals(isbnProducto)) {
		    return libroCatalogo;
		}
	    }
	}
	return null;
    }

    /**
     * Método que calcula el valor total del carrito
     *
     * @return valor total del carrito
     * @throws RuntimeException 
     * @throws SQLException 
     */
    public ValorCompra calculoResumenCompra(ReciboDAO reciboDAO) throws IOException, SQLException, RuntimeException {
	ValorCompra valorCompra = new ValorCompra();
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null || libroCarrito.getCorreo_usuario().isBlank()) {
	    RegistroLog.registrarAdvertencia("❗ Se intentó seleccionar registros con un correo de usuario nulo o vacío.");
	    throw new RuntimeException("⚠️ No se proporcionó un usuario válido para consultar su carrito.");
	}
	ArrayList<LibroCarrito> librosCarritoUsuario = carritoDAO.seleccionarRegistros(libroCarrito);
	RegistroLog.registrarInfo("📦 Se encontraron " + librosCarritoUsuario.size() + " libros en el carrito del usuario: " + libroCarrito.getCorreo_usuario());
	setValorCompra(valorCompra, librosCarritoUsuario);
	Recibo recibo = new Recibo();
	recibo.setCorreo(gestionUsuario.userLog().getCuenta().getCorreo());
	setTotal(reciboDAO, valorCompra, recibo);
	return valorCompra;
    }

    private void setTotal(ReciboDAO reciboDAO, ValorCompra valorCompra, Recibo recibo)
	    throws IOException, SQLException {
	valorCompra.setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(reciboDAO.seleccionarRegistrosCompras(recibo), valorCompra.getTotal()));
	valorCompra.setTotal(valorCompra.getTotal() - valorCompra.getDescuentoPremium());
    }

    private void setValorCompra(ValorCompra valorCompra, ArrayList<LibroCarrito> librosCarritoUsuario)
	    throws SQLException {
	valorCompra.setImpuestos(calculadoraIVA.impuestos(librosCarritoUsuario, libroDAO));
	valorCompra.setSubtotal(calculadoraIVA.subtotal(librosCarritoUsuario, libroDAO));
	valorCompra.setTotal(calculadoraIVA.total(valorCompra.getSubtotal(), valorCompra.getImpuestos()));
	valorCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(valorCompra.getTotal(), gestionUsuario.userLog()));
    }

    
    public void disminuirStock() throws IOException, SQLException, RuntimeException {
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null || libroCarrito.getCorreo_usuario().isBlank()) {
	    RegistroLog.registrarAdvertencia("❗ Se intentó seleccionar registros con un correo de usuario nulo o vacío.");
	    throw new RuntimeException("⚠️ No se proporcionó un usuario válido para consultar su carrito.");
	}
	ArrayList<LibroCarrito> librosCarritoUser = carritoDAO.seleccionarRegistros(libroCarrito);
	Iterator<LibroCarrito> iteratorCarritoUser = librosCarritoUser.iterator();
	while (iteratorCarritoUser.hasNext()) {
	    libroCarrito = iteratorCarritoUser.next();
	     Libro libroCatalogo = new Libro();
	     libroCatalogo.setIsbn(String.valueOf(libroCarrito.getIsbn_libro()));
	     libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
	     libroCatalogo.confirmarCompra(libroCarrito.getCantidad());
	     carritoDAO.eliminarRegistro(libroCarrito);
	     libroDAO.actualizarDatos(libroCatalogo);
	     iteratorCarritoUser.remove();
	}
    }
}
