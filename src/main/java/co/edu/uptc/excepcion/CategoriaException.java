package co.edu.uptc.excepcion;

/**
 * Excepción personalizada para el manejo de errores relacionados con categorías en la aplicación.
 * Permite identificar el tipo de conflicto ocurrido al registrar o modificar una categoría.
 */
public class CategoriaException extends RuntimeException {

    /**
     * Tipos de conflicto posibles al trabajar con categorías.
     */
    public enum TipoConflicto {
        DUPLICADO,
        VACIO,
        PARECIDA,
        INVALIDA
    }

    /** Tipo de conflicto detectado. */
    private final TipoConflicto tipoConflicto;
    /** Nombre de la categoría conflictiva (si aplica). */
    private final String categoriaConflictiva;

    /**
     * Constructor para excepciones de categoría sin nombre conflictivo.
     * @param message Mensaje descriptivo del error.
     * @param tipoConflicto Tipo de conflicto detectado.
     */
    public CategoriaException(String message, TipoConflicto tipoConflicto) {
        super(message);
        this.tipoConflicto = tipoConflicto;
        this.categoriaConflictiva = null;
    }

    /**
     * Constructor para excepciones de categoría con nombre conflictivo.
     * @param message Mensaje descriptivo del error.
     * @param tipoConflicto Tipo de conflicto detectado.
     * @param categoriaConflictiva Nombre de la categoría conflictiva.
     */
    public CategoriaException(String message, TipoConflicto tipoConflicto, String categoriaConflictiva) {
        super(message);
        this.tipoConflicto = tipoConflicto;
        this.categoriaConflictiva = categoriaConflictiva;
    }

    /**
     * Obtiene el tipo de conflicto asociado a la excepción.
     * @return Tipo de conflicto.
     */
    public TipoConflicto getTipoConflicto() {
        return tipoConflicto;
    }

    /**
     * Obtiene el nombre de la categoría conflictiva, si existe.
     * @return Nombre de la categoría conflictiva o null si no aplica.
     */
    public String getCategoriaConflictiva() {
        return categoriaConflictiva;
    }
}
