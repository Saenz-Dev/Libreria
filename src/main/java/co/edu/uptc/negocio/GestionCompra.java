package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.LibroCarrito;
import co.edu.uptc.modelo.LibroComprado;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.TipoPagoEnum;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.CompraDAO;
import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.ReciboDAO;
import co.edu.uptc.persistencia.UsuarioDAO;

public class GestionCompra {

    private LibroComprado libroComprado;
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
        libroComprado = new LibroComprado();
        this.compraDAO = compraDAO;
        this.reciboDAO = reciboDAO;
        this.carritoDAO = carritoDAO;
    }

    public LibroComprado getProductoCompra() {
        return libroComprado;
    }

    public void setProductoCompra(LibroComprado libroComprado) {
        this.libroComprado = libroComprado;
    }

    public void aggListaCompra(Usuario usuarioLog, TipoPagoEnum tipoPagoEnum, UsuarioDAO usuarioDAO, LibroDAO libroDAO) throws IOException, SQLException {
        CalculadoraIVA calculadoraIVA = new CalculadoraIVA();
        LibroCarrito libroCarrito = new LibroCarrito();
        libroCarrito.setCorreo_usuario(usuarioLog.getCuenta().getCorreo());
        if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null || libroCarrito.getCorreo_usuario().isBlank()) {
            RegistroLog.registrarAdvertencia("❗ Se intentó seleccionar registros con un correo de usuario nulo o vacío.");
            throw new RuntimeException("⚠️ No se proporcionó un usuario válido para consultar su carrito.");
        }
        ArrayList<LibroCarrito> listaCarritoUser = carritoDAO.seleccionarRegistros(libroCarrito);
        numeroRecibo = compraDAO.seleccionarRegistros().size() + 1;
        LocalDateTime fecha = LocalDateTime.now();
        boolean registradoCompra = false;
        for (LibroCarrito libroCarritoUser : listaCarritoUser) {//Itera el carrito del usuario
            Recibo recibo = new Recibo();
            LibroComprado libroComprado = new LibroComprado();
            Libro libro = consultaCatalogo(libroDAO, libroCarritoUser);
            libroComprado.setIsbn(libro.getIsbn());
            libroComprado.setTitulo(libro.getTitulo());

            setInfoRecibo(usuarioLog, fecha, recibo);
            registradoCompra = validarCompraRegistrada(registradoCompra, recibo);

            setProductoCompra(calculadoraIVA, libroCarritoUser, recibo, libroComprado, libro);
            setValorCompra(usuarioLog, tipoPagoEnum, libroDAO, calculadoraIVA, listaCarritoUser, recibo);
            reciboDAO.insertarDatos(recibo);
        }
    }

    private Libro consultaCatalogo(LibroDAO libroDAO, LibroCarrito libroCarritoUser) throws SQLException {
        Libro libro = new Libro();
        libro.setIsbn(String.valueOf(libroCarritoUser.getIsbn_libro()));
        libro = libroDAO.seleccionarRegistro(libro);
        return libro;
    }

    private boolean validarCompraRegistrada(boolean registradoCompra, Recibo recibo) throws SQLException {
        if (!registradoCompra) {
            compraDAO.insertarDatos(recibo);
            registradoCompra = true;
        }
        return registradoCompra;
    }

    private void setValorCompra(Usuario usuarioLog, TipoPagoEnum tipoPagoEnum, LibroDAO libroDAO, CalculadoraIVA calculadoraIVA, ArrayList<LibroCarrito> listaCarritoUser, Recibo recibo) throws SQLException, IOException {
        recibo.getValorCompra().setImpuestos(calculadoraIVA.impuestos(listaCarritoUser, libroDAO));
        recibo.getValorCompra().setSubtotal(calculadoraIVA.subtotal(listaCarritoUser, libroDAO));
        recibo.getValorCompra().setTotal(calculadoraIVA.total(recibo.getValorCompra().getSubtotal(), recibo.getValorCompra().getImpuestos()));
        recibo.getValorCompra().setDescuentoPremium(calculadoraIVA.descuentoPremium(recibo.getValorCompra().getTotal(), usuarioLog));
        recibo.getValorCompra().setDescuentoFrecuencia(calculadoraIVA.descuentoFrecuencia(reciboDAO.seleccionarRegistrosCompras(recibo), recibo.getValorCompra().getTotal()));
        recibo.getValorCompra().setTotal(recibo.getValorCompra().getTotal() - recibo.getValorCompra().getDescuentoPremium() - recibo.getValorCompra().getDescuentoFrecuencia());
        recibo.setTipoPago(tipoPagoEnum);
    }

    private void setProductoCompra(CalculadoraIVA calculadoraIVA, LibroCarrito libroCarritoUser, Recibo recibo, LibroComprado libroComprado, Libro libro) {
        libroComprado.setNumeroLibros(libroCarritoUser.getCantidad());
        libroComprado.setPrecioUnitario(libro.getPrecioVenta());
        libroComprado.setImpuestoUnitario(calculadoraIVA.impuestoProducto(libro));
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

    public Libro libroCarrito(String isbn, Usuario usuario) {
        for (Libro libro : usuario.getCarrito().getLibros()) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }

}
