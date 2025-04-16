package co.edu.uptc.negocio;

import java.io.IOException;
import java.util.ArrayList;

import co.edu.uptc.modelo.Administrador;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.Usuario;

/**
 * Clase encargada de gestionar los usuarios.
 */
public class GestionUsuario {

    /**
     * Instancia de Manejo de usuarios con JSON
     */
    private ManejoUsuarioJSON manejoUsuarioJSON;

    /**
     * Instancia de Expresión regular
     */
    private Expresion expresion;

    /**
     * Instancia de Usuario
     */
    private Usuario usuarioLogin;

    /**
     * Instancia de Administrador
     */
    private Administrador administrador;
    
    /**
     * Método que devuelve el usuario logueado
     *
     * @return usuario logueado
     */
    public Usuario userLogin() {
        return manejoUsuarioJSON.getUsuarioLogin();
    }

    /**
     * Método que devuelve la instancia de ManejoUsuarioJSON
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
        manejoUsuarioJSON = new ManejoUsuarioJSON(tienda);
        expresion = new Expresion();
        usuarioLogin = new Usuario();
        administrador = new Administrador();
    }

    /**
     * Registra un usuario en la base de datos
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple con las reglas
     */
    public void registrarUsuario(Usuario usuario) throws RuntimeException {
        if (manejoUsuarioJSON.buscarUsuario(manejoUsuarioJSON.getListaUsuarios(), usuario) != null) {
            throw new IllegalArgumentException("El correo '" + usuario.getCuenta().getCorreo() + "' ya está vinculado a otra cuenta");
        }
        expresion.validarDatosObligatoriosUser(usuario);
        expresion.validarDatosUsuario(usuario);
        usuario.getCuenta().setLog(false);
        manejoUsuarioJSON.crearUsuario(usuario);
    }

    /**
     * Inicia sesión del usuario
     * @param correo correo del usuario
     * @param contrasena constrasena del usuario
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple con las reglas
     */
    public void iniciarSesion(String correo, String contrasena) throws IllegalArgumentException {
        validarCamposVaciosLogin(correo, contrasena);
        Usuario usuario = new Usuario();
        usuario.getCuenta().setCorreo(correo);
        usuario.getCuenta().setContrasena(contrasena);
        manejoUsuarioJSON.validarDatosLogin(usuario);
    }

    /**
     * Valida si los datos del usuario están vacios
     * @param correo correo del usuario
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
     * @return retorna true si el correo del administrador es el del usuario logueado
     */
    public boolean isAdminLogin() {
        return manejoUsuarioJSON.getUsuarioLogin().getCuenta().getCorreo().equals(administrador.getCORREO());
    }

    public boolean isGenericoLogin() {
        return manejoUsuarioJSON.getUsuarioLogin().getCuenta().getCorreo().equals("user_default");
    }

    /**
     * Modifica los datos del usuario logueado
     * @param usuario usuario a modificar
     * @throws IOException si ocurre algún error cuando no se escribe el usuario en el JSON
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple con las reglas
     */
    public void modificarUsuario(Usuario usuario) throws IOException, IllegalArgumentException {
        expresion.validarDatosUsuario(usuario);
        manejoUsuarioJSON.modificarUsuario(usuario);
    }

    public void modificarUsuarioCarrito(Usuario usuario) throws IOException, IllegalArgumentException {
        expresion.validarDatosUsuario(usuario);
        manejoUsuarioJSON.modificarUsuarioCarrito(usuario);
    }

    /**
     * Cierra sesión del usuario logueado
     * @throws IOException si ocurre algún error cuando no se escribe el usuario en el JSON
     */
    public void cerrarSesion() throws IOException {
        manejoUsuarioJSON.getUsuarioLogin().getCuenta().setLog(false);
        manejoUsuarioJSON.modificarUsuario(manejoUsuarioJSON.getUsuarioLogin());
        manejoUsuarioJSON.setUsuarioLogin(null);
    }

    public void cerrarSesionUsuario() throws RuntimeException, IOException{
        manejoUsuarioJSON.getUsuarioLogin().getCuenta().setLog(false);
        manejoUsuarioJSON.escribirUsuario();
        asignarUsuarioGenerico();
        manejoUsuarioJSON.escribirUsuarioLogin();
    }

    public void asignarUsuarioGenerico() throws IOException{
        manejoUsuarioJSON.leerListaUsuarios();
        manejoUsuarioJSON.setUsuarioLogin(manejoUsuarioJSON.getListaUsuarios().get(0));
        manejoUsuarioJSON.getListaUsuarios().get(0).getCarrito().setLibros(new ArrayList<>());
        manejoUsuarioJSON.escribirUsuarioLogin();
    }
}
