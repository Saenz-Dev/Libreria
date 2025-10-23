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

    public Cuenta(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
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
     * Devuelve la contraseña del usuario.
     * @return contraseña del usuario
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario.
     * @param contrasena contraseña del usuario
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Indica si el usuario está logueado.
     * @return true si está logueado, false en caso contrario
     */
    public boolean isLog() {
        return isLog;
    }

    /**
     * Establece el estado de logueo del usuario.
     * @param isLog true si está logueado, false en caso contrario
     */
    public void setLog(boolean isLog) {
        this.isLog = isLog;
    }
}
