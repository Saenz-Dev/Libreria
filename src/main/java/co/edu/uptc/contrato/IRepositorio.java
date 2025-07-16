package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.modelo.Recibo;

import java.util.List;

public interface IRepositorio<T> {
    void guardar(T objeto) throws RepositorioException;

    T consultar(T objeto) throws RepositorioException;

    List<T> consultar() throws RepositorioException;

    void actualizar(T objeto) throws RepositorioException;

    void eliminar(T objeto) throws RepositorioException;

    List<Recibo> consultar(Recibo recib) throws RepositorioException;
}
