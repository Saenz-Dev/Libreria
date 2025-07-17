package co.edu.uptc.persistencia.busqueda;

import co.edu.uptc.contrato.IBusquedaStrategy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BusquedaPorTitulo implements IBusquedaStrategy {
    private String titulo;

    public BusquedaPorTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String getSQL() {
        return "SELECT * FROM libros WHERE titulo = ?";
    }

    @Override
    public void ajustarParametro(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, titulo);
    }
}
