package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class ConexionBD<T>{

    private final String URL = "jdbc:mysql://localhost:3306/db_libreria";
    private final String USUARIO = "root";
    private final String CONTRASENA = "Niosaenz123";
    
    
    protected Connection crearConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    public abstract void insertarDatos(T objeto) throws  SQLException, RuntimeException;

    public abstract void actualizarDatos(T objeto) throws  SQLException, RuntimeException;

    public abstract T seleccionarRegistro(T objeto) throws SQLException, RuntimeException;

    public abstract ArrayList<T> seleccionarRegistros() throws SQLException, RuntimeException;
}
