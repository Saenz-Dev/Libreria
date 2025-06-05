package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.modelo.DescFrecuencia;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.LibroCarrito;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.TipoLibroEnum;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.persistencia.LibroDAO;

/**
 * Clase encargada de realizar cálculos sobre el carrito de compras.
 */
public class CalculadoraIVA {

    private DescFrecuencia descFrecuencia;

    public CalculadoraIVA() {
        descFrecuencia = new DescFrecuencia();
    }

    /**
     * Método que devuelve el subtotal total de un carrito
     *
     * @param carrito carrito a calcular
     * @return subtotal total del carrito
     * @throws RuntimeException
     * @throws SQLException
     */
    public double subtotal(ArrayList<Libro> librosCarrito, LibroDAO libroDAO) throws SQLException, RuntimeException {
        double subtotal = 0;
        if (librosCarrito == null || librosCarrito.isEmpty()) return subtotal;

        for (Libro libroCarrito : librosCarrito) {
            Libro libro = new Libro();
            libro.setIsbn(String.valueOf(libroCarrito.getIsbn()));
            libro = libroDAO.seleccionarRegistro(libro);
            subtotal += libroCarrito.getStockReservado() * libro.getPrecioVenta();
        }
        return subtotal;
    }

    /**
     * Método que devuelve el impuesto total de un carrito
     *
     * @param carrito carrito a calcular
     * @return impuesto total del carrito
     * @throws RuntimeException
     * @throws SQLException
     */
    public double impuestos(ArrayList<Libro> librosCarrito, LibroDAO libroDAO) throws SQLException, RuntimeException {
        double impuestos = 0;
        if (librosCarrito == null || librosCarrito.isEmpty()) return impuestos;
        for (Libro libroCarrito : librosCarrito) {
            Libro libro = new Libro();
            libro.setIsbn(String.valueOf(libroCarrito.getIsbn()));
            libro = libroDAO.seleccionarRegistro(libro);
            if (libro.getTipoLibro() == TipoLibroEnum.FISICO) {
                impuestos += libroCarrito.getStockReservado() * 0.19 * libro.getPrecioVenta();
            } else {
                impuestos += libroCarrito.getStockReservado() * 0.05 * libro.getPrecioVenta();
            }
        }
        return impuestos;
    }

    /**
     * Método que devuelve el precio total de los productos que contiene un carrito
     *
     * @param subtotal  subtotal de los productos del carrito
     * @param impuestos impuestos de los productos del carrito
     * @return suma de subtotal e impuestos
     */
    public double total(double subtotal, double impuestos) {
        return subtotal + impuestos;
    }

    /**
     * Método que devuelve el impuesto de un producto
     *
     * @param libroParametro libro a calcular
     * @param catalogo       catalogo de libros
     * @return impuesto del producto
     */
    public double impuestoProductos(Libro libroCarrito, Libro libroParametro) {
        if (libroParametro.getTipoLibro() == TipoLibroEnum.FISICO) {
            return libroCarrito.getStockReservado() * libroParametro.getPrecioVenta() * 0.19;
        } else {
            return libroCarrito.getStockReservado() * libroParametro.getPrecioVenta() * 0.05;
        }
    }

    public double impuestoProducto(Libro libroParametro, Libro libroCatalogo) {
        if (libroParametro.getTipoLibro() == TipoLibroEnum.FISICO) {
            return libroCatalogo.getPrecioVenta() * 0.19;
        } else {
            return libroCatalogo.getPrecioVenta() * 0.05;
        }
    }

    /**
     * Método que devuelve el subtotal de un producto
     *
     * @param libroParametro libro a calcular
     * @param librosCarrito  libros del carrito de libros en el stock
     * @return subtotal del producto
     */
    public double subtotalProducto(Libro libroCarrito, Libro libroCatalogo) {
        return libroCarrito.getStockReservado() * libroCatalogo.getPrecioVenta();
        /*for (Libro libro : librosCarrito) { //Busca el libro en el carrito del usuario 
            if (libro.getIsbn().equals(libroParametro.getIsbn())) {//Si el isbn del libroParametro es igual a algun libro del carrito
                return libro.getStockReservado() * libro.getPrecioVenta();//Retorna el precio del libro 
            }
        }
        return 0;//Si no retorna cero*/
    }

    public double descuentoPremium(double total, Usuario usuario) {
        return total * usuario.getDescuentoTipoUsuario();
    }


    public double descuentoFrecuencia(ArrayList<Recibo> listaRecibos, double total) throws IOException {
        if (listaRecibos == null || listaRecibos.isEmpty()) return 0;
        if (listaRecibos.size() == 10) return total * descFrecuencia.getDESCUENTO_DIEZ_COMPRAS();
        if (listaRecibos.size() == 50) return total * descFrecuencia.getDESCUENTO_CINCUENTA_COMPRAS();
        if (listaRecibos.size() == 100) return total * descFrecuencia.getDESCUENTO_CIEN_COMPRAS();
        return 0;
    }
}

