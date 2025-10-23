package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.excepcion.UsuarioNoEncontradoException;
import co.edu.uptc.modelo.Cuenta;

public interface IUsuarioValidator {
    boolean validarCuentaEncontrada(Cuenta cuentaBuscada, Cuenta cuentaEncontrada);

    Cuenta validarExistenciaUsuario(Cuenta cuenta);

    void validarCuentaNula(Cuenta cuenta);
}
