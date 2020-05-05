package modelo;

public class Producto {
	
	private String nombre;
	private int volumenAnual;
	private double costo;
	private int unidades;
	
	public Producto(String nombre, int volumenAnual, double costo, int unidades) {
		super();
		this.nombre = nombre;
		this.volumenAnual = volumenAnual;
		this.costo = costo;
		this.unidades = unidades;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getVolumenAnual() {
		return volumenAnual;
	}

	public void setVolumenAnual(int volumenAnual) {
		this.volumenAnual = volumenAnual;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	
}
