package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Clase abstracta base para los DAOs que gestiona la conexión y operaciones genéricas con la base de datos MySQL.
 * Proporciona métodos para crear la conexión y define las operaciones CRUD básicas que deben implementar las subclases.
 *
 * @param <T> Tipo de entidad que maneja el DAO
 */
public abstract class ConexionBD<T>{

    /**
     * URL de conexión a la base de datos MySQL.
     */
    private final String URL = "jdbc:mysql://localhost:3306/db_libreria";
    /**
     * Usuario de la base de datos.
     */
    private final String USUARIO = "root";
    /**
     * Contraseña de la base de datos.
     */
    private final String CONTRASENA = "Niosaenz123";
    
    /**
     * Crea y retorna una nueva conexión a la base de datos.
     *
     * @return conexión a la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
    protected Connection crearConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    /**
     * Inserta un registro en la base de datos.
     *
     * @param objeto entidad a insertar
     * @throws SQLException si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    public abstract void insertarDatos(T objeto) throws  SQLException, RuntimeException;

    /**
     * Actualiza un registro en la base de datos.
     *
     * @param objeto entidad a actualizar
     * @throws SQLException si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    public abstract void actualizarDatos(T objeto) throws  SQLException, RuntimeException;

    /**
     * Selecciona un registro específico de la base de datos.
     *
     * @param objeto entidad con los datos de búsqueda
     * @return entidad encontrada o null si no existe
     * @throws SQLException si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    public abstract T seleccionarRegistro(T objeto) throws SQLException, RuntimeException;

    /**
     * Selecciona todos los registros de la base de datos.
     *
     * @return lista de entidades encontradas
     * @throws SQLException si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    public abstract ArrayList<T> seleccionarRegistros() throws SQLException, RuntimeException;

    //Este es un comentario
}
