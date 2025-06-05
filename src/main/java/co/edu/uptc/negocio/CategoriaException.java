package co.edu.uptc.negocio;

public class CategoriaException extends RuntimeException {

    public enum TipoConflicto {
        DUPLICADO,
        VACIO,
        PARECIDA,
        INVALIDA
    }

    private final TipoConflicto tipoConflicto;
    private final String categoriaConflictiva;

    public CategoriaException(String message, TipoConflicto tipoConflicto) {
        super(message);
        this.tipoConflicto = tipoConflicto;
        this.categoriaConflictiva = null;
    }

    public CategoriaException(String message, TipoConflicto tipoConflicto, String categoriaConflictiva) {
        super(message);
        this.tipoConflicto = tipoConflicto;
        this.categoriaConflictiva = categoriaConflictiva;
    }

    public TipoConflicto getTipoConflicto() {
        return tipoConflicto;
    }

    public String getCategoriaConflictiva() {
        return categoriaConflictiva;
    }
}
