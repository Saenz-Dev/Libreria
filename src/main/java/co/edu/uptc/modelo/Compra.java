package co.edu.uptc.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Compra implements Serializable {

    private int numeroCompra;
    private String correo;
    private LocalDateTime fecha;

    public int getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(int numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
