package co.edu.uptc.negocio;

import co.edu.uptc.modelo.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Clase encargada de gestionar el carrito de compras del usuario.
 */
public class GestionCarrito {

    /**
     * Carrito del usuario
     */
    private Carrito carrito;

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
     */
    public GestionCarrito(ManejoUsuarioJSON manejoUsuarioJSON, Tienda tienda) {
        carrito = new Carrito();
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

    public void agregarProductos(Carrito carrito) {
        for (Libro libro : carrito.getLibros()) {
            this.carrito.agregarLibroCarrito(libro);
        }
    }

    /**
     * Método que agrega los libros al carrito del usuario
     *
     * @param isbnLibro libro a agregar al carrito
     * @param cantidad cantidad de libros a agregar al carrito
     * @throws IllegalArgumentException si el libro no existe en el catálogo
     * @throws IOException              si ocurre algún error cuando no se escribe el usuario en el JSON
     */
    public void anadirLibrosCarrito(String isbnLibro, int cantidad) throws IllegalArgumentException, IOException {
        Usuario usuarioLogin = manejoUsuarioJSON.getUsuarioLogin();
        Libro libroCatalogo = validarDisponibilidadLibros(isbnLibro, cantidad);
        if (libroCatalogo == null) {
            throw new IllegalArgumentException("No se pudo realizar la acción de añadir libros al carrito");
        }

        Libro libroCarrito = existeProductoCarrito(isbnLibro, usuarioLogin);
        libroCatalogo.setIsComprado(true);
        if (libroCarrito != null) {
            anadirProductoExistente(libroCarrito, libroCatalogo, usuarioLogin);
        } else {
            anadirProductoNuevo(usuarioLogin, libroCatalogo);
        }
    }

    /**
     * Suma productos si ya existen en el carrito del usuario.
     * @param libroCarrito libro del carrito existente en el carrito para aumentar su cantidad.
     * @param libroCatalogo libro del catalogo a modificar.
     * @param usuarioLogin usuario logueado.
     * @throws IllegalArgumentException si no hay mas disponibilidad del libro en el catálogo.
     * @throws IOException si llega a ocurrir algun error al serializar los datos.
     */
    public void anadirProductoExistente(Libro libroCarrito, Libro libroCatalogo, Usuario usuarioLogin) throws IllegalArgumentException, IOException {
        if (libroCatalogo.getStockDisponible() == 0) throw new IllegalArgumentException("Libro Agotado");
        libroCatalogo.reservarLibro();
        libroCarrito.aumentarCantidad(1);
        actualizarDatos(usuarioLogin, libroCatalogo);
    }

    /**
     * Agrega un libro al carrito.
     * @param usuarioLogin usuario logueado.
     * @param libroCatalogo libro del catalogo para agregar al carrito.
     * @throws IOException si ocurre algún error al serializar los datos.
     * @throws IllegalArgumentException si no encuentra el usuario logueado.
     */
    public void anadirProductoNuevo(Usuario usuarioLogin, Libro libroCatalogo) throws IOException, IllegalArgumentException {
        usuarioLogin.getCarrito().agregarLibroCarrito(libroCatalogo);
        libroCatalogo.reservarLibro();
        actualizarDatos(usuarioLogin, libroCatalogo);
    }

    /**
     * Actualiza los dato en la lista de los usuarios, serializando los datos.
     * @param usuarioLogin usuario logueado.
     * @param libroCatalogo libro del catalogo.
     * @throws IOException si ocurre algún error al serializar los datos.
     */
    public void actualizarDatos(Usuario usuarioLogin, Libro libroCatalogo) throws IOException {
        manejoUsuarioJSON.modificarUsuarioCarrito(usuarioLogin);
        manejoLibroJSON.modificarLibro(libroCatalogo);
    }

    /**
     * Metodo que valida si el libro está disponible en el catálogo.
     * @param isbn isbn del libro.
     * @param cantidadSolicitada cantidad solicitada.
     * @return libro disponible.
     * @throws IllegalArgumentException si el libro no está disponible.
     */
    public Libro validarDisponibilidadLibros(String isbn, int cantidadSolicitada) throws IllegalArgumentException {
        Map<String, ArrayList<Libro>> mapLibros = manejoLibroJSON.leerLibro();
        for (ArrayList<Libro> listLibros : mapLibros.values()) {
            for (Libro libroList : listLibros) {
                if (libroList.getIsbn().equals(isbn)) {
                    if (libroList.getStockDisponible() == 0) throw new IllegalArgumentException("Libro Agotado");
                    return libroList;
                }
            }
        }
        return null;
    }

    /**
     * Método que verifica si el libro ya está en el carrito
     *
     * @param isbn isbn del libro
     * @return libro encontrado en el carrito
     * @throws IOException si ocurre algún error cuando no se lee el usuario en el JSON
     */
    public Libro existeProductoCarrito(String isbn, Usuario usuarioLogin) throws IOException {
        if (usuarioLogin == null) return null;
        for (Libro libro : usuarioLogin.getCarrito().getLibros()) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }

    /**
     * Método que devuelve el arrayList de libros del carrito del usuario
     *
     * @return arrayList de libros del carrito del usuario
     */
    public ArrayList<Libro> listarLibros() {
        return manejoUsuarioJSON.getUsuarioLogin().getCarrito().getLibros();
    }

    /**
     * Método que suma la cantidad de un libro en el carrito
     *
     * @param isbnProducto libro a sumar
     * @return subtotal del producto
     * @throws IOException si ocurre algún error cuando no se lee el usuario en el JSON
     */
    public ResumenProductoDTO sumarProducto(String isbnProducto) throws IOException {
        ArrayList<Libro> librosCarrito = manejoUsuarioJSON.getUsuarioLogin().getCarrito().getLibros();
        Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
        int index = buscarIndexProducto(isbnProducto, librosCarrito);

        if (index >= 0) {
            Libro libroModificar = encontrarLibro(isbnProducto, catalogo);
            if (libroModificar.getStockDisponible() == 0) throw new IllegalArgumentException("Libro agotado.");

            libroModificar.reservarLibro();
            librosCarrito.get(index).aumentarCantidad(1);

            return actualizarProductoCarrito(librosCarrito.get(index), librosCarrito, catalogo, index);
        }
        return null;
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
     * @param productoCarrito producto actualizar.
     * @param librosCarrito lista de libros del carrito del usuario.
     * @param catalogo catalogo disponible en la tienda.
     * @param index posición en la que se encuentra el producto en el carrito del usuario.
     * @return El resumen del producto.
     * @throws IOException si al serializar los datos ocurre algún error.
     */
    public ResumenProductoDTO actualizarProductoCarrito(Libro productoCarrito, ArrayList<Libro> librosCarrito, Map<String, ArrayList<Libro>> catalogo, int index) throws IOException {
        actualizarCantidadProducto(catalogo);

        ResumenProductoDTO resumenProductoDTO = new ResumenProductoDTO();
        resumenProductoDTO.setSubtotal(calculadoraIVA.subtotalProducto(productoCarrito, librosCarrito));
        resumenProductoDTO.setCantidadReservada(librosCarrito.get(index).getStockReservado());
        return resumenProductoDTO;
    }

    /**
     * Actualiza la cantidad de los productos del catalogo y actualiza el usuario logueado.
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
     * @throws IOException si ocurre algún error cuando no se lee el usuario en el JSON
     */
    public ResumenProductoDTO disminuirProducto(String isbnProducto) throws IOException {
        ArrayList<Libro> librosCarrito = manejoUsuarioJSON.getUsuarioLogin().getCarrito().getLibros();
        Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
        int index = buscarIndexProducto(isbnProducto, librosCarrito);

        if (index >= 0) {
            Libro libroModificar = encontrarLibro(isbnProducto, catalogo);
            //if (libroModificar.getStockDisponible() == 0) throw new IllegalArgumentException("Libro agotado.");
            libroModificar.cancelarReserva();
            librosCarrito.get(index).disminuirCantidadUnidad();

            return actualizarProductoCarrito(librosCarrito.get(index), librosCarrito, catalogo, index);
        }
        return null;
    }

    /**
     * Método que elimina el libro del carrito
     *
     * @param isbnProducto libro a eliminar
     * @return subtotal del producto
     * @throws IOException si ocurre algún error cuando no se lee el usuario en el JSON
     */
    public void eliminarProducto(String isbnProducto) throws IOException {
        ArrayList<Libro> librosCarrito = manejoUsuarioJSON.getUsuarioLogin().getCarrito().getLibros();
        Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
        int index = buscarIndexProducto(isbnProducto, librosCarrito);

        if (index >= 0) {
            Libro libroModificar = encontrarLibro(isbnProducto, catalogo);
            libroModificar.setIsComprado(false);
            libroModificar.eliminarReserva(librosCarrito.get(index).getStockReservado());
            manejoLibroJSON.escribirLibros(catalogo);
            librosCarrito.remove(index);

            actualizarCantidadProducto(catalogo);
        }
    }

    /**
     * Método que busca un libro en el catálogo
     *
     * @param isbnProducto libro a buscar
     * @param catalogo catálogo de libros para buscar
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
     */
    public ValorCompra calculoResumenCompra() {
        ValorCompra valorCompra = new ValorCompra();
        Carrito carritoLocal = manejoUsuarioJSON.getUsuarioLogin().getCarrito();
        valorCompra.setImpuestos(calculadoraIVA.impuestos(carritoLocal));
        valorCompra.setSubtotal(calculadoraIVA.subtotal(carritoLocal));
        valorCompra.setTotal(calculadoraIVA.total(valorCompra.getSubtotal(), valorCompra.getImpuestos()));
        return valorCompra;
    }

    public void disminuirStock() throws IOException {
        Usuario userLogin = manejoUsuarioJSON.getUsuarioLogin();
        Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
//        for (Libro libro : userLogin.getCarrito().getLibros()) {
//            Libro libroDisminuir = encontrarLibro(libro.getIsbn(), catalogo);
//            libroDisminuir.confirmarCompra(libro.getStockReservado());
//            userLogin.getCarrito().getLibros().remove(libro);
//        }
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
