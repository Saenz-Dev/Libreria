package co.edu.uptc.modelo;

import java.io.Serializable;

/**
 * Clase encargada de almacenar la cuenta asociada al usuario.
 */
public class Cuenta implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -379150053994745711L;

    /**
     * Correo del usuario
     */
    private String correo;

    /**
     * Contraseña del usuario
     */
    private String contrasena;

    /**
     * Indica si el usuario está logueado
     */
    private boolean isLog;

    /**
     * Constructor de la clase
     */
    public Cuenta() {}

    /**
     * Constructor de la clase
     * @param correo correo del usuario
     * @param contrasena contraseña del usuario
     * @param isLog indica si el usuario está logueado
     */
    public Cuenta(String correo, String contrasena, boolean isLog) {
        this.correo = correo;
        this.contrasena = contrasena;
        this.isLog = isLog;
    }

    /**
     * Metodo que devuelve el correo del usuario
     * @return correo del usuario
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Metodo que actualiza el correo del usuario
     * @param correo correo del usuario
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Metodo que devuelve la contraseña del usuario
     * @return contraseña del usuario
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Metodo que actualiza la contraseña del usuario
     * @param contrasena contraseña del usuario
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Metodo que devuelve si el usuario está logueado
     * @return indica si el usuario está logueado
     */
    public boolean isLog() {
        return isLog;
    }

    /**
     * Metodo que actualiza si el usuario está logueado
     * @param log indica si el usuario está logueado
     */
    public void setLog(boolean log) {
        isLog = log;
    }
}
