package co.edu.uptc.persistencia.mapper;


import co.edu.uptc.contrato.ICategoriaConsulta;
import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.TipoLibroEnum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibroMapper implements IMapper<Libro> {

    private ICategoriaConsulta iCategoriaConsulta;

    public LibroMapper(ICategoriaConsulta iCategoriaConsulta) {
        this.iCategoriaConsulta = iCategoriaConsulta;
    }

    @Override
    public void mapearObjeto(Libro libro, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, libro.getIsbn());
        preparedStatement.setString(2, libro.getTitulo());
        preparedStatement.setString(3, libro.getAutor());
        preparedStatement.setInt(4, libro.getAnioPublicacion());
        preparedStatement.setString(5, libro.getEditorial());
        preparedStatement.setInt(6, libro.getNumeroPaginas());
        preparedStatement.setDouble(7, libro.getPrecioVenta());
        preparedStatement.setInt(8, libro.getStockDisponible());
        preparedStatement.setInt(9, libro.getStockReservado());
        preparedStatement.setString(10, String.valueOf(libro.getTipoLibro()));
        preparedStatement.setBoolean(11, libro.getIsComprado());
        preparedStatement.setInt(12, iCategoriaConsulta.consultar(libro.getCategoria().getNombre()).getIdCategoria());
    }

    @Override
    public Libro mapearResultSet(ResultSet resultSet) throws SQLException {
        Libro libroResult = new Libro();
        libroResult.setIsbn(resultSet.getString("isbn"));
        libroResult.setTitulo(resultSet.getString("titulo"));
        libroResult.setAutor(resultSet.getString("autor"));
        libroResult.setAnioPublicacion(resultSet.getInt("año_publicación"));
        libroResult.setCategoria(iCategoriaConsulta.consultar(resultSet.getInt("id_categoria")));
        libroResult.setEditorial(resultSet.getString("editorial"));
        libroResult.setNumeroPaginas(resultSet.getInt("páginas"));
        libroResult.setPrecioVenta(resultSet.getDouble("precio"));
        libroResult.setStockDisponible(resultSet.getInt("stockDisponible"));
        libroResult.setStockReservado(resultSet.getInt("stockReservado"));
        libroResult.setTipoLibro(TipoLibroEnum.valueOf(resultSet.getString("tipo")));
        libroResult.setIsComprado(resultSet.getBoolean("comprado"));
        return libroResult;
    }
}
