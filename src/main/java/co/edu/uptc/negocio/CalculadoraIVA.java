package co.edu.uptc.negocio;

import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.modelo.DescFrecuencia;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.TipoLibroEnum;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.persistencia.LibroDAO;

/**
 * Clase encargada de realizar cálculos sobre el carrito de compras, como el cálculo de subtotal, impuestos y descuentos.
 * Utiliza la clase {@link DescFrecuencia} para aplicar descuentos por frecuencia de compra.
 */
public class CalculadoraIVA {

    /**
     * Instancia para el cálculo de descuentos por frecuencia de compra.
     */
    private DescFrecuencia descFrecuencia;

    /**
     * Constructor por defecto. Inicializa la instancia de descuentos por frecuencia.
     */
    public CalculadoraIVA() {
        descFrecuencia = new DescFrecuencia();
    }

    /**
     * Metodo que devuelve el subtotal total de un carrito.
     * Calcula el precio base de todos los libros en el carrito, excluyendo el IVA.
     *
     * @param librosCarrito carrito a calcular
     * @param libroDAO      DAO de tabla libros
     * @return subtotal total del carrito
     * @throws RuntimeException si ocurre un error al acceder a la base de datos
     * @throws SQLException     si ocurre un error al acceder a la base de datos
     */
    public double precioBaseTotal(ArrayList<Libro> librosCarrito, LibroDAO libroDAO) throws SQLException, RuntimeException {
        double precioBase = 0;
        if (librosCarrito == null || librosCarrito.isEmpty()) return precioBase;

        for (Libro libroCarrito : librosCarrito) {
            Libro libro = new Libro();
            libro.setIsbn(String.valueOf(libroCarrito.getIsbn()));
            libro = libroDAO.seleccionarRegistro(libro);
            if (libro.getTipoLibro() == TipoLibroEnum.FISICO) {
                precioBase += (libro.getPrecioVenta() * libroCarrito.getStockReservado()) / 1.19; //Quitamos el IVA
            } else {
                precioBase += (libro.getPrecioVenta() * libroCarrito.getStockReservado()) / 1.05;//Aqui tambien
            }
        }
        return precioBase;
    }

    /**
     * Metodo que devuelve el impuesto total de un carrito.
     * Calcula el total de impuestos de todos los libros en el carrito.
     *
     * @param librosCarrito lista de libros de carrito
     * @param libroDAO      DAO de libros
     * @return impuesto total del carrito
     * @throws RuntimeException si ocurre un error al acceder a la base de datos
     * @throws SQLException     si ocurre un error al acceder a la base de datos
     */
    public double impuestos(ArrayList<Libro> librosCarrito, LibroDAO libroDAO) throws SQLException, RuntimeException {
        double impuestos = 0;
        double precioBase;
        if (librosCarrito == null || librosCarrito.isEmpty()) return impuestos;
        for (Libro libroCarrito : librosCarrito) {
            Libro libro = new Libro();
            libro.setIsbn(String.valueOf(libroCarrito.getIsbn()));
            libro = libroDAO.seleccionarRegistro(libro);
            if (libro.getTipoLibro() == TipoLibroEnum.FISICO) {
                precioBase = (libro.getPrecioVenta() * libroCarrito.getStockReservado()) / 1.19; //Le saco el precioBase, osea precio sin IVA
            } else {
                precioBase = (libro.getPrecioVenta() * libroCarrito.getStockReservado()) / 1.05; //Le saco el precioBase, osea precio sin IVA
            }
            impuestos += (libro.getPrecioVenta() * libroCarrito.getStockReservado()) - precioBase;
        }
        return impuestos;
    }

    /**
     * Metodo que devuelve el impuesto por cada libro (su cantidad)
     *
     * @param libroCatalogo libro a calcular
     * @param libroCarrito  libro del carrito
     * @return impuesto del producto
     */
    public double impuestoProductos(Libro libroCarrito, Libro libroCatalogo) {
        double precioBase, impuestos = 0;
        if (libroCatalogo.getTipoLibro() == TipoLibroEnum.FISICO) {
            precioBase = (libroCatalogo.getPrecioVenta() * libroCarrito.getStockReservado()) / 1.19;
        } else {
            precioBase = (libroCatalogo.getPrecioVenta() * libroCarrito.getStockReservado()) / 1.05;
        }
        impuestos += (libroCatalogo.getPrecioVenta() * libroCarrito.getStockReservado()) - precioBase;
        return impuestos;
    }

    /**
     * Metodo que devuelve el impuesto de un producto
     *
     * @param libroCatalogo libro del catalogo
     * @return impuesto del producto
     */
    public double impuestoProducto(Libro libroCatalogo) {
        double precioBase;
        if (libroCatalogo.getTipoLibro() == TipoLibroEnum.FISICO) {
            precioBase = libroCatalogo.getPrecioVenta() / 1.19;
        } else {
            precioBase = libroCatalogo.getPrecioVenta() / 1.05;
        }
        return libroCatalogo.getPrecioVenta() - precioBase;
    }


    /**
     * Metodo que devuelve el precio total de los productos que contiene un carrito
     *
     * @param precioBase subtotal de los productos del carrito
     * @param impuestos  impuestos de los productos del carrito
     * @return suma de subtotal e impuestos
     */
    public double total(double precioBase, double impuestos) {
        return precioBase + impuestos;
    }

    /**
     * Metodo que devuelve el precio base por la cantidad de un producto en el carrito
     *
     * @param libroCarrito libro del carrito
     * @param precioBase   precio base del producto
     * @return subtotal del producto
     */
    public double subtotalProducto(Libro libroCarrito, double precioBase) {
        return libroCarrito.getStockReservado() * precioBase;
    }

    /**
     * Metodo que devuelve el descuento total de un carrito
     *
     * @param precioTotal subtotal de los productos del carrito
     * @param usuario     usuario logueado
     * @return descuento total del carrito
     */
    public double descuentoPremiumTotal(double precioTotal, Usuario usuario) {
        return precioTotal * usuario.getDescuentoTipoUsuario();
    }

    /**
     * Metodo que devuelve el descuento por frecuencia de compra
     *
     * @param listaRecibos lista de recibos del usuario
     * @param total        total del carrito
     * @return descuento por frecuencia de compra
     */
    public double descuentoFrecuencia(ArrayList<Recibo> listaRecibos, double total) {
        if (listaRecibos == null || listaRecibos.isEmpty()) return 0;
        if (listaRecibos.size() == 10) return total * descFrecuencia.getDESCUENTO_DIEZ_COMPRAS();
        if (listaRecibos.size() == 50) return total * descFrecuencia.getDESCUENTO_CINCUENTA_COMPRAS();
        if (listaRecibos.size() == 100) return total * descFrecuencia.getDESCUENTO_CIEN_COMPRAS();
        return 0;
    }

    /**
     * Metodo que devuelve el precio base unitario de un libro
     *
     * @param libroCatalogo libro del catalogo
     * @return precio base unitario del libro
     */
    public double precioBaseUnitario(Libro libroCatalogo) {
        if (libroCatalogo.getTipoLibro() == TipoLibroEnum.FISICO) {
            return libroCatalogo.getPrecioVenta() / 1.19;
        } else {
            return libroCatalogo.getPrecioVenta() / 1.05;
        }
    }
}
