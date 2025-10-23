package co.edu.uptc.negocio;

import co.edu.uptc.contrato.IRepositorio;
import co.edu.uptc.contrato.IUsuarioValidator;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.excepcion.UsuarioNoEncontradoException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Administrador;
import co.edu.uptc.modelo.Cuenta;

public class UsuarioValidatorImp implements IUsuarioValidator {


    /**
     * Valida si la cuenta buscada es nula o si la contraseña es incorrecta
     *
     * @param cuentaBuscada    cuenta del usuario.
     * @param cuentaEncontrada cuenta encontrada en la base de datos.
     * @throws IllegalArgumentException si la cuenta no está registrada o si la contraseña es incorrecta.
     */
    @Override
    public boolean validarCuentaEncontrada(Cuenta cuentaBuscada, Cuenta cuentaEncontrada) {
        if (cuentaEncontrada == null) {
            RegistroLog.registrarAdvertencia("❌ Intento de inicio de sesión con correo no registrado: " + cuentaBuscada.getCorreo());
            throw new IllegalArgumentException("El usuario ingresado no está registrado.");
        }

        if (!cuentaEncontrada.getContrasena().equals(cuentaBuscada.getContrasena())) {
            RegistroLog.registrarAdvertencia("❌ Contraseña incorrecta para el correo: " + cuentaBuscada.getCorreo());
            throw new IllegalArgumentException("El correo o la contraseña es incorrecta.");
        }

        if (cuentaEncontrada.getCorreo().equals(Administrador.CORREO) && !cuentaEncontrada.getContrasena().equals(cuentaBuscada.getContrasena())) {
            RegistroLog.registrarAdvertencia("❌ Contraseña incorrecta para el administrador: " + cuentaBuscada.getCorreo());
            throw new IllegalArgumentException("El correo o la contraseña es incorrecta.");
        }
        return true;
    }

    /**
     * Valida si el usuario existe
     *
     * @param cuenta cuenta del usuario
     * @throws IllegalArgumentException si el usuario no existe
     */
    @Override
    public Cuenta validarExistenciaUsuario(Cuenta cuenta) throws UsuarioNoEncontradoException {
        if (cuenta == null)
            throw new UsuarioNoEncontradoException("Esta cuenta no ha sido encontrada."); // Si la cuenta consultada es nula es porque no existe.
        return cuenta; //Retorna la cuenta.
    }

    @Override
    public void validarCuentaNula(Cuenta cuenta) throws UsuarioNoEncontradoException {
        if (cuenta != null) {
            RegistroLog.registrarAdvertencia("Intento de registrar un correo ya existente: " + cuenta.getCorreo());
            throw new IllegalArgumentException("El correo '" + cuenta.getCorreo() + "' ya está vinculado a otra cuenta");
        }
    }
}
