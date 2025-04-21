package co.edu.uptc.negocio;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import co.edu.uptc.modelo.*;

public class GestionCompra {

    private ProductoCompra productoCompra;
    private ManejoCompraJSON manejoCompraJSON;

    public ManejoCompraJSON getManejoCompraJSON() {
        return manejoCompraJSON;
    }

    public GestionCompra(Tienda tienda) {
        manejoCompraJSON = new ManejoCompraJSON(tienda);
        productoCompra = new ProductoCompra();
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

    public void aggListaCompra(ArrayList<String> isbns, Usuario usuarioLog, TipoPago tipoPago) throws IOException {
        Recibo recibo = new Recibo();
        CalculadoraIVA calculadoraIVA = new CalculadoraIVA();
        if (isbns.isEmpty()) return;

        for (String isbn : isbns) {
            ProductoCompra productoCompra = new ProductoCompra();
            ValorCompra valorCompra = new ValorCompra();
            Libro libro = buscarLibro(isbn);
            if (libro == null) throw new RuntimeException("No se encontró el libro para registrarlo en la compra...");
            productoCompra.setIsbn(libro.getIsbn());
            productoCompra.setTitulo(libro.getTitulo());
            productoCompra.setNumeroLibros(libroCarrito(isbn, usuarioLog).getStockReservado());
            productoCompra.setPrecioUnitario(libro.getPrecioVenta());
            productoCompra.setPrecioTotal(calculadoraIVA.subtotalProducto(libro, usuarioLog.getCarrito().getLibros()));
            recibo.getListaProductosComprados().add(productoCompra);
            LocalTime horaActual = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            recibo.setFecha(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth(), horaActual.format(formatter));
            recibo.setUsuario(buscarUsuarioLogin().getCuenta().getCorreo());
            recibo.setNombreUser(buscarUsuarioLogin().getNombre());
            recibo.setDireccion(usuarioLog.getDireccionEnvio());

            valorCompra.setImpuestos(calculadoraIVA.impuestos(usuarioLog.getCarrito()));
            valorCompra.setSubtotal(calculadoraIVA.subtotal(usuarioLog.getCarrito()));
            valorCompra.setTotal(calculadoraIVA.total(valorCompra.getSubtotal(), valorCompra.getImpuestos()));
            recibo.setValorCompra(valorCompra);

            recibo.setTipoPago(tipoPago);
        }
        manejoCompraJSON.crearCompra(recibo, usuarioLog);
    }

    public double buscarCantidadLibro(String isbn, Usuario usuario) {
        for (Libro libro : usuario.getCarrito().getLibros()) {
            if (libro.getIsbn().equals(isbn)) {
                return libro.getStockReservado();
            }
        }
        return -1;
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
