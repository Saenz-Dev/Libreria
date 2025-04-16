package co.edu.uptc.modelo;

import java.io.Serializable;

public class ResumenProductoDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4950101089868893193L;
    private double subtotal;
    private int cantidadReservada;

    public ResumenProductoDTO(double subtotal, int cantidadReservada) {
        this.subtotal = subtotal;
        this.cantidadReservada = cantidadReservada;
    }

    public ResumenProductoDTO() {}

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getCantidadReservada() {
        return cantidadReservada;
    }

    public void setCantidadReservada(int cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }
}
