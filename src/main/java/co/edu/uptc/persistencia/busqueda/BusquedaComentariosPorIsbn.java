package co.edu.uptc.persistencia.busqueda;

import co.edu.uptc.contrato.IBusquedaStrategy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BusquedaComentariosPorIsbn implements IBusquedaStrategy {

    private String isbn;

    public BusquedaComentariosPorIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String getSQL() {
        return "SELECT * FROM comentarios WHERE isbn_libro = ?";
    }

    @Override
    public void ajustarParametro(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, Long.parseLong(isbn));
    }
}
