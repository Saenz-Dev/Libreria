package co.edu.uptc.negocio;

import co.edu.uptc.contrato.IUsuarioConverter;
import co.edu.uptc.modelo.TipoUsuarioEnum;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.UsuarioPremium;
import co.edu.uptc.modelo.UsuarioRegular;

public class UsuarioConverter implements IUsuarioConverter {
    @Override
    public Usuario transformarEntidad(Usuario usuario) {
        Usuario usuarioGuardar;
        if (usuario.getTipoCliente().equals(TipoUsuarioEnum.Premium)) {
            usuarioGuardar = new UsuarioPremium(usuario);
            usuarioGuardar.setTipoCliente(TipoUsuarioEnum.Premium);
        } else {
            usuarioGuardar = new UsuarioRegular(usuario);
        }
        return usuarioGuardar;
    }
}
