package modelo;

import java.util.ArrayList;

public class VentasSemanal {
	
	private String semana;
	private ArrayList<Producto> productosVendidos;
	
	public String getSemana() {
		return semana;
	}

	public VentasSemanal(String semana, ArrayList<Producto> productosVendidos) {
		super();
		this.semana = semana;
		this.productosVendidos = productosVendidos;
	}
	
	public void agregarProducto(Producto p) {
		boolean repetido = false;
		for (int i = 0; i < productosVendidos.size() && !repetido; i++) {
			Producto p2 = productosVendidos.get(i);
			if(p2.getDia() == p.getDia()) {
				p2.setCantidad(p2.getCantidad()+p.getCantidad());
				repetido = true;
			}
		}
		if(!repetido) {
		productosVendidos.add(p);
		}
	}
	
	public void setSemana(String semana) {
		this.semana = semana;
	}

	public ArrayList<Producto> getProductosVendidos() {
		return productosVendidos;
	}

	public void setProductosVendidos(ArrayList<Producto> productosVendidos) {
		this.productosVendidos = productosVendidos;
	}

}
