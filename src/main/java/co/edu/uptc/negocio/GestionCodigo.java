package co.edu.uptc.negocio;

import java.sql.SQLException;

import co.edu.uptc.modelo.CodigoPremium;
import co.edu.uptc.persistencia.CodigoDAO;

public class GestionCodigo {
    
    private CodigoDAO codigoDao;
    
    public GestionCodigo(CodigoDAO codigoDao) {
	this.codigoDao = codigoDao;
    }
    
    public void registrarCodigo(String codigo) throws SQLException, RuntimeException {
	validarCodigo(codigo);
	CodigoPremium cod = new CodigoPremium();
	cod.setCodigo(codigo);
	cod.setUsado(false);
	codigoDao.insertarDatos(cod);
    }

    private void validarCodigo(String codigo) throws RuntimeException{
	codigo = codigo.trim();
	codigo = codigo.replaceAll("\\s", "");
	if (codigo == null) throw new RuntimeException("El codigo no puede ser nulo.");
	if (codigo.isEmpty() || codigo.isBlank()) {
	    throw new RuntimeException("Por favor, ingresa un código.");
	}
	
	if (codigo.length() < 6) {
	    throw new RuntimeException("El codigo debe tener al menos 6 o mas caracteres.");
	}
    }
    
    public void usarCodigo(String codigo) throws SQLException, RuntimeException {
	validarCodigo(codigo);
	CodigoPremium codPre = new CodigoPremium();
	codPre.setCodigo(codigo);
	codPre = codigoDao.seleccionarRegistro(codPre);
	if (codPre == null) { 
	    throw new RuntimeException("El código es incorrecto.");
	}
	if (codPre.getUsado() == true) {
	    throw new RuntimeException("El código ya está en uso.");
	}
	codPre.setUsado(true);
	codigoDao.actualizarDatos(codPre);
    }
}
