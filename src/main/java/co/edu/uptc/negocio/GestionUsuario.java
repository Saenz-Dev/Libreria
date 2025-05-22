package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.modelo.Administrador;
import co.edu.uptc.modelo.Cuenta;
import co.edu.uptc.modelo.LibroCarrito;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.UsuarioPremium;
import co.edu.uptc.modelo.UsuarioRegular;
import co.edu.uptc.persistencia.CarritoDAO;
import co.edu.uptc.persistencia.CuentaDAO;
import co.edu.uptc.persistencia.UsuarioDAO;

/**
 * Clase encargada de gestionar los usuarios.
 */
public class GestionUsuario {

    private CuentaDAO cuentaDAO;

    private UsuarioDAO usuarioDAO;

    private CarritoDAO carritoDAO;

    private Usuario usuarioLog;

    /**
     * Instancia de Manejo de usuarios con JSON
     */
    private ManejoUsuarioJSON manejoUsuarioJSON;

    /**
     * Instancia de Expresión regular
     */
    private Expresion expresion;

    /**
     * Instancia de Administrador
     */
    private Administrador administrador;

    public Usuario userLog() throws SQLException, RuntimeException {
	usuarioLog = usuarioDAO.seleccionarRegistro(usuarioLog);
	usuarioLog.setCuenta(cuentaDAO.seleccionarRegistro(usuarioLog.getCuenta()));
	return usuarioLog;
    }

    public Cuenta crearCuentaUsuarioDefault() {
	Cuenta cuenta = new Cuenta();
	cuenta.setCorreo("user_default");
	cuenta.setContrasena("NN");
	cuenta.setLog(false);
	return cuenta;
    }

    public Usuario crearUsuarioDefault() {
	Usuario usuario = new Usuario();
	Cuenta cuenta = new Cuenta();
	cuenta.setCorreo("user_default");
	usuario.setNombre("Default");
	usuario.setDireccionEnvio("NN");
	usuario.setTelefono(0);
	usuario.setTipoCliente("NN");
	usuario.setDescuentoTipoUsuario(0);
	usuario.setCuenta(cuenta);
	return usuario;
    }

    public Cuenta crearCuentaAdmin() {
	Cuenta cuenta = new Cuenta();
	cuenta.setCorreo("administrador");
	cuenta.setContrasena("");
	cuenta.setLog(false);
	return cuenta;
    }

    public Usuario crearUsuarioAdmin() {
	Usuario usuario = new Usuario();
	Cuenta cuenta = new Cuenta();
	cuenta.setCorreo("administrador");
	usuario.setNombre("administrador");
	usuario.setDireccionEnvio("");
	usuario.setTelefono(0);
	usuario.setTipoCliente("NN");
	usuario.setDescuentoTipoUsuario(0);
	usuario.setCuenta(cuenta);
	return usuario;
    }

    /**
     * Metodo que devuelve la instancia de ManejoUsuarioJSON
     *
     * @return instancia de ManejoUsuarioJSON
     */
    public ManejoUsuarioJSON getManejoUsuarioJSON() {
	return manejoUsuarioJSON;
    }

    /**
     * Constructor de la clase
     * 
     * @throws SQLException
     */
    public GestionUsuario(Tienda tienda, UsuarioDAO usuarioDAO, CuentaDAO cuentaDAO, CarritoDAO carritoDAO)
	    throws SQLException {
	usuarioLog = new Usuario();
	this.cuentaDAO = cuentaDAO;
	this.usuarioDAO = usuarioDAO;
	this.carritoDAO = carritoDAO;
	manejoUsuarioJSON = new ManejoUsuarioJSON(tienda);
	expresion = new Expresion();
	administrador = new Administrador();
	crearTablasUserDefault();
	crearAdmin();
    }

    public void crearTablasUserDefault() throws SQLException {
	usuarioLog.getCuenta().setCorreo("user_default");
	if (usuarioDAO.seleccionarRegistro(usuarioLog) == null) {
	    cuentaDAO.insertarDatos(crearCuentaUsuarioDefault());
	    usuarioDAO.insertarDatos(crearUsuarioDefault());
	}

	usuarioLog = usuarioDAO.seleccionarRegistro(usuarioLog);
	usuarioLog.setCuenta(cuentaDAO.seleccionarRegistro(usuarioLog.getCuenta()));
    }

    public void crearAdmin() throws SQLException {
	usuarioLog.getCuenta().setCorreo("administrador");
	if (usuarioDAO.seleccionarRegistro(usuarioLog) == null) {
	    cuentaDAO.insertarDatos(crearCuentaAdmin());
	    usuarioDAO.insertarDatos(crearUsuarioAdmin());
	}
    }

    /**
     * Registra un usuario en la base de datos
     *
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple
     *                                  con las reglas
     */
    public void registrarUsuario(Usuario usuario) throws RuntimeException, SQLException {
	if (cuentaDAO.seleccionarRegistro(usuario.getCuenta()) != null) {
	    throw new IllegalArgumentException(
		    "El correo '" + usuario.getCuenta().getCorreo() + "' ya está vinculado a otra cuenta");
	}
	expresion.validarDatosObligatoriosUser(usuario);
	expresion.validarDatosUsuario(usuario);
	Usuario usuarioGuardar = convertirUsuario(usuario);
	usuarioGuardar.getCuenta().setLog(false);
	cuentaDAO.insertarDatos(usuarioGuardar.getCuenta());
	usuarioDAO.insertarDatos(usuarioGuardar);
    }

    private Usuario convertirUsuario(Usuario usuario) {
	Usuario usuarioGuardar;
	if (usuario.getTipoCliente().equals("Premium")) {
	    usuarioGuardar = new UsuarioPremium(usuario);
	} else {
	    usuarioGuardar = new UsuarioRegular(usuario);
	}
	return usuarioGuardar;
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
	Cuenta cuenta = new Cuenta();
	cuenta.setCorreo(correo);
	cuenta.setContrasena(contrasena);
	validarCamposVaciosLogin(correo, contrasena);
	Usuario usuario = new Usuario();
	usuario.getCuenta().setCorreo(correo);
	usuario.getCuenta().setContrasena(contrasena);
	validarDatosLogin(cuenta);
    }

    /**
     * Valida si el usuario existe
     *
     * @param cuenta cuenta del usuario
     * @throws IllegalArgumentException si el usuario no existe
     */
    public boolean validarDatosLogin(Cuenta cuenta) throws SQLException {
	Cuenta cuentaEncontrada = cuentaDAO.seleccionarRegistro(cuenta);
	Usuario usuarioLog = new Usuario();
	usuarioLog.setCuenta(cuentaEncontrada);// Creo y asigno la cuenta al usuario para seleccionar el registro
					       // despues para asignar a una variable de Usuario el usuario logueado
	if (cuentaEncontrada == null) {
	    throw new IllegalArgumentException("El usuario no existe");
	}
	if (!cuentaEncontrada.getContrasena().equals(cuenta.getContrasena())) {
	    throw new IllegalArgumentException("La contraseña es incorrecta");
	}
	cuentaEncontrada.setLog(true);
	this.usuarioLog = usuarioDAO.seleccionarRegistro(usuarioLog);
	LibroCarrito libroCarrito = new LibroCarrito();
	libroCarrito.setCorreo_usuario("user_default");
	ArrayList<LibroCarrito> librosCarritoDefault = carritoDAO.seleccionarRegistros(libroCarrito);
	for (LibroCarrito libroCarritoDefault : librosCarritoDefault) {
	    libroCarritoDefault.setCorreo_usuario(usuarioLog.getCuenta().getCorreo());
	    libroCarrito.setIsbn_libro(libroCarritoDefault.getIsbn_libro());
	    carritoDAO.insertarDatos(libroCarritoDefault);
	    carritoDAO.eliminarRegistro(libroCarrito);
	}

	cuentaDAO.actualizarDatos(cuentaEncontrada);
	return true;
    }

    /**
     * Valida si los datos del usuario están vacios
     *
     * @param correo     correo del usuario
     * @param contrasena contraseña del usuario
     * @throws IllegalArgumentException si alguno de los datos del inicio de sesión
     *                                  no cumple con las reglas
     */
    public void validarCamposVaciosLogin(String correo, String contrasena) throws IllegalArgumentException {
	if (!correo.equals(Administrador.CORREO)) {
	    if (correo.isBlank() && contrasena.isBlank()) {
		throw new IllegalArgumentException("Complete los campos de texto.");
	    } else if (correo.isBlank()) {
		throw new IllegalArgumentException("Ingrese un correo.");
	    } else if (contrasena.isBlank()) {
		throw new IllegalArgumentException("Ingrese una contraseña.");
	    }
	}
    }

    /**
     * Valida si el correo del administrador es el del usuario logueado
     *
     * @return retorna true si el correo del administrador es el del usuario
     *         logueado
     */
    public boolean isAdminLogin() {
	return usuarioLog.getCuenta().getCorreo().equals(administrador.getCORREO());
    }

    /**
     * Valida si el usuario logueado es el default
     *
     * @return retorna true si el usuario logueado es el default
     */
    public boolean isDefaultUserLogin() {
	return this.usuarioLog.getCuenta().getCorreo().equals("user_default");
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
    public void modificarUsuario(Usuario usuario) throws IllegalArgumentException, SQLException {
	expresion.validarDatosUsuario(usuario);
	if (usuario.getTipoCliente().equalsIgnoreCase("Premium")) {
	    UsuarioPremium usuarioPremium = new UsuarioPremium(usuario);
	    usuarioDAO.actualizarDatos(usuarioPremium);
	    cuentaDAO.actualizarDatos(usuarioPremium.getCuenta());
	    return;
	}
	usuarioDAO.actualizarDatos(usuario);
	cuentaDAO.actualizarDatos(usuario.getCuenta());
    }

    public void cerrarSesionUsuario() throws RuntimeException, IOException, SQLException {
	usuarioLog.setCuenta(cuentaDAO.seleccionarRegistro(usuarioLog.getCuenta()));
	usuarioLog.getCuenta().setLog(false);
	usuarioDAO.actualizarDatos(this.usuarioLog);
	cuentaDAO.actualizarDatos(this.usuarioLog.getCuenta());
	asignarUsuarioGenerico();
    }

    public void asignarUsuarioGenerico() throws SQLException {
	Usuario usuario = new Usuario();
	usuario.setCuenta(new Cuenta());
	usuario.getCuenta().setCorreo("user_default");
	this.usuarioLog = usuarioDAO.seleccionarRegistro(usuario);
	// TODO Implementar logica para que el carrito del usuario quede vacio ->
	// reemplazar esto ->
	// manejoUsuarioJSON.getListaUsuarios().get(0).getCarrito().setLibros(new
	// ArrayList<>());
    }
}
