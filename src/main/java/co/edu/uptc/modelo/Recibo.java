package co.edu.uptc.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Recibo {

    private String correo;
    private String nombreUser;
    private ResumenCompra resumenCompra;
    private int numeroRecibo;
    private LocalDateTime fecha;
    private TipoPagoEnum tipoPagoEnum;
    private String direccion;
    private ArrayList<ProductoCompra> listaProductosComprados;
    //private Descuento descuento;

    public Recibo() {
        listaProductosComprados = new ArrayList<>();
        resumenCompra = new ResumenCompra();
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public ResumenCompra getValorCompra() {
        return resumenCompra;
    }

    public void setValorCompra(ResumenCompra resumenCompra) {
        this.resumenCompra = resumenCompra;
    }

    public int getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(int numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
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

    public ArrayList<ProductoCompra> getListaProductosComprados() {
        return listaProductosComprados;
    }

    public void setListaProductosComprados(ArrayList<ProductoCompra> listaProductosComprados) {
        this.listaProductosComprados = listaProductosComprados;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }
}
