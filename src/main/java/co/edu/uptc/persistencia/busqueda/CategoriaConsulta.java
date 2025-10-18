package co.edu.uptc.persistencia.busqueda;

import co.edu.uptc.contrato.ICategoriaConsulta;
import co.edu.uptc.contrato.ICategoriaRepositorio;
import co.edu.uptc.modelo.Categoria;

import java.sql.SQLException;

public class CategoriaConsulta implements ICategoriaConsulta {

    private ICategoriaRepositorio iCategoriaRepositorio;

    public CategoriaConsulta(ICategoriaRepositorio iCategoriaRepositorio) {
        this.iCategoriaRepositorio = iCategoriaRepositorio;
    }

    @Override
    public Categoria consultar(int idCategoria) throws SQLException {
        return iCategoriaRepositorio.consultar(idCategoria);
    }

    @Override
    public Categoria consultar(String nombre) throws SQLException {
        return iCategoriaRepositorio.consultar(nombre);
    }
}
