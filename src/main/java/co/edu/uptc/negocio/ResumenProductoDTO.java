package co.edu.uptc.negocio;

public class ResumenProductoDTO {

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
