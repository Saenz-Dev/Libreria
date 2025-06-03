package co.edu.uptc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Administrador;
import co.edu.uptc.modelo.Cuenta;
import co.edu.uptc.modelo.LibroCarrito;
import co.edu.uptc.modelo.Tienda;
import co.edu.uptc.modelo.TipoUsuarioEnum;
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

    private Expresion expresion;

    private Administrador administrador;

    private Tienda tienda;


    public Usuario usuarioLogueado() throws SQLException, RuntimeException {
        return tienda.getUsuarioActual();
        /*usuarioLog = usuarioDAO.seleccionarRegistro(usuarioLog);
        usuarioLog.setCuenta(cuentaDAO.seleccionarRegistro(usuarioLog.getCuenta()));
        return usuarioLog;*/
    }

    public GestionUsuario(Tienda tienda, UsuarioDAO usuarioDAO, CuentaDAO cuentaDAO, CarritoDAO carritoDAO) throws SQLException {
        this.tienda = tienda;
        this.cuentaDAO = cuentaDAO;
        this.usuarioDAO = usuarioDAO;
        this.carritoDAO = carritoDAO;
        tienda.getUsuarioActual().getCuenta().setCorreo("user_default");// TODO cambiar esto y mejor dejar usuario default en un JSON
        tienda.setUsuarioActual(usuarioDAO.seleccionarRegistro(tienda.getUsuarioActual()));
        expresion = new Expresion();
        administrador = new Administrador();
    }


    /**
     * Registra un usuario en la base de datos
     *
     * @throws IllegalArgumentException si alguno de los datos del usuario no cumple con las reglas
     */
    public void registrarUsuario(Usuario usuario) throws RuntimeException, SQLException {
        if (cuentaDAO.seleccionarRegistro(usuario.getCuenta()) != null) {
            RegistroLog.registrarAdvertencia("Intento de registrar un correo ya existente: " + usuario.getCuenta().getCorreo());
            throw new IllegalArgumentException("El correo '" + usuario.getCuenta().getCorreo() + "' ya está vinculado a otra cuenta");
        }
        expresion.validarDatosObligatoriosUser(usuario);
        expresion.validarDatosUsuario(usuario);
        Usuario usuarioGuardar = convertirUsuario(usuario);
        usuarioGuardar.getCuenta().setLog(false);
        cuentaDAO.insertarDatos(usuarioGuardar.getCuenta());
        usuarioDAO.insertarDatos(usuarioGuardar);
    }

    public Usuario convertirUsuario(Usuario usuario) {
        Usuario usuarioGuardar;
        if (usuario.getTipoCliente().equals(TipoUsuarioEnum.Premium)) {
            usuarioGuardar = new UsuarioPremium(usuario);
            usuarioGuardar.setTipoCliente(TipoUsuarioEnum.Premium);
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
        expresion.validarCamposVaciosLogin(correo, contrasena);
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
        Cuenta cuentaEncontrada = cuentaDAO.seleccionarRegistro(cuenta); //Consulta la cuenta en la base de datos
        Usuario usuarioLog = new Usuario(); // Creo un usuario para signar la cuenta encontrada
        usuarioLog.setCuenta(cuentaEncontrada);// Asigno la cuenta al usuario para seleccionar el registro despues para asignar a una variable de Usuario el usuario logueado
        validarCuentaBuscada(cuenta, cuentaEncontrada); // Valido que la cuenta no sea nula

        cuentaEncontrada.setLog(true); // Actualizo el estado de la cuenta logueada
        tienda.setUsuarioActual(usuarioDAO.seleccionarRegistro(usuarioLog)); // Selecciono el usuario logueado en la BD con la cuenta encontrada
        if (usuarioLog.getCuenta().getCorreo().equals(Administrador.CORREO)) {
            return false;
        }
        LibroCarrito libroCarrito = new LibroCarrito(); // Creo un libroCarrito para seleccionar los registros del carrito del usuario default
        libroCarrito.setCorreo_usuario("user_default"); //
        if (libroCarrito == null || libroCarrito.getCorreo_usuario() == null || libroCarrito.getCorreo_usuario().isBlank()) {
            RegistroLog.registrarAdvertencia("❗ Se intentó seleccionar registros con un correo de usuario nulo o vacío.");
            throw new RuntimeException("⚠️ No se proporcionó un usuario válido para consultar su carrito.");
        }
        ArrayList<LibroCarrito> librosCarritoDefault = carritoDAO.seleccionarRegistros(libroCarrito); // Selecciono los libros del carrito del usuario default
        for (LibroCarrito libroCarritoDefault : librosCarritoDefault) {
            libroCarritoDefault.setCorreo_usuario(usuarioLog.getCuenta().getCorreo()); // Seteo el correo del usuario logueado
            libroCarrito.setIsbn_libro(libroCarritoDefault.getIsbn_libro()); // Seteo el isbn del libro del carrito
            carritoDAO.insertarDatos(libroCarritoDefault); // Inserto el libro del carrito de usuario default en el carrito del usuario logueado
            carritoDAO.eliminarRegistro(libroCarrito); // Elimino el libro del carrito del usuario default
        }
        cuentaDAO.actualizarDatos(cuentaEncontrada); // Actualizo la cuenta logueada en la base de datos
        tienda.getUsuarioActual().setCuenta(cuentaEncontrada); // Asigno la cuenta logueada al usuario actual de la tienda
        return true;
    }

    /**
     * Valida si la cuenta buscada es nula o si la contraseña es incorrecta
     *
     * @param cuenta           cuenta del usuario
     * @param cuentaEncontrada cuenta encontrada en la base de datos
     * @throws IllegalArgumentException si la cuenta no está registrada o si la contraseña es incorrecta
     */
    private void validarCuentaBuscada(Cuenta cuenta, Cuenta cuentaEncontrada) {
        if (cuentaEncontrada == null) {
            RegistroLog.registrarAdvertencia("❌ Intento de inicio de sesión con correo no registrado: " + cuenta.getCorreo());
            throw new IllegalArgumentException("El usuario ingresado no está registrado.");
        }

        if (!cuentaEncontrada.getContrasena().equals(cuenta.getContrasena())) {
            RegistroLog.registrarAdvertencia("❌ Contraseña incorrecta para el correo: " + cuenta.getCorreo());
            throw new IllegalArgumentException("El correo o la contraseña es incorrecta.");
        }

        if (cuentaEncontrada.getCorreo().equals(Administrador.CORREO) && !cuentaEncontrada.getContrasena().equals(cuenta.getContrasena())) {
            RegistroLog.registrarAdvertencia("❌ Contraseña incorrecta para el administrador: " + cuenta.getCorreo());
            throw new IllegalArgumentException("El correo o la contraseña es incorrecta.");
        }
    }

    /**
     * Valida si el correo del administrador es el del usuario logueado
     *
     * @return retorna true si el correo del administrador es el del usuario
     * logueado
     */
    public boolean isAdminLogin() {
        return tienda.getUsuarioActual().getCuenta().getCorreo().equals(administrador.getCORREO());
    }

    /**
     * Valida si el usuario logueado es el default
     *
     * @return retorna true si el usuario logueado es el default
     */
    public boolean isDefaultUserLogin() {
        return tienda.getUsuarioActual().getCuenta().getCorreo().equals("user_default");
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
        if (usuario.getTipoCliente().equals(TipoUsuarioEnum.Premium)) {
            UsuarioPremium usuarioPremium = new UsuarioPremium(usuario);
            usuarioDAO.actualizarDatos(usuarioPremium);
            cuentaDAO.actualizarDatos(usuarioPremium.getCuenta());
            return;
        }
        usuarioDAO.actualizarDatos(usuario);
        cuentaDAO.actualizarDatos(usuario.getCuenta());
    }

    /**
     * Cierra la sesión del usuario logueado y asigna el usuario genérico.
     *
     * @param cerrarAplicacion si se debe cerrar la aplicación
     * @throws RuntimeException si ocurre un error al cerrar la sesión
     * @throws SQLException     si ocurre un error al acceder a la base de datos
     */
    public void cerrarSesionUsuario(boolean cerrarAplicacion) throws RuntimeException, SQLException {
        tienda.setUsuarioActual(usuarioDAO.seleccionarRegistro(tienda.getUsuarioActual()));
        tienda.getUsuarioActual().setCuenta(cuentaDAO.seleccionarRegistro(tienda.getUsuarioActual().getCuenta()));
        tienda.getUsuarioActual().getCuenta().setLog(false);
        RegistroLog.registrarInfo(tienda.getUsuarioActual().getCuenta().getCorreo() + " cerró la sesión.");
        usuarioDAO.actualizarDatos(tienda.getUsuarioActual());
        cuentaDAO.actualizarDatos(tienda.getUsuarioActual().getCuenta());
        asignarUsuarioGenerico();
        if (cerrarAplicacion) {
            RegistroLog.fileHandler.close();
        }
    }

    public void asignarUsuarioGenerico() throws SQLException {
        Usuario usuario = new Usuario();
        usuario.getCuenta().setCorreo("user_default");
        tienda.setUsuarioActual(usuarioDAO.seleccionarRegistro(usuario));
    }
}
