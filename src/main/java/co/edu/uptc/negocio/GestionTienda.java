package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.*;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.CodigoDAO;
import co.edu.uptc.persistencia.ComentarioDAO;
import co.edu.uptc.persistencia.CompraDAO;
import co.edu.uptc.persistencia.CuentaDAO;
import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.ReciboDAO;
import co.edu.uptc.persistencia.UsuarioDAO;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

public class GestionTienda {

    private Tienda tienda;
    private GestionUsuario gestionUsuario;
    private GestionLibro gestionLibro;
    private GestionCatalogo gestionCatalogo;
    private GestionCarrito gestionCarrito;
    private GestionCompra gestionCompra;
    private GestionComentario gestionComentario;
    private GestionCodigo gestionCodigo;
    private CarritoDAO carritoDAO;
    private UsuarioDAO usuarioDAO;
    private CuentaDAO cuentaDAO;
    private LibroDAO libroDAO;
    private ReciboDAO reciboDAO;
    private ComentarioDAO comentarioDAO;
    private CompraDAO compraDAO;
    private CodigoDAO codigoDAO;
    private CalculadoraIVA calculadoraIVA;

    public GestionTienda() throws SQLException {
        tienda = new Tienda();
        carritoDAO = new CarritoDAO();
        usuarioDAO = new UsuarioDAO();
        cuentaDAO = new CuentaDAO();
        libroDAO = new LibroDAO();
        reciboDAO = new ReciboDAO();
        compraDAO = new CompraDAO();
        comentarioDAO = new ComentarioDAO();
        codigoDAO = new CodigoDAO();
        gestionUsuario = new GestionUsuario(tienda, usuarioDAO, cuentaDAO, carritoDAO);
        gestionLibro = new GestionLibro(tienda, libroDAO);
        gestionCatalogo = new GestionCatalogo(tienda, libroDAO);
        gestionCarrito = new GestionCarrito(tienda, carritoDAO, usuarioDAO, libroDAO, gestionUsuario);
        gestionCompra = new GestionCompra(tienda, reciboDAO, carritoDAO, compraDAO);
        gestionComentario = new GestionComentario(tienda, comentarioDAO);
        gestionCodigo = new GestionCodigo(codigoDAO);
        calculadoraIVA = new CalculadoraIVA();
    }

    public Tienda getTienda() {
        return tienda;
    }

    public Usuario getUserLogin() throws SQLException, RuntimeException {
        return gestionUsuario.usuarioLogueado();
    }

    public void asignarUsuarioGenerico() throws IOException, SQLException {
        gestionUsuario.asignarUsuarioGenerico();
    }

    public void iniciarSesion(String correo, String contrasena) throws SQLException {
        gestionUsuario.iniciarSesion(correo, contrasena);
        tienda.getUsuarioActual().getCarrito().setLibros(carritoDAO.seleccionarRegistros(tienda.getUsuarioActual().getCuenta().getCorreo()));
        buscarInfoCarrito(tienda.getUsuarioActual().getCarrito().getLibros());
        tienda.getUsuarioActual().setRecibosCompras(reciboDAO.seleccionarRegistrosCompras(getUserLogin().getCuenta().getCorreo()));
        tienda.setRecibosTienda(reciboDAO.seleccionarRecibosTienda());
    }

    public void buscarInfoCarrito(ArrayList<Libro> librosCarrito) {
        for (Libro libroCarrito : librosCarrito) {
            for (Libro libroCatalogo : tienda.getCatalogo().getCatalogoLibros()) {
                if (libroCatalogo.getIsbn().equals(libroCarrito.getIsbn())) {
                    libroCarrito.setTitulo(libroCatalogo.getTitulo());
                    libroCarrito.setPrecioVenta(libroCatalogo.getPrecioVenta());
                }
            }
        }
    }


    public boolean isAdminLogin() {
        return gestionUsuario.isAdminLogin();
    }

    public void cerrarSesion(boolean cerrarAplicacion) throws IOException, RuntimeException, SQLException {
        gestionUsuario.cerrarSesionUsuario(cerrarAplicacion);
        eliminarLibroUsuarioGenerico();
    }

    public void registrarUsuario(Usuario usuario) throws RuntimeException, SQLException {
        gestionUsuario.registrarUsuario(usuario);
    }

    public void modificarUsuario(Usuario usuario) throws RuntimeException, SQLException {
        if (usuario.getCuenta().getCorreo() == "user_default" || usuario.getCuenta().getCorreo() == Administrador.CORREO) {
            throw new RuntimeException("No se puede modificar el usuario por defecto ni el correo.");
        }
        gestionUsuario.modificarUsuario(usuario);
    }

    public boolean isGenericoLogin() {
        return gestionUsuario.isDefaultUserLogin();
    }

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

    public ArrayList<Libro> listarLibros() throws SQLException {
        return gestionCatalogo.listarLibros();
    }

    public TotalesCompra resumenCompra() throws IOException, SQLException, RuntimeException {
        return gestionCarrito.calculoResumenCompra(reciboDAO);
    }

    public Libro anadirLibrosCarrito(String isbnLibro) throws RuntimeException, IOException, SQLException {
        return gestionCarrito.anadirLibrosCarrito(isbnLibro);
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

    public void eliminarLibroUsuarioGenerico() throws SQLException, RuntimeException {
        ArrayList<Libro> librosCarritoDefaul = carritoDAO.seleccionarRegistros("user_default");
        if (librosCarritoDefaul == null || librosCarritoDefaul.isEmpty())
            return;// throw new IllegalArgumentException("El usuario default no tiene libros");
        for (Libro libroCarritoDefautl : librosCarritoDefaul) {
            Libro libroCatalogo = new Libro();
            libroCatalogo.setIsbn(String.valueOf(libroCarritoDefautl.getIsbn()));
            libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
            libroCatalogo.setIsComprado(gestionCarrito.validarComprado(librosCarritoDefaul, libroCatalogo.getIsbn()));
            libroCatalogo.eliminarReserva(libroCarritoDefautl.getStockReservado());
            gestionCarrito.actualizarCatalogoMemoria(libroCatalogo);
            carritoDAO.eliminarRegistro(libroCarritoDefautl, "user_default");
            libroDAO.actualizarDatos(libroCatalogo);
        }
    }

    public void registrarCompra(TipoPagoEnum tipoPagoEnum) throws IOException, SQLException, RuntimeException {
        ArrayList<Libro> listaLibrosCarrito = carritoDAO.seleccionarRegistros(tienda.getUsuarioActual().getCuenta().getCorreo());// Selecciona lista de libros de carrito user log
        if (listaLibrosCarrito == null || listaLibrosCarrito.isEmpty()) { // Valida que esta lista no esté nula ni vacia
            throw new IllegalArgumentException("No puede continuar con la compra, no tiene productos en el carrito...");
        }
        gestionCompra.aggListaCompra(getUserLogin(), tipoPagoEnum, libroDAO);
        gestionCarrito.disminuirStock();
    }

    //TODO modificar metodo para que envie compras y no recibos
    public ArrayList<Recibo> getComprasUserLogin() throws IOException, SQLException, RuntimeException {
        return tienda.getUsuarioActual().getRecibosCompras();
        //return tienda.getRecibos().get(tienda.getUsuarioActual().getCuenta().getCorreo());
        /*Recibo recibo = new Recibo();
        recibo.setCorreo(gestionUsuario.usuarioLogueado().getCuenta().getCorreo());
        return reciboDAO.seleccionarRegistrosCompras(recibo);*/
    }

    public Recibo reciboUsuario() throws IOException, SQLException, RuntimeException {
        /*Recibo recibo = new Recibo();
        Usuario usuarioLog = gestionUsuario.usuarioLogueado(); //Se utiliza el metodo que devuelve el usuario logueado
        recibo.setCorreo(usuarioLog.getCuenta().getCorreo()); //Y tambien el correo del usuario
        recibo.setNumeroRecibo(compraDAO.seleccionarRegistros().size()); //En la BD compras se busca el numero de compra
        recibo = reciboDAO.seleccionarRegistroNumero(recibo);
        recibo.setNombreUsuario(usuarioLog.getNombre()); //Se asigna el nombre del usuario logueado// Y con este dato se manda por parametro a recibo para buscar cuales fueron los productos comprados*/
        Recibo ultimoRecibo = tienda.getRecibosTienda().get(tienda.getUsuarioActual().getCuenta().getCorreo()).getLast();
        buscarNombresLibros(ultimoRecibo);//Se busca los nombres de los libros y se asignan al recibo
        return ultimoRecibo;
    }

    public void buscarNombresLibros(Recibo recibo) throws SQLException, RuntimeException {
        for (LibroComprado libroComprado : recibo.getListaProductosComprados()) {
            buscarNombreLibros(libroComprado);
            /*Libro libroCatalogo = new Libro();
            libroCatalogo.setIsbn(libroComprado.getIsbn());
            libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
            libroComprado.setTitulo(libroCatalogo.getTitulo());*/
        }
    }

    public void buscarNombreLibros(LibroComprado libroComprado) {
        for (Libro libroCatalogo : tienda.getCatalogo().getCatalogoLibros()) {
            if (libroCatalogo.getIsbn().equals(libroComprado.getIsbn()))
                libroComprado.setTitulo(libroCatalogo.getTitulo());
        }
    }

    public ArrayList<LibroComprado> listaCarrito() throws SQLException, RuntimeException {
        if (tienda.getUsuarioActual().getCarrito().getLibros() == null) {
            throw new IllegalArgumentException("No se encuentran libros en el carrito,");
        }
        ArrayList<LibroComprado> listaCarrito = new ArrayList<>();
        buscarInfoCarrito(tienda.getUsuarioActual().getCarrito().getLibros());
        for (Libro libroCarritoUser : tienda.getUsuarioActual().getCarrito().getLibros()) {
            LibroComprado libroComprado = aggInfoProductoCompra(calculadoraIVA, libroCarritoUser);
            listaCarrito.add(libroComprado);
        }
        return listaCarrito;
    }

    public void vaciarCarrito() throws SQLException, RuntimeException {
        gestionCarrito.vaciarCarrito();
    }

    private LibroComprado aggInfoProductoCompra(CalculadoraIVA calculadoraIVA, Libro libroCarrito) throws SQLException {
        Libro libroCatalogo = libroDAO.seleccionarRegistro(libroCarrito);
        LibroComprado libroComprado = setProductoCompra(libroCarrito);

        libroComprado.setCantidadComprada(libroCarrito.getStockReservado());
        libroComprado.setPrecioVenta(calculadoraIVA.precioBaseUnitario(libroCatalogo));
        libroComprado.setDescuentoPremium(calculadoraIVA.descuentoPremium(libroComprado.getPrecioVenta(), tienda.getUsuarioActual()));
        libroComprado.setDesPremiumTotal(calculadoraIVA.descuentoPremiumTotal(libroComprado.getPrecioVenta(), libroCarrito, tienda.getUsuarioActual()));
        libroComprado.setImpuestoUnitario(calculadoraIVA.impuestoProducto(libroCatalogo, tienda.getUsuarioActual()));
        libroComprado.setImpuestoTotal(calculadoraIVA.impuestoProductos(libroCarrito, libroCatalogo, tienda.getUsuarioActual()));
        libroComprado.setPrecioTotal(calculadoraIVA.subtotalProducto(libroCarrito, libroComprado.getPrecioVenta(), libroComprado.getDesPremiumTotal(), libroComprado.getImpuestoTotal()));
        libroCarrito.setIsbn(libroCatalogo.getIsbn());
        return libroComprado;
    }

    private LibroComprado setProductoCompra(Libro libroCarritoUser) {
        LibroComprado libroComprado = new LibroComprado();
        libroComprado.setTitulo(libroCarritoUser.getTitulo());
        libroComprado.setIsbn(libroCarritoUser.getIsbn());
        libroComprado.setCantidadComprada(libroCarritoUser.getStockReservado());
        libroComprado.setPrecioVenta(libroCarritoUser.getPrecioVenta());
        return libroComprado;
    }

    public TotalesCompra valorCompra() throws IOException, SQLException, RuntimeException {
        TotalesCompra totalesCompra = new TotalesCompra();
        setValorCompra(totalesCompra, calculadoraIVA);
        return totalesCompra;
    }

    private void setValorCompra(TotalesCompra totalesCompra, CalculadoraIVA calculadoraIVA) throws SQLException, IOException {
        ArrayList<Libro> librosCarritoUserLog = tienda.getUsuarioActual().getCarrito().getLibros();
        totalesCompra.setPrecioBase(calculadoraIVA.precioBaseTotal(librosCarritoUserLog, libroDAO));
        totalesCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(totalesCompra.getPrecioBase(), gestionUsuario.usuarioLogueado()));
        totalesCompra.setImpuestos(calculadoraIVA.impuestos(librosCarritoUserLog, libroDAO, gestionUsuario.usuarioLogueado()));
        totalesCompra.setTotal(calculadoraIVA.total(totalesCompra.getPrecioBase(), totalesCompra.getDescuentoPremium(), totalesCompra.getImpuestos()));
        Recibo recibo = new Recibo();
        recibo.setCorreo(gestionUsuario.usuarioLogueado().getCuenta().getCorreo());
        totalesCompra.setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(reciboDAO.seleccionarRegistrosCompras(tienda.getUsuarioActual().getCuenta().getCorreo()), totalesCompra.getTotal()));
        totalesCompra.setTotal(totalesCompra.getTotal() - totalesCompra.getDescuentoFrecuencia());
    }

    public void guardarComentario(Comentario comentario) throws IOException, RuntimeException, SQLException {
        Usuario userLog = gestionUsuario.usuarioLogueado();
        comentario.setCorreo(userLog.getCuenta().getCorreo());
        comentario.setUsuario(userLog.getNombre());
        comentario.setFecha(LocalDateTime.now());
        gestionComentario.registrarComentario(comentario);
    }

    public Stack<Comentario> listarComentarios(String isbn) throws IOException, RuntimeException, SQLException {
        Stack<Comentario> comentarios = gestionComentario.buscarComentario(isbn);
        Usuario usuarioConsulta = new Usuario();
        Libro libroConsulta = new Libro();
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
        recibo.setFechaCompra(LocalDateTime.parse(fecha, formater));
        Recibo compraRecibo = compraDAO.seleccionarRegistro(recibo);
        if (compraRecibo == null) {
            throw new RuntimeException("Compra no encontrada");
        }

        Recibo reciboFinal = reciboDAO.seleccionarRegistro(compraRecibo);
        if (reciboFinal == null) {
            throw new RuntimeException("Recibo no encontrado");
        }
        reciboFinal.setNombreUsuario(gestionUsuario.usuarioLogueado().getNombre());
        buscarNombresLibros(reciboFinal);
        return reciboFinal;

    }

    public void registrarCodigo(String codigo) throws SQLException, RuntimeException {
        gestionCodigo.registrarCodigo(codigo);
    }

    public void usarCodigo(String codigo) throws SQLException, RuntimeException {
        gestionCodigo.usarCodigo(codigo);
        Usuario usuarioPremium = new UsuarioPremium(getUserLogin());
        usuarioPremium.setTipoCliente(TipoUsuarioEnum.Premium);
        usuarioDAO.actualizarDatos(usuarioPremium);
        usuarioPremium.setCarrito(tienda.getUsuarioActual().getCarrito());
        usuarioPremium.setRecibosCompras(tienda.getUsuarioActual().getRecibosCompras());
        tienda.setUsuarioActual(usuarioPremium);
    }

    public ArrayList<CodigoPremium> consultaCodigos() throws SQLException, RuntimeException {
        return gestionCodigo.consultarCodigo();
    }

    public ArrayList<Categoria> listarCategorias() throws SQLException {
        return libroDAO.seleccionarCateorias();
    }

    public ArrayList<Libro> filtrarLibros(String categoria, String formato) throws SQLException, RuntimeException {
        tienda.getCatalogo().setListaLibros(libroDAO.seleccionarRegistros());
        if (categoria.equals("TODOS") && formato.equals("TODOS")) {
            return tienda.getCatalogo().getCatalogoLibros();
        }
        if (!categoria.equals("TODOS") && !formato.equals("TODOS")) {
            return filtrarCategoriaFormato(categoria, formato);
        } else if (!categoria.equals("TODOS")) {
            return filtrarLibrosCategoria(categoria);
        } else {
            return filtrarLibrosFormato(formato);
        }
    }

    public ArrayList<Libro> filtrarCategoriaFormato(String categoria, String formato) throws SQLException, RuntimeException {
        ArrayList<Libro> librosFiltrados = new ArrayList<>();
        for (Libro libro : tienda.getCatalogo().getCatalogoLibros()) {
            if (libro.getCategoria().getNombre().equals(categoria) && libro.getTipoLibro().equals(TipoLibroEnum.valueOf(formato))) {
                librosFiltrados.add(libro);
            }
        }
        return librosFiltrados;
    }

    public ArrayList<Libro> filtrarLibrosCategoria(String categoria) throws SQLException, RuntimeException {
        ArrayList<Libro> librosFiltrados = new ArrayList<>();
        for (Libro libro : tienda.getCatalogo().getCatalogoLibros()) {
            if (libro.getCategoria().getNombre().equals(categoria)) {
                librosFiltrados.add(libro);
            }
        }
        return librosFiltrados;
    }

    public ArrayList<Libro> filtrarLibrosFormato(String formato) throws RuntimeException {
        ArrayList<Libro> librosFiltrados = new ArrayList<>();
        for (Libro libro : tienda.getCatalogo().getCatalogoLibros()) {
            if (libro.getTipoLibro().equals(TipoLibroEnum.valueOf(formato))) {
                librosFiltrados.add(libro);
            }
        }
        return librosFiltrados;
    }

    public ArrayList<Usuario> listarUsuarios() throws SQLException {
        tienda.setUsuarios(usuarioDAO.seleccionarRegistros());
        Iterator<Usuario> iteratorUsuarios = tienda.getUsuarios().iterator();
        while (iteratorUsuarios.hasNext()) {
            Usuario usuario = iteratorUsuarios.next();
            if (usuario.getCuenta().getCorreo().equals("user_default") || usuario.getCuenta().getCorreo().equals(Administrador.CORREO)) {
                iteratorUsuarios.remove();
            }
            usuario.setCuenta(cuentaDAO.seleccionarRegistro(usuario.getCuenta()));
        }
        return tienda.getUsuarios();
    }

    public Usuario buscarUsuario(String usuario) {
        for (Usuario user : tienda.getUsuarios()) {
            if (user.getNombre().equals(usuario)) {
                return user;
            }
        }
        return null;
    }

    public void eliminarUsuario(String correo) throws SQLException, RuntimeException {
        ArrayList<Recibo> listaRecibos = reciboDAO.seleccionarRegistrosCompras(correo);
        ArrayList<Libro> listaCarrito = carritoDAO.seleccionarRegistros(correo);
        ArrayList<Comentario> listaComentarios = comentarioDAO.seleccionarRegistros();
        buscarComentarioUsuario(listaComentarios, correo);
        if (!listaCarrito.isEmpty()) {
            RegistroLog.registrarAdvertencia("El usuario no se puede eliminar, tiene productos en el carrito.");
            throw new RuntimeException("El usuario no se puede eliminar, tiene productos en el carrito.");
        }
        if (listaRecibos == null || !listaRecibos.isEmpty()) {
            RegistroLog.registrarAdvertencia("El usuario no se puede eliminar, tiene compras asociadas.");
            throw new RuntimeException("El usuario no se puede eliminar, tiene compras asociadas.");
        }
        usuarioDAO.eliminarRegistro(correo);
        cuentaDAO.eliminarRegistro(correo);
        tienda.setUsuarios(usuarioDAO.seleccionarRegistros());
    }

    public void buscarComentarioUsuario(ArrayList<Comentario> comentarios, String correo) {
        for (Comentario comentario : comentarios) {
            if (Objects.equals(comentario.getCorreo(), correo)) throw new RuntimeException("El usuario no se puede eliminar, tiene compras asociadas");
        }
    }

    public void agregarCategoria(String categoria) throws SQLException, CategoriaException {
        if (categoria == null || categoria.isBlank()) {
            throw new RuntimeException("La categoría no puede ser nula o vacía.");
        }
        categoria = categoria.trim();
        ArrayList<Categoria> categorias = libroDAO.seleccionarCateorias();
        JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();
        for (Categoria existente : categorias) {
            double similitud = jaroWinklerSimilarity.apply(existente.getNombre().toLowerCase(), categoria.toLowerCase());
            if (similitud >= 1.0) {
                throw new CategoriaException("La categoría ya existe: " + existente.getNombre(), CategoriaException.TipoConflicto.DUPLICADO);
            }
            if (similitud >= 0.8) {
                throw new CategoriaException(("Categoría similar encontrada: " + existente.getNombre() + ". Por favor, elige un nombre diferente."), CategoriaException.TipoConflicto.PARECIDA);
            }
        }
        insertarCategoria(categoria);
    }

    public void insertarCategoria(String categoria) throws SQLException, CategoriaException {
        Expresion expresion = new Expresion();
        expresion.validarCategoria(categoria);
        libroDAO.insertarCategoria(categoria.toUpperCase());
    }
}
