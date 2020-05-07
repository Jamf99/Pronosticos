package modelo;

public class ProductoDiario {
	
	private String nombre;
	private int cantidad;
	private int dia;
	
	public ProductoDiario(String nombre, int cantidad, int dia) {
		super();
		this.dia = dia;
		this.nombre = nombre;
		this.cantidad = cantidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

}
