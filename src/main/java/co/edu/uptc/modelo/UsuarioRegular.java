package co.edu.uptc.modelo;

import java.io.Serializable;

/**
 * Clase que representa a un usuario regular en la tienda virtual.
 * Hereda de {@link Usuario} y no aplica descuentos especiales.
 * Permite la persistencia mediante serialización.
 */
public class UsuarioRegular extends Usuario implements Serializable {

    /**
     * Identificador de versión para la serialización de la clase.
     */
    private static final long serialVersionUID = -7536014726436107538L;

    /**
     * Constructor que crea un usuario regular a partir de un usuario existente.
     * No aplica descuentos adicionales.
     *
     * @param usuario Usuario base a convertir en regular
     */
    public UsuarioRegular(Usuario usuario) {
        super(usuario);
    }
}
