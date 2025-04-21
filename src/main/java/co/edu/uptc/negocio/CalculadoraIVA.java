package co.edu.uptc.negocio;

import co.edu.uptc.modelo.Carrito;
import co.edu.uptc.modelo.Libro;

import java.util.ArrayList;
import java.util.Map;

/**
 * Clase encargada de realizar cálculos sobre el carrito de compras.
 */
public class CalculadoraIVA {

    /**
     * Método que devuelve el subtotal total de un carrito
     *
     * @param carrito carrito a calcular
     * @return subtotal total del carrito
     */
    public double subtotal(Carrito carrito) {
        double subtotal = 0;
        if (carrito.getLibros().isEmpty()) return subtotal;
        for (Libro libro : carrito.getLibros()) {
            subtotal += libro.getStockReservado() * libro.getPrecioVenta();
        }
        return subtotal;
    }

    /**
     * Método que devuelve el impuesto total de un carrito
     *
     * @param carrito carrito a calcular
     * @return impuesto total del carrito
     */
    public double impuestos(Carrito carrito) {
        double impuestos = 0;
        if (carrito.getLibros().isEmpty()) return impuestos;
        for (Libro libro : carrito.getLibros()) {
            if (libro.getTipoLibro() == TipoLibro.FISICO) {
                impuestos += libro.getStockReservado() * 0.19 * libro.getPrecioVenta();
            } else {
                impuestos += libro.getStockReservado() * 0.05 * libro.getPrecioVenta();
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
    public double impuestoProducto(Libro libroParametro, ArrayList<Libro> librosCarrito) {
        for (Libro libro : librosCarrito) {
            if (libro.getTitulo().equals(libroParametro.getTitulo())) {
                if (libroParametro.getTipoLibro() == TipoLibro.FISICO) {
                    return libroParametro.getStockReservado() * libro.getPrecioVenta() * 0.19;
                } else {
                    return libroParametro.getStockReservado() * libro.getPrecioVenta() * 0.05;
                }
            }
        }

        return 0;
    }

    /**
     * Método que devuelve el subtotal de un producto
     *
     * @param libroParametro libro a calcular
     * @param librosCarrito  libros del carrito de libros en el stock
     * @return subtotal del producto
     */
    public double subtotalProducto(Libro libroParametro, ArrayList<Libro> librosCarrito) {
        for (Libro libro : librosCarrito) {
            if (libro.getIsbn().equals(libroParametro.getIsbn())) {
                return libroParametro.getStockReservado() * libro.getPrecioVenta();
            }
        }
        return 0;
    }
}

