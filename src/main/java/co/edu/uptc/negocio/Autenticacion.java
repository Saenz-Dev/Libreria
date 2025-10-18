package co.edu.uptc.negocio;

import co.edu.uptc.contrato.*;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.excepcion.UsuarioNoEncontradoException;
import co.edu.uptc.modelo.*;
import co.edu.uptc.persistencia.CarritoDAO;

import java.util.ArrayList;

public class Autenticacion implements IAutenticacion {

    private IRepositorio<Cuenta> repositorioCuenta;
    private IRepositorio<Carrito> repositorioCarrito;
    private IUsuarioValidator validadorUsuario;
    private IUsuarioConverter convertidorUsuario;
    private IGestionTienda<Carrito> gestionCarrito;
    private Tienda tienda;


    public Autenticacion(IRepositorio<Cuenta> repositorioCuenta, IUsuarioValidator validadorUsuario, IUsuarioConverter convertidorUsuario, IGestionTienda<Carrito> gestionCarrito, Tienda tienda) {
        this.repositorioCuenta = repositorioCuenta;
        this.validadorUsuario = validadorUsuario;
        this.convertidorUsuario = convertidorUsuario;
        this.gestionCarrito = gestionCarrito;
        this.tienda = tienda;
    }

    @Override
    public void iniciarSesion(Cuenta cuenta) {
        cuenta.setLog(true); //Actualizo el estado de la cuenta para iniciar sesión.
    }

    @Override
    public void cerrarSesion(Cuenta cuenta) {
        cuenta.setLog(false);
    }
}
