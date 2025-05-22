package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.LibroCarrito;
import co.edu.uptc.modelo.ProductoCompra;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.ValorCompra;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.CompraDAO;
import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.ReciboDAO;
import co.edu.uptc.persistencia.UsuarioDAO;

public class GestionCompra {

    private ProductoCompra productoCompra;
    private ManejoCompraJSON manejoCompraJSON;
    private ReciboDAO reciboDAO;
    private CarritoDAO carritoDAO;
    private CompraDAO compraDAO;
    private int numeroRecibo;

    public ManejoCompraJSON getManejoCompraJSON() {
	return manejoCompraJSON;
    }

    public GestionCompra(Tienda tienda, ReciboDAO reciboDAO, CarritoDAO carritoDAO, CompraDAO compraDAO) throws SQLException {
	numeroRecibo = 0;
	manejoCompraJSON = new ManejoCompraJSON(tienda);
	productoCompra = new ProductoCompra();
	this.compraDAO = compraDAO;
	this.reciboDAO = reciboDAO;
	this.carritoDAO = carritoDAO;
    }

    public ProductoCompra getProductoCompra() {
	return productoCompra;
    }

    public void setProductoCompra(ProductoCompra productoCompra) {
	this.productoCompra = productoCompra;
    }

//    public ArrayList<ProductoCompra> crearCompra(ArrayList<String> isbns) {
//        if (isbns.isEmpty()) throw new RuntimeException("No hay libros en el carrito");
//        return aggListaCompra(isbns);
//    }

    public void aggListaCompra(Usuario usuarioLog, TipoPago tipoPago, UsuarioDAO usuarioDAO, LibroDAO libroDAO) throws IOException, SQLException{
        CalculadoraIVA calculadoraIVA = new CalculadoraIVA();
        LibroCarrito libroCarrito = new LibroCarrito();
        libroCarrito.setCorreo_usuario(usuarioLog.getCuenta().getCorreo());
        boolean registradoCompra = false;
        ArrayList<LibroCarrito> listaCarritoUser = carritoDAO.seleccionarRegistros(libroCarrito);
        numeroRecibo = compraDAO.seleccionarRegistros().size() + 1;
        LocalDateTime fecha = LocalDateTime.now();
        for(LibroCarrito libroCarritoUser : listaCarritoUser) {//Itera el carrito del usuario
            Recibo recibo = new Recibo();
            ProductoCompra productoCompra = new ProductoCompra();
            Libro libro = new Libro();
            libro.setIsbn(String.valueOf(libroCarritoUser.getIsbn_libro()));
            libro = libroDAO.seleccionarRegistro(libro);
            productoCompra.setIsbn(libro.getIsbn());
            productoCompra.setTitulo(libro.getTitulo());
            
            recibo.setCorreo(usuarioLog.getCuenta().getCorreo());
            recibo.setNombreUser(usuarioLog.getNombre());
            recibo.setDireccion(usuarioLog.getDireccionEnvio());
            recibo.setNumeroRecibo(numeroRecibo);
            recibo.setFecha(fecha);
            if (!registradoCompra) {
        	compraDAO.insertarDatos(recibo);
        	registradoCompra = true;
            }
            
            productoCompra.setNumeroLibros(libroCarritoUser.getCantidad());
            productoCompra.setPrecioUnitario(libro.getPrecioVenta());
            productoCompra.setPrecioTotal(calculadoraIVA.subtotalProducto(libroCarritoUser, libro));
            recibo.getListaProductosComprados().add(productoCompra);
            

            recibo.getValorCompra().setImpuestos(calculadoraIVA.impuestos(listaCarritoUser, libroDAO));
            recibo.getValorCompra().setSubtotal(calculadoraIVA.subtotal(listaCarritoUser, libroDAO));
            recibo.getValorCompra().setTotal(calculadoraIVA.total(recibo.getValorCompra().getSubtotal(), recibo.getValorCompra().getImpuestos()));
            recibo.getValorCompra().setDescuentoPremium(calculadoraIVA.descuentoPremium(recibo.getValorCompra().getTotal(), usuarioLog));
            recibo.getValorCompra().setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(reciboDAO.seleccionarRegistrosCompras(recibo), recibo.getValorCompra().getTotal()));//TODO modificar esta linea a metodos de BD
            recibo.getValorCompra().setTotal(recibo.getValorCompra().getTotal() - recibo.getValorCompra().getDescuentoPremium() - recibo.getValorCompra().getDescuentoFrecuencia());
            recibo.setTipoPago(tipoPago);
            reciboDAO.insertarDatos(recibo);
        }     
    }

    public Usuario buscarUsuarioLogin() {
	for (Usuario user : manejoCompraJSON.getTienda().getUsuarios()) {
	    if (user.getCuenta().isLog()) {
		return user;
	    }
	}
	return null;
    }

    public Libro buscarLibro(String isbn) {
	for (ArrayList<Libro> libros : manejoCompraJSON.getTienda().getMapLibros().values()) {
	    for (Libro libro : libros) {
		if (libro.getIsbn().equals(isbn)) {
		    return libro;
		}
	    }
	}
	return null;
    }

    public Libro libroCarrito(String isbn, Usuario usuario) {
	for (Libro libro : usuario.getCarrito().getLibros()) {
	    if (libro.getIsbn().equals(isbn)) {
		return libro;
	    }
	}
	return null;
    }

}
