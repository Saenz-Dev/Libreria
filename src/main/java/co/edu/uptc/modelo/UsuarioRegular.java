package co.edu.uptc.modelo;

import java.io.Serializable;

public class UsuarioRegular extends Usuario implements Serializable {

    private static final long serialVersionUID = -7536014726436107538L;

    public UsuarioRegular(Usuario usuario) {
        super(usuario);
    }
}
