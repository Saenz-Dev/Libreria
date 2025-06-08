package co.edu.uptc.log;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Clase utilitaria para el registro de logs en la aplicación de la Librería Virtual.
 * Utiliza java.util.logging para registrar mensajes de error, advertencia e información en archivos de log.
 */
public class RegistroLog {

    /**
     * Logger principal utilizado para registrar los mensajes.
     */
    private static final Logger LOGGER = Logger.getLogger(RegistroLog.class.getName());
    /**
     * Handler para escribir los logs en un archivo.
     */
    public static FileHandler fileHandler;

    static { // Es cargada una única vez cuando la clase es cargada por primera vez en la JVM
        try {
            File file = new File("logs");
            if (!file.exists()) {
                file.mkdir();
            }
            fileHandler = new FileHandler("logs/logs_libreria.log", true); // Controlador(Handler) de archivos a escribir
            fileHandler.setFormatter(new SimpleFormatter()); // Formato en el que se escribe en el archivo
            fileHandler.setLevel(Level.ALL);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
            LOGGER.setUseParentHandlers(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Registra un mensaje de error junto con una excepción en el log.
     *
     * @param mensaje Mensaje de error a registrar.
     * @param e       Excepción asociada al error.
     */
    public static void registrarError(String mensaje, Exception e) {
        LOGGER.log(Level.SEVERE, mensaje + "\n", e);
    }

    /**
     * Registra un mensaje de error en el log.
     *
     * @param mensaje Mensaje de error a registrar.
     */
    public static void registrarError(String mensaje) {
        LOGGER.log(Level.SEVERE, mensaje + "\n");
    }

    /**
     * Registra una advertencia junto con una excepción en el log.
     *
     * @param mensaje Mensaje de advertencia a registrar.
     * @param e       Excepción asociada a la advertencia.
     */
    public static void registrarAdvertencia(String mensaje, Exception e) {
        LOGGER.log(Level.WARNING, mensaje + "\n", e);
    }

    /**
     * Registra una advertencia en el log.
     *
     * @param mensaje Mensaje de advertencia a registrar.
     */
    public static void registrarAdvertencia(String mensaje) {
        LOGGER.log(Level.WARNING, mensaje + "\n");
    }

    /**
     * Registra un mensaje informativo junto con una excepción en el log.
     *
     * @param mensaje Mensaje informativo a registrar.
     * @param e       Excepción asociada a la información.
     */
    public static void registrarInfo(String mensaje, Exception e) {
        LOGGER.log(Level.INFO, mensaje + "\n", e);
    }

    /**
     * Registra un mensaje informativo en el log.
     *
     * @param mensaje Mensaje informativo a registrar.
     */
    public static void registrarInfo(String mensaje) {
        LOGGER.log(Level.INFO, mensaje + "\n");
    }
}
