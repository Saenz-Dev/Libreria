package co.edu.uptc.negocio;

import co.edu.uptc.contrato.*;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.excepcion.UsuarioNoEncontradoException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.*;
import co.edu.uptc.persistencia.CarritoDAO;

import java.util.ArrayList;

public class Autenticacion implements IAutenticacion {

    private IRepositorio<Cuenta> repositorioCuenta;
    private IRepositorio<Carrito> repositorioCarrito;
    private IRepositorio<Usuario> repositorioUsuario;
    private IUsuarioValidator usuarioValidator;
    private IUsuarioConverter convertidorUsuario;
    private IGestionTienda<Carrito> gestionCarrito;
    private Tienda tienda;
    private Expresion expresion;


    public Autenticacion(IRepositorio<Cuenta> repositorioCuenta, IUsuarioValidator validadorUsuario, IUsuarioConverter convertidorUsuario, IGestionTienda<Carrito> gestionCarrito, IRepositorio<Usuario> repositorioUsuario, Expresion expresion, Tienda tienda) {
        this.repositorioCuenta = repositorioCuenta;
        this.usuarioValidator = validadorUsuario; //TODO se debe recibir una clase implementación osea UsuarioValidatorImpl
        this.convertidorUsuario = convertidorUsuario;
        this.gestionCarrito = gestionCarrito;
        this.expresion = expresion;
        this.tienda = tienda;
    }

    /**
     * Autentica el usuario con la cuenta.
     *
     * @param cuenta cuenta del usuario para autenticar
     * @throws RepositorioException         Excepción del repositorio.
     * @throws UsuarioNoEncontradoException si no encuentra un usuario en persistencia.
     */
    @Override
    public void iniciarSesion(Cuenta cuenta) throws RepositorioException {
        Cuenta cuentaConsultada = repositorioCuenta.consultar(cuenta);
        Cuenta cuentaEncontrada = usuarioValidator.validarExistenciaUsuario(cuentaConsultada);
        usuarioValidator.validarCuentaEncontrada(cuentaConsultada, cuentaEncontrada);
        tienda.getUsuarioActual().setCuenta(cuentaEncontrada);
        cuentaEncontrada.setLog(true); //Actualizo el estado de la cuenta para iniciar sesión.
        repositorioCuenta.actualizar(cuentaEncontrada);
    }

    @Override
    public void cerrarSesion(Cuenta cuenta) throws RepositorioException {
        cuenta.setLog(false);
        RegistroLog.registrarInfo(cuenta.getCorreo() + " cerró la sesión.");
        repositorioUsuario.actualizar(tienda.getUsuarioActual());
        repositorioCuenta.actualizar(tienda.getUsuarioActual().getCuenta());
        Usuario usuario = new Usuario();
        usuario.getCuenta().setCorreo("user_default");
        tienda.setUsuarioActual(repositorioUsuario.consultar(usuario));
    }
}
