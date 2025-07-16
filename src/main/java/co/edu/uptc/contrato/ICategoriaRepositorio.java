package co.edu.uptc.contrato;

import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.modelo.Categoria;

public interface ICategoriaRepositorio extends IRepositorio<Categoria>{
    Categoria consultarCategoriaID(int idCategoria) throws RepositorioException;
    Categoria consultarCategoriaNombre(String titulo) throws RepositorioException;
}
