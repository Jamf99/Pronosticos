package modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	
	public static String patron(double cvd, VentasSemanal ventas, String producto) {
		if(cvd >= 0 && cvd <= 0.1) {
			return "Horizontal";
		}else if(cvd > 1) {
			return "Errático";
		}else {
			return "Tendencia "+pendiente(ventas, producto);
		}
	}
	
	public static String pendiente(VentasSemanal ventas, String producto) {
		boolean flag = false;
		int x1 = 0;
		int x2 = 0;
		int y1 = 0;
		int y2 = 0;
		for (int i = 0; i < ventas.getProductosVendidos().size(); i++) {
			Producto p = ventas.getProductosVendidos().get(i);
			if(producto.equals(p.getNombre()) && !flag) {
				x1 = p.getDia();
				y1 = p.getCantidad();
				flag = true;
			}else {
				x2 =  p.getDia();
				y2 = p.getCantidad();
			}
		}
		double pendiente = (y2-y1)/(x2-x1);
		if(pendiente >= 0) {
			return "Creciente";
		}else {
			return "Decreciente";
		}
	}
	
	public static double calcularCVD(VentasSemanal ventas, String producto) {
		return calcularDesviacionEstandar(ventas, producto) / calcularMedia(producto, ventas);
	}
	
	public static double calcularDesviacionEstandar(VentasSemanal ventas, String producto) {
		double varianza = 0;
		double sumatoria;
		int n = 0;
		for (int i = 0; i < ventas.getProductosVendidos().size(); i++) {
			if(producto.equals(ventas.getProductosVendidos().get(i).getNombre())) {
				sumatoria = Math.pow(ventas.getProductosVendidos().get(i).getCantidad() - calcularMedia(producto, ventas), 2);
				varianza += sumatoria;
				n++;
			}
		}
		varianza/=(n);
		double desviacion = Math.sqrt(varianza);
		return desviacion;
	}
	
	public static double calcularMedia(String producto, VentasSemanal ventas) {
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
		if(mes == 12 && dia == 30) {
			semana = 52;
		}
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
	
	public static String identificarPronostico(String patron) {
		if(patron.equals("Tendencia Creciente") || patron.equals("Tendencia Decreciente")) {
			return "Proyección de tendencia o Suavización exponencial doble";
		}else if(patron.equals("Horizontal")){
			return "Promedio móvil simple o Promedio móvil ponderado o Suavización exponencial simple";
		}else {
			return "Métodos cualitativos";
		}
	}
	
	public static HashMap<String, Producto> unificar(ArrayList<Producto> productos) {
		HashMap<String, Producto> hash = new HashMap<String, Producto>();
		for (int i = 0; i < productos.size(); i++) {
			if(!hash.containsKey(productos.get(i).getNombre())) {
				hash.put(productos.get(i).getNombre(), productos.get(i));
			}
		}
		return hash;
	}
	
	public static void punto1() {
		System.out.println("======================= VENTAS DE PRODUCTOS SEMANALES ============================");
		for (int i = 0; i < ventas.length; i++) {
			System.out.println("Ventas semana "+(i+1)+" = "+ventas[i].getProductosVendidos().size());
			System.out.println("=========================================");
			for (int j = 0; j < ventas[i].getProductosVendidos().size(); j++) {
				System.out.println("PRODUCTO Día: "+ventas[i].getProductosVendidos().get(j).getDia()+" -> "+ventas[i].getProductosVendidos().get(j).getNombre()+" cantidad: "
						+ventas[i].getProductosVendidos().get(j).getCantidad());
			}
			System.out.println("=========================================");
		}
	}
	
	public static void punto2(){
		System.out.println("======================= CVD DE PRODUCTO POR SEMANA ============================");
		for (int i = 0; i < ventas.length; i++) {
			HashMap<String, Producto> map = unificar(ventas[i].getProductosVendidos());
			System.out.println("Para la semana "+(i+1)+":");
			System.out.println("=========================================");
			for(Producto producto : map.values()) {
				double cvd = calcularCVD(ventas[i], producto.getNombre());
				String patron = patron(cvd, ventas[i], producto.getNombre());
				System.out.println("CVD para el producto "+producto.getNombre()+" es: "+cvd+" ("+patron+")");
				System.out.println("--> Se aconsejan los métodos de pronóstico: \n"+identificarPronostico(patron)+"\n");
			}
			System.out.println("=========================================");
		}
	}
	
	public static HashMap<Integer, Producto> ordenarPorDia(ArrayList<Producto> productos){
		HashMap<Integer, Producto> map = new HashMap<Integer, Producto>();
		for (int i = 0; i < productos.size(); i++) {
			if(!map.containsKey(productos.get(i).getDia())) {
				map.put(productos.get(i).getDia(), productos.get(i));
			}
		}
		return map;
	}
	
	public static double[] exponencialSimple(VentasSemanal ventas, String producto) {
		HashMap<Integer, Producto> map = ordenarPorDia(ventas.getProductosVendidos());
		List<Producto> lista = new ArrayList<Producto>(map.values());
		ArrayList<Double> pronosticos1 = new ArrayList<Double>();
		ArrayList<Double> pronosticos2 = new ArrayList<Double>();
		boolean flag1 = false;
		boolean flag2 = false;
		double alfa2 = 0.3;
		double alfa1 = 0.1;
		for (int i = 0; i < lista.size(); i++) {
			Producto p = lista.get(i);
			if(p.getNombre().equals(producto) && !flag1) {
				pronosticos1.add((double) p.getCantidad());
				flag1 = true;
			}else if(flag1 && p.getNombre().equals(producto) ) {
				int tamanio = pronosticos1.size();
				int posicion = i - tamanio;
				int resultado = i - (posicion+1);
				pronosticos1.add(pronosticos1.get(resultado)+alfa1*(p.getCantidad()-pronosticos1.get(resultado)));
			}
		}
		for (int i = 0; i < lista.size(); i++) {
			Producto p = lista.get(i);
			if(p.getNombre().equals(producto) && !flag2) {
				pronosticos2.add((double) p.getCantidad());
				flag2 = true;
			}else if(flag2 && p.getNombre().equals(producto) ) {
				int tamanio = pronosticos2.size();
				int posicion = i - tamanio;
				int resultado = i - (posicion+1);
				pronosticos2.add(pronosticos2.get(resultado)+alfa2*(p.getCantidad()-pronosticos2.get(resultado)));
			}
		}
		double pronosticoAlfa1 = pronosticos1.get(pronosticos1.size() - 1);
		double pronosticoAlfa2 = pronosticos2.get(pronosticos2.size() - 1);
		double[] resultado = {pronosticoAlfa1, pronosticoAlfa2};
		return resultado;
		
	}
	
	public double[] promedioMovilSimple(VentasSemanal ventas, String producto) {
		HashMap<Integer, Producto> map = ordenarPorDia(ventas.getProductosVendidos());
		List<Producto> lista = new ArrayList<Producto>(map.values());
		if (lista.size() > 2) {
			double[] resultado = {lista.get(0).getCantidad(), 0};
			return resultado;
		} else {
			double[] pronosticos = new double[lista.size()-1];
			for (int i = 2; i < lista.size() + 1; i++) {
				int cantidad1 = lista.get(i-2).getCantidad();
				int cantidad2 = lista.get(i-1).getCantidad();
				pronosticos[i - 2] = (cantidad1 + cantidad2) / 2;
			}
			double sumMad = 0;
			for (int i = 0; i < pronosticos.length-1; i++) {
				sumMad += Math.abs(pronosticos[i] - lista.get(i+2).getCantidad());
			}
			double mad = sumMad / (pronosticos.length-1);
			double[] resultado = {pronosticos[pronosticos.length - 1], mad};
			return resultado;
		}
	}
	
	public double[] suavizacionExponencialDoble(VentasSemanal ventas, String producto) {
		HashMap<Integer, Producto> map = ordenarPorDia(ventas.getProductosVendidos());
		List<Producto> lista = new ArrayList<Producto>(map.values());
		double[] pronosticos = new double[lista.size()+1];
		double alpha = 0.2;
		pronosticos[0] = lista.get(0).getCantidad();
		for (int i = 1; i < lista.size() + 1; i++) {
			pronosticos[i] = pronosticos[i-1] + alpha * (lista.get(i-1).getCantidad() - pronosticos[i-1]);
		}
		double sumMad = 0;
		for (int i = 0; i < pronosticos.length - 2; i++) {
			double pr = pronosticos[i+1];
			double li = lista.get(i+1).getCantidad();
			double er = Math.abs(pr - li);
			sumMad += er;
		}
		double mad = sumMad / (pronosticos.length-2);
		double[] resultado = {pronosticos[pronosticos.length - 1], mad};
		return resultado;
	}
	
	public double[] promedioMovilPonderado(VentasSemanal ventas, String producto) {
		HashMap<Integer, Producto> map = ordenarPorDia(ventas.getProductosVendidos());
		List<Producto> lista = new ArrayList<Producto>(map.values());
		double[] pronosticos = new double[lista.size()-1];
		for (int i = 2; i < lista.size()+1; i++) {
			double l1 = lista.get(i-2).getCantidad() * 0.4;
			double l2 = lista.get(i-1).getCantidad() * 0.6;
			pronosticos[i-2] = l1 + l2;
		}
		double sumMad = 0;
		for (int i = 0; i < pronosticos.length - 1; i++) {
			double pr = pronosticos[i];
			double li = lista.get(i+2).getCantidad();
			double er = Math.abs(pr - li);
			sumMad += er;
		}
		double mad = sumMad / (pronosticos.length-2);
		double[] resultado = {pronosticos[pronosticos.length - 1], mad};
		return resultado;
	}
	
	public static void punto3() {
		System.out.println("======================= MÉTODOS DE PRONÓSTICO POR CADA PRODUCTO EN CADA SEMANA  ============================");
		for (int i = 0; i < ventas.length; i++) {
			HashMap<String, Producto> map = unificar(ventas[i].getProductosVendidos());
			System.out.println("|| Para la semana "+(i+1)+": ||");
			System.out.println("=========================================");
			for(Producto producto : map.values()) {
				System.out.println("- Para el producto: "+producto.getNombre());
				double cvd = calcularCVD(ventas[i], producto.getNombre());
				String patron = patron(cvd, ventas[i], producto.getNombre());
				if(patron.equals("Horizontal")) {
					System.out.println("\t<< Método de pronóstico Suavización Exponencial Simple>>");
					double alfa1 = exponencialSimple(ventas[i], producto.getNombre())[0];
					double alfa2 = exponencialSimple(ventas[i], producto.getNombre())[1];
					System.out.println("\t\tPara la semana "+(i+2)+" la cantidad de ventas será para alfa1: "+alfa1+" unidades y para alfa2: "+alfa2+" unidades");
					System.out.println("\t<< Método de pronóstico Promedio Móvil Simple>>");
					System.out.println("\t<< Método de pronóstico Promedio Móvil Ponderado>>");
				}
			}
			System.out.println("=========================================");
		}
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Innova n = new Innova();
		leerDatos();
		punto1();
		punto2();
		punto3();
	}

}
