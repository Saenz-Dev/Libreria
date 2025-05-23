package co.edu.uptc.negocio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Tienda;

/**
 * Clase encargada de gestionar los libros del catálogo.
 */
public class GestionLibro {

    /**
     * Instancia Manejo de Libros con JSON
     */
    private ManejoLibroJSON manejoLibroJSON;

    /**
     * Expresión regular
     */
    private Expresion expresion;

    /**
     * Constructor de la clase
     */
    public GestionLibro(Tienda tienda) {
        manejoLibroJSON = new ManejoLibroJSON(tienda);
        expresion = new Expresion();
    }

    /**
     * Método que devuelve la instancia Manejo de Libros con JSON
     * @return instancia Manejo de Libros con JSON
     */
    public ManejoLibroJSON getManejoLibroJSON() {
        return manejoLibroJSON;
    }

    /**
     * Método que actualiza la instancia Manejo de Libros con JSON
     * @param manejoLibroJSON instancia Manejo de Libros con JSON
     */
    public void setManejoLibroJSON(ManejoLibroJSON manejoLibroJSON) {
        this.manejoLibroJSON = manejoLibroJSON;
    }

    /**
     * Método que registra un libro en el catálogo
     * @param libro libro a registrar
     * @throws IllegalArgumentException si alguno de los campos no cumple con las reglas
     * @throws IOException si ocurre algún error cuando no se escribe el JSON
     */
    public void registrarLibro(Libro libro) throws IllegalArgumentException, IOException {
        expresion.validarDatosObligatorios(libro);
        expresion.validarFormatoDatosLibro(libro);
        manejoLibroJSON.crearLibro(libro);
    }

    /**
     * Método que modifica un libro en el catálogo
     * @param libro libro a modificar
     * @throws IllegalArgumentException si alguno de los campos no cumple con las reglas
     * @throws IOException si ocurre algún error cuando no se escribe el JSON
     */
    public void modificarLibro(Libro libro) throws IllegalArgumentException, IOException {
        expresion.validarDatosObligatorios(libro);
        expresion.validarFormatoDatosLibro(libro);
        manejoLibroJSON.modificarLibro(libro);
    }


    /**
     * Método que elimina un libr del catálogo
     * @param listaLibros lista de libros a eliminar
     * @throws IllegalArgumentException si alguno de los libros no está en el catálogo
     * @throws IOException si ocurre algún error cuando no se escribe el JSON
     */
    /*public void eliminarLibros(ArrayList<PanelLibroEliminar> listaLibros) throws IllegalArgumentException, IOException{
        Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
        ArrayList<PanelLibroEliminar> listaLibrosLocal = (ArrayList<PanelLibroEliminar>) listaLibros.clone();
        if (listaLibros.isEmpty()) throw new IllegalArgumentException("No hay libros registrados para eliminar");
        for (PanelLibroEliminar pl : listaLibrosLocal) {
            if (existeLibro(pl.getLibro()) && pl.isSelected()) {
                int index = eliminarLibroPosicion(pl.getLibro(), catalogo);
                listaLibros.remove(pl);
            }

        }
        manejoLibroJSON.escribirLibros(catalogo);
    }*/

    //TODO revisar este metodo, puede ocasionar errores
    public void eliminarLibro(ArrayList<String> isbnLibros) throws IllegalArgumentException, IOException{
        Map<String, ArrayList<Libro>> catalogo = manejoLibroJSON.leerLibro();
        StringBuilder sb = new StringBuilder();
        if (isbnLibros.isEmpty()) throw new RuntimeException("No hay libros registrados para eliminar");
        for (String isbn : isbnLibros) {
            Libro libro = buscarLibroCatalogo(isbn, catalogo);
            if (libro.getIsComprado()){
                sb.append("\n- " + libro.getTitulo());
                continue;
            }
            if (existeLibro(isbn)) {
                Libro libroEliminar = new Libro();
                libroEliminar.setIsbn(isbn);
                eliminarLibroPosicion(libroEliminar, catalogo);
            }
        }
        if (!sb.isEmpty()) throw new IllegalArgumentException("Estos libros no se pueden eliminar por que ya se han comprado: " + sb);
        manejoLibroJSON.escribirLibros(catalogo);
    }

    public boolean existeLibro(String isbn) {
        for (ArrayList<Libro> libros : manejoLibroJSON.getMapLibros().values()) {
            for (Libro libroCatalogo : libros) {
                if (libroCatalogo.getIsbn().equals(isbn)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Método que devuelve un array con los títulos de los libros que se encuentran en el catálogo
     * @return array con los títulos de los libros que se encuentran en el catálogo
     */
    public String[] obtenerLibros() {
        ArrayList<String> libros = new ArrayList<>();
        String[] arrayLibros;
        for (ArrayList<Libro> catalogo : manejoLibroJSON.getMapLibros().values()) {
            for (Libro libro : catalogo) {
                libros.add(libro.getTitulo());
            }
        }
        arrayLibros = new String[libros.size()];
        for (int i = 0; i < arrayLibros.length; i++) {
            arrayLibros[i] = libros.get(i);
        }
        return arrayLibros;
    }

    public Libro buscarLibroCatalogo(String isbn, Map<String, ArrayList<Libro>> catalogo) {
        for (ArrayList<Libro> catalogoLib : catalogo.values()) {
            for (Libro libroBuscado : catalogoLib) {
                if (libroBuscado.getIsbn().equals(isbn)) {
                    return libroBuscado;
                }
            }
        }
        return null;
    }

    /**
     * Método que devuelve el libro que se encuentra en el catálogo con el título dado
     * @param tituloLibro título del libro que se busca
     * @return libro del catálogo
     */
    public Libro buscarLibro(String tituloLibro) {
        for (ArrayList<Libro> catalogo : manejoLibroJSON.getMapLibros().values()) {
            for (Libro libroCatalogo : catalogo) {
                if (libroCatalogo.getTitulo().equals(tituloLibro)) {
                    return libroCatalogo;
                }
            }
        }
        return null;
    }

    /**
     * MMétodo que devuelve true si el libro existe en el catálogo
     * @param libro libro qe se busca
     * @return true si el libro existe en el catálogo
     */
    public boolean existeLibro(Libro libro) {
        for (ArrayList<Libro> libros : manejoLibroJSON.getMapLibros().values()) {
            for (Libro libroCatalogo : libros) {
                if (libro.getIsbn().equals(libroCatalogo.getIsbn())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifica si el libro tiene stock disponible en el catálogo
     * @param isbnLibro isbn para validar que el libro que lo contenga tenga disponibilidad para ser vendido.
     * @return true si el stock disponible es mayor a 0
     */
    public boolean validarExistencia(String isbnLibro) {
        for (ArrayList<Libro> libros : manejoLibroJSON.getMapLibros().values()) {
            for (Libro libro : libros) {
                if (libro.getIsbn().equals(isbnLibro)) {
                    return libro.getStockDisponible() > 0;
                }
            }
        }
        return false;
    }

    /**
     * Devuelve la posición del libro en el Map del catálogo
     * @param libroParametro libro a buscar
     * @param catalogo libros disponibles en el catálogo
     * @return posición del libro en el Map del catálogo
     */
    public void eliminarLibroPosicion(Libro libroParametro, Map<String,ArrayList<Libro>> catalogo) {
        //TODO hacer validación la cual si el producto que se va a eliminar ya tiene compras, no se puede eliminar
        int index = 0;
        for (ArrayList<Libro> libros : catalogo.values()) {
            for (Libro libro : libros) {
                if (libro.getIsbn().equals(libroParametro.getIsbn())) {
                    libros.remove(index);
                    return;
                }
                index++;
            }
            index = 0;
        }
    }
}
