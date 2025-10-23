package co.edu.uptc.modelo;

/**
 * Clase que representa un código premium en la aplicación.
 * Un código premium puede ser utilizado por un usuario para acceder a beneficios exclusivos.
 */
public class CodigoPremium {
    /** Código premium único. */
    private String codigo;
    /** Indica si el código ya ha sido usado. */
    private boolean usado;

    /**
     * Obtiene el valor del código premium.
     * @return Código premium.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el valor del código premium.
     * @param codigo Código premium a establecer.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Indica si el código premium ya ha sido usado.
     * @return true si el código ha sido usado, false en caso contrario.
     */
    public boolean getUsado() {
        return usado;
    }

    /**
     * Establece el estado de uso del código premium.
     * @param usado true si el código ha sido usado, false en caso contrario.
     */
    public void setUsado(boolean usado) {
        this.usado = usado;
    }
}
