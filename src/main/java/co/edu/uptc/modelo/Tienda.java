package co.edu.uptc.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tienda implements Serializable {
	
	/**
     * 
     */
    private static final long serialVersionUID = 4563764538288413272L;
	private Map<String, ArrayList<Libro>> mapLibros;
	private ArrayList<Usuario> usuarios;
	private List<Recibo> recibos;
	
	public Tienda() {
		mapLibros = new HashMap<>();
		usuarios = new ArrayList<>();
		recibos = new ArrayList<>();
	}
	
	public Tienda(Map<String, ArrayList<Libro>> mapLibros, ArrayList<Usuario> usuarios, List<Recibo> recibos) {
		this.mapLibros = mapLibros;
		this.usuarios = usuarios;
		this.recibos = recibos;
	}

	public Map<String, ArrayList<Libro>> getMapLibros() {
		return mapLibros;
	}
	
	public void setMapLibros(Map<String, ArrayList<Libro>> mapLibros) {
		this.mapLibros = mapLibros;
	}
	
	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios= usuarios;
	}
	
	public List<Recibo> getRecibos() {
		return recibos;
	}
	
	public void setRecibos(List<Recibo> recibos) {
		this.recibos = recibos;
	}
	
}
