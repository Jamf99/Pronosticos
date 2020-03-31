package modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class Innova {
	
	private static VentasSemanal[] ventas;
	
	public Innova() {
		ventas = new VentasSemanal[52];
		inicializarVentas();
	}
	
	public void inicializarVentas() {
		for (int i = 0; i < ventas.length; i++) {
			ventas[i] = new VentasSemanal(i+1+"", new ArrayList<Producto>());
		}
	}
	
	public double calcularCVD(VentasSemanal ventas, String producto) {
		return calcularDesviacionEstandar(ventas, producto) / calcularMedia(producto, ventas);
	}
	
	public static double calcularDesviacionEstandar(VentasSemanal ventas, String producto) {
		double varianza = 0;
		double sumatoria;
		int n = 0;
		for (int i = 0; i < ventas.getProductosVendidos().size(); i++) {
			if(producto.equals(ventas.getProductosVendidos().get(i).getNombre())) {
				sumatoria = Math.pow(ventas.getProductosVendidos().get(i).getCantidad(), 2);
				varianza += sumatoria;
				n++;
			}
		}
		varianza/=(n-1);
		double desviacion = Math.sqrt(varianza);
		return desviacion;
	}
	
	public double calcularMedia(String producto, VentasSemanal ventas) {
		double suma = 0;
		int n = 0;
		for (int i = 0; i < ventas.getProductosVendidos().size(); i++) {
			if(producto.equals(ventas.getProductosVendidos().get(i).getNombre())) {
				suma+=ventas.getProductosVendidos().get(i).getCantidad();
				n++;
			}
		}
		double promedio =suma/n;
		return promedio;
	}
	
	@SuppressWarnings("deprecation")
	public static int calcularSemanaAnio(int mes, int dia) {
		Date date = new Date(119, mes - 1, dia);
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek( Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek( 1 );
		calendar.setTime(date);
		int semana = calendar.get(Calendar.WEEK_OF_YEAR);
		return semana;
	}
	
	@SuppressWarnings("deprecation")
	public static int calcularDiaSemana(int mes, int dia) {
		Date date = new Date(119, mes - 1, dia);
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return 0;
		}else {
			return calendar.get(Calendar.DAY_OF_WEEK) - 1;
		}
	}

	public static void leerDatos() {
		Scanner input = null;
		try {
			input = new Scanner(new File("datos/datosPrimarios.txt"));
	        while (input.hasNextLine()) {
	        	String[] lines = input.nextLine().split("\t");
	        	String fecha = lines[0];
	        	String[] fecha2 = fecha.split("/");
	        	int semana = calcularSemanaAnio(Integer.parseInt(fecha2[1]), Integer.parseInt(fecha2[0]));
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
	        	int diaSemana = calcularDiaSemana(Integer.parseInt(fecha.split("/")[1]), Integer.parseInt(fecha.split("/")[0]));
	        	if(diaSemana != 0) {
	        		ventas[semana-1].agregarProducto(new Producto(producto[0], Integer.parseInt(producto[1]), diaSemana));
	        	}
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
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Innova n = new Innova();
		leerDatos();
		for (int i = 0; i < ventas.length; i++) {
			System.out.println("Ventas semana "+(i+1)+" = "+ventas[i].getProductosVendidos().size());
			System.out.println("=========================================");
			for (int j = 0; j < ventas[i].getProductosVendidos().size(); j++) {
				System.out.println("PRODUCTO Día: "+ventas[i].getProductosVendidos().get(j).getDia()+" -> "+ventas[i].getProductosVendidos().get(j).getNombre()+" cantidad: "
						+ventas[i].getProductosVendidos().get(j).getCantidad());
			}
			System.out.println("=========================================");
		}
		System.out.println("======================= CVD DE PRODUCTO POR SEMANA ============================");
		for (int i = 0; i < ventas.length; i++) {
		
		}
	}

}
