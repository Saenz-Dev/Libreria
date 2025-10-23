package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.contrato.*;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.*;
import co.edu.uptc.persistencia.ReciboDAO;
import co.edu.uptc.persistencia.UsuarioDAO;
import co.edu.uptc.persistencia.busqueda.BusquedaCarritoPorCorreo;
import co.edu.uptc.persistencia.busqueda.BusquedaComentarioPorCorreo;
import co.edu.uptc.persistencia.busqueda.BusquedaReciboPorCorreo;

/**
 * Clase encargada de gestionar los usuarios de la tienda virtual.
 * Permite registrar, modificar, eliminar y autenticar usuarios, así como validar sus datos y gestionar su carrito de compras.
 * Utiliza DAOs para la persistencia de usuarios, cuentas y carritos, y utilidades para la validación de datos.
 */
public class GestionUsuario implements IGestionTienda<Usuario> {

    private IRolAutenticacion rolAutenticacion;
    private IUsuarioConverter usuarioConverter;
    private IUsuarioValidator usuarioValidator;
    private IAutenticacion autenticacion;
    private IRepositorio<Usuario> repositorioUsuario;
    private IRepositorio<Cuenta> repositorioCuenta;
    private IRepositorio<Recibo> repositorioRecibo;
    private IRepositorio<Carrito> repositorioCarrito;
    private IConsultaStrategy<Recibo> consultaReciboStrategy;
    private IConsultaStrategy<Libro> consultaLibroStrategy;
    private IConsultaStrategy<Comentario> consultaComentarioStrategy;
    private Expresion expresion;
    private Tienda tienda;


    /**
     * Constructor que inicializa la gestión de usuarios con las dependencias necesarias.
     *
     * @param tienda     referencia a la tienda virtual
     * @param usuarioDAO DAO para usuarios
     * @param cuentaDAO  DAO para cuentas
     * @param carritoDAO DAO para carritos
     * @throws SQLException si ocurre un error de base de datos
     */
    public GestionUsuario(Tienda tienda, UsuarioDAO usuarioDAO, IRepositorio<Usuario> repositorioUsuario, IRepositorio<Cuenta> repositorioCuenta, IRolAutenticacion rolAutenticacion, IUsuarioConverter usuarioConverter, IAutenticacion autenticacion, UsuarioValidatorImp usuarioValidator, IRepositorio<Recibo> repositorioRecibo, IRepositorio<Carrito> repositorioCarrito, IConsultaStrategy<Recibo> consultaReciboStrategy, IConsultaStrategy<Libro> consultaLibroStrategy, IConsultaStrategy<Comentario> consultaComentarioStrategy) throws SQLException {
        this.rolAutenticacion = rolAutenticacion;
        this.usuarioConverter = usuarioConverter;
        this.usuarioValidator = usuarioValidator;
        this.autenticacion = autenticacion;
        this.tienda = tienda;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioCuenta = repositorioCuenta;
        this.repositorioRecibo = repositorioRecibo;
        this.repositorioCarrito = repositorioCarrito;
        this.consultaReciboStrategy = consultaReciboStrategy;
        this.consultaLibroStrategy = consultaLibroStrategy;
        this.consultaComentarioStrategy = consultaComentarioStrategy;

        tienda.getUsuarioActual().getCuenta().setCorreo("user_default");
        tienda.setUsuarioActual(repositorioUsuario.consultar(tienda.getUsuarioActual()));
        expresion = new Expresion();
    }

    /**
     * Inicia sesión del usuario
     *
     * @param correo     correo del usuario
     * @param contrasena constrasena del usuario
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple
     *                                  con las reglas
     */
    public void iniciarSesion(String correo, String contrasena) throws IllegalArgumentException, SQLException {
        Cuenta cuenta = new Cuenta(correo, contrasena);
        expresion.validarCamposVaciosCuenta(cuenta);
        autenticacion.iniciarSesion(cuenta);
//        migrarLibrosCarrito(); //TODO pasarlo a la clase que contiene esta clase osea GestionTienda
    }

    /**
     * Cierra la sesión del usuario logueado y asigna el usuario genérico.
     *
     * @param cerrarAplicacion si se debe cerrar la aplicación
     * @throws RuntimeException si ocurre un error al cerrar la sesión
     * @throws SQLException     si ocurre un error al acceder a la base de datos
     */
    public void cerrarSesionUsuario(boolean cerrarAplicacion) throws RuntimeException, SQLException {
        autenticacion.cerrarSesion(tienda.getUsuarioActual().getCuenta());
        asignarUsuarioGenerico();
        if (cerrarAplicacion) RegistroLog.cerrar();
    }

    public void asignarUsuarioGenerico() throws SQLException {
        Usuario usuario = new Usuario();
        usuario.getCuenta().setCorreo("user_default");
        tienda.setUsuarioActual(repositorioUsuario.consultar(usuario));
    }

    /**
     * Registra un usuario en la base de datos.
     *
     * @param usuario usuario a registrar
     * @throws RuntimeException si alguno de los datos del usuario no cumple con las reglas
     * @throws SQLException     si ocurre un error de base de datos
     */
    @Override
    public void guardar(Usuario usuario) throws RepositorioException {
        Cuenta cuenta = repositorioCuenta.consultar(usuario.getCuenta());
        usuarioValidator.validarCuentaNula(cuenta);
        expresion.validarDatosObligatoriosUser(usuario);
        expresion.validarDatosUsuario(usuario);
        Usuario usuarioGuardar = usuarioConverter.transformarEntidad(usuario);
        usuarioGuardar.getCuenta().setLog(false);
        repositorioCuenta.guardar(usuarioGuardar.getCuenta());
        repositorioUsuario.guardar(usuarioGuardar);
    }

    /**
     * Modifica los datos del usuario logueado
     *
     * @param usuario usuario a modificar
     * @throws IOException              si ocurre algún error cuando no se escribe
     *                                  el usuario en el JSON
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple
     *                                  con las reglas
     */
    @Override
    public void actualizar(Usuario usuario) throws RepositorioException {
        expresion.validarDatosObligatoriosUser(usuario);
        expresion.validarDatosUsuario(usuario);
        validarIgualdadCorreo(usuario);
        repositorioUsuario.actualizar(usuario);
        repositorioCuenta.actualizar(usuario.getCuenta());
        if (usuario.getTipoCliente().equals(TipoUsuarioEnum.Premium)) {
            usuario = new UsuarioPremium(usuario);
        }
        if (usuario.getCuenta().getCorreo().equals(tienda.getUsuarioActual().getCuenta().getCorreo())) {
            usuario.setRecibosCompras(tienda.getUsuarioActual().getRecibosCompras());
            tienda.setUsuarioActual(usuario);
        }
    }

    private void validarIgualdadCorreo(Usuario usuario) {
        if (usuario.getCuenta().getCorreo().equals(tienda.getUsuarioActual().getCuenta().getCorreo())) {
            usuario.getCuenta().setLog(true);
        }
    }

    @Override
    public Usuario leer(Usuario usuario) throws RepositorioException {
        return repositorioUsuario.consultar(usuario);
    }

    @Override
    public void eliminar(Usuario usuario) throws SQLException {
        IBusquedaStrategy busquedaStrategy = new BusquedaReciboPorCorreo(usuario.getCuenta().getCorreo());
        List<Recibo> listaRecibos = consultaReciboStrategy.consultar(busquedaStrategy);
        busquedaStrategy = new BusquedaCarritoPorCorreo(usuario.getCuenta().getCorreo());
        List<Libro> listaCarrito = consultaLibroStrategy.consultar(busquedaStrategy);
        busquedaStrategy = new BusquedaComentarioPorCorreo(usuario.getCuenta().getCorreo());
        List<Comentario> listaComentarios = consultaComentarioStrategy.consultar(busquedaStrategy);
        verificarRequisitosEliminacion(listaCarrito, listaRecibos, listaComentarios);
        repositorioUsuario.eliminar(usuario);
        repositorioCuenta.eliminar(usuario.getCuenta());
        tienda.setUsuarios(repositorioUsuario.consultar());
    }

    private static void verificarRequisitosEliminacion(List<Libro> listaCarrito, List<Recibo> listaRecibos, List<Comentario> listaComentarios) {
        if (!listaCarrito.isEmpty()) {
            RegistroLog.registrarAdvertencia("El usuario no se puede eliminar, tiene productos en el carrito.");
            throw new RuntimeException("El usuario no se puede eliminar, tiene productos en el carrito.");
        }
        if (listaRecibos == null || !listaRecibos.isEmpty()) {
            RegistroLog.registrarAdvertencia("El usuario no se puede eliminar, tiene compras asociadas.");
            throw new RuntimeException("El usuario no se puede eliminar, tiene compras asociadas.");
        }
        if (listaComentarios == null || !listaComentarios.isEmpty()) {
            RegistroLog.registrarAdvertencia("El usuario no se puede eliminar, tiene comentarios asociados.");
            throw new RuntimeException("El usuario no se puede eliminar, tiene comentarios asociadas.");
        }
    }
}
