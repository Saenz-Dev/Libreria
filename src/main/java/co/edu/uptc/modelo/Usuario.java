package co.edu.uptc.modelo;

import java.io.Serializable;

/**
 * Clase que representa a un usuario en el sistema.
 * Contiene información personal y de cuenta para la gestión de compras y acceso.
 */
public class Usuario implements Serializable{

    /**
     * 
     */
    public static final long serialVersionUID = 7354483159484201335L;

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Dirección de envío del usuario.
     */
    private String direccionEnvio;

    /**
     * Número de teléfono del usuario.
     */
    private long telefono;

    /**
     * Tipo de cliente (por ejemplo, "regular", "VIP", etc.).
     */
    private String tipoCliente;

    /**
     * Cuenta asociada al usuario, que contiene información de inicio de sesión.
     */
    private Cuenta cuenta;

    private double descuentoTipoUsuario;

    /**
     * Carrito de compras del usuario, donde se almacenan los libros seleccionados.
     */
    private Carrito carrito;

    public Usuario() {
        cuenta = new Cuenta();
        carrito = new Carrito();
    }

    public Usuario(Usuario usuario) {
        cuenta = new Cuenta();
        carrito = new Carrito();
        nombre = usuario.getNombre();
        direccionEnvio = usuario.getDireccionEnvio();
        telefono = usuario.getTelefono();
        tipoCliente = usuario.getTipoCliente();
        cuenta.setCorreo(usuario.getCuenta().getCorreo());
        cuenta.setContrasena(usuario.getCuenta().getContrasena());
        descuentoTipoUsuario = 0;
        cuenta.setLog(false);
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre El nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la dirección de envío del usuario.
     *
     * @return La dirección de envío.
     */
    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    /**
     * Establece la dirección de envío del usuario.
     *
     * @param direccionEnvio La dirección de envío a asignar.
     */
    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    /**
     * Obtiene el número de teléfono del usuario.
     *
     * @return El número de teléfono.
     */
    public long getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del usuario.
     *
     * @param telefono El número de teléfono a asignar.
     */
    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la cuenta asociada al usuario.
     *
     * @return La cuenta del usuario.
     */
    public Cuenta getCuenta() {
        return cuenta;
    }

    /**
     * Establece la cuenta del usuario.
     *
     * @param cuenta La cuenta a asignar.
     */
    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    /**
     * Obtiene el tipo de cliente del usuario.
     *
     * @return El tipo de cliente.
     */
    public String getTipoCliente() {
        return tipoCliente;
    }

    /**
     * Establece el tipo de cliente del usuario.
     *
     * @param tipoCliente El tipo de cliente a asignar.
     */
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    /**
     * Obtiene el carrito de compras del usuario.
     * Si el carrito es nulo, se inicializa antes de retornarlo.
     *
     * @return El carrito de compras del usuario.
     */
    public Carrito getCarrito() {
        if (this.carrito == null) {
            carrito = new Carrito();
        }
        return carrito;
    }

    /**
     * Establece el carrito de compras del usuario.
     *
     * @param carrito El carrito a asignar.
     */
    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public double getDescuentoTipoUsuario() {
        return descuentoTipoUsuario;
    }

    public void setDescuentoTipoUsuario(double descuentoTipoUsuario) {
        this.descuentoTipoUsuario = descuentoTipoUsuario;
    }
}
