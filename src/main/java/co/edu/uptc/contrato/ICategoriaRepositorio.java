package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.modelo.Categoria;

public interface ICategoriaRepositorio extends IRepositorio<Categoria> {
    Categoria consultar(int idCategoria) throws RepositorioException;

    Categoria consultar(String titulo) throws RepositorioException;
}
