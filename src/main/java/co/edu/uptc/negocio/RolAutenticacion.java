package co.edu.uptc.negocio;

import co.edu.uptc.contrato.IRolAutenticacion;
import co.edu.uptc.modelo.Usuario;

/**
 * Encargada de validar el rol del usuario, por ejemplo si es:
 * - Administrador
 * - Usuario no logueado (user_default)
 */
public class RolAutenticacion implements IRolAutenticacion {

    private static final String ADMIN = "administrador";
    private static final String USUARIO_DEFAULT = "user_default";

    /**
     * Valida si el usuario logueado es el default
     *
     * @return retorna true si el usuario logueado es el default
     */
    @Override
    public boolean esUsuarioDefaultLogueado(Usuario usuario) {
        return "user_default".equals(usuario.getCuenta().getCorreo());
    }

    /**
     * Valida si el correo del administrador es el del usuario logueado
     *
     * @return retorna true si el correo del administrador es el del usuario
     * logueado
     */
    @Override
    public boolean isAdminLogueado(Usuario usuario) {
        return "administrador".equals(usuario.getCuenta().getCorreo());
    }
}
