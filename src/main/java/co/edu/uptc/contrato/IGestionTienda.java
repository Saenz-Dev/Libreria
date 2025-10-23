package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.modelo.Usuario;

import java.sql.SQLException;

public interface IGestionTienda<T> {
    void guardar(T objeto) throws RepositorioException;
    void actualizar(T objeto) throws RepositorioException;
    Usuario leer(T objeto) throws RepositorioException;
    void eliminar(T objeto) throws SQLException;
}
