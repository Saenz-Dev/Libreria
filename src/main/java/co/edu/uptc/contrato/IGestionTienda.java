package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.modelo.Usuario;

public interface IGestionTienda<T> {
    void guardar(T objeto) throws RepositorioException;
    void actualizar(T objeto) throws RepositorioException;
    Usuario leer(T objeto) throws RepositorioException;
    void eliminar(T objeto) throws RepositorioException;
}
