package co.edu.uptc.negocio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Clase encargada de almacenar la cuenta asociada al usuario.
 */
@Entity @Table(name = "CUENTA")
public class Cuenta {

    /**
     * Correo del usuario
     */
    @Id @Column(name = "CORREO") private String correo;

    /**
     * Contraseña del usuario
     */
    @Column(name = "CONTRASENA") private String contrasena;

    /**
     * Indica si el usuario está logueado
     */
    @Column(name = "LOGIN") private boolean isLog;

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
     * Método que devuelve el correo del usuario
     * @return correo del usuario
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Método que actualiza el correo del usuario
     * @param correo correo del usuario
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Método que devuelve la contraseña del usuario
     * @return contraseña del usuario
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Método que actualiza la contraseña del usuario
     * @param contrasena contraseña del usuario
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Método que devuelve si el usuario está logueado
     * @return indica si el usuario está logueado
     */
    public boolean isLog() {
        return isLog;
    }

    /**
     * Método que actualiza si el usuario está logueado
     * @param log indica si el usuario está logueado
     */
    public void setLog(boolean log) {
        isLog = log;
    }
}
