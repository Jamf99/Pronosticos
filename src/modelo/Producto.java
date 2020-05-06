package modelo;

public class Producto implements Comparable<Producto>{
	
	private String nombre;
	private int volumenAnual;
	private double costo;
	private int unidades;
	private int volumenPorUnidad;
	private double costoPorUnidad;
	private double volumenAnualDinero;
	private double porcentajeVolumenAnualDinero;
	private double acumuladoPorcentaje;
	private String clasificacion;
	
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

	public int getVolumenPorUnidad() {
		return volumenPorUnidad;
	}

	public void setVolumenPorUnidad(int volumenPorUnidad) {
		this.volumenPorUnidad = volumenPorUnidad;
	}

	public double getCostoPorUnidad() {
		return costoPorUnidad;
	}

	public void setCostoPorUnidad(double costoPorUnidad) {
		this.costoPorUnidad = costoPorUnidad;
	}

	public double getVolumenAnualDinero() {
		return volumenAnualDinero;
	}

	public void setVolumenAnualDinero(double volumenAnualDinero) {
		this.volumenAnualDinero = volumenAnualDinero;
	}

	public double getPorcentajeVolumenAnualDinero() {
		return porcentajeVolumenAnualDinero;
	}

	public void setPorcentajeVolumenAnualDinero(double porcentajeVolumenAnualDinero) {
		this.porcentajeVolumenAnualDinero = porcentajeVolumenAnualDinero;
	}

	public double getAcumuladoPorcentaje() {
		return acumuladoPorcentaje;
	}

	public void setAcumuladoPorcentaje(double acumuladoPorcentaje) {
		this.acumuladoPorcentaje = acumuladoPorcentaje;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	@Override
	public int compareTo(Producto p) {
		if(porcentajeVolumenAnualDinero < p.getPorcentajeVolumenAnualDinero()) {
			return -1;
		}else if(porcentajeVolumenAnualDinero > p.getPorcentajeVolumenAnualDinero()) {
			return 1;
		}
		return 0;
	}
	
}
