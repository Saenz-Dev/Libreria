package co.edu.uptc.modelo;

import co.edu.uptc.negocio.TipoPago;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Recibo {

    private String usuario;
    private String nombreUser;
    private ValorCompra valorCompra;
    private int numeroRecibo;
    private String fecha;
    private TipoPago tipoPago;
    private String direccion;
    private ArrayList<ProductoCompra> listaProductosComprados;
    //private Descuento descuento;

    public Recibo() {
        listaProductosComprados = new ArrayList<>();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(int anio, int mes, int dia, String horaActual) {
        this.fecha = String.format("%d/%d/%d %s", anio, mes, dia, horaActual);
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
