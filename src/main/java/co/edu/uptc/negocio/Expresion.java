package co.edu.uptc.negocio;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.Administrador;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Usuario;

import java.time.LocalDate;

/**
 * Clase encargad de validar los datos del usuario. Contiene expresiones
 * regulares para validar los datos.
 */
public class Expresion {

    /**
     * Expresiones regulares
     */
    public static final String EXPRESION_ALFABETICA = "^[a-zA-Z\\p{L}\\s]+$";
    public static final String EXPRESION_NUMERICA_TELEFONO = "^3[0-9]{9}$";
    public static final String EXPRESION_NUMERICA_PRECIO = "^[0-9]+$";
    public static final String EXPRESION_NUMERO_PAGINAS = "^[0-9]{1,4}$";
    public static final String EXPRESION_DIRECCION = "^([\\w\\s#.-]+),\\s*[\\p{L}\\s]+,\\s*[\\p{L}\\s]+$";
    public static final String EXPRESION_CORREO = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,3}+$";
    public static final String EXPRESION_CONTRASENA = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+\\.])[a-zA-Z\\d!@#$%^&*()\\-+\\.]{8,}$";
    public static final String EXPRESION_ISBN = "^(978|979)(-?[0-9]){10}$";
    public static final String EXPRESION_ANO_PUBLICACION = "^[0-9]{4}$";
    public static final String EXPRESION_CONTRASENA_ADMIN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>.,?/\\\\])[a-zA-Z\\d!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>.,?/\\\\]{16,}$";


    /**
     * Metodo que valida los datos del usuario en el formulario
     *
     * @param usuario a validar los datos
     * @throws IllegalArgumentException si alguno de los campos no cumple con las
     *                                  reglas
     */
    public void validarDatosUsuario(Usuario usuario) throws IllegalArgumentException {
        validarLongitudDatos(usuario);
        StringBuilder sb = new StringBuilder();
        if (usuario.getCuenta().getCorreo().equals(Administrador.CORREO)) {
            if (!usuario.getCuenta().getContrasena().matches(EXPRESION_CONTRASENA_ADMIN)) {
                throw new IllegalArgumentException("La contraseña del administrador debe tener al menos ocho letras.\n");
            }
            return;
        }
        if (!usuario.getCuenta().getCorreo().matches(EXPRESION_CORREO)) {
            sb.append("Formato de correo incorrecto, ejemplo: usuario@dominio.extension\n");
        }
        if (!usuario.getCuenta().getContrasena().matches(EXPRESION_CONTRASENA)) {
            sb.append("La contraseña debe tener al menos ocho letras, un número y un caracter especial.\n");
        }
        if (usuario.getTelefono() == -1) {
            sb.append("El formato del teléfono es incorrecto.\n");
        }
        if (!String.valueOf(usuario.getTelefono()).matches(EXPRESION_NUMERICA_TELEFONO)) {
            sb.append("Ej formato teléfono: 3 seguido de nueve números.\n");
        }
        if (!usuario.getDireccionEnvio().matches(EXPRESION_DIRECCION)) {
            sb.append("Formato de dirección ej: Calle 123 #45-67, Bogotá, Colombia\n");
        }
        if (!usuario.getNombre().matches(EXPRESION_ALFABETICA)) {
            sb.append("El nombre solo puede llevar letras\n");
        }
        if (!sb.isEmpty()) {
            RegistroLog.registrarAdvertencia(sb.toString());
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /**
     * Metodo que valida los datos del usuario en el formulario
     *
     * @param libro libro a validar
     * @throws IllegalArgumentException si alguno de los campos no cumple con las
     *                                  reglas
     */
    public void validarFormatoDatosLibro(Libro libro) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        if (!libro.getIsbn().matches(EXPRESION_ISBN)) {
            sb.append("El ISBN debe tener 979 o 978 seguido de 10 números.\n");
        }
        if (!libro.getAutor().matches(EXPRESION_ALFABETICA)) {
            sb.append("El nombre del autor solo puede tener letras\n");
        }
        if (String.valueOf(libro.getAnioPublicacion()).isBlank() || libro.getAnioPublicacion() != 0) {
            if (libro.getAnioPublicacion() == -1) {
                sb.append("El año de publicación no es válido.\n");
            } else if (libro.getAnioPublicacion() < -1) {
                sb.append("El año de publicación debe ser positivo.\n");
            }
            if (!String.valueOf(libro.getAnioPublicacion()).matches(EXPRESION_ANO_PUBLICACION) || libro.getAnioPublicacion() > LocalDate.now().getYear()) {
                sb.append("El año de publicación debe tener cuatro digitos y debe ser igual o menor al actual.\n");
            }
        }
        if (!String.valueOf(libro.getNumeroPaginas()).isBlank() || libro.getNumeroPaginas() == -1) {
            if (!String.valueOf(libro.getNumeroPaginas()).matches(EXPRESION_NUMERO_PAGINAS)) {
                sb.append("Numero de páginas invalido\n");
            } else if (libro.getNumeroPaginas() < -1) {
                sb.append("El número de páginas debe ser positivo.\n");
            }
        }
        if (!String.valueOf((int) libro.getPrecioVenta()).matches(EXPRESION_NUMERICA_PRECIO) || libro.getPrecioVenta() == -0.1) {
            sb.append("Precio Unitario del libro invalido.\n");
        } else if (libro.getPrecioVenta() < -1) {
            sb.append("El precio debe ser positivo.\n");
        }

        if (!String.valueOf(libro.getStockDisponible()).matches(EXPRESION_NUMERICA_PRECIO) || libro.getStockDisponible() == -1) {
            sb.append("Cantidad ingresada invalida\n");
        } else if (libro.getStockDisponible() < -1) {
            sb.append("El stock disponible debe ser positivo.\n");
        }
        validarLongitudDatos(libro);
        if (!sb.isEmpty()) {
            RegistroLog.registrarAdvertencia(sb.toString());
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /**
     * Metodo que valida los datos del libro en el formulario
     *
     * @param libro libro a validar
     * @throws IllegalArgumentException si alguno de los campos no cumple con las
     *                                  reglas
     */
    public void validarDatosObligatorios(Libro libro) throws IllegalArgumentException {
        if (libro.getIsbn() == null || libro.getIsbn().isBlank() || libro.getTitulo() == null || libro.getTitulo().isBlank() || libro.getAutor() == null || libro.getAutor().isBlank() || libro.getNumeroPaginas() == 0 || libro.getPrecioVenta() == 0 || libro.getStockDisponible() == -999 || libro.getCategoria() == null || libro.getTipoLibro() == null || libro.getEditorial() == null) {
            RegistroLog.registrarAdvertencia("Los campos con * con obligatorios.");
            throw new IllegalArgumentException("Los campos con * con obligatorios.");
        }
    }

    /**
     * Valida los datos obligatorios del usuario.
     *
     * @param usuario usuario para validar los datos.
     * @throws RuntimeException si algún campo de texto que es obligatorio está
     *                          vacío.
     */
    public void validarDatosObligatoriosUser(Usuario usuario) throws RuntimeException {
        if (usuario.getCuenta().getCorreo() == Administrador.CORREO) {
            if (usuario.getCuenta().getContrasena() == null || usuario.getCuenta().getContrasena().isBlank()) {
                RegistroLog.registrarAdvertencia("La contraseña del administrador es obligatoria.");
                throw new RuntimeException("La contraseña del administrador es obligatoria.");
            }
        }
        if (usuario.getNombre().isBlank() || String.valueOf(usuario.getTelefono()).isBlank() || usuario.getTelefono() == 0 || usuario.getDireccionEnvio().isBlank() || usuario.getCuenta().getCorreo().isBlank() || usuario.getCuenta().getContrasena().isBlank()) {
            RegistroLog.registrarAdvertencia("Los campos con * con obligatorios.");
            throw new IllegalArgumentException("Los campos con * son obligatorios.\n");
        }

    }

    public void validarLongitudDatos(Usuario usuario) throws IllegalArgumentException {
        //Por cada dato ingresado de usuario, en un StringBuilder guardar los errores que salgan de los datos que excedan los 50 caracteres
        StringBuilder sb = new StringBuilder();
        if (usuario.getNombre() != null && usuario.getNombre().length() > 50) {
            sb.append("El nombre no puede exceder los 50 caracteres.\n");
        }
        if (usuario.getDireccionEnvio() != null && usuario.getDireccionEnvio().length() > 100) {
            sb.append("La dirección de envío no puede exceder los 100 caracteres.\n");
        }
        if (usuario.getCuenta().getCorreo() != null && usuario.getCuenta().getCorreo().length() > 50) {
            sb.append("El correo no puede exceder los 50 caracteres.\n");
        }
        if (usuario.getCuenta().getContrasena() != null && usuario.getCuenta().getContrasena().length() > 50) {
            sb.append("La contraseña no puede exceder los 50 caracteres.\n");
        }
        if (sb.length() > 0) {
            RegistroLog.registrarAdvertencia(sb.toString());
            throw new IllegalArgumentException(sb.toString());
        }
    }

    //Ahora hacer el anterior metodo pero para los datos del libro
    public void validarLongitudDatos(Libro libro) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        if (libro.getTitulo() != null && libro.getTitulo().length() > 100) {
            sb.append("El título no puede exceder los 100 caracteres.\n");
        }
        if (libro.getAutor() != null && libro.getAutor().length() > 60) {
            sb.append("El autor no puede exceder los 50 caracteres.\n");
        }
        if (libro.getCategoria() != null && libro.getCategoria().length() > 30) {
            sb.append("La categoría no puede exceder los 30 caracteres.\n");
        }
        if (libro.getTipoLibro() != null && libro.getTipoLibro().toString().length() > 30) {
            sb.append("El tipo de libro no puede exceder los 30 caracteres.\n");
        }
        if (libro.getEditorial() != null && libro.getEditorial().length() > 50) {
            sb.append("La editorial no puede exceder los 50 caracteres.\n");
        }
        if (libro.getIsbn() != null && libro.getIsbn().length() > 13) {
            sb.append("El ISBN no puede exceder los 13 caracteres.\n");
        }
        if (sb.length() > 0) {
            RegistroLog.registrarAdvertencia(sb.toString());
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /**
     * Valida si los datos del usuario están vacios
     *
     * @param correo     correo del usuario
     * @param contrasena contraseña del usuario
     * @throws IllegalArgumentException si alguno de los datos del inicio de sesión no cumple con las reglas
     */
    public void validarCamposVaciosLogin(String correo, String contrasena) throws IllegalArgumentException {
        if (!correo.equals(Administrador.CORREO)) {
            if (correo.isBlank() && contrasena.isBlank()) {
                throw new IllegalArgumentException("Digite el correo y la contraseña.");
            } else if (correo.isBlank()) {
                throw new IllegalArgumentException("Digite el correo.");
            } else if (contrasena.isBlank()) {
                throw new IllegalArgumentException("Digite la contraseña.");
            }
        }
    }
}
