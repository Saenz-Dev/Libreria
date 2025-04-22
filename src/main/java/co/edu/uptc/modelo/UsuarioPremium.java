package co.edu.uptc.modelo;

import java.io.Serializable;

public class UsuarioPremium extends Usuario implements Serializable {

    private static final long serialVersionUID = 9050878676607141388L;

    public UsuarioPremium(Usuario usuario) {
        super(usuario);
        super.setDescuentoTipoUsuario(0.08);
    }
}
