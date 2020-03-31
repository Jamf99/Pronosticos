package modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class Innova {
	
	private static VentasSemanal[] ventas;
	
	public Innova() {
		ventas = new VentasSemanal[54];
		inicializarVentas();
	}
	
	public void inicializarVentas() {
		for (int i = 0; i < ventas.length; i++) {
			ventas[i] = new VentasSemanal(i+1+"", new ArrayList<Producto>());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static int calcularSemana(int mes, int dia) {
		Date date = new Date(120, mes - 1, dia);
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek( Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek( 4 );
		calendar.setTime(date);
		int semana = calendar.get(Calendar.WEEK_OF_YEAR);
		return semana;
	}

	public static void leerDatos() {
		Scanner input = null;
		try {
			input = new Scanner(new File("datos/datosPrimarios.txt"));
	        while (input.hasNextLine()) {
	        	String[] lines = input.nextLine().split("\t");
	        	String fecha = lines[0];
	        	int semana = calcularSemana(Integer.parseInt(fecha.split("/")[1]), Integer.parseInt(fecha.split("/")[0]));
	        	String[] producto = new String[2];
	        	boolean flag = false;
	        	for (int i = 1; i < lines.length; i++) {
	        		if(!lines[i].equals("")) {
	        			if(!flag) {
	        				producto[0] = lines[i];
		        			flag = true;
	        			}else {
	        				producto[1] = lines[i];
	        			}
	        		}
				}
	        	ventas[semana].agregarProducto(new Producto(producto[0], Integer.parseInt(producto[1])));
	        }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    } finally {
			input.close();
		}
	}

	public VentasSemanal[] getVentas() {
		return ventas;
	}

	public void setVentas(VentasSemanal[] ventas) {
		this.ventas = ventas;
	}
	
	public static void main(String[] args) {
		Innova n = new Innova();
		leerDatos();
		for (int i = 0; i < ventas.length; i++) {
			System.out.println("Ventas semana "+(i+1)+" = "+ventas[i].getProductosVendidos().size());
			System.out.println("=========================================");
			for (int j = 0; j < ventas[i].getProductosVendidos().size(); j++) {
				System.out.println("PRODUCTO: "+ventas[i].getProductosVendidos().get(j).getNombre()+" cantidad: "
						+ventas[i].getProductosVendidos().get(j).getCantidad());
			}
			System.out.println("=========================================");
		}
		
	}

}
