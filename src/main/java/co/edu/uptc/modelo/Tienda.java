package co.edu.uptc.modelo;


import java.io.Serializable;
import java.util.*;

public class Tienda implements Serializable {

    private static final long serialVersionUID = 4563764538288413272L;

    /**
     * Catalogo de productos de la tienda.
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
     * Mapa que almacena los comentarios de cada libro.
     * La clave es el ISBN del libro y el valor es una lista de comentarios.
     */
    private TreeMap<String, Stack<Comentario>> mapComentarios;

    /**
     * Usuario actual que está interactuando con la tienda.
     */
    private Usuario usuarioActual;


    public Tienda() {
        catalogo = new Catalogo();
        usuarios = new ArrayList<>();
        recibosTienda = new TreeMap<>();
        mapComentarios = new TreeMap<>();
        usuarioActual = new Usuario();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public TreeMap<String, ArrayList<Recibo>> getRecibosTienda() {
        return recibosTienda;
    }

    public void setRecibosTienda(TreeMap<String, ArrayList<Recibo>> recibosTienda) {
        this.recibosTienda = recibosTienda;
    }

    public TreeMap<String, Stack<Comentario>> getMapComentarios() {
        return mapComentarios;
    }

    public void setMapComentarios(TreeMap<String, Stack<Comentario>> mapComentarios) {
        this.mapComentarios = mapComentarios;
    }
}
