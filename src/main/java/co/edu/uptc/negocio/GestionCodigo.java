package co.edu.uptc.negocio;

import java.sql.SQLException;
import java.util.ArrayList;

import co.edu.uptc.modelo.CodigoPremium;
import co.edu.uptc.persistencia.CodigoDAO;

/**
 * Clase encargada de la gestión de códigos premium en la tienda virtual.
 * Permite registrar, consultar y usar códigos premium, validando su formato y estado de uso.
 * Utiliza un DAO para la persistencia de los códigos.
 */
public class GestionCodigo {

    /**
     * DAO para operaciones de persistencia de códigos premium.
     */
    private CodigoDAO codigoDao;

    /**
     * Constructor que inicializa la gestión de códigos con el DAO correspondiente.
     * @param codigoDao DAO para códigos premium
     */
    public GestionCodigo(CodigoDAO codigoDao) {
        this.codigoDao = codigoDao;
    }

    /**
     * Registra un nuevo código premium en el sistema.
     * Valida el formato del código antes de guardarlo.
     *
     * @param codigo código premium a registrar
     * @throws SQLException si ocurre un error de base de datos
     * @throws RuntimeException si el código no es válido
     */
    public void registrarCodigo(String codigo) throws SQLException, RuntimeException {
        validarCodigo(codigo);
        CodigoPremium cod = new CodigoPremium();
        cod.setCodigo(codigo);
        cod.setUsado(false);
        codigoDao.insertarDatos(cod);
    }

    /**
     * Consulta todos los códigos premium registrados en el sistema.
     *
     * @return lista de códigos premium
     * @throws SQLException si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    public ArrayList<CodigoPremium> consultarCodigo() throws SQLException, RuntimeException {
        return codigoDao.seleccionarRegistros();
    }

    /**
     * Valida el formato y longitud de un código premium.
     *
     * @param codigo código a validar
     * @throws RuntimeException si el código es inválido
     */
    private void validarCodigo(String codigo) throws RuntimeException {
        codigo = codigo.trim();
        codigo = codigo.replaceAll("\\s", "");
        if (codigo.isBlank()) {
            throw new RuntimeException("Por favor, ingresa un código.");
        }
        if (codigo.length() < 6) {
            throw new RuntimeException("El codigo debe tener al menos 6 o mas caracteres.");
        }
    }

    /**
     * Marca un código premium como usado si es válido y no ha sido utilizado previamente.
     *
     * @param codigo código premium a usar
     * @throws SQLException si ocurre un error de base de datos
     * @throws RuntimeException si el código es inválido o ya fue usado
     */
    public void usarCodigo(String codigo) throws SQLException, RuntimeException {
        validarCodigo(codigo);
        CodigoPremium codPre = new CodigoPremium();
        codPre.setCodigo(codigo);
        codPre = codigoDao.seleccionarRegistro(codPre);
        if (codPre == null) {
            throw new RuntimeException("El código es incorrecto.");
        }
        if (codPre.getUsado()) {
            throw new RuntimeException("El código ya está en uso.");
        }
        codPre.setUsado(true);
        codigoDao.actualizarDatos(codPre);
    }
}
