package modelo;

import java.util.ArrayList;

public class VentasSemanal {
	
	private String semana;
	private ArrayList<ProductoDiario> productosVendidos;
	
	public String getSemana() {
		return semana;
	}

	public VentasSemanal(String semana, ArrayList<ProductoDiario> productosVendidos) {
		super();
		this.semana = semana;
		this.productosVendidos = productosVendidos;
	}
	
	public void agregarProducto(ProductoDiario p) {
		boolean repetido = false;
		for (int i = 0; i < productosVendidos.size() && !repetido; i++) {
			ProductoDiario p2 = productosVendidos.get(i);
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

	public ArrayList<ProductoDiario> getProductosVendidos() {
		return productosVendidos;
	}

	public void setProductosVendidos(ArrayList<ProductoDiario> productosVendidos) {
		this.productosVendidos = productosVendidos;
	}

}
