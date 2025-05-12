package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.modelo.*;
import co.edu.uptc.persistencia.CuentaDAO;
import co.edu.uptc.persistencia.UsuarioDAO;

/**
 * Clase encargada de gestionar los usuarios.
 */
public class GestionUsuario {

    private CuentaDAO cuentaDAO;

    private UsuarioDAO usuarioDAO;

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

    public Usuario userLog() {
        return usuarioLog;
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
     */
    public GestionUsuario(Tienda tienda) {
        usuarioLog = new Usuario();
        cuentaDAO = new CuentaDAO();
        usuarioDAO = new UsuarioDAO();
        manejoUsuarioJSON = new ManejoUsuarioJSON(tienda);
        expresion = new Expresion();
        administrador = new Administrador();
    }

    /**
     * Registra un usuario en la base de datos
     *
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple con las reglas
     */
    public void registrarUsuario(Usuario usuario) throws RuntimeException, SQLException {
        cuentaDAO.crearTabla();
        usuarioDAO.crearTabla();
        if (cuentaDAO.seleccionarRegistro(usuario.getCuenta()) != null) {
            throw new IllegalArgumentException("El correo '" + usuario.getCuenta().getCorreo() + "' ya está vinculado a otra cuenta");
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
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple con las reglas
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
        usuarioLog.setCuenta(cuentaEncontrada);//Creo y asigno la cuenta al usuario para seleccionar el registro despues para asignar a una variable de Usuario el usuario logueado
        if (cuentaEncontrada == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        if (!cuentaEncontrada.getContrasena().equals(cuenta.getContrasena())) {
            throw new IllegalArgumentException("La contraseña es incorrecta");
        }
        cuentaEncontrada.setLog(true);
        //TODO pasar los libros del usuario default al usuario logueado
        //TODO vaciar el carrito del usuario default
        this.usuarioLog = usuarioDAO.seleccionarRegistro(usuarioLog);
        cuentaDAO.actualizarDatos(cuentaEncontrada);
        return true;
    }

    /**
     * Valida si los datos del usuario están vacios
     *
     * @param correo     correo del usuario
     * @param contrasena contraseña del usuario
     * @throws IllegalArgumentException si alguno de los datos del inicio de sesión no cumple con las reglas
     */
    public void validarCamposVaciosLogin(String correo, String contrasena) throws IllegalArgumentException {
        if (correo.isBlank() && contrasena.isBlank()) {
            throw new IllegalArgumentException("Complete los campos de texto.");
        } else if (correo.isBlank()) {
            throw new IllegalArgumentException("Ingrese un correo.");
        } else if (contrasena.isBlank()) {
            throw new IllegalArgumentException("Ingrese una contraseña.");
        }
    }

    /**
     * Valida si el correo del administrador es el del usuario logueado
     *
     * @return retorna true si el correo del administrador es el del usuario logueado
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
     * @throws IOException              si ocurre algún error cuando no se escribe el usuario en el JSON
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple con las reglas
     */
    public void modificarUsuario(Usuario usuario) throws IllegalArgumentException, SQLException {
        expresion.validarDatosUsuario(usuario);
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
        //TODO Implementar logica para que el carrito del usuario quede vacio -> reemplazar esto -> manejoUsuarioJSON.getListaUsuarios().get(0).getCarrito().setLibros(new ArrayList<>());
    }
}
