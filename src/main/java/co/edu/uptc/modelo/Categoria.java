package co.edu.uptc.modelo;

/**
 * Clase que representa una categoría de libros en la librería virtual.
 * Contiene el nombre y el identificador único de la categoría.
 */
public class Categoria {

    /** Nombre de la categoría. */
    private String nombre;
    /** Identificador único de la categoría. */
    private int idCategoria;

    /**
     * Constructor que inicializa la categoría con nombre e id.
     * @param nombre Nombre de la categoría.
     * @param idCategoria Identificador único de la categoría.
     */
    public Categoria(String nombre, int idCategoria) {
        this.nombre = nombre;
        this.idCategoria = idCategoria;
    }

    /**
     * Constructor vacío para crear una categoría sin datos iniciales.
     */
    public Categoria() {}

    /**
     * Obtiene el nombre de la categoría.
     * @return Nombre de la categoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     * @param nombre Nombre de la categoría.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el identificador único de la categoría.
     * @return Identificador de la categoría.
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador único de la categoría.
     * @param idCategoria Identificador de la categoría.
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
