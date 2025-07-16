package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.modelo.Categoria;

public interface ICategoriaBusquedaPorTitulo {
    Categoria consultarCategoriaNombre(String titulo) throws RepositorioException;
}
