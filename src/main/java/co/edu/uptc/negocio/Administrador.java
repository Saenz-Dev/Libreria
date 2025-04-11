package co.edu.uptc.negocio;

/**
 * Clase encargada de almacenar los datos del administrador.
 */
public class Administrador {

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
