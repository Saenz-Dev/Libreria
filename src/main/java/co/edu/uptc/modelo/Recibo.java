package co.edu.uptc.modelo;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Recibo implements Serializable {

    /**
     * Serialización de la clase para persistencia
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private String correo;
    private String nombreUsuario;
    private TotalesCompra totalesCompra;
    private int numeroRecibo;
    private LocalDateTime fechaCompra;
    private TipoPagoEnum tipoPagoEnum;
    private String direccion;
    private ArrayList<LibroComprado> listaProductosComprados;

    public Recibo() {
        listaProductosComprados = new ArrayList<>();
        totalesCompra = new TotalesCompra();
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public TotalesCompra getValorCompra() {
        return totalesCompra;
    }

    public void setValorCompra(TotalesCompra totalesCompra) {
        this.totalesCompra = totalesCompra;
    }

    public int getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(int numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public LocalDateTime obtenerFechaActual() {
	return LocalDateTime.now();
    }

    public TipoPagoEnum getTipoPago() {
        return tipoPagoEnum;
    }

    public void setTipoPago(TipoPagoEnum tipoPagoEnum) {
        this.tipoPagoEnum = tipoPagoEnum;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public ArrayList<LibroComprado> getListaProductosComprados() {
        return listaProductosComprados;
    }

    public void setListaProductosComprados(ArrayList<LibroComprado> listaProductosComprados) {
        this.listaProductosComprados = listaProductosComprados;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
