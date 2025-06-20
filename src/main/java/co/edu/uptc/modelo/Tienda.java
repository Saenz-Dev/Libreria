package co.edu.uptc.modelo;


import java.io.Serializable;
import java.util.*;

/**
 * Representa la tienda virtual de libros, gestionando el catálogo, usuarios, recibos y comentarios.
 * Permite la administración de productos, usuarios registrados, historial de compras y comentarios sobre los libros.
 * Implementa la interfaz Serializable para permitir la persistencia del estado de la tienda.
 */
public class Tienda implements Serializable {

    /**
     * Identificador de versión para la serialización de la clase.
     */
    private static final long serialVersionUID = 4563764538288413272L;

    /**
     * Catálogo de productos de la tienda.
     */
    private Catalogo catalogo;

    /**
     * Lista de usuarios registrados en la tienda.
     */
    private ArrayList<Usuario> usuarios;

    /**
     * Mapa que almacena los recibos de compras por usuario.
     * La clave es el correo del usuario y el valor es una lista de recibos.
     */
    private TreeMap<String, ArrayList<Recibo>> recibosTienda;

    /**
     * Usuario actual que está interactuando con la tienda.
     */
    private Usuario usuarioActual;

    /**
     * Constructor por defecto. Inicializa los atributos principales de la tienda.
     */
    public Tienda() {
        catalogo = new Catalogo();
        usuarios = new ArrayList<>();
        recibosTienda = new TreeMap<>();
        usuarioActual = new Usuario();
    }

    /**
     * Obtiene el usuario actual que está interactuando con la tienda.
     * @return usuario actual
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Establece el usuario actual que está interactuando con la tienda.
     * @param usuarioActual usuario actual
     */
    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    /**
     * Obtiene el catálogo de productos de la tienda.
     * @return catálogo de productos
     */
    public Catalogo getCatalogo() {
        return catalogo;
    }

    /**
     * Establece el catálogo de productos de la tienda.
     * @param catalogo catálogo de productos
     */
    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    /**
     * Obtiene la lista de usuarios registrados en la tienda.
     * @return lista de usuarios
     */
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Establece la lista de usuarios registrados en la tienda.
     * @param usuarios lista de usuarios
     */
    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * Obtiene el mapa que almacena los recibos de compras por usuario.
     * @return mapa de recibos por usuario
     */
    public TreeMap<String, ArrayList<Recibo>> getRecibosTienda() {
        return recibosTienda;
    }

    /**
     * Establece el mapa que almacena los recibos de compras por usuario.
     * @param recibosTienda mapa de recibos por usuario
     */
    public void setRecibosTienda(TreeMap<String, ArrayList<Recibo>> recibosTienda) {
        this.recibosTienda = recibosTienda;
    }
}
