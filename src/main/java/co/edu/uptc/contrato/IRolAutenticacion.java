package co.edu.uptc.contrato;

import co.edu.uptc.modelo.Usuario;

public interface IRolAutenticacion {
    boolean esUsuarioDefaultLogueado(Usuario usuario);

    boolean isAdminLogueado(Usuario usuario);
}
