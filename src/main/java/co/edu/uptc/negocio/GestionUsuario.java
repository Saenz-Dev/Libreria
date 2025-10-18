package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.contrato.*;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.*;
import co.edu.uptc.persistencia.UsuarioDAO;

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

    /**
     * Utilidad para validación de datos de usuario.
     */
    private Expresion expresion;

    /**
     * Instancia del administrador del sistema.
     */
    private Administrador administrador;

    /**
     * Referencia a la tienda virtual.
     */
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
    public GestionUsuario(Tienda tienda, UsuarioDAO usuarioDAO, IRepositorio<Usuario> repositorioUsuario, IRepositorio<Cuenta> repositorioCuenta, IRolAutenticacion rolAutenticacion, IUsuarioConverter usuarioConverter, IUsuarioValidator usuarioValidator, IAutenticacion autenticacion) throws SQLException {
        this.rolAutenticacion = rolAutenticacion;
        this.usuarioConverter = usuarioConverter;
        this.usuarioValidator = usuarioValidator;
        this.autenticacion = autenticacion;
        this.tienda = tienda;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioCuenta = repositorioCuenta;

        tienda.getUsuarioActual().getCuenta().setCorreo("user_default");// TODO cambiar esto y mejor dejar usuario default en un JSON
        tienda.setUsuarioActual(usuarioDAO.seleccionarRegistro(tienda.getUsuarioActual()));
        expresion = new Expresion();
        administrador = new Administrador();
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
        Cuenta cuentaBuscada = crearEntidadCuenta(correo, contrasena);
        expresion.validarCamposVaciosCuenta(cuentaBuscada);
        Cuenta cuentaEncontrada = usuarioValidator.validarExistenciaUsuario(cuentaBuscada);
        usuarioValidator.validarCuentaEncontrada(cuentaBuscada, cuentaEncontrada);
        tienda.getUsuarioActual().setCuenta(cuentaEncontrada);
        autenticacion.iniciarSesion(cuentaEncontrada);
        migrarLibrosCarrito();
    }

    private Cuenta crearEntidadCuenta(String correo, String contrasena) {
        Cuenta cuenta = new Cuenta();
        cuenta.setCorreo(correo);
        cuenta.setContrasena(contrasena);
        return cuenta;
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
        RegistroLog.registrarInfo(tienda.getUsuarioActual().getCuenta().getCorreo() + " cerró la sesión.");
        repositorioUsuario.actualizar(tienda.getUsuarioActual());
        repositorioCuenta.actualizar(tienda.getUsuarioActual().getCuenta());
        asignarUsuarioGenerico();
        if (cerrarAplicacion) {
            RegistroLog.fileHandler.close();
        }
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
        if (usuario.getCuenta().getCorreo().equals(tienda.getUsuarioActual().getCuenta().getCorreo())) {
            usuario.getCuenta().setLog(true);
        }
        if (usuario.getTipoCliente().equals(TipoUsuarioEnum.Premium)) {
            UsuarioPremium usuarioPremium = new UsuarioPremium(usuario);
            usuarioDAO.actualizarDatos(usuarioPremium);
            cuentaDAO.actualizarDatos(usuarioPremium.getCuenta());
            if (usuario.getCuenta().getCorreo().equals(tienda.getUsuarioActual().getCuenta().getCorreo())) {
                usuarioPremium.setRecibosCompras(tienda.getUsuarioActual().getRecibosCompras());
                tienda.setUsuarioActual(usuarioPremium);
            }
            return;
        }
        usuarioDAO.actualizarDatos(usuario);
        cuentaDAO.actualizarDatos(usuario.getCuenta());
        if (usuario.getCuenta().getCorreo().equals(tienda.getUsuarioActual().getCuenta().getCorreo())) {
            usuario.setRecibosCompras(tienda.getUsuarioActual().getRecibosCompras());
            tienda.setUsuarioActual(usuario);
        }
    }

    @Override
    public Usuario leer(Usuario usuario) throws RepositorioException {
        return repositorioUsuario.consultar(usuario);
    }

    @Override
    public void eliminar(Usuario usuario) throws RepositorioException {
        ArrayList<Recibo> listaRecibos = reciboDAO.seleccionarRegistrosCompras(correo);
        ArrayList<Libro> listaCarrito = carritoDAO.seleccionarRegistros(correo);
        ArrayList<Comentario> listaComentarios = comentarioDAO.seleccionarRegistros();
        buscarComentarioUsuario(listaComentarios, correo);
        if (!listaCarrito.isEmpty()) {
            RegistroLog.registrarAdvertencia("El usuario no se puede eliminar, tiene productos en el carrito.");
            throw new RuntimeException("El usuario no se puede eliminar, tiene productos en el carrito.");
        }
        if (listaRecibos == null || !listaRecibos.isEmpty()) {
            RegistroLog.registrarAdvertencia("El usuario no se puede eliminar, tiene compras asociadas.");
            throw new RuntimeException("El usuario no se puede eliminar, tiene compras asociadas.");
        }
        usuarioDAO.eliminarRegistro(correo);
        cuentaDAO.eliminarRegistro(correo);
        tienda.setUsuarios(usuarioDAO.seleccionarRegistros());
    }
}
