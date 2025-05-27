package co.edu.uptc.log;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RegistroLog {

    private static final Logger LOGGER = Logger.getLogger(RegistroLog.class.getName());

    static {// Es cargada una única vez cuando la clase es cargada por primera vez en la JVM
	try {
	    File file = new File("logs");
	    if (!file.exists()) {
		file.mkdir();
	    }
	    FileHandler fileHandler = new FileHandler("logs/logs_libreria.log", true); // Controlador(Handler) de
										       // archivos a escribir
	    fileHandler.setFormatter(new SimpleFormatter()); // Formato en el que se escribe en el archivo
	    fileHandler.setLevel(Level.ALL);
	    LOGGER.addHandler(fileHandler);
	    LOGGER.setLevel(Level.ALL);
	    LOGGER.setUseParentHandlers(false);
	    fileHandler.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void registrarError(String mensaje, Exception e) {
	LOGGER.log(Level.SEVERE, mensaje, e);
    }

    public static void registrarError(String mensaje) {
	LOGGER.log(Level.SEVERE, mensaje);
    }

    public static void registrarAdvertencia(String mensaje, Exception e) {
	LOGGER.log(Level.WARNING, mensaje, e);
    }

    public static void registrarAdvertencia(String mensaje) {
	LOGGER.log(Level.WARNING, mensaje);
    }
    
    public static void registrarInfo(String mensaje, Exception e) {
	LOGGER.log(Level.INFO, mensaje, e);
    }
    
    public static void registrarInfo(String mensaje) {
	LOGGER.log(Level.INFO, mensaje);
    }
}
