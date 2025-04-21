package co.edu.uptc.negocio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Tienda;

/**
 * Clase para gestionar la persistencia de libros en formato JSON. Permite la
 * lectura, escritura y manipulación de libros almacenados en un archivo JSON.
 */
public class ManejoLibroJSON {

    /**
     * Archivo donde se almacenarán o cargarán los datos de los libros.
     */
    private File file;

    /**
     * Ruta del archivo donde se guardará la información de los libros en formato
     * JSON.
     */
    private String ruta;

    private Tienda tienda;

    /**
     * Objeto utilizado para manejar la serialización y deserialización de objetos
     * en formato JSON.
     */
    private ObjectMapper objectMapper;

    /**
     * Constructor de la clase ManejoLibroJSON. Inicializa el mapa de libros, define
     * la ruta del archivo JSON y crea los objetos necesarios para la manipulación
     * de archivos y la conversión de objetos a JSON.
     */
    public ManejoLibroJSON(Tienda tienda) {
        this.tienda = tienda;

        // Ruta del archivo donde se almacenarán los libros en formato JSON.
        ruta = "src/main/java/co/edu/uptc/persistencia/libro.json";

        // Se crea un objeto File con la ruta especificada.
        file = new File(ruta);

        // Se inicializa el ObjectMapper para manejar la serialización y deserialización
        // de JSON.
        objectMapper = new ObjectMapper();
    }

    /**
     * Escribe el mapa de libros en un archivo JSON.
     *
     * @param categoriasLibros Mapa que contiene las categorías como claves y listas
     *                         de libros como valores.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public void escribirLibros(Map<String, ArrayList<Libro>> categoriasLibros) throws IOException {
        Map<String, ArrayList<Libro>> librosMap = categoriasLibros;
        objectMapper.writeValue(file, librosMap);
    }

    /**
     * Obtiene el mapa de libros almacenado en el archivo JSON.
     *
     * @return Mapa que contiene las categorías como claves y listas de libros como
     * valores.
     */
    public Map<String, ArrayList<Libro>> getMapLibros() {
        leerLibro();
        return tienda.getMapLibros();
    }

    /**
     * Establece el mapa de libros almacenado en el archivo JSON.
     *
     * @param mapLibros Mapa que contiene las categorías como claves y listas de
     *                  libros como valores.
     */
    public void setMapLibros(Map<String, ArrayList<Libro>> mapLibros) {
        tienda.setMapLibros(mapLibros);
    }

    /**
     * Obtiene el objeto ObjectMapper utilizado para serializar y deserializar
     * objetos en formato JSON.
     *
     * @return Objeto ObjectMapper utilizado para serializar y deserializar objetos
     * en formato JSON.
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Obtiene el objeto File que representa el archivo JSON.
     *
     * @return Objeto File que representa el archivo JSON.
     */
    public File getFile() {
        return file;
    }

    /**
     * Obtiene la ruta del archivo JSON.
     *
     * @return ruta del archivo JSON.
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * Establece el objeto File que representa el archivo JSON.
     *
     * @param file Objeto File que representa el archivo JSON.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Establece la ruta del archivo JSON.
     *
     * @param ruta Ruta del archivo JSON.
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    /**
     * Establece el objeto ObjectMapper utilizado para serializar y deserializar
     * objetos en formato JSON.
     *
     * @param objectMapper Objeto ObjectMapper utilizado para serializar y
     *                     deserializar objetos en formato JSON.
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Agrega un nuevo libro al mapa y lo guarda en el archivo JSON.
     *
     * @param libroIngresado El libro que se desea agregar.
     * @throws IllegalArgumentException Si el libro ya está registrado con el mismo
     *                                  ISBN.
     * @throws IOException              Si ocurre un error al leer o escribir el
     *                                  archivo JSON.
     */
    public void crearLibro(Libro libroIngresado) throws IllegalArgumentException, IOException {
        try {
            file.createNewFile();
            tienda.setMapLibros(objectMapper.readValue(file, new TypeReference<Map<String, ArrayList<Libro>>>() {
            }));
            libroIngresado.setIsComprado(false);
            if (isRegistrado(libroIngresado)) {
                throw new IllegalArgumentException("El libro con ISBN " + libroIngresado.getIsbn() + " ya existe.");
            }
            ArrayList<Libro> libros = buscarCategoria(libroIngresado, tienda.getMapLibros());
            if (libros == null) {
                ArrayList<Libro> libroNuevo = new ArrayList<>();
                libroNuevo.add(libroIngresado);
                tienda.getMapLibros().put(libroIngresado.getCategoria(), libroNuevo);
            } else {
                libros.add(libroIngresado);
            }
            objectMapper.writeValue(file, tienda.getMapLibros());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee el mapa de libros almacenado en el archivo JSON.
     *
     * @return Mapa que contiene las categorías como claves y listas de libros como
     * valores.
     */
    public Map<String, ArrayList<Libro>> leerLibro() {
        try {
            tienda.setMapLibros(objectMapper.readValue(file, new TypeReference<HashMap<String, ArrayList<Libro>>>() {
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tienda.getMapLibros();
    }

    /**
     * Modifica un libro en el mapa y lo guarda en el archivo JSON.
     *
     * @param libro Libro a modificar.
     * @throws IOException Si ocurre un error al leer o escribir el archivo JSON.
     */
    public void modificarLibro(Libro libro) throws IOException {
        tienda.setMapLibros(objectMapper.readValue(file, new TypeReference<>() {
        }));
        ArrayList<Libro> categoria = tienda.getMapLibros().get(libro.getCategoria());
        eliminarLibro(libro, tienda.getMapLibros());
        if (categoria != null) {
            for (Libro libroArray : categoria) {
                if (libro.getIsbn().equals(libroArray.getIsbn())) {
                    libroArray.setIsbn(libro.getIsbn());
                    libroArray.setTitulo(libro.getTitulo());
                    libroArray.setEditorial(libro.getEditorial());
                    libroArray.setPrecioVenta(libro.getPrecioVenta());
                    libroArray.setNumeroPaginas(libro.getNumeroPaginas());
                    libroArray.setTipoLibro(libro.getTipoLibro());
                    libroArray.setAutor(libro.getAutor());
                    libroArray.setAnioPublicacion(libro.getAnioPublicacion());
                    libroArray.setCategoria(libro.getCategoria());
                    libroArray.setStockDisponible(libro.getStockDisponible());
                    libroArray.setStockReservado(libro.getStockReservado());
                    objectMapper.writeValue(file, tienda.getMapLibros());
                    return;
                }
            }
        }

        categoria.add(libro);
        objectMapper.writeValue(file, tienda.getMapLibros());
    }

    /**
     * Verifica si un libro ya está registro en el mapa, comparando su ISBN.
     *
     * @param libroBuscado libro para buscar en el mapa
     * @return {@code true} si el libro ya está registrado en el mapa, {@code false}
     * en caso contrario.
     */
    public boolean isRegistrado(Libro libroBuscado) {
        for (ArrayList<Libro> Categoria : tienda.getMapLibros().values()) {
            for (Libro libro : Categoria) {
                if (libro.getIsbn().equals(libroBuscado.getIsbn())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Busca un libro en el mapa según la categoria del libro ingresado y devuelve
     * su lista de libros.
     *
     * @param libroBuscado libro para buscar en el mapa.
     * @return {@code lista} de libros que contiene el libro buscado.
     */
    public ArrayList<Libro> buscarLibro(Libro libroBuscado, Map<String, ArrayList<Libro>> listMap) throws IOException {

        for (ArrayList<Libro> categoria : listMap.values()) {
            for (Libro libro : categoria) {
                if (libroBuscado.getIsbn().equals(libro.getIsbn())) {
                    return categoria;
                }
            }
        }
        return null;
    }

    // Mejor hacer un listMap.get(libroBuscado.categoria) para obtener el arrayList

    public ArrayList<Libro> buscarCategoria(Libro libroBuscado, Map<String, ArrayList<Libro>> listMap) throws IOException {

        for (ArrayList<Libro> categoria : listMap.values()) {
            for (Libro libro : categoria) {
                if (libroBuscado.getCategoria().equals(libro.getCategoria())) {
                    return categoria;
                }
            }
        }
        return null;
    }

    public void eliminarLibro(Libro libro, Map<String, ArrayList<Libro>> catalogo) {
        for (ArrayList<Libro> categoria : catalogo.values()) {
            int index = 0;
            for (Libro libroCategoria : categoria) {
                if (libro.getIsbn().equals(libroCategoria.getIsbn())) {
                    categoria.remove(index);
                    return;
                }
                index++;
            }
        }
    }
}
