package co.edu.uptc.negocio;

import co.edu.uptc.contrato.IRolAutenticacion;
import co.edu.uptc.modelo.Administrador;
import co.edu.uptc.modelo.Tienda;

public class RolAutenticacion implements IRolAutenticacion {

    private Tienda tienda;

    public RolAutenticacion(Tienda tienda) {
        this.tienda = tienda;
    }

    /**
     * Valida si el usuario logueado es el default
     *
     * @return retorna true si el usuario logueado es el default
     */
    @Override
    public boolean esUsuarioDefaultLogueado() {
        return "user_default".equals(tienda.getUsuarioActual().getCuenta().getCorreo());
    }

    /**
     * Valida si el correo del administrador es el del usuario logueado
     *
     * @return retorna true si el correo del administrador es el del usuario
     * logueado
     */
    @Override
    public boolean isAdminLogueado() {
        return "administrador".equals(tienda.getUsuarioActual().getCuenta().getCorreo());
    }
}
