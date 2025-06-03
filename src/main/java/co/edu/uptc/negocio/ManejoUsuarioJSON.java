package co.edu.uptc.negocio;

import java.io.File;
import java.io.IOException;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.*;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase para gestionar la persistencia de usuarios en formato JSON.
 * Permite la lectura, escritura y manipulación de los datos de los usuarios en un archivo JSON.
 */
public class ManejoUsuarioJSON {

    private Tienda tienda;

    /**
     * Archivo donde se almacenan los datos de los usuarios en formato JSON.
     */
    private File file;

    /**
     * Objeto de Jackson para serializar y deserializar los datos en JSON.
     */
    private ObjectMapper objectMapper;

    /**
     * Ruta del archivo JSON donde se guardan los usuarios.
     */
    private String ruta;

    /**
     * Constructor de la clase ManejoUsuarioJSON.
     * Inicializa el ObjectMapper, la lista de usuarios y define la ruta del archivo JSON.
     */
    public ManejoUsuarioJSON(Tienda tienda) {
        objectMapper = new ObjectMapper();
        this.tienda = tienda;
        ruta = "src/main/java/co/edu/uptc/persistencia/usuario.json";
        file = new File(ruta);
    }

    /**
     * Carga los usuarios desde el archivo JSON.
     *
     * @return Lista de usuarios cargados desde el archivo JSON.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public void guardarCarritoUsuarioDefault(Carrito carrito) throws IOException, IllegalArgumentException {
        try {
            Usuario usuario = objectMapper.readValue(file, Usuario.class);
            usuario.setCarrito(carrito);
            objectMapper.writeValue(file, usuario);
        } catch (IOException e) {
            //Un mensaje que sea entendible por el usuario
            throw new IOException("Error al guardar el carrito del usuario por defecto");
        }
    }

    /**
     * Valida si los datos para iniciar sesión son correctos.
     *
     * @return {@code true} si los datos son correctos, {@code false} en caso contrario.
     * @throws IllegalArgumentException Si el usuario no existe o la contraseña es incorrecta.
     */
    public void agregarLibrosCarrito(Carrito carrito) throws IllegalArgumentException {
        try {
            Usuario userDefault = objectMapper.readValue(file, Usuario.class);
            userDefault.setCarrito(carrito);
            for (Libro libro : carrito.getLibros()) {
                userDefault.getCarrito().trasladarLibros(libro);
            }
            objectMapper.writeValue(file, userDefault);
        } catch (IOException e) {
            RegistroLog.registrarError("Error al agregar libros al carrito en el JSON: " + e.getMessage());
            throw new IllegalArgumentException("Error al agregar libros al carrito, intentalo más tarde.");
        }
    }

    /**
     * Valida si los datos para iniciar sesión son correctos.
     *
     * @return {@code true} si los datos son correctos, {@code false} en caso contrario.
     * @throws IllegalArgumentException Si el usuario no existe o la contraseña es incorrecta.
     */
    public void eliminarLibrosCarrito(Carrito carrito) throws IllegalArgumentException {
        try {
            Usuario userDefault = objectMapper.readValue(file, Usuario.class);
            userDefault.setCarrito(new Carrito());
            objectMapper.writeValue(file, userDefault);
        } catch (IOException e) {
            RegistroLog.registrarError("Error al eliminar libros del carrito en el JSON: " + e.getMessage());
            throw new IllegalArgumentException("Error al eliminar libros del carrito, intentalo más tarde.");
        }
    }
}
