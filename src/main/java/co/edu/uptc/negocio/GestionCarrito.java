package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.LibroCarrito;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.ResumenProductoDTO;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.TotalesCompra;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.CuentaDAO;
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
    public void anadirLibrosCarrito(String isbnLibro) throws IOException, SQLException, RuntimeException {
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
    public void actualizarDatos(Usuario usuarioLogin, Libro libroCatalogo, Libro libroCarrito) throws IOException, SQLException, RuntimeException {
        validarLibroCarrito(libroCarrito);
        usuarioDAO.actualizarDatos(usuarioLogin);
        libroDAO.actualizarDatos(libroCatalogo);
        actualizarCatalogoMemoria(libroCarrito);
        carritoDAO.actualizarDatos(libroCarrito, tienda.getUsuarioActual().getCuenta().getCorreo());
        tienda.getUsuarioActual().getCarrito().setLibros(carritoDAO.seleccionarRegistros(libroCarrito, usuarioLogin.getCuenta().getCorreo()));
    }

    /**
     * Actualiza el catálogo en memoria con los datos del libro modificado.
     *
     * @param libro libro modificado.
     */
    private void actualizarCatalogoMemoria(Libro libro) {
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
    public ArrayList<LibroCarrito> listarLibros() throws SQLException, RuntimeException {
        return carritoDAO.seleccionarRegistros();
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
     * @throws IOException      si al serializar los datos ocurre algún error.
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

    private void validarConsulta(Libro libroCarrito, String correoUsuario) {
        if (correoUsuario.isEmpty()) {
            RegistroLog.registrarInfo("✅ Libro encontrado en el carrito: ISBN " + libroCarrito.getIsbn());
        } else {
            RegistroLog.registrarInfo("🔍 No se encontró el libro en el carrito: ISBN " + libroCarrito.getIsbn() + ", usuario " + libroCarrito.getIsbn());
        }
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
     * @throws IOException      si ocurre algún error cuando no se lee el usuario en el
     *                          JSON
     * @throws RuntimeException
     * @throws SQLException
     */
    public void eliminarProducto(String isbnProducto) throws SQLException, RuntimeException {
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

    private Libro consultaLibroCarrito(String isbnProducto) throws SQLException {
        for (Libro libro : tienda.getUsuarioActual().getCarrito().getLibros()) {
            if (libro.getIsbn().equals(isbnProducto)) {
                validarLibroCarritoNull(libro);
                return libro;
            }
        }
        Libro libroCarrito = new Libro();
        libroCarrito.setIsbn(isbnProducto);
        validarLibroCarritoNull(libroCarrito);
        libroCarrito = carritoDAO.seleccionarRegistro(libroCarrito, tienda.getUsuarioActual().getCuenta().getCorreo());
        validarConsulta(libroCarrito, tienda.getUsuarioActual().getCuenta().getCorreo());
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
    public TotalesCompra calculoResumenCompra(ReciboDAO reciboDAO) throws IOException, SQLException, RuntimeException {
        TotalesCompra totalesCompra = new TotalesCompra();
        LibroCarrito libroCarrito = new LibroCarrito();
        libroCarrito.setCorreo_usuario(gestionUsuario.usuarioLogueado().getCuenta().getCorreo());
        if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null || libroCarrito.getCorreo_usuario().isBlank()) {
            RegistroLog.registrarAdvertencia("❗ Se intentó seleccionar registros con un correo de usuario nulo o vacío.");
            throw new RuntimeException("⚠️ No se proporcionó un usuario válido para consultar su carrito.");
        }
        ArrayList<LibroCarrito> librosCarritoUsuario = carritoDAO.seleccionarRegistros(libroCarrito);
        RegistroLog.registrarInfo("📦 Se encontraron " + librosCarritoUsuario.size() + " libros en el carrito del usuario: " + libroCarrito.getCorreo_usuario());
        setValorCompra(totalesCompra, librosCarritoUsuario);
        Recibo recibo = new Recibo();
        recibo.setCorreo(gestionUsuario.usuarioLogueado().getCuenta().getCorreo());
        setTotal(reciboDAO, totalesCompra, recibo);
        return totalesCompra;
    }

    private void setTotal(ReciboDAO reciboDAO, TotalesCompra totalesCompra, Recibo recibo) throws IOException, SQLException {
        totalesCompra.setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(reciboDAO.seleccionarRegistrosCompras(recibo), totalesCompra.getTotal()));
        totalesCompra.setTotal(totalesCompra.getTotal() - totalesCompra.getDescuentoPremium());
    }

    private void setValorCompra(TotalesCompra totalesCompra, ArrayList<LibroCarrito> librosCarritoUsuario) throws SQLException {
        totalesCompra.setImpuestos(calculadoraIVA.impuestos(librosCarritoUsuario, libroDAO));
        totalesCompra.setSubtotal(calculadoraIVA.subtotal(librosCarritoUsuario, libroDAO));
        totalesCompra.setTotal(calculadoraIVA.total(totalesCompra.getSubtotal(), totalesCompra.getImpuestos()));
        totalesCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(totalesCompra.getTotal(), gestionUsuario.usuarioLogueado()));
    }


    public void disminuirStock() throws IOException, SQLException, RuntimeException {
        LibroCarrito libroCarrito = new LibroCarrito();
        libroCarrito.setCorreo_usuario(gestionUsuario.usuarioLogueado().getCuenta().getCorreo());
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
