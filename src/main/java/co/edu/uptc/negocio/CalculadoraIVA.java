package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.modelo.DescFrecuencia;
import co.edu.uptc.modelo.Libro;
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
     * @param librosCarrito carrito a calcular
     * @param libroDAO      DAO de tabla libros
     * @return subtotal total del carrito
     * @throws RuntimeException
     * @throws SQLException
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
     * Método que devuelve el impuesto total de un carrito
     *
     * @param librosCarrito lista de libros de carrito
     * @param libroDAO      DAO de libros
     * @param usuario       usuario logueado
     */
    public double impuestos(ArrayList<Libro> librosCarrito, LibroDAO libroDAO, Usuario usuario) throws SQLException, RuntimeException {
        double impuestos = 0;
        if (librosCarrito == null || librosCarrito.isEmpty()) return impuestos;
        for (Libro libroCarrito : librosCarrito) {
            double precioBase;
            double descuentoPremium;
            double precioConDescuento;
            double impuesto;
            Libro libro = new Libro();
            libro.setIsbn(String.valueOf(libroCarrito.getIsbn()));
            libro = libroDAO.seleccionarRegistro(libro);
            if (libro.getTipoLibro() == TipoLibroEnum.FISICO) {
                precioBase = (libro.getPrecioVenta() *libroCarrito.getStockReservado()) / 1.19; //Le saco el precioBase, osea precio sin IVA
                descuentoPremium = precioBase * usuario.getDescuentoTipoUsuario(); //Con el precio base y si el user es premium saco el descuentopremium
                precioConDescuento = precioBase - descuentoPremium; //Resto  el precioBase y el descuento (si hay) para el nuevo precio base
                impuesto = precioConDescuento * 0.19;
                impuestos += impuesto;
                /*precioBase = (libro.getPrecioVenta() * libroCarrito.getStockReservado()) / 1.19; //Obtengo el precio base
                precioDescuento = precioBase * (1 - usuario.getDescuentoTipoUsuario());// despues el precio con descuento para el producto
                impuestos += precioDescuento * 0.19;*/
            } else {
                precioBase = (libro.getPrecioVenta() *libroCarrito.getStockReservado()) / 1.05; //Le saco el precioBase, osea precio sin IVA
                descuentoPremium = precioBase * usuario.getDescuentoTipoUsuario(); //Con el precio base y si el user es premium saco el descuentopremium
                precioConDescuento = precioBase - descuentoPremium; //Resto  el precioBase y el descuento (si hay) para el nuevo precio base
                impuesto = precioConDescuento * 0.05;
                impuestos += impuesto;
                /*precioBase = (libro.getPrecioVenta() * libroCarrito.getStockReservado()) / 1.05; //Obtengo el precio base
                precioDescuento = precioBase * (1 - usuario.getDescuentoTipoUsuario());// despues el precio con descuento para el producto
                impuestos += precioDescuento * 0.05;*/
            }
        }
        return impuestos;
    }

    /**
     * Método que devuelve el precio total de los productos que contiene un carrito
     *
     * @param precioBase subtotal de los productos del carrito
     * @param impuestos  impuestos de los productos del carrito
     * @return suma de subtotal e impuestos
     */
    public double total(double precioBase, double descuento, double impuestos) {
        return precioBase - descuento + impuestos;
    }

    /**
     * Método que devuelve el impuesto por cada libro (su cantidad)
     *
     * @param libroCatalogo libro a calcular
     * @param libroCarrito  libro del carrito
     * @return impuesto del producto
     */
    public double impuestoProductos(Libro libroCarrito, Libro libroCatalogo, Usuario usuario) {
        double precioBase;
        double descuentoPremium;
        double precioConDescuento;
        if (libroCatalogo.getTipoLibro() == TipoLibroEnum.FISICO) {
            precioBase = (libroCarrito.getStockReservado() *libroCatalogo.getPrecioVenta()) / 1.19; //Le saco el precioBase, osea precio sin IVA
            descuentoPremium = precioBase * usuario.getDescuentoTipoUsuario(); //Con el precio base y si el user es premium saco el descuentopremium
            precioConDescuento = precioBase - descuentoPremium; //Resto  el precioBase y el descuento (si hay) para el nuevo precio base
            return precioConDescuento * 0.19;
            /*precioBase = libroCatalogo.getPrecioVenta() * libroCarrito.getStockReservado() / 1.19;
            return libroCarrito.getStockReservado() * libroCatalogo.getPrecioVenta() - precioBase;*/
        } else {
            precioBase = (libroCatalogo.getPrecioVenta() * libroCarrito.getStockReservado()) / 1.05; //Le saco el precioBase, osea precio sin IVA
            descuentoPremium = precioBase * usuario.getDescuentoTipoUsuario(); //Con el precio base y si el user es premium saco el descuentopremium
            precioConDescuento = precioBase - descuentoPremium; //Resto  el precioBase y el descuento (si hay) para el nuevo precio base
            return precioConDescuento * 0.05;
            /*precioBase = libroCatalogo.getPrecioVenta() * libroCarrito.getStockReservado() / 1.05;
            return libroCarrito.getStockReservado() * libroCatalogo.getPrecioVenta() - precioBase;*/
        }
    }

    public double impuestoProducto(Libro libroCatalogo, Usuario usuario) {
        double precioBase;
        double descuentoPremium;
        double precioConDescuento;
        double impuesto;
        if (libroCatalogo.getTipoLibro() == TipoLibroEnum.FISICO) {
            precioBase = libroCatalogo.getPrecioVenta() / 1.19; //Le saco el precioBase, osea precio sin IVA
            descuentoPremium = precioBase * usuario.getDescuentoTipoUsuario(); //Con el precio base y si el user es premium saco el descuentopremium
            precioConDescuento = precioBase - descuentoPremium; //Resto  el precioBase y el descuento (si hay) para el nuevo precio base
            return precioConDescuento * 0.19;
            //return precioConDescuento + impuesto;
        } else {
            precioBase = libroCatalogo.getPrecioVenta() / 1.05; //Le saco el precioBase, osea precio sin IVA
            descuentoPremium = precioBase * usuario.getDescuentoTipoUsuario(); //Con el precio base y si el user es premium saco el descuentopremium
            precioConDescuento = precioBase - descuentoPremium; //Resto  el precioBase y el descuento (si hay) para el nuevo precio base
            return precioConDescuento * 0.05;
            ///return precioConDescuento + impuesto;
            /*
            precioBase = libroCatalogo.getPrecioVenta() / 1.05;
            return libroCatalogo.getPrecioVenta() - precioBase;*/
        }
    }

    /**
     * Método que devuelve el subtotal de un producto
     *
     * @param libroCatalogo libro del catalogo
     * @param libroCarrito  libros del carrito de libros en el stock
     * @return subtotal del producto
     */
    public double subtotalProducto(Libro libroCarrito, double precioBase, double descuento, double impuesto) {
        return (libroCarrito.getStockReservado() * precioBase) - descuento + impuesto;
    }

    public double descuentoPremium(double precioBase, Usuario usuario) {
        return precioBase * usuario.getDescuentoTipoUsuario();
    }

    public double descuentoPremiumTotal(double precioBase, Libro libroCarrito, Usuario usuario) {
        return (precioBase * libroCarrito.getStockReservado()) * usuario.getDescuentoTipoUsuario();
    }


    public double descuentoFrecuencia(ArrayList<Recibo> listaRecibos, double total) throws IOException {
        if (listaRecibos == null || listaRecibos.isEmpty()) return 0;
        if (listaRecibos.size() == 10) return total * descFrecuencia.getDESCUENTO_DIEZ_COMPRAS();
        if (listaRecibos.size() == 50) return total * descFrecuencia.getDESCUENTO_CINCUENTA_COMPRAS();
        if (listaRecibos.size() == 100) return total * descFrecuencia.getDESCUENTO_CIEN_COMPRAS();
        return 0;
    }

    public double precioBaseUnitario(Libro libroCatalogo) {
        if (libroCatalogo.getTipoLibro() == TipoLibroEnum.FISICO) {
            return libroCatalogo.getPrecioVenta() / 1.19;
        } else {
            return libroCatalogo.getPrecioVenta() / 1.05;
        }
    }
}

