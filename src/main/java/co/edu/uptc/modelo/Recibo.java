package co.edu.uptc.modelo;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Representa un recibo de compra generado al realizar una transacción en la librería virtual.
 * Contiene información del usuario, detalles de la compra, productos adquiridos, fecha, tipo de pago y dirección de envío.
 * Permite la persistencia mediante serialización.
 */
public class Recibo implements Serializable {

    /**
     * Serialización de la clase para persistencia
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Correo electrónico del usuario que realizó la compra.
     */
    private String correo;
    /**
     * Nombre del usuario que realizó la compra.
     */
    private String nombreUsuario;
    /**
     * Objeto que almacena los totales de la compra (subtotal, descuentos, impuestos, total).
     */
    private TotalesCompra totalesCompra;
    /**
     * Número identificador único del recibo.
     */
    private int numeroRecibo;
    /**
     * Fecha y hora en que se realizó la compra.
     */
    private LocalDateTime fechaCompra;
    /**
     * Tipo de pago utilizado en la compra.
     */
    private TipoPagoEnum tipoPagoEnum;
    /**
     * Dirección de envío asociada a la compra.
     */
    private String direccion;
    /**
     * Lista de productos (libros) comprados en la transacción.
     */
    private ArrayList<LibroComprado> listaProductosComprados;

    /**
     * Constructor por defecto. Inicializa la lista de productos y los totales de la compra.
     */
    public Recibo() {
        listaProductosComprados = new ArrayList<>();
        totalesCompra = new TotalesCompra();
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * @return correo electrónico
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico del usuario.
     * @param correo correo electrónico
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene los totales de la compra.
     * @return objeto TotalesCompra
     */
    public TotalesCompra getValorCompra() {
        return totalesCompra;
    }

    /**
     * Establece los totales de la compra.
     * @param totalesCompra objeto TotalesCompra
     */
    public void setValorCompra(TotalesCompra totalesCompra) {
        this.totalesCompra = totalesCompra;
    }

    /**
     * Obtiene el número identificador del recibo.
     * @return número de recibo
     */
    public int getNumeroRecibo() {
        return numeroRecibo;
    }

    /**
     * Establece el número identificador del recibo.
     * @param numeroRecibo número de recibo
     */
    public void setNumeroRecibo(int numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    /**
     * Obtiene la fecha y hora de la compra.
     * @return fecha de compra
     */
    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    /**
     * Establece la fecha y hora de la compra.
     * @param fechaCompra fecha de compra
     */
    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    /**
     * Obtiene la fecha y hora actual del sistema.
     * @return fecha y hora actual
     */
    public LocalDateTime obtenerFechaActual() {
	return LocalDateTime.now();
    }

    /**
     * Obtiene el tipo de pago utilizado en la compra.
     * @return tipo de pago
     */
    public TipoPagoEnum getTipoPago() {
        return tipoPagoEnum;
    }

    /**
     * Establece el tipo de pago utilizado en la compra.
     * @param tipoPagoEnum tipo de pago
     */
    public void setTipoPago(TipoPagoEnum tipoPagoEnum) {
        this.tipoPagoEnum = tipoPagoEnum;
    }

    /**
     * Obtiene la dirección de envío asociada a la compra.
     * @return dirección de envío
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de envío asociada a la compra.
     * @param direccion dirección de envío
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la lista de productos (libros) comprados en la transacción.
     * @return lista de productos comprados
     */
    public ArrayList<LibroComprado> getListaProductosComprados() {
        return listaProductosComprados;
    }

    /**
     * Establece la lista de productos (libros) comprados en la transacción.
     * @param listaProductosComprados lista de productos comprados
     */
    public void setListaProductosComprados(ArrayList<LibroComprado> listaProductosComprados) {
        this.listaProductosComprados = listaProductosComprados;
    }

    /**
     * Obtiene el nombre del usuario que realizó la compra.
     * @return nombre del usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre del usuario que realizó la compra.
     * @param nombreUsuario nombre del usuario
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
