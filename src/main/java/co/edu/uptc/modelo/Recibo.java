package co.edu.uptc.modelo;

import co.edu.uptc.negocio.TipoPago;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Recibo {

    private String correo;
    private String nombreUser;
    private ValorCompra valorCompra;
    private int numeroRecibo;
    private LocalDateTime fecha;
    private TipoPago tipoPago;
    private String direccion;
    private ArrayList<ProductoCompra> listaProductosComprados;
    //private Descuento descuento;

    public Recibo() {
        listaProductosComprados = new ArrayList<>();
        valorCompra = new ValorCompra();
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public ValorCompra getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(ValorCompra valorCompra) {
        this.valorCompra = valorCompra;
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

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
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
