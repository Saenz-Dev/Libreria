package co.edu.uptc.modelo;

import com.sun.source.tree.Tree;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public class Tienda implements Serializable {
	
	/**
     * 
     */
    private static final long serialVersionUID = 4563764538288413272L;
	private Map<String, ArrayList<Libro>> mapLibros;
	private ArrayList<Usuario> usuarios;
	private TreeMap<String, ArrayList<Recibo>> productoCompras;
	
	public Tienda() {
		mapLibros = new HashMap<>();
		usuarios = new ArrayList<>();
		productoCompras = new TreeMap<>();
	}
	
	public Tienda(Map<String, ArrayList<Libro>> mapLibros, ArrayList<Usuario> usuarios, TreeMap<String, ArrayList<Recibo>> productoCompras) {
		this.mapLibros = mapLibros;
		this.usuarios = usuarios;
		this.productoCompras = productoCompras;
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
	
	public TreeMap<String, ArrayList<Recibo>> getRecibos() {
		return productoCompras;
	}
	
	public void setRecibos(TreeMap<String, ArrayList<Recibo>> productoCompras) {
		this.productoCompras = productoCompras;
	}
}
