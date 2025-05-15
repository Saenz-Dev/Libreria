package co.edu.uptc.negocio;

import co.edu.uptc.modelo.*;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.CuentaDAO;
import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.UsuarioDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class GestionTienda {

    private Tienda tienda;
    private GestionUsuario gestionUsuario;
    private GestionLibro gestionLibro;
    private GestionCatalogo gestionCatalogo;
    private GestionCarrito gestionCarrito;
    private GestionCompra gestionCompra;
    private GestionComentario gestionComentario;
    private CarritoDAO carritoDAO;
    private UsuarioDAO usuarioDAO;
    private CuentaDAO cuentaDAO;
    private LibroDAO libroDAO;

    public GestionTienda() throws SQLException {
	tienda = new Tienda();
	carritoDAO = new CarritoDAO();
	usuarioDAO = new UsuarioDAO();
	cuentaDAO = new CuentaDAO();
	libroDAO = new LibroDAO();
	gestionUsuario = new GestionUsuario(tienda, usuarioDAO, cuentaDAO, carritoDAO);
	gestionLibro = new GestionLibro(tienda, libroDAO);
	gestionCatalogo = new GestionCatalogo(tienda, libroDAO);
	gestionCarrito = new GestionCarrito(gestionUsuario.getManejoUsuarioJSON(), tienda, carritoDAO, usuarioDAO,
		cuentaDAO, libroDAO, gestionUsuario);
	gestionCompra = new GestionCompra(tienda);
	gestionComentario = new GestionComentario(tienda);
    }

    // -----------------------------------Métodos
    // GestionUsuario-----------------------------------

    public Usuario getUserLogin() throws SQLException, RuntimeException {
	return gestionUsuario.userLog();
    }

    public void asignarUsuarioGenerico() throws IOException, SQLException {
	gestionUsuario.asignarUsuarioGenerico();
    }

    public void iniciarSesion(String correo, String contrasena) throws SQLException {
	gestionUsuario.iniciarSesion(correo, contrasena);
    }

    public boolean isAdminLogin() {
	return gestionUsuario.isAdminLogin();
    }

    public void cerrarSesion() throws IOException, RuntimeException, SQLException {
	gestionUsuario.cerrarSesionUsuario();
    }

    public void registrarUsuario(Usuario usuario) throws RuntimeException, SQLException {
	gestionUsuario.registrarUsuario(usuario);
    }

    public void modificarUsuario(Usuario usuario) throws IOException, RuntimeException, SQLException {
	usuario.getCuenta().setLog(true);
	gestionUsuario.modificarUsuario(usuario);
    }

    public boolean isGenericoLogin() {
	return gestionUsuario.isDefaultUserLogin();
    }

    // ----------------------------------------Métodos de
    // GestionLibro---------------------------------------------

    public String[] obtenerTitulosLibros() throws SQLException, RuntimeException {
	return gestionLibro.obtenerLibros();
    }

    public Libro buscarLibro(String titulo) throws SQLException {
	return gestionLibro.buscarLibro(titulo);
    }

    public void eliminarLibro(ArrayList<String> listaIsbn) throws IllegalArgumentException, IOException, SQLException {
	gestionLibro.eliminarLibro(listaIsbn);
    }

    public void modificarLibro(Libro libro) throws RuntimeException, IOException, SQLException {
	gestionLibro.modificarLibro(libro);
    }

    public void registrarLibro(Libro libro) throws IOException, RuntimeException, SQLException {
	gestionLibro.registrarLibro(libro);
    }

    public boolean validarExistenciaLibro(String isbnLibro) throws SQLException, RuntimeException {
	return gestionLibro.validarExistencia(isbnLibro);
    }

    // ---------------------------------------------Métodos de
    // GestionCatalogo--------------------------------------------

    public ArrayList<Libro> listarLibros() throws SQLException {
	return gestionCatalogo.listarLibros();
    }

    // ---------------------------------------------Métodos de
    // GesionCarrito----------------------------------------------------------------------

    public ValorCompra resumenCompra() throws IOException, SQLException, RuntimeException {
	// gestionCompra.getManejoCompraJSON().leerCompras();
	return gestionCarrito.calculoResumenCompra();
    }

    public void anadirLibrosCarrito(String isbnLibro, int cantidad) throws RuntimeException, IOException, SQLException {
	gestionCarrito.anadirLibrosCarrito(isbnLibro, cantidad);
    }

    public ResumenProductoDTO sumarProductos(String isbnProducto) throws IOException, SQLException, RuntimeException {
	return gestionCarrito.sumarProducto(isbnProducto);
    }

    public void eliminarProductoCarrito(String isbnProducto) throws IOException, SQLException, RuntimeException {
	gestionCarrito.eliminarProducto(isbnProducto);
    }

    public ResumenProductoDTO disminuirProductoCarrito(String isbnProducto) throws IOException, SQLException, RuntimeException {
	return gestionCarrito.disminuirProducto(isbnProducto);
    }

    public void eliminarLibroUsuarioGenerico() throws IOException, SQLException, RuntimeException {
	LibroCarrito libroCarrito  = new LibroCarrito();
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	ArrayList<LibroCarrito> librosCarritoDefaul = carritoDAO.seleccionarRegistros(libroCarrito);
	if (librosCarritoDefaul == null || librosCarritoDefaul.isEmpty()) throw new IllegalArgumentException("El usuario default no tiene libros");
	for (LibroCarrito libroCarritoDefautl : librosCarritoDefaul) {
	    Libro libroCatalogo = new Libro();
	    libroCatalogo.setIsbn(String.valueOf(libroCarritoDefautl.getIsbn_libro()));
	    libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
	    libroCatalogo.setIsComprado(gestionCarrito.validarComprado(librosCarritoDefaul, libroCatalogo.getIsbn()));
	    libroCatalogo.eliminarReserva(libroCarritoDefautl.getCantidad());
	    carritoDAO.eliminarRegistro(libroCarritoDefautl);
	    libroDAO.actualizarDatos(libroCatalogo);
	    //eliminarProductoCarrito(libro.getIsbn());
	    /*if (getUserLogin().getCarrito().getLibros().isEmpty()) {
		break;
	    }*/
	}
    }

    // Metodos de GestionCompra

    public void registrarCompra(ArrayList<String> listaIsbn, TipoPago tipoPago) throws IOException {
	gestionCompra.aggListaCompra(listaIsbn, gestionCarrito.getManejoUsuarioJSON().getUsuarioLogin(), tipoPago);
	gestionCarrito.disminuirStock();
    }

    public ArrayList<Recibo> getComprasUserLogin() throws IOException {
	gestionCompra.getManejoCompraJSON().leerCompras();
	return tienda.getRecibos().get(gestionCarrito.getManejoUsuarioJSON().getUsuarioLogin().getCuenta().getCorreo());
    }

    public Carrito carritoUserLog() {
	return gestionCarrito.getManejoUsuarioJSON().getUsuarioLogin().getCarrito();
    }

    public ArrayList<ProductoCompra> listaCarrito() throws SQLException, RuntimeException {
	ArrayList<ProductoCompra> listaCarrito = new ArrayList<>();
	CalculadoraIVA calculadoraIVA = new CalculadoraIVA();
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	ArrayList<LibroCarrito> librosCarrito= carritoDAO.seleccionarRegistros(libroCarrito);//Devuelve una lista de libros del carrito del usuario
	if (librosCarrito == null) {
	    throw new IllegalArgumentException("No se encuentran libros en el carrito,");
	}
	for (LibroCarrito libroCarritoUser : librosCarrito) {
	    Libro libro = new Libro();
	    libro.setIsbn(String.valueOf(libroCarritoUser.getIsbn_libro()));
	    libro = libroDAO.seleccionarRegistro(libro);
	    ProductoCompra productoCompra = new ProductoCompra();
	    productoCompra.setTitulo(libro.getTitulo());
	    productoCompra.setIsbn(libro.getIsbn());
	    productoCompra.setNumeroLibros(libroCarritoUser.getCantidad());
	    productoCompra.setPrecioUnitario(libro.getPrecioVenta());
	   
	    libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	    libroCarrito.setIsbn_libro(Long.parseLong(libro.getIsbn()));
	    productoCompra.setPrecioTotal(calculadoraIVA.subtotalProducto(libroCarritoUser, libro));
	    listaCarrito.add(productoCompra);
	}
	return listaCarrito;
	
	
	/*ArrayList<ProductoCompra> listaCarrito = new ArrayList<>();
	CalculadoraIVA calculadoraIVA = new CalculadoraIVA();
	for (Libro libro : carritoUserLog().getLibros()) {
	    ProductoCompra productoCompra = new ProductoCompra();
	    productoCompra.setTitulo(libro.getTitulo());
	    productoCompra.setIsbn(libro.getIsbn());
	    productoCompra.setNumeroLibros(libro.getStockReservado());
	    productoCompra.setPrecioUnitario(libro.getPrecioVenta());
	    productoCompra.setPrecioTotal(calculadoraIVA.subtotalProducto( libro, carritoUserLog().getLibros()));
	    listaCarrito.add(productoCompra);
	}
	return listaCarrito;*/
    }

    public ValorCompra valorCompra() throws IOException, SQLException, RuntimeException {
	ValorCompra valorCompra = new ValorCompra();
	CalculadoraIVA calculadoraIVA = new CalculadoraIVA();
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	ArrayList<LibroCarrito> librosCarritoUsuario = carritoDAO.seleccionarRegistros(libroCarrito);
	valorCompra.setImpuestos(calculadoraIVA.impuestos(librosCarritoUsuario, libroDAO));
	valorCompra.setSubtotal(calculadoraIVA.subtotal(librosCarritoUsuario, libroDAO));
	valorCompra.setTotal(calculadoraIVA.total(valorCompra.getSubtotal(), valorCompra.getImpuestos()));
	valorCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(valorCompra.getTotal(),
		gestionUsuario.userLog()));
	valorCompra.setDescuentoFrecuencia(
		calculadoraIVA.descuentoFrecuencia(valorCompra.getTotal(), tienda, gestionUsuario.userLog()));
	valorCompra.setTotal(valorCompra.getTotal() - valorCompra.getDescuentoPremium());
	return valorCompra;
    }

    public void guardarComentario(Comentario comentario) throws IOException, RuntimeException {
	comentario.setCorreo(gestionCarrito.getManejoUsuarioJSON().getUsuarioLogin().getCuenta().getCorreo());
	comentario.setUsuario(gestionCarrito.getManejoUsuarioJSON().getUsuarioLogin().getNombre());
	comentario.fechaActual();
	gestionComentario.registrarComentario(comentario);
    }

    public Stack<Comentario> listarComentarios(String isbn) throws IOException, RuntimeException {
	return gestionComentario.buscarComentario(isbn);
    }
}
