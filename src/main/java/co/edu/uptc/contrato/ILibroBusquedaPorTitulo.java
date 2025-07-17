package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.modelo.Libro;

public interface ILibroBusquedaPorTitulo {
    public Libro buscarLibroPorTitulo(IBusquedaStrategy estrategia) throws RepositorioException;
}
