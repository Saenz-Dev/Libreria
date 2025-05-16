package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
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
import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.ReciboDAO;
import co.edu.uptc.persistencia.UsuarioDAO;

public class GestionCompra {

    private ProductoCompra productoCompra;
    private ManejoCompraJSON manejoCompraJSON;
    private ReciboDAO reciboDAO;
    private CarritoDAO carritoDAO;

    public ManejoCompraJSON getManejoCompraJSON() {
	return manejoCompraJSON;
    }

    public GestionCompra(Tienda tienda, ReciboDAO reciboDAO, CarritoDAO carritoDAO) throws SQLException {
	manejoCompraJSON = new ManejoCompraJSON(tienda);
	productoCompra = new ProductoCompra();
	this.reciboDAO = reciboDAO;
	this.reciboDAO.crearTabla();
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
        ArrayList<LibroCarrito> listaCarritoUser = carritoDAO.seleccionarRegistros(libroCarrito);
        for(LibroCarrito libroCarritoUser : listaCarritoUser) {
            Recibo recibo = new Recibo();
            ProductoCompra productoCompra = new ProductoCompra();
            ValorCompra valorCompra = new ValorCompra();
            Libro libro = new Libro();
            libro.setIsbn(String.valueOf(libroCarritoUser.getIsbn_libro()));
            libro = libroDAO.seleccionarRegistro(libro);
            productoCompra.setIsbn(libro.getIsbn());
            productoCompra.setTitulo(libro.getTitulo());
            productoCompra.setNumeroLibros(libroCarritoUser.getCantidad());
            productoCompra.setPrecioUnitario(libro.getPrecioVenta());
            productoCompra.setPrecioTotal(calculadoraIVA.subtotalProducto(libroCarritoUser, libro));
            recibo.getListaProductosComprados().add(productoCompra);
            LocalTime horaActual = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            recibo.formatearFecha(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth(), horaActual.format(formatter));
            recibo.setCorreo(usuarioLog.getCuenta().getCorreo());
            recibo.setNombreUser(usuarioLog.getNombre());
            recibo.setDireccion(usuarioLog.getDireccionEnvio());

            valorCompra.setImpuestos(calculadoraIVA.impuestos(listaCarritoUser, libroDAO));
            valorCompra.setSubtotal(calculadoraIVA.subtotal(listaCarritoUser, libroDAO));
            valorCompra.setTotal(calculadoraIVA.total(valorCompra.getSubtotal(), valorCompra.getImpuestos()));
            valorCompra.setDescuentoPremium(calculadoraIVA.descuentoPremium(valorCompra.getTotal(), usuarioLog));
            valorCompra.setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(reciboDAO.seleccionarRegistrosCompras(recibo), valorCompra.getTotal()));//TODO modificar esta linea a metodos de BD
            valorCompra.setTotal(valorCompra.getTotal() - valorCompra.getDescuentoPremium() - valorCompra.getDescuentoFrecuencia());
            recibo.setValorCompra(valorCompra);
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
