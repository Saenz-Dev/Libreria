package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.ResumenProductoDTO;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.TotalesCompra;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.ReciboDAO;
import co.edu.uptc.persistencia.UsuarioDAO;

/**
 * Clase encargada de gestionar el carrito de compras del usuario.
 */
public class GestionCarrito {

    private CarritoDAO carritoDAO;

    private UsuarioDAO usuarioDAO;

    private LibroDAO libroDAO;

    private GestionUsuario gestionUsuario;

    private CalculadoraIVA calculadoraIVA;

    private Tienda tienda;

    public GestionCarrito(Tienda tienda, CarritoDAO carritoDAO, UsuarioDAO usuarioDAO, LibroDAO libroDAO, GestionUsuario gestionUsuario) {
        this.tienda = tienda;
        this.carritoDAO = carritoDAO;
        this.usuarioDAO = usuarioDAO;
        this.libroDAO = libroDAO;
        this.gestionUsuario = gestionUsuario;
        calculadoraIVA = new CalculadoraIVA();
    }

    /**
     * Agrega los libros al carrito del usuario
     *
     * @param isbnLibro libro a agregar al carrito
     * @throws IOException      si ocurre algún error cuando no se escribe el
     *                          usuario en el JSON
     * @throws RuntimeException
     * @throws SQLException
     */
    public void  anadirLibrosCarrito(String isbnLibro) throws IOException, SQLException, RuntimeException {
        Usuario usuarioLog = tienda.getUsuarioActual();
        Libro libroCatalogo = validarDisponibilidadLibros(isbnLibro);
        if (libroCatalogo == null) {
            throw new IllegalArgumentException("No se pudo realizar la acción de añadir libros al carrito");
        }
        Libro libroCarrito = existeLibroCarrito(isbnLibro);
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
    public void anadirProductoExistente(Libro libroCarrito, Libro libroCatalogo, Usuario usuarioLogin) throws IOException, SQLException, RuntimeException {
        if (libroCatalogo.getStockDisponible() == 0) throw new IllegalArgumentException("Libro Agotado");
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
    public void anadirProductoNuevo(Usuario usuarioLogin, Libro libroCatalogo) throws IOException, SQLException, RuntimeException {
        agregarLibroCarrito(libroCatalogo, usuarioLogin);
        libroCatalogo.reservarLibro();
        usuarioDAO.actualizarDatos(usuarioLogin);
        libroDAO.actualizarDatos(libroCatalogo);
        tienda.getUsuarioActual().getCarrito().setLibros(carritoDAO.seleccionarRegistros(usuarioLogin.getCuenta().getCorreo()));
    }

    /**
     * Agrega un libro al carrito
     *
     * @param libro libro a agregar a la base de datos
     * @throws RuntimeException
     * @throws SQLException
     */
    public void agregarLibroCarrito(Libro libro, Usuario usuarioLog) throws SQLException, RuntimeException {
        Libro libroCarrito = new Libro();
        libroCarrito.setIsbn(libro.getIsbn());
        libroCarrito.setStockReservado(1);
        carritoDAO.insertarDatos(libroCarrito, tienda.getUsuarioActual().getCuenta().getCorreo());
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
    public void actualizarDatos(Usuario usuarioLogin, Libro libroCatalogo, Libro libroCarrito) throws IOException, SQLException, RuntimeException {
        validarLibroCarrito(libroCarrito);
        usuarioDAO.actualizarDatos(usuarioLogin);
        libroDAO.actualizarDatos(libroCatalogo);
        //actualizarCatalogoMemoria(libroCarrito);
        carritoDAO.actualizarDatos(libroCarrito, tienda.getUsuarioActual().getCuenta().getCorreo());
        tienda.getUsuarioActual().getCarrito().setLibros(carritoDAO.seleccionarRegistros(usuarioLogin.getCuenta().getCorreo()));
    }

    /**
     * Actualiza el catálogo en memoria con los datos del libro modificado.
     *
     * @param libro libro modificado.
     */
    public void actualizarCatalogoMemoria(Libro libro) {
        ArrayList<Libro> catalogo = tienda.getCatalogo().getCatalogoLibros();
        for (Libro libroBuscado : catalogo) {
            if (libroBuscado.getIsbn().equals(libro.getIsbn())) {
                libroBuscado.setStockDisponible(libro.getStockDisponible());
                libroBuscado.setStockReservado(libro.getStockReservado());
                libroBuscado.setTitulo(libro.getTitulo());
                libroBuscado.setAutor(libro.getAutor());
                libroBuscado.setAnioPublicacion(libro.getAnioPublicacion());
                libroBuscado.setNumeroPaginas(libro.getNumeroPaginas());
                libroBuscado.setPrecioVenta(libro.getPrecioVenta());
                libroBuscado.setCategoria(libro.getCategoria());
                libroBuscado.setTipoLibro(libro.getTipoLibro());
                libroBuscado.setEditorial(libro.getEditorial());
                libroBuscado.setIsComprado(libro.getIsComprado());
                break;
            }
        }
    }

    /**
     * Valída el libro del carrito.
     *
     * @param libroCarrito libro a validar.
     * @throws RuntimeException si el libro es nulo o la cantidad es menor o igual a 0.
     */
    private void validarLibroCarrito(Libro libroCarrito) {
        if (libroCarrito == null) {
            RegistroLog.registrarAdvertencia("El libro proporcionado es nulo.");
            throw new RuntimeException("No se proporcionó un libro válido.");
        }
        if (libroCarrito.getStockReservado() <= 0) {
            RegistroLog.registrarAdvertencia("Intento de actualizar con cantidad no válida: " + libroCarrito.getStockReservado());
            throw new RuntimeException("La cantidad de libros debe ser mayor a 0.");
        }
    }

    /**
     * Metodo que valida si el libro está disponible en el catálogo.
     *
     * @param isbn isbn del libro.
     * @return libro disponible.
     * @throws RuntimeException si el libro no está disponible o si el stock es 0.
     * @throws SQLException
     */
    public Libro validarDisponibilidadLibros(String isbn) throws SQLException, RuntimeException {
        Libro libroBd = new Libro();
        libroBd.setIsbn(isbn);
        libroBd = libroDAO.seleccionarRegistro(libroBd);
        if (libroBd.getStockDisponible() == 0) throw new IllegalArgumentException("Libro Agotado");
        actualizarLibroLocal(libroBd);
        return libroBd;
    }

    /**
     * Metodo que actualiza el libro en el catálogo local.
     *
     * @param libro libro a actualizar.
     * @throws RuntimeException si el libro es nulo o no se encuentra en el catálogo local.
     */
    private void actualizarLibroLocal(Libro libro) {
        if (libro == null) {
            RegistroLog.registrarAdvertencia("Intento de actualizar un libro nulo.");
            throw new RuntimeException("No se proporcionó un libro válido.");
        }
        ArrayList<Libro> catalogo = tienda.getCatalogo().getCatalogoLibros();
        for (int i = 0; i < catalogo.size(); i++) {
            if (catalogo.get(i).getIsbn().equals(libro.getIsbn())) {
                catalogo.set(i, libro);
                return;
            }
        }
        RegistroLog.registrarAdvertencia("El libro con ISBN " + libro.getIsbn() + " no se encuentra en el catálogo local.");
    }

    /**
     * Metodo que verifica si un libro ya existe en el carrito del usuario.
     *
     * @param isbn ISBN del libro a buscar en el carrito.
     * @return LibroCarrito si existe, null si no existe.
     * @throws RuntimeException si ocurre algún error al consultar el carrito del usuario.
     */
    public Libro existeLibroCarrito(String isbn) throws RuntimeException {
        for (Libro libro : tienda.getUsuarioActual().getCarrito().getLibros()) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }


    /**
     * Metodo que devuelve el arrayList de libros del carrito del usuario
     *
     * @return arrayList de libros del carrito del usuario
     * @throws RuntimeException
     * @throws SQLException
     */
    public ArrayList<Libro> listarLibros() throws SQLException, RuntimeException {
        return carritoDAO.seleccionarRegistros(tienda.getUsuarioActual().getCuenta().getCorreo());
    }

    /**
     * Suma la cantidad de un libro en el carrito
     *
     * @param isbnProducto libro a sumar
     * @return subtotal del producto
     * @throws IOException      si ocurre algún error cuando no se lee el usuario en el
     *                          JSON
     * @throws RuntimeException
     * @throws SQLException
     */
    public ResumenProductoDTO sumarProducto(String isbnProducto) throws SQLException, RuntimeException {
        Libro libroCarrito = consultaLibroCarrito(isbnProducto);
        Libro libroCatalogo = consultaLibroCatalogo(isbnProducto);

        validarDisponibilidad(isbnProducto, libroCarrito, libroCatalogo);

        libroCatalogo.reservarLibro();
        libroCarrito.aumentarCantidad(1);
        validarLibroCarrito(libroCarrito);
        carritoDAO.actualizarDatos(libroCarrito, tienda.getUsuarioActual().getCuenta().getCorreo());
        libroDAO.actualizarDatos(libroCatalogo);

        return actualizarProductoCarrito(isbnProducto);
    }

    private void validarDisponibilidad(String isbnProducto, Libro libroCarrito, Libro libroCatalogo) {
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
     * @param isbn isbn del producto a actualizar en el carrito.
     * @return El resumen del producto.
     * @throws RuntimeException
     * @throws SQLException
     */
    public ResumenProductoDTO actualizarProductoCarrito(String isbn) throws SQLException, RuntimeException {

        ResumenProductoDTO resumenProductoDTO = new ResumenProductoDTO();
        Libro libroCarrito = consultaLibroCarrito(isbn);
        Libro libroCatalogo = consultaLibroCatalogo(isbn);

        resumenProductoDTO.setSubtotal(calculadoraIVA.subtotalProducto(libroCarrito, libroCatalogo));
        resumenProductoDTO.setCantidadReservada(libroCarrito.getStockReservado());

        return resumenProductoDTO;
    }

    private void validarLibroCarritoNull(Libro libroCarrito) {
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
     * @throws IOException      si ocurre algún error cuando no se lee el usuario en el
     *                          JSON
     * @throws RuntimeException
     * @throws SQLException
     */
    public ResumenProductoDTO disminuirProducto(String isbnProducto) throws IOException, SQLException, RuntimeException {
        Libro libroCarrito = consultaLibroCarrito(isbnProducto);
        Libro libroCatalogo = consultaLibroCatalogo(isbnProducto);

        libroCatalogo.cancelarReserva();
        libroCarrito.disminuirCantidadUnidad();

        validarLibroCarrito(libroCarrito);
        carritoDAO.actualizarDatos(libroCarrito, tienda.getUsuarioActual().getCuenta().getCorreo());
        libroDAO.actualizarDatos(libroCatalogo);

        return actualizarProductoCarrito(isbnProducto);
    }

    /**
     * Método que elimina el libro del carrito
     *
     * @param isbnProducto libro a eliminar
     * @return subtotal del producto
     * @throws IOException      si ocurre algún error cuando no se lee el usuario en el
     *                          JSON
     * @throws RuntimeException
     * @throws SQLException
     */
    public void eliminarProducto(String isbnProducto) throws SQLException, RuntimeException {
        Libro libroCarrito = consultaLibroCarrito(isbnProducto);
        Libro libroCatalogo = consultaLibroCatalogo(isbnProducto);

        if (libroCarrito == null || libroCatalogo == null) {
            throw new IllegalArgumentException("No se pudo sumar el producto con ISBN:" + isbnProducto);
        }

        ArrayList<Libro> librosCarrito = listarLibros(); //Selecciona todos los libros que estan en todos los carritos

        libroCatalogo.setIsComprado(validarComprado(librosCarrito, isbnProducto));
        libroCatalogo.eliminarReserva(libroCarrito.getStockReservado());

        carritoDAO.eliminarRegistro(libroCarrito, tienda.getUsuarioActual().getCuenta().getCorreo());
        libroDAO.actualizarDatos(libroCatalogo);
        tienda.getUsuarioActual().getCarrito().setLibros(carritoDAO.seleccionarRegistros(tienda.getUsuarioActual().getCuenta().getCorreo()));
    }

    private Libro consultaLibroCatalogo(String isbnProducto) throws SQLException {
        Libro libroCatalogo = new Libro();
        libroCatalogo.setIsbn(isbnProducto);
        libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
        return libroCatalogo;
    }

    private Libro consultaLibroCarrito(String isbnProducto) {
        for (Libro libro : tienda.getUsuarioActual().getCarrito().getLibros()) {
            if (libro.getIsbn().equals(isbnProducto)) {
                validarLibroCarritoNull(libro);
                return libro;
            }
        }
        return null;
    }


    public boolean validarComprado(ArrayList<Libro> librosCarrito, String isbn) {
        int numeroCompras = 0;//Variable de control para validar si el producto lo tienen dos usuarios
        if (librosCarrito.isEmpty()) return false; //Si la lista de libros de la tabla carrito esta vacia devuelve null
        for (Libro libroCarrito : librosCarrito) { //Por cada libroCarrito que hay en la lista de libros en el carrito
            if (libroCarrito.getIsbn().equals(isbn)) { //Compara el isbn del libro del carrito con el isbn del libro a buscar y si es igual entra al if
                numeroCompras++; //Aumenta el numero de compras
                if (numeroCompras > 1) {
                    //guardado en la variable local correoUsuario y el numero de compras es mayor a 2 devuelve true
                    return true;
                }
            }
        }
        return false; //En el caso de que no este comprado o apartado en otro carrito de otro usuario devuelve false.
    }

    /**
     * Metodo que calcula el valor total del carrito
     *
     * @return valor total del carrito
     * @throws RuntimeException
     * @throws SQLException
     */
    public TotalesCompra calculoResumenCompra(ReciboDAO reciboDAO) throws IOException, SQLException, RuntimeException {
        TotalesCompra totalesCompra = new TotalesCompra();
        ArrayList<Libro> librosCarritoUsuario = tienda.getUsuarioActual().getCarrito().getLibros();
        RegistroLog.registrarInfo("📦 Se encontraron " + librosCarritoUsuario.size() + " libros en el carrito del usuario: " + tienda.getUsuarioActual().getCuenta().getCorreo());
        setValorCompra(totalesCompra, librosCarritoUsuario);
        Recibo recibo = new Recibo();
        recibo.setCorreo(gestionUsuario.usuarioLogueado().getCuenta().getCorreo());
        setTotal(totalesCompra);
        return totalesCompra;
    }

    private void setTotal(TotalesCompra totalesCompra) throws IOException {
        totalesCompra.setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(tienda.getRecibosTienda().get(tienda.getUsuarioActual().getCuenta().getCorreo()), totalesCompra.getTotal()));
        totalesCompra.setTotal(totalesCompra.getTotal() - totalesCompra.getDescuentoPremium());
    }

    private void setValorCompra(TotalesCompra totalesCompra, ArrayList<Libro> librosCarritoUsuario) throws SQLException {
        totalesCompra.setImpuestos(calculadoraIVA.impuestos(librosCarritoUsuario, libroDAO));
        totalesCompra.setSubtotal(calculadoraIVA.subtotal(librosCarritoUsuario, libroDAO));
        totalesCompra.setTotal(calculadoraIVA.total(totalesCompra.getSubtotal(), totalesCompra.getImpuestos()));
        totalesCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(totalesCompra.getTotal(), gestionUsuario.usuarioLogueado()));
    }

    public void disminuirStock() throws SQLException, RuntimeException {
        ArrayList<Libro> librosCarritoUser = tienda.getUsuarioActual().getCarrito().getLibros();
        Iterator<Libro> iteratorCarritoUser = librosCarritoUser.iterator();
        while (iteratorCarritoUser.hasNext()) {
            Libro libroCarrito = iteratorCarritoUser.next();
            Libro libroCatalogo = new Libro();
            libroCatalogo.setIsbn(String.valueOf(libroCarrito.getIsbn()));
            libroCatalogo = libroDAO.seleccionarRegistro(libroCatalogo);
            libroCatalogo.confirmarCompra(libroCarrito.getStockReservado());
            carritoDAO.eliminarRegistro(libroCarrito, tienda.getUsuarioActual().getCuenta().getCorreo());
            libroDAO.actualizarDatos(libroCatalogo);
            iteratorCarritoUser.remove();
        }
        tienda.getCatalogo().setListaLibros(libroDAO.seleccionarRegistros());
    }
}
