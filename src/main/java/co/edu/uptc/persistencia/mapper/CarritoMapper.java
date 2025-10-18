package co.edu.uptc.persistencia.mapper;

import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.modelo.Carrito;
import co.edu.uptc.modelo.Libro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarritoMapper implements IMapper<Carrito> {
    @Override
    public void mapearObjeto(Carrito carrito, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, carrito.getUsuario().getCuenta().getCorreo());
        preparedStatement.setString(2, carrito.getLibros().getLast().getIsbn());
        preparedStatement.setInt(3, carrito.getLibros().getLast().getStockReservado());
    }

    @Override
    public Carrito mapearResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Libro> librosCarrito = new ArrayList<>();
        while (resultSet.next()) {
            Libro libroCarritoEncontrado = new Libro();
            libroCarritoEncontrado.setIsbn(resultSet.getString("isbn_libro"));
            libroCarritoEncontrado.setStockReservado(resultSet.getInt("cantidad"));
            librosCarrito.add(libroCarritoEncontrado);
        }
        Carrito carrito = new Carrito();
        carrito.setLibros(librosCarrito);
        return carrito;
    }
}
