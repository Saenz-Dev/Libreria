package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Stack;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Carrito;
import co.edu.uptc.modelo.Comentario;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.LibroCarrito;
import co.edu.uptc.modelo.ProductoCompra;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.ResumenProductoDTO;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.TipoPago;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.ValorCompra;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.ComentarioDAO;
import co.edu.uptc.persistencia.CompraDAO;
import co.edu.uptc.persistencia.CuentaDAO;
import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.ReciboDAO;
import co.edu.uptc.persistencia.UsuarioDAO;

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
    private ReciboDAO reciboDAO;
    private ComentarioDAO comentarioDAO;
    private CompraDAO compraDAO;

    public GestionTienda() throws SQLException {
	tienda = new Tienda();
	carritoDAO = new CarritoDAO();
	usuarioDAO = new UsuarioDAO();
	cuentaDAO = new CuentaDAO();
	libroDAO = new LibroDAO();
	reciboDAO = new ReciboDAO();
	compraDAO = new CompraDAO();
	comentarioDAO = new ComentarioDAO();
	gestionUsuario = new GestionUsuario(tienda, usuarioDAO, cuentaDAO, carritoDAO);
	gestionLibro = new GestionLibro(tienda, libroDAO);
	gestionCatalogo = new GestionCatalogo(tienda, libroDAO);
	gestionCarrito = new GestionCarrito(gestionUsuario.getManejoUsuarioJSON(), tienda, carritoDAO, usuarioDAO, cuentaDAO, libroDAO, gestionUsuario);
	gestionCompra = new GestionCompra(tienda, reciboDAO, carritoDAO, compraDAO);
	gestionComentario = new GestionComentario(tienda, comentarioDAO);
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

    public void cerrarSesion(boolean cerrarAplicacion) throws IOException, RuntimeException, SQLException {
	gestionUsuario.cerrarSesionUsuario(cerrarAplicacion);
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

    public void eliminarLibro(ArrayList<String> listaIsbn) throws RuntimeException, IOException, SQLException {
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
	return gestionCarrito.calculoResumenCompra(reciboDAO);
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
	if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null || libroCarrito.getCorreo_usuario().isBlank()) {
	    RegistroLog.registrarAdvertencia("❗ Se intentó seleccionar registros con un correo de usuario nulo o vacío.");
	    throw new RuntimeException("⚠️ No se proporcionó un usuario válido para consultar su carrito.");
	}
	ArrayList<LibroCarrito> librosCarritoDefaul = carritoDAO.seleccionarRegistros(libroCarrito);
	if (librosCarritoDefaul == null || librosCarritoDefaul.isEmpty())return;// throw new IllegalArgumentException("El usuario default no tiene libros");
	for (LibroCarrito libroCarritoDefautl : librosCarritoDefaul) {
	    Libro libroCatalogo = new Libro();
	    libroCatalogo.setIsbn(String.valueOf(libroCarritoDefautl.getIsbn_libro()));
	    libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
	    libroCatalogo.setIsComprado(gestionCarrito.validarComprado(librosCarritoDefaul, libroCatalogo.getIsbn()));
	    libroCatalogo.eliminarReserva(libroCarritoDefautl.getCantidad());
	    carritoDAO.eliminarRegistro(libroCarritoDefautl);
	    libroDAO.actualizarDatos(libroCatalogo);
	}
    }

    // Metodos de GestionCompra

    public void registrarCompra(ArrayList<String> listaIsbn, TipoPago tipoPago) throws IOException, SQLException, RuntimeException {
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null || libroCarrito.getCorreo_usuario().isBlank()) {
	    RegistroLog.registrarAdvertencia("❗ Se intentó seleccionar registros con un correo de usuario nulo o vacío.");
	    throw new RuntimeException("⚠️ No se proporcionó un usuario válido para consultar su carrito.");
	}
	ArrayList<LibroCarrito> listaLibrosCarrito = carritoDAO.seleccionarRegistros(libroCarrito);
	if (listaLibrosCarrito == null || listaLibrosCarrito.isEmpty()) {
	    throw new IllegalArgumentException("No puede continuar con la compra, no tiene productos en el carrito...");
	}
	gestionCompra.aggListaCompra(getUserLogin(), tipoPago, usuarioDAO, libroDAO);
	gestionCarrito.disminuirStock();
    }
    //TODO modificar metodo para que envie compras y no recibos
    public ArrayList<Recibo> getComprasUserLogin() throws IOException, SQLException, RuntimeException {
	Recibo recibo = new Recibo();
	recibo.setCorreo(gestionUsuario.userLog().getCuenta().getCorreo());
	return reciboDAO.seleccionarRegistrosCompras(recibo);
	/*gestionCompra.getManejoCompraJSON().leerCompras();
	return tienda.getRecibos().get(gestionCarrito.getManejoUsuarioJSON().getUsuarioLogin().getCuenta().getCorreo());*/
    }    
    
    public Recibo reciboUsuario() throws IOException, SQLException, RuntimeException {
	Recibo recibo = new Recibo();
	Usuario usuarioLog = gestionUsuario.userLog(); //Se utiliza el metodo que devuelve el usuario logueado
	recibo.setCorreo(usuarioLog.getCuenta().getCorreo()); //Y tambien el correo del usuario
	recibo.setNumeroRecibo(compraDAO.seleccionarRegistros().size()); //En la BD compras se busca el numero de compra
	recibo = reciboDAO.seleccionarRegistroNumero(recibo);
	recibo.setNombreUser(usuarioLog.getNombre()); //Se asigna el nombre del usuario logueado// Y con este dato se manda por parametro a recibo para buscar cuales fueron los productos comprados
	buscarNombresLibros(recibo); //Se busca los nombres de los libros y se asignan al recibo
	return recibo; // Y se retorna el recibo
	/*gestionCompra.getManejoCompraJSON().leerCompras();
	return tienda.getRecibos().get(gestionCarrito.getManejoUsuarioJSON().getUsuarioLogin().getCuenta().getCorreo());*/
    }
    
    public void buscarNombresLibros(Recibo recibo) throws SQLException, RuntimeException {
	for (ProductoCompra productoCompra : recibo.getListaProductosComprados()) {
	    Libro libroCatalogo = new Libro();
	    libroCatalogo.setIsbn(productoCompra.getIsbn());
	    libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
	    productoCompra.setTitulo(libroCatalogo.getTitulo());
	}
    }

    public Carrito carritoUserLog() {
	return gestionCarrito.getManejoUsuarioJSON().getUsuarioLogin().getCarrito();
    }

    public ArrayList<ProductoCompra> listaCarrito() throws SQLException, RuntimeException {
	ArrayList<ProductoCompra> listaCarrito = new ArrayList<>();
	CalculadoraIVA calculadoraIVA = new CalculadoraIVA();
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null || libroCarrito.getCorreo_usuario().isBlank()) {
	    RegistroLog.registrarAdvertencia("❗ Se intentó seleccionar registros con un correo de usuario nulo o vacío.");
	    throw new RuntimeException("⚠️ No se proporcionó un usuario válido para consultar su carrito.");
	}
	ArrayList<LibroCarrito> librosCarrito= carritoDAO.seleccionarRegistros(libroCarrito);//Devuelve una lista de libros del carrito del usuario
	if (librosCarrito == null) {
	    throw new IllegalArgumentException("No se encuentran libros en el carrito,");
	}
	for (LibroCarrito libroCarritoUser : librosCarrito) {
	    ProductoCompra productoCompra = aggInfoProductoCompra(calculadoraIVA, libroCarrito, libroCarritoUser);
	    listaCarrito.add(productoCompra);
	}
	return listaCarrito;
    }

    private ProductoCompra aggInfoProductoCompra(CalculadoraIVA calculadoraIVA, LibroCarrito libroCarrito,
	    LibroCarrito libroCarritoUser) throws SQLException {
	Libro libro = new Libro();
	libro.setIsbn(String.valueOf(libroCarritoUser.getIsbn_libro()));
	libro = libroDAO.seleccionarRegistro(libro);
	ProductoCompra productoCompra = setProductoCompra(libroCarritoUser, libro);
  
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	libroCarrito.setIsbn_libro(Long.parseLong(libro.getIsbn()));
	productoCompra.setPrecioTotal(calculadoraIVA.subtotalProducto(libroCarritoUser, libro));
	return productoCompra;
    }

    private ProductoCompra setProductoCompra(LibroCarrito libroCarritoUser, Libro libro) {
	ProductoCompra productoCompra = new ProductoCompra();
	productoCompra.setTitulo(libro.getTitulo());
	productoCompra.setIsbn(libro.getIsbn());
	productoCompra.setNumeroLibros(libroCarritoUser.getCantidad());
	productoCompra.setPrecioUnitario(libro.getPrecioVenta());
	return productoCompra;
    }

    public ValorCompra valorCompra() throws IOException, SQLException, RuntimeException {
	ValorCompra valorCompra = new ValorCompra();
	CalculadoraIVA calculadoraIVA = new CalculadoraIVA();
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setCorreo_usuario(gestionUsuario.userLog().getCuenta().getCorreo());
	if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null || libroCarrito.getCorreo_usuario().isBlank()) {
	    RegistroLog.registrarAdvertencia("❗ Se intentó seleccionar registros con un correo de usuario nulo o vacío.");
	    throw new RuntimeException("⚠️ No se proporcionó un usuario válido para consultar su carrito.");
	}
	setValorCompra(valorCompra, calculadoraIVA, libroCarrito);
	return valorCompra;
    }

    private void setValorCompra(ValorCompra valorCompra, CalculadoraIVA calculadoraIVA, LibroCarrito libroCarrito)
	    throws SQLException, IOException {
	ArrayList<LibroCarrito> librosCarritoUsuario = carritoDAO.seleccionarRegistros(libroCarrito);
	valorCompra.setImpuestos(calculadoraIVA.impuestos(librosCarritoUsuario, libroDAO));
	valorCompra.setSubtotal(calculadoraIVA.subtotal(librosCarritoUsuario, libroDAO));
	valorCompra.setTotal(calculadoraIVA.total(valorCompra.getSubtotal(), valorCompra.getImpuestos()));
	valorCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(valorCompra.getTotal(), gestionUsuario.userLog()));
	Recibo recibo = new Recibo();
	recibo.setCorreo(gestionUsuario.userLog().getCuenta().getCorreo());
	valorCompra.setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(reciboDAO.seleccionarRegistrosCompras(recibo), valorCompra.getTotal()));
	valorCompra.setTotal(valorCompra.getTotal() - valorCompra.getDescuentoPremium());
    }

    public void guardarComentario(Comentario comentario) throws IOException, RuntimeException, SQLException {
	Usuario userLog = gestionUsuario.userLog();
	comentario.setCorreo(userLog.getCuenta().getCorreo());
	comentario.setUsuario(userLog.getNombre());
	comentario.setFecha(LocalDateTime.now());
	gestionComentario.registrarComentario(comentario);
    }

    public Stack<Comentario> listarComentarios(String isbn) throws IOException, RuntimeException, SQLException {
	Stack<Comentario> comentarios = gestionComentario.buscarComentario(isbn);
	Usuario usuarioConsulta = new Usuario();
	Libro libroConsulta= new Libro();
	for (Comentario comentario : comentarios) {
	    usuarioConsulta.getCuenta().setCorreo(comentario.getCorreo());
	    libroConsulta.setIsbn(isbn);
	    comentario.setUsuario(usuarioDAO.seleccionarRegistro(usuarioConsulta).getNombre());
	    comentario.setTituloLibro(libroDAO.seleccionarRegistro(libroConsulta).getTitulo());
	}
	return comentarios;
    }

    public Recibo comprasUsuarioLog(String fecha, int numeroCompra) throws SQLException, RuntimeException {
	Recibo recibo = new Recibo();
	recibo.setNumeroRecibo(numeroCompra);
	DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
	recibo.setFecha(LocalDateTime.parse(fecha, formater));
	Recibo compraRecibo = compraDAO.seleccionarRegistro(recibo);
	if (compraRecibo == null) {
	    throw new RuntimeException("Compra no encontrada");
	}

	Recibo reciboFinal = reciboDAO.seleccionarRegistro(compraRecibo);
	if (reciboFinal == null) {
	    throw new RuntimeException("Recibo no encontrado");
	}
	reciboFinal.setNombreUser(gestionUsuario.userLog().getNombre());
	buscarNombresLibros(reciboFinal);
	return reciboFinal;
	
    }
}
