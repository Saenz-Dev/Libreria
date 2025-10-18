package co.edu.uptc.contrato;

import co.edu.uptc.modelo.Categoria;

import java.sql.SQLException;

public interface ICategoriaConsulta {
    public Categoria consultar(int idCategoria) throws SQLException;
    public Categoria consultar(String nombreCategoria) throws SQLException;
}
