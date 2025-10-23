package co.edu.uptc.excepcion;

import java.sql.SQLException;

public class RepositorioException extends SQLException {
    public RepositorioException(String message) {
        super(message);
    }

    public RepositorioException(String mensaje, Throwable exception) {
        super(mensaje, exception);
    }
}
