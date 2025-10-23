package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.modelo.Cuenta;
import co.edu.uptc.modelo.Usuario;

public interface IAutenticacion {
    void iniciarSesion(Cuenta cuenta) throws RepositorioException;

    void cerrarSesion(Cuenta cuenta) throws RepositorioException;
}
