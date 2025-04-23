package co.edu.uptc.negocio;

import co.edu.uptc.modelo.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GestionTienda {

    private Tienda tienda;
    private GestionUsuario gestionUsuario;
    private GestionLibro gestionLibro;
    private GestionCatalogo gestionCatalogo;
    private GestionCarrito gestionCarrito;
    private GestionCompra gestionCompra;

    public GestionTienda() {
        tienda = new Tienda();
        gestionUsuario = new GestionUsuario(tienda);
        gestionLibro = new GestionLibro(tienda);
        gestionCatalogo = new GestionCatalogo(tienda);
        gestionCarrito = new GestionCarrito(gestionUsuario.getManejoUsuarioJSON(), tienda);
        gestionCompra = new GestionCompra(tienda);
    }

    // -----------------------------------Métodos GestionUsuario-----------------------------------

    public Usuario getUserLogin() {
        return gestionUsuario.userLogin();
    }

    public void asignarUsuarioGenerico() throws IOException {
        gestionUsuario.asignarUsuarioGenerico();
    }

    public void iniciarSesion(String correo, String contrasena) {
        gestionUsuario.iniciarSesion(correo, contrasena);
    }

    public boolean isAdminLogin() {
        return gestionUsuario.isAdminLogin();
    }

    public void cerrarSesion() throws IOException, RuntimeException {
        gestionUsuario.cerrarSesionUsuario();
    }

    public void registrarUsuario(Usuario usuario) throws RuntimeException {
        gestionUsuario.registrarUsuario(usuario);
    }

    public void modificarUsuario(Usuario usuario) throws IOException, RuntimeException {
        usuario.getCuenta().setLog(true);
        gestionUsuario.modificarUsuario(usuario);
    }

    public boolean isGenericoLogin() {
        return gestionUsuario.isGenericoLogin();
    }

    public void cerrarSesionUsuario() throws RuntimeException, IOException {
        gestionUsuario.cerrarSesionUsuario();
    }

    // ----------------------------------------Métodos de
    // GestionLibro---------------------------------------------

    public String[] obtenerTitulosLibros() {
        return gestionLibro.obtenerLibros();
    }

    public Libro buscarLibro(String titulo) {
        return gestionLibro.buscarLibro(titulo);
    }

    public void eliminarLibro(ArrayList<String> listaIsbn) throws IllegalArgumentException, IOException {
        gestionLibro.eliminarLibro(listaIsbn);
    }

    public void modificarLibro(Libro libro) throws RuntimeException, IOException {
        gestionLibro.modificarLibro(libro);
    }

    public void registrarLibro(Libro libro) throws IOException, RuntimeException {
        gestionLibro.registrarLibro(libro);
    }

    public boolean validarExistenciaLibro(String isbnLibro) {
        return gestionLibro.validarExistencia(isbnLibro);
    }

    // ---------------------------------------------Métodos de
    // GestionCatalogo--------------------------------------------

    public Map<String, ArrayList<Libro>> listarLibros() throws IOException {
        return gestionCatalogo.listarLibros();
    }

    // ---------------------------------------------Métodos de
    // GesionCarrito----------------------------------------------------------------------

    public ValorCompra resumenCompra() throws IOException {
        gestionCompra.getManejoCompraJSON().leerCompras();
        return gestionCarrito.calculoResumenCompra();
    }

    public void anadirLibrosCarrito(String isbnLibro, int cantidad) throws RuntimeException, IOException {
        gestionCarrito.anadirLibrosCarrito(isbnLibro, cantidad);
    }

    public ResumenProductoDTO sumarProductos(String isbnProducto) throws IOException {
        return gestionCarrito.sumarProducto(isbnProducto);
    }

    public void eliminarProductoCarrito(String isbnProducto) throws IOException {
        gestionCarrito.eliminarProducto(isbnProducto);
    }

    public ResumenProductoDTO disminuirProductoCarrito(String isbnProducto) throws IOException {
        return gestionCarrito.disminuirProducto(isbnProducto);
    }

    public void eliminarLibroUsuarioGenerico() throws IOException {
        for (Libro libro : getUserLogin().getCarrito().getLibros()) {
            eliminarProductoCarrito(libro.getIsbn());
            if (getUserLogin().getCarrito().getLibros().isEmpty()) {
                break;
            }
        }
    }

    //Metodos de GestionCompra

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

    public ArrayList<ProductoCompra> listaCarrito() {
        ArrayList<ProductoCompra> listaCarrito = new ArrayList<>();
        CalculadoraIVA calculadoraIVA = new CalculadoraIVA();
        for (Libro libro : carritoUserLog().getLibros()) {
            ProductoCompra productoCompra = new ProductoCompra();
            productoCompra.setTitulo(libro.getTitulo());
            productoCompra.setIsbn(libro.getIsbn());
            productoCompra.setNumeroLibros(libro.getStockReservado());
            productoCompra.setPrecioUnitario(libro.getPrecioVenta());
            productoCompra.setPrecioTotal(calculadoraIVA.subtotalProducto(libro, carritoUserLog().getLibros()));
            listaCarrito.add(productoCompra);
        }
        return listaCarrito;
    }

    public ValorCompra valorCompra() throws IOException {
        ValorCompra valorCompra = new ValorCompra();
        CalculadoraIVA calculadoraIVA = new CalculadoraIVA();

        valorCompra.setImpuestos(calculadoraIVA.impuestos(carritoUserLog()));
        valorCompra.setSubtotal(calculadoraIVA.subtotal(carritoUserLog()));
        valorCompra.setTotal(calculadoraIVA.total(valorCompra.getSubtotal(), valorCompra.getImpuestos()));
        valorCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(valorCompra.getTotal(), gestionCarrito.getManejoUsuarioJSON().getUsuarioLogin()));
        valorCompra.setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(valorCompra.getTotal(), tienda, gestionUsuario.userLogin()));
        valorCompra.setTotal(valorCompra.getTotal() - valorCompra.getDescuentoPremium());
        return valorCompra;
    }
}
