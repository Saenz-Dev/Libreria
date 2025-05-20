package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class ConexionBD<T>{

    private final String URL = "jdbc:mysql://localhost:3306/db_libreria";
    private final String USUARIO = "root";
    private final String CONTRASENA = "";
    
    public ConexionBD() {
	String sql = "CREATE DATABASE IF NOT EXISTS db_libreria";
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", USUARIO, CONTRASENA); Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.print("Error al crear la base de datos");
	}
    }
    
    
    protected Connection crearConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    public abstract void crearTabla() throws SQLException;

    public abstract void insertarDatos(T objeto) throws  SQLException, RuntimeException;

    public abstract void actualizarDatos(T objeto) throws  SQLException, RuntimeException;

    public abstract T seleccionarRegistro(T objeto) throws SQLException, RuntimeException;

    public abstract ArrayList<T> seleccionarRegistros() throws SQLException, RuntimeException;
}
