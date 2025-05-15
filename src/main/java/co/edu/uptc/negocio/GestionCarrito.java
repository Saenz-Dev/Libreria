package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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
     * @param manejoUsuarioJSON Instancia deManejo de usuarios con JSON
     * @throws SQLException
     */
    public GestionCarrito(ManejoUsuarioJSON manejoUsuarioJSON, Tienda tienda, CarritoDAO carritoDAO,
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
	crearTabla();
    }

    public void crearTabla() throws SQLException {
	carritoDAO.crearTabla();
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
	usuarioDAO.actualizarDatos(usuarioLogin);
	libroDAO.actualizarDatos(libroCatalogo);
	carritoDAO.actualizarDatos(libroCarrito);
	/*
	 * manejoUsuarioJSON.modificarUsuarioCarrito(usuarioLogin);
	 * manejoLibroJSON.modificarLibro(libroCatalogo);
	 */
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
	/*if (libroCarrito == null)
	    throw new IllegalArgumentException("No hay ejemplares de este libro en el carrito");*/
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
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setIsbn_libro(Long.parseLong(isbnProducto));
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	libroCarrito = carritoDAO.seleccionarRegistro(libroCarrito);
	
	Libro libroCatalogo = new Libro();
	libroCatalogo.setIsbn(isbnProducto);
	libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
	
	
	if (libroCarrito == null || libroCatalogo == null) {
	    throw new IllegalArgumentException("No se pudo sumar el producto con ISBN:" + isbnProducto);
	}
	
	if (libroCatalogo.getStockDisponible() == 0) {
	    throw new IllegalArgumentException("El libo agotado.");
	}
	
	libroCatalogo.reservarLibro();
	libroCarrito.aumentarCantidad(1);
	carritoDAO.actualizarDatos(libroCarrito);
	libroDAO.actualizarDatos(libroCatalogo);
	
	return actualizarProductoCarrito(isbnProducto);
	
	
	/*ArrayList<Libro> librosCarrito = manejoUsuarioJSON.getUsuarioLogin().getCarrito().getLibros();
	Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
	int index = buscarIndexProducto(isbnProducto, librosCarrito);

	if (index >= 0) {
	    Libro libroModificar = encontrarLibro(isbnProducto, catalogo);
	    if (libroModificar.getStockDisponible() == 0)
		throw new IllegalArgumentException("Libro agotado.");

	    libroModificar.reservarLibro();
	    librosCarrito.get(index).aumentarCantidad(1);

	    return actualizarProductoCarrito(librosCarrito.get(index), librosCarrito, catalogo, index);
	}
	return null;*/
    }

    public int buscarIndexProducto(String isbnProducto, ArrayList<Libro> librosCarrito) {
	int index = 0;
	for (Libro libro : librosCarrito) {
	    if (libro.getIsbn().equals(isbnProducto)) {
		return index;
	    }
	    index++;
	}
	return -1;
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
    public ResumenProductoDTO actualizarProductoCarrito(String isbn/*, Libro productoCarrito, ArrayList<Libro> librosCarrito,
	    Map<String, ArrayList<Libro>> catalogo, int index*/) throws IOException, SQLException, RuntimeException {
	/*actualizarCantidadProducto(catalogo);*/

	ResumenProductoDTO resumenProductoDTO = new ResumenProductoDTO();
	LibroCarrito libroCarrito = new LibroCarrito();
	Libro libroCatalogo = new Libro();
	
	libroCarrito.setIsbn_libro(Long.parseLong(isbn));
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	libroCarrito = carritoDAO.seleccionarRegistro(libroCarrito);
	
	libroCatalogo.setIsbn(isbn);
	libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
	
	resumenProductoDTO.setSubtotal(calculadoraIVA.subtotalProducto(libroCarrito, libroCatalogo));
	resumenProductoDTO.setCantidadReservada(libroCarrito.getCantidad());
	
	/*resumenProductoDTO.setSubtotal(calculadoraIVA.subtotalProducto(productoCarrito, librosCarrito));
	resumenProductoDTO.setCantidadReservada(librosCarrito.get(index).getStockReservado());*/
	return resumenProductoDTO;
    }

    /**
     * Actualiza la cantidad de los productos del catalogo y actualiza el usuario
     * logueado.
     * 
     * @param catalogo catalogo existente en la tienda.
     * @throws IOException si ocurre alguna excepción al serializar los datos.
     */
    private void actualizarCantidadProducto(Map<String, ArrayList<Libro>> catalogo) throws IOException {
	
	
	manejoUsuarioJSON.escribirUsuarioLogin();
	manejoLibroJSON.escribirLibros(catalogo);
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
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setIsbn_libro(Long.parseLong(isbnProducto));
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	libroCarrito = carritoDAO.seleccionarRegistro(libroCarrito);
	
	Libro libroCatalogo = new Libro();
	libroCatalogo.setIsbn(isbnProducto);
	libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
	
	
	if (libroCarrito == null || libroCatalogo == null) {
	    throw new IllegalArgumentException("No se pudo sumar el producto con ISBN:" + isbnProducto);
	}
	
	libroCatalogo.cancelarReserva();
	libroCarrito.disminuirCantidad(1);
	
	carritoDAO.actualizarDatos(libroCarrito);
	libroDAO.actualizarDatos(libroCatalogo);
	
	return actualizarProductoCarrito(isbnProducto);
	
	
	/*ArrayList<Libro> librosCarrito = manejoUsuarioJSON.getUsuarioLogin().getCarrito().getLibros();
	Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
	int index = buscarIndexProducto(isbnProducto, librosCarrito);

	if (index >= 0) {
	    Libro libroModificar = encontrarLibro(isbnProducto, catalogo);
	    // if (libroModificar.getStockDisponible() == 0) throw new
	    // IllegalArgumentException("Libro agotado.");
	    libroModificar.cancelarReserva();
	    librosCarrito.get(index).disminuirCantidadUnidad();

	    return actualizarProductoCarrito(librosCarrito.get(index), librosCarrito, catalogo, index);
	}
	return null;*/
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
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setIsbn_libro(Long.parseLong(isbnProducto));
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	libroCarrito = carritoDAO.seleccionarRegistro(libroCarrito);
	
	Libro libroCatalogo = new Libro();
	libroCatalogo.setIsbn(isbnProducto);
	libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
	
	
	if (libroCarrito == null || libroCatalogo == null) {
	    throw new IllegalArgumentException("No se pudo sumar el producto con ISBN:" + isbnProducto);
	}
	
	ArrayList<LibroCarrito> librosCarrito = carritoDAO.seleccionarRegistros();
	libroCatalogo.setIsComprado(validarComprado(librosCarrito, isbnProducto));
	libroCatalogo.eliminarReserva(libroCarrito.getCantidad());
	
	carritoDAO.eliminarRegistro(libroCarrito);
	libroDAO.actualizarDatos(libroCatalogo);
	
	
	
	/*ArrayList<Libro> librosCarrito = manejoUsuarioJSON.getUsuarioLogin().getCarrito().getLibros();
	Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
	int index = buscarIndexProducto(isbnProducto, librosCarrito);

	if (index >= 0) {
	    Libro libroModificar = encontrarLibro(isbnProducto, catalogo);
	    libroModificar.setIsComprado(isComprado(isbnProducto));
	    libroModificar.eliminarReserva(librosCarrito.get(index).getStockReservado());
	    manejoLibroJSON.escribirLibros(catalogo);
	    librosCarrito.remove(index);

	    actualizarCantidadProducto(catalogo);
	}*/
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

    /*public boolean isComprado(String isbn) {
	TreeMap<String, ArrayList<Recibo>> datosRecibos = manejoLibroJSON.getTienda().getRecibos();
	if (datosRecibos.isEmpty()) {
	    return false;
	}
	for (ArrayList<Recibo> listaRecibo : datosRecibos.values()) {
	    for (Recibo recibo : listaRecibo) {
		for (ProductoCompra productoCompra : recibo.getListaProductosComprados()) {
		    if (productoCompra.getIsbn().equals(isbn)) {
			return true;
		    }
		}
	    }
	}
	return false;
    }*/

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
    public ValorCompra calculoResumenCompra() throws IOException, SQLException, RuntimeException {
	ValorCompra valorCompra = new ValorCompra();
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	ArrayList<LibroCarrito> librosCarritoUsuario = carritoDAO.seleccionarRegistros(libroCarrito);
	valorCompra.setImpuestos(calculadoraIVA.impuestos(librosCarritoUsuario, libroDAO));
	valorCompra.setSubtotal(calculadoraIVA.subtotal(librosCarritoUsuario, libroDAO));
	valorCompra.setTotal(calculadoraIVA.total(valorCompra.getSubtotal(), valorCompra.getImpuestos()));
	valorCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(valorCompra.getTotal(), gestionUsuario.userLog()));
	//valorCompra.setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(valorCompra.getTotal(), manejoUsuarioJSON.getTienda(), manejoUsuarioJSON.getUsuarioLogin()));
	//TODO revisar si se esta restando bien el descuento premium y el descuento por frecuencia
	valorCompra.setTotal(valorCompra.getTotal() - valorCompra.getDescuentoPremium());
	return valorCompra;
	
	/*Usuario usuario = manejoUsuarioJSON.getUsuarioLogin();
	ValorCompra valorCompra = new ValorCompra();
	if (usuario.getCarrito().getLibros().isEmpty())
	    return valorCompra;
	Carrito carritoLocal = usuario.getCarrito();
	valorCompra.setImpuestos(calculadoraIVA.impuestos(carritoLocal));
	valorCompra.setSubtotal(calculadoraIVA.subtotal(carritoLocal));
	valorCompra.setTotal(calculadoraIVA.total(valorCompra.getSubtotal(), valorCompra.getImpuestos()));
	valorCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(valorCompra.getTotal(), manejoUsuarioJSON.getUsuarioLogin()));
	valorCompra.setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(valorCompra.getTotal(), manejoUsuarioJSON.getTienda(), manejoUsuarioJSON.getUsuarioLogin()));
	valorCompra.setTotal(valorCompra.getTotal() - valorCompra.getDescuentoPremium());*/
    }

    
    //TODO falta implementar este metodo
    public void disminuirStock() throws IOException {
	Usuario userLogin = manejoUsuarioJSON.getUsuarioLogin();
	Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
	Iterator<Libro> iteratorCarrito = userLogin.getCarrito().getLibros().iterator();
	while (iteratorCarrito.hasNext()) {
	    Libro libro = iteratorCarrito.next();
	    Libro libroDisminuir = encontrarLibro(libro.getIsbn(), catalogo);
	    libroDisminuir.confirmarCompra(libro.getStockReservado());
	    iteratorCarrito.remove();
	}
	manejoLibroJSON.escribirLibros(catalogo);
	manejoUsuarioJSON.escribirUsuario();
    }
}
