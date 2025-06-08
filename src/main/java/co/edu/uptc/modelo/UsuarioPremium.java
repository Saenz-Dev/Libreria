package co.edu.uptc.modelo;

import java.io.Serializable;

/**
 * Clase que representa a un usuario premium en la tienda virtual.
 * Hereda de {@link Usuario} y aplica un descuento especial para usuarios premium.
 * Permite la persistencia mediante serialización.
 */
public class UsuarioPremium extends Usuario implements Serializable {

    /**
     * Identificador de versión para la serialización de la clase.
     */
    private static final long serialVersionUID = 9050878676607141388L;

    /**
     * Constructor que crea un usuario premium a partir de un usuario existente.
     * Aplica automáticamente el descuento premium.
     *
     * @param usuario Usuario base a convertir en premium
     */
    public UsuarioPremium(Usuario usuario) {
        super(usuario);
        super.setDescuentoTipoUsuario(0.2);
    }
}
