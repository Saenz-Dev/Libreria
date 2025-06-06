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

public class GestionCompra {

    private LibroComprado libroComprado;
    private ReciboDAO reciboDAO;
    private CarritoDAO carritoDAO;
    private CompraDAO compraDAO;
    private int numeroRecibo;
    private Tienda tienda;
    private CalculadoraIVA calculadoraIVA;


    public GestionCompra(Tienda tienda, ReciboDAO reciboDAO, CarritoDAO carritoDAO, CompraDAO compraDAO) {
        this.tienda = tienda;
        numeroRecibo = 0;
        libroComprado = new LibroComprado();
        this.compraDAO = compraDAO;
        this.reciboDAO = reciboDAO;
        this.carritoDAO = carritoDAO;
        calculadoraIVA = new CalculadoraIVA();
    }

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

            setProductoCompra(calculadoraIVA, libroCarritoUser, recibo, libroComprado, libro);
            setValorCompra(usuarioLog, tipoPagoEnum, libroDAO, listaCarritoUser, recibo);
        }
        reciboDAO.insertarDatos(recibo);
        validarExistenciaRecibosTienda(recibo);
        tienda.getRecibosTienda().get(tienda.getUsuarioActual().getCuenta().getCorreo()).add(recibo);
        tienda.getUsuarioActual().setRecibosCompras(reciboDAO.seleccionarRegistrosCompras(tienda.getUsuarioActual().getCuenta().getCorreo()));
    }

    private void validarExistenciaRecibosTienda(Recibo recibo) {
        if (tienda.getRecibosTienda().get(tienda.getUsuarioActual().getCuenta().getCorreo()) == null) {
            ArrayList<Recibo> listaRecibos = new ArrayList<>();
            listaRecibos.add(recibo);
            tienda.getRecibosTienda().put(tienda.getUsuarioActual().getCuenta().getCorreo(),listaRecibos);
        }
    }

    private Libro consultaCatalogo(LibroDAO libroDAO, Libro libroCarritoUser) throws SQLException {
        Libro libro = new Libro();
        libro.setIsbn(String.valueOf(libroCarritoUser.getIsbn()));
        libro = libroDAO.seleccionarRegistro(libro);
        return libro;
    }

    private boolean validarCompraRegistrada(boolean registradoCompra, Recibo recibo) throws SQLException {
        if (!registradoCompra) {
            compraDAO.insertarDatos(recibo);
            return true;
        }
        return registradoCompra;
    }

    private void setValorCompra(Usuario usuarioLog, TipoPagoEnum tipoPagoEnum, LibroDAO libroDAO, ArrayList<Libro> listaCarritoUser, Recibo recibo) throws SQLException, IOException {
        recibo.getValorCompra().setImpuestos(calculadoraIVA.impuestos(listaCarritoUser, libroDAO));
        recibo.getValorCompra().setSubtotal(calculadoraIVA.subtotal(listaCarritoUser, libroDAO));
        recibo.getValorCompra().setTotal(calculadoraIVA.total(recibo.getValorCompra().getSubtotal(), recibo.getValorCompra().getImpuestos()));
        recibo.getValorCompra().setDescuentoPremium(calculadoraIVA.descuentoPremium(recibo.getValorCompra().getTotal(), usuarioLog));
        recibo.getValorCompra().setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(reciboDAO.seleccionarRegistrosCompras(usuarioLog.getCuenta().getCorreo()), recibo.getValorCompra().getTotal()));
        recibo.getValorCompra().setTotal(recibo.getValorCompra().getTotal() - recibo.getValorCompra().getDescuentoPremium() - recibo.getValorCompra().getDescuentoFrecuencia());
        recibo.setTipoPago(tipoPagoEnum);
    }

    private void setProductoCompra(CalculadoraIVA calculadoraIVA, Libro libroCarritoUser, Recibo recibo, LibroComprado libroComprado, Libro libro) {
        libroComprado.setCantidadComprada(libroCarritoUser.getStockReservado());
        libroComprado.setPrecioVenta(libro.getPrecioVenta());
        libroComprado.setImpuestoUnitario(calculadoraIVA.impuestoProducto(libroCarritoUser, libro));
        libroComprado.setImpuestoTotal(calculadoraIVA.impuestoProductos(libroCarritoUser, libro));
        libroComprado.setPrecioTotal(calculadoraIVA.subtotalProducto(libroCarritoUser, libro));
        recibo.getListaProductosComprados().add(libroComprado);
    }

    private void setInfoRecibo(Usuario usuarioLog, LocalDateTime fecha, Recibo recibo) {
        recibo.setCorreo(usuarioLog.getCuenta().getCorreo());
        recibo.setNombreUsuario(usuarioLog.getNombre());
        recibo.setDireccion(usuarioLog.getDireccionEnvio());
        recibo.setNumeroRecibo(numeroRecibo);
        recibo.setFechaCompra(fecha);
    }
}
