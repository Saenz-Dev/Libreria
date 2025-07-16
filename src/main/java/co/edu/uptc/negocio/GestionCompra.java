package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.LibroComprado;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.TipoPagoEnum;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.CompraDAO;
import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.ReciboDAO;

/**
 * Clase encargada de gestionar el proceso de compra en la tienda virtual.
 * Permite registrar compras, generar recibos, asociar productos comprados y actualizar la información de la tienda y el usuario.
 * Utiliza DAOs para la persistencia de compras, recibos, carritos y libros, así como utilidades para el cálculo de totales e impuestos.
 */
public class GestionCompra {

    /**
     * Objeto que representa el libro comprado en la transacción.
     */
    private LibroComprado libroComprado;
    /**
     * DAO para operaciones de persistencia de recibos.
     */
    private ReciboDAO reciboDAO;
    /**
     * DAO para operaciones de persistencia del carrito de compras.
     */
    private CarritoDAO carritoDAO;
    /**
     * DAO para operaciones de persistencia de compras.
     */
    private CompraDAO compraDAO;
    /**
     * Número identificador del recibo generado en la compra.
     */
    private int numeroRecibo;
    /**
     * Referencia a la tienda virtual.
     */
    private Tienda tienda;
    /**
     * Utilidad para cálculos de IVA, descuentos y totales.
     */
    private CalculadoraTiendaImpl calculadoraTiendaImpl;

    /**
     * Constructor que inicializa la gestión de compras con las dependencias necesarias.
     *
     * @param tienda referencia a la tienda virtual
     * @param reciboDAO DAO para recibos
     * @param carritoDAO DAO para carritos
     * @param compraDAO DAO para compras
     */
    public GestionCompra(Tienda tienda, ReciboDAO reciboDAO, CarritoDAO carritoDAO, CompraDAO compraDAO) {
        this.tienda = tienda;
        numeroRecibo = 0;
        libroComprado = new LibroComprado();
        this.compraDAO = compraDAO;
        this.reciboDAO = reciboDAO;
        this.carritoDAO = carritoDAO;
        calculadoraTiendaImpl = new CalculadoraTiendaImpl();
    }

    /**
     * Registra la compra de los productos del carrito del usuario, genera el recibo y actualiza la tienda.
     *
     * @param usuarioLog usuario que realiza la compra
     * @param tipoPagoEnum tipo de pago seleccionado
     * @param libroDAO DAO para libros
     * @throws IOException si ocurre un error de entrada/salida
     * @throws SQLException si ocurre un error de base de datos
     */
    public void aggListaCompra(Usuario usuarioLog, TipoPagoEnum tipoPagoEnum, LibroDAO libroDAO) throws IOException, SQLException {
        ArrayList<Libro> listaCarritoUser = tienda.getUsuarioActual().getCarrito().getLibros();
        numeroRecibo = compraDAO.seleccionarRegistros().size() + 1;
        LocalDateTime fecha = LocalDateTime.now();
        boolean registradoCompra = false;
        Recibo recibo = new Recibo();
        for (Libro libroCarritoUser : listaCarritoUser) {//Itera el carrito del usuario
            LibroComprado libroComprado = new LibroComprado();
            Libro libro = consultaCatalogo(libroDAO, libroCarritoUser);
            libroComprado.setIsbn(libro.getIsbn());
            libroComprado.setTitulo(libro.getTitulo());

            setInfoRecibo(usuarioLog, fecha, recibo);
            registradoCompra = validarCompraRegistrada(registradoCompra, recibo);

            setProductoCompra(calculadoraTiendaImpl, libroCarritoUser, recibo, libroComprado, libro);
            setResumenCarritoCompra(usuarioLog, tipoPagoEnum, libroDAO, listaCarritoUser, recibo);
        }
        reciboDAO.insertarDatos(recibo);
        validarExistenciaRecibosTienda(recibo);
        tienda.getRecibosTienda().get(tienda.getUsuarioActual().getCuenta().getCorreo()).add(recibo);
        tienda.getUsuarioActual().setRecibosCompras(reciboDAO.seleccionarRegistrosCompras(tienda.getUsuarioActual().getCuenta().getCorreo()));
    }

    /**
     * Valida la existencia de la lista de recibos en la tienda para el usuario actual.
     * Si no existe, la crea y agrega el recibo.
     *
     * @param recibo Recibo generado en la compra
     */
    private void validarExistenciaRecibosTienda(Recibo recibo) {
        if (tienda.getRecibosTienda().get(tienda.getUsuarioActual().getCuenta().getCorreo()) == null) {
            ArrayList<Recibo> listaRecibos = new ArrayList<>();
            listaRecibos.add(recibo);
            tienda.getRecibosTienda().put(tienda.getUsuarioActual().getCuenta().getCorreo(), listaRecibos);
        }
    }

    /**
     * Consulta un libro en el catálogo a partir del ISBN del libro en el carrito.
     *
     * @param libroDAO DAO para libros
     * @param libroCarritoUser libro del carrito
     * @return libro encontrado en el catálogo
     * @throws SQLException si ocurre un error de base de datos
     */
    private Libro consultaCatalogo(LibroDAO libroDAO, Libro libroCarritoUser) throws SQLException {
        Libro libro = new Libro();
        libro.setIsbn(String.valueOf(libroCarritoUser.getIsbn()));
        libro = libroDAO.seleccionarRegistro(libro);
        return libro;
    }

    /**
     * Valida si la compra ya fue registrada y, si no, la registra.
     *
     * @param registradoCompra bandera de registro
     * @param recibo recibo generado
     * @return true si se registró la compra, false si ya estaba registrada
     * @throws SQLException si ocurre un error de base de datos
     */
    private boolean validarCompraRegistrada(boolean registradoCompra, Recibo recibo) throws SQLException {
        if (!registradoCompra) {
            compraDAO.insertarDatos(recibo);
            return true;
        }
        return registradoCompra;
    }

    /**
     * Establece el resumen de la compra del carrito en el recibo, calculando totales, impuestos y descuentos.
     *
     * @param usuarioLog usuario que realiza la compra
     * @param tipoPagoEnum tipo de pago seleccionado
     * @param libroDAO DAO para libros
     * @param listaCarritoUser lista de libros en el carrito
     * @param recibo recibo generado
     * @throws SQLException si ocurre un error de base de datos
     */
    private void setResumenCarritoCompra(Usuario usuarioLog, TipoPagoEnum tipoPagoEnum, LibroDAO libroDAO, ArrayList<Libro> listaCarritoUser, Recibo recibo) throws SQLException {
        recibo.getValorCompra().setPrecioBaseTotal(calculadoraTiendaImpl.calcularBaseTotalCompra(listaCarritoUser));
        recibo.getValorCompra().setImpuestos(calculadoraTiendaImpl.calcularImpuestoTotalCompra(listaCarritoUser));
        recibo.getValorCompra().setTotal(calculadoraTiendaImpl.total(recibo.getValorCompra().getPrecioBase(), recibo.getValorCompra().getImpuestos()));
        recibo.getValorCompra().setDescuentoPremium(calculadoraTiendaImpl.descuentoPremiumTotal(recibo.getValorCompra().getTotal(), tienda.getUsuarioActual()));
        recibo.getValorCompra().setDescuentoFrecuencia(calculadoraTiendaImpl.descuentoFrecuencia(reciboDAO.seleccionarRegistrosCompras(usuarioLog.getCuenta().getCorreo()), recibo.getValorCompra().getTotal()));
        recibo.getValorCompra().setTotal(recibo.getValorCompra().getTotal() - recibo.getValorCompra().getDescuentoFrecuencia() - recibo.getValorCompra().getDescuentoPremium());
        recibo.setTipoPago(tipoPagoEnum);
    }

    /**
     * Establece la información de un producto comprado en el recibo.
     *
     * @param calculadoraTiendaImpl utilidad para cálculos
     * @param libroCarritoUser libro del carrito
     * @param recibo recibo generado
     * @param libroComprado objeto de libro comprado
     * @param libroCatalogo libro del catálogo
     */
    private void setProductoCompra(CalculadoraTiendaImpl calculadoraTiendaImpl, Libro libroCarritoUser, Recibo recibo, LibroComprado libroComprado, Libro libroCatalogo) {
        libroComprado.setCantidadComprada(libroCarritoUser.getStockReservado());
        libroComprado.setPrecioVenta(calculadoraTiendaImpl.calcularBase(libroCatalogo));
        libroComprado.setImpuestoUnitario(calculadoraTiendaImpl.calcularImpuesto(libroCatalogo));
        libroComprado.setImpuestoTotal(calculadoraTiendaImpl.calcularImpuestoTotalProducto(libroCarritoUser));
        libroComprado.setPrecioTotalSinIva(calculadoraTiendaImpl.calcularBaseTotalProducto(libroCarritoUser));
        libroComprado.setPrecioTotal(calculadoraTiendaImpl.total(libroComprado.getPrecioTotalSinIva(), libroComprado.getImpuestoTotal()));
        recibo.getListaProductosComprados().add(libroComprado);
    }

    /**
     * Establece la información general del recibo (correo, nombre, dirección, número y fecha).
     *
     * @param usuarioLog usuario que realiza la compra
     * @param fecha fecha y hora de la compra
     * @param recibo recibo generado
     */
    private void setInfoRecibo(Usuario usuarioLog, LocalDateTime fecha, Recibo recibo) {
        recibo.setCorreo(usuarioLog.getCuenta().getCorreo());
        recibo.setNombreUsuario(usuarioLog.getNombre());
        recibo.setDireccion(usuarioLog.getDireccionEnvio());
        recibo.setNumeroRecibo(numeroRecibo);
        recibo.setFechaCompra(fecha);
    }
}
