package co.edu.uptc.modelo;

import java.io.Serializable;

/**
 * Clase encargada de almacenar los datos del administrador.
 */
public class Administrador implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3956750637462768249L;
    /**
     * Correo del administrador
     */
    private final String CORREO = "admin@gmail.com";

    /**
     * Constructor de la clase
     */
    public Administrador() {}


    /**
     * Método que devuelve el correo del administrador
     * @return correo del administrador
     */
    public String getCORREO() {
        return CORREO;
    }
}
