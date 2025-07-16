package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.modelo.Categoria;

public interface ICategoriaBusquedaPorId {
    Categoria consultarCategoriaID(int idCategoria) throws RepositorioException;
}
