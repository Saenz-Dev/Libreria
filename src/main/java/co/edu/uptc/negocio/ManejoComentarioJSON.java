package co.edu.uptc.negocio;

import co.edu.uptc.modelo.Comentario;
import co.edu.uptc.modelo.Tienda;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.TreeMap;

public class ManejoComentarioJSON {

    private File file;
    private String ruta;
    private Tienda tienda;
    private ObjectMapper objectMapper;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ManejoComentarioJSON(Tienda tienda) {
        this.tienda = tienda;
        ruta = "src/main/java/co/edu/uptc/persistencia/comentario.json";
        file = new File(ruta);
        objectMapper = new ObjectMapper();
        objectMapper.registerModules(new JavaTimeModule());
    }

    public void leerComentario() throws IOException {
        try {
            if (file.length() == 0) {//Si el archivo no contiene nada, crear y agregar un TreeMap
                tienda.setMapComentarios(new TreeMap<>());
                objectMapper.writeValue(file, tienda.getMapComentarios()); //Escribirlo en el archivo JSON
            }
            tienda.setMapComentarios(objectMapper.readValue(file, new TypeReference<TreeMap<String, Stack<Comentario>>>() {
            })); //Por ultimo leerlo y guardarlo en el TreeMap del sistema
        } catch (IOException e) {
            throw new IOException("Error al leer los comentarios");
        }
    }

    public void escribirComentario(String isbn, Comentario comentario) throws IOException {
        try {
            leerComentario();
            if (tienda.getMapComentarios().get(isbn) == null) {
                Stack<Comentario> comentarios = new Stack<>();
                comentarios.add(comentario);
                tienda.getMapComentarios().put(isbn, comentarios);
                objectMapper.writeValue(file, tienda.getMapComentarios());
                return;
            }
            tienda.getMapComentarios().get(isbn).add(comentario);
            objectMapper.writeValue(file, comentario);
        } catch (IOException e) {
            throw new IOException("Error al guarar el comentario");
        }
    }

    public Stack<Comentario> buscarComentario(String isbn) throws IOException, RuntimeException {
        try {
            leerComentario();
            Stack<Comentario> comentarios = tienda.getMapComentarios().get(isbn);
            if (comentarios == null) {
                throw new RuntimeException("No se encontraron comentarios este libro.");
            }
            return tienda.getMapComentarios().get(isbn);
        } catch (IOException e) {
            throw new IOException("Error al buscar los comentarios");
        }
    }
}
