package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Innova {
	
	private HashMap<String, HashMap<String, Integer>> mapProductos = new HashMap<String, HashMap<String, Integer>>();
	private VentasSemanal[] ventas;
	
	private Producto[] productos;
	
	public Innova() {
		ventas = new VentasSemanal[52];
		inicializarVentas();
		leerDatos();
		PrintStream original = System.out;
		try {
			PrintStream fileStream = new PrintStream("datos/reporte.txt");
			System.setOut(fileStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dividirProductos();
		productos = new Producto[57];
		leerDatosProductoExcel();
		punto1();
		punto2();
		punto3();
		System.setOut(original);
	}
	
	public void leerDatosProductoExcel() {
		BufferedReader br = null;
	    try {    
	    	br = new BufferedReader(new FileReader("datos/Inventario.csv"));
	        String line = br.readLine();
	        int n = 0;
	        while (null!=line) {
	        	String [] atributos = line.split(";");
	            String nombre = atributos[0];
	            int volumenAnual = Integer.parseInt(atributos[1]);
	            double costo = Double.parseDouble(atributos[2]);
	            int unidades = Integer.parseInt(atributos[3]);
	            productos[n] = new Producto(nombre, volumenAnual, costo, unidades);
	            n++;
	            line = br.readLine();
	         }
	    }catch (Exception ex) {
		  	ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<String> getSemana(String nomProd) {
		ArrayList<String> semanas = new ArrayList<String>();
		for (int i = 0; i < ventas.length; i++) {
			ArrayList<ProductoDiario> listaProductos = ventas[i].getProductosVendidos();
			for (int j = 0; j < listaProductos.size(); j++) {
				if (listaProductos.get(j).getNombre().equals(nomProd)) {
					semanas.add(ventas[i].getSemana());
					break;
				}
			}
		}
		return semanas;
	}
	
	private ArrayList<VentasSemanal> getSemanasF(String nomProd) {
		ArrayList<VentasSemanal> semanas = new ArrayList<VentasSemanal>();
		for (int i = 0; i < ventas.length; i++) {
			ArrayList<ProductoDiario> listaProductos = ventas[i].getProductosVendidos();
			for (int j = 0; j < listaProductos.size(); j++) {
				if (listaProductos.get(j).getNombre().equals(nomProd)) {
					semanas.add(ventas[i]);
					break;
				}
			}
		}
		return semanas;
	}
	
	public void inicializarVentas() {
		for (int i = 0; i < ventas.length; i++) {
			ventas[i] = new VentasSemanal(i+1+"", new ArrayList<ProductoDiario>());
		}
	}
	
	public String patron(double cvd, VentasSemanal ventas, String producto) {
		if(cvd >= 0 && cvd <= 0.1) {
			return "Horizontal";
		}else if(cvd > 1) {
			return "Errático";
		}else {
			return "Tendencia "+pendiente(ventas, producto);
		}
	}
	
	public String[] darProductos() {
		String[] productos = new String[33];
		int n = 0;
		for(String producto : mapProductos.keySet()) {
			productos[n] = producto;
			n++;
		}
		return productos;
	}
	
	public String pendiente(VentasSemanal ventas, String producto) {
		boolean flag = false;
		int x1 = 0;
		int x2 = 0;
		int y1 = 0;
		int y2 = 0;
		for (int i = 0; i < ventas.getProductosVendidos().size(); i++) {
			ProductoDiario p = ventas.getProductosVendidos().get(i);
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
	
	public double calcularCVD(VentasSemanal ventas, String producto) {
		return calcularDesviacionEstandar(ventas, producto) / calcularMedia(producto, ventas);
	}
	
	public double calcularDesviacionEstandar(VentasSemanal ventas, String producto) {
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
	public int calcularSemanaAnio(int mes, int dia) {
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
	public int calcularDiaSemana(int mes, int dia) {
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

	public void leerDatos() {
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
	        		ventas[semana-1].agregarProducto(new ProductoDiario(producto[0], Integer.parseInt(producto[1]), diaSemana));
	        	}
	        }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    } finally {
			input.close();
		}
	}
	
	public String identificarPronostico(String patron) {
		if(patron.equals("Tendencia Creciente") || patron.equals("Tendencia Decreciente")) {
			return "Proyección de tendencia o Suavización exponencial doble";
		}else if(patron.equals("Horizontal")){
			return "Promedio móvil simple o Promedio móvil ponderado o Suavización exponencial simple";
		}else {
			return "Métodos cualitativos";
		}
	}
	
	public HashMap<String, ProductoDiario> unificar(ArrayList<ProductoDiario> productos) {
		HashMap<String, ProductoDiario> hash = new HashMap<String, ProductoDiario>();
		for (int i = 0; i < productos.size(); i++) {
			if(!hash.containsKey(productos.get(i).getNombre())) {
				hash.put(productos.get(i).getNombre(), productos.get(i));
			}
		}
		return hash;
	}
	
	public void punto1() {
		Date objDate = new Date();
		String strDateFormat = "hh:mm:ss a  dd/MM/yyyy";
		SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
		System.out.println("Reporte hecho: "+objSDF.format(objDate));
		
		System.out.println("\n\n======================= VENTAS DE PRODUCTOS SEMANALES ============================\n\n");
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
	
	public void punto2(){
		System.out.println("\n\n======================= CVD DE PRODUCTO POR SEMANA ============================\n\n");
		for (int i = 0; i < ventas.length; i++) {
			HashMap<String, ProductoDiario> map = unificar(ventas[i].getProductosVendidos());
			System.out.println("Para la semana "+(i+1)+":");
			System.out.println("=========================================");
			for(ProductoDiario producto : map.values()) {
				double cvd = calcularCVD(ventas[i], producto.getNombre());
				String patron = patron(cvd, ventas[i], producto.getNombre());
				System.out.println("CVD para el producto "+producto.getNombre()+" es: "+cvd+" ("+patron+")");
				System.out.println("--> Se aconsejan los métodos de pronóstico: \n"+identificarPronostico(patron)+"\n");
			}
			System.out.println("=========================================");
		}
	}
	
	public HashMap<Integer, ProductoDiario> ordenarPorDia(int index, String nombreProducto){
		HashMap<Integer, ProductoDiario> map = new HashMap<Integer, ProductoDiario>();
		for (int l = 0; l < index + 1; l++) {
			for (int i = 0; i < ventas[l].getProductosVendidos().size(); i++) {
				ProductoDiario p = ventas[l].getProductosVendidos().get(i);
				if(p.getNombre().equals(nombreProducto)) {
					if(!map.containsKey(p.getDia())) {
						map.put(p.getDia(), p);
					}
				}
			}
		}
		
		return map;
	}
	
	public void dividirProductos() {
		for (int i = 0; i < ventas.length; i++) {
			HashMap<String, Integer> aux = new HashMap<String, Integer>();
			for (int j = 0; j < ventas[i].getProductosVendidos().size(); j++) {
				String nombreP = ventas[i].getProductosVendidos().get(j).getNombre();
				if(!mapProductos.containsKey(nombreP)) {
					aux.put(ventas[i].getSemana(), ventas[i].getProductosVendidos().get(j).getCantidad());
					mapProductos.put(nombreP, aux);				
				}else {
					if(!aux.containsKey(ventas[i].getSemana())) {
						aux = mapProductos.get(nombreP);
						aux.put(ventas[i].getSemana(), ventas[i].getProductosVendidos().get(j).getCantidad());
						mapProductos.put(nombreP, aux);
					}else {
						aux.put(ventas[i].getSemana(), aux.get(ventas[i].getSemana()) + ventas[i].getProductosVendidos().get(j).getCantidad());
						mapProductos.put(nombreP, aux);
					}	
				}
			}
		}
	}
	
	public double[] politicaNumerosEnterosConEOQ(String nombreProducto) {
		HashMap<String, Integer> aux = mapProductos.get(nombreProducto);
		double costoOrdenarPorPedido = 300;
		double costoMantenerPorPedido = 0.8;
		int[] demandas = new int[52], pedidos = new int[52], invFinal = new int[52], invInicial = new int[52];
		invInicial[0] = 0;
		int totalDemandas = 0, totalInvFinal = 0;
		for (int i = 0; i < ventas.length; i++) {
			if(aux.get((i+1)+"") != null) {
				demandas[i] = aux.get((i+1)+"");
				totalDemandas += demandas[i];
			}
		}
		
		int demandaPromedio = Math.round(totalDemandas/demandas.length);
		double eoq = Math.round(Math.sqrt((2*demandaPromedio*costoOrdenarPorPedido)/costoMantenerPorPedido));
		int periodos = (int) Math.ceil(eoq/demandaPromedio);
		System.out.println(periodos);
		for (int i = 0; i < demandas.length; i++) {
			if(i%periodos == 0) {
				for (int j = 0; j < periodos; j++) {
					if(i+j < demandas.length) {
						pedidos[i] = demandas[i] + demandas[i+j];
					}
				}
			}
		}
		
		for (int i = 0; i < pedidos.length; i++) {
			if(pedidos[i] != 0) {
				invFinal[i] = pedidos[i] - demandas[i];
			}else {
				invFinal[i] = 0;
			}
			totalInvFinal += invFinal[i];
		}
		
		int n = 0;
		for (int i = 0; i < pedidos.length; i++) {
			if(pedidos[i] > 0) {
				n++;
			}
		}
		
		for (int i = 1; i < invFinal.length; i++) {
			if(i % periodos == 0) {
				invInicial[i] = invFinal[i-(periodos-1)];
			}else {
				invInicial[i] = 0;
			}
		}
		int inventarioPromedio = 0;
		int rotacionInventario = 0;
		if(totalInvFinal != 0) {
			inventarioPromedio = Math.round(totalInvFinal/ventas.length);
			rotacionInventario = totalDemandas / inventarioPromedio;
		}
		
		
		double costoOrdenarTotal = costoOrdenarPorPedido * n;
		double costoMantenerTotal = costoMantenerPorPedido * totalInvFinal;
		double costoTotal = costoMantenerTotal + costoOrdenarTotal;
		double[] resultado = {totalInvFinal, inventarioPromedio, rotacionInventario, costoOrdenarTotal, costoMantenerTotal, costoTotal, eoq, demandaPromedio, periodos};
		return resultado;
	}
	
	public int demandaSemanal(String nombre, int initialWeek, int endWeek) {
		int demandaTotal = 0;
		for (int i = initialWeek; i < endWeek; i++) {
			ArrayList<ProductoDiario> prods = ventas[i].getProductosVendidos();
			for (int j = 0; j < prods.size(); j++) {
				if (prods.get(j).getNombre().equals(nombre)) {
					demandaTotal += prods.get(j).getCantidad();
				}
			}
		}
		return demandaTotal;
	}
	
	public double provisionPeriodica(String nomProd, int initialWeek, int endWeek, int percent) {
		double Z = 0;
		if(percent == 95) {
			Z = 1.644854;
		}else if(percent == 90 ) {
			Z = 1.28155157;
		}else if(percent == 98) {
			Z = 2.05374891;
		}else {
			return 0;
		}
		return demandaSemanal(nomProd, initialWeek, endWeek) * (8) + Z * calcularDesviacionEstandarProMasterMegaCool(getSemanasF(nomProd), nomProd, initialWeek, endWeek);
	}
	
	private double calcularDesviacionEstandarProMasterMegaCool(ArrayList<VentasSemanal> ventas, String nomProd, int initialWeek, int endWeek) {
		double varianza = 0;
		double sumatoria;
		int n = 0;
		for (int i = initialWeek; i <= endWeek; i++) {
			ArrayList<ProductoDiario> productos = ventas.get(i).getProductosVendidos(); 
			for (int j = 0; j < productos.size(); j++) {
				if (productos.get(j).getNombre().equals(nomProd)) {
					sumatoria = Math.pow(productos.get(j).getCantidad() - calcularMedia(productos.get(j).getNombre(), ventas.get(i)), 2);
					varianza += sumatoria;
					n++;
				}
			}
		}
		varianza/=(n);
		double desviacion = Math.sqrt(varianza);
		return desviacion;
	}
	
	public double[] proyeccionTendencia(String producto, int index) {
		HashMap<Integer, ProductoDiario> map = ordenarPorDia(index, producto);
		List<ProductoDiario> lista = new ArrayList<ProductoDiario>(map.values());
		if (lista.size() == 1) {
			double[] resultado = {lista.get(0).getCantidad(), 0};
			return resultado;
		} else if(lista.size() > 1) {
			double sumPeriodo = 0;
			double sumCantidad = 0;
			double sumxx = 0;
			double sumxy = 0;
				for (int i = 2; i < lista.size() - 1; i++) {
					sumPeriodo += lista.get(i).getDia();
					sumCantidad += lista.get(i).getCantidad();
					sumxx += sumPeriodo * sumPeriodo;
					sumxy += sumCantidad * lista.get(i).getDia();
				}
				double proPeriodo = sumPeriodo / lista.size();
				double proCantidad = sumCantidad / lista.size();
				double b = ((sumxy)-(lista.size()*proPeriodo*proCantidad))/(sumxx-(lista.size()*(proPeriodo*proPeriodo)));
				double a = proCantidad - b * proPeriodo;
				double prediccion = a+b*lista.size()+1;
				double[] resultado = {prediccion};
				return resultado;
		}else {
			double[] r = {0,0,0,0};
			return r;
		}
	}
	
	public double[] promedioMovilSimple(String producto, int index) {
		HashMap<Integer, ProductoDiario> map = ordenarPorDia(index, producto);
		List<ProductoDiario> lista = new ArrayList<ProductoDiario>(map.values());
		if (lista.size() == 1) {
			double[] resultado = {lista.get(0).getCantidad(), 0};
			return resultado;
			
		} else if(lista.size() > 1) {
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
			double[] resultado = {pronosticos[pronosticos.length - 1], (double)Math.round(mad * 100d) / 100d};
			return resultado;
		}else {
			double[] r = {0,0,0,0};
			return r;
		}
	}
	
	public double[] suavizacionExponencialSimple(String producto, int index) {
		HashMap<Integer, ProductoDiario> map = ordenarPorDia(index, producto);
		List<ProductoDiario> lista = new ArrayList<ProductoDiario>(map.values());
		if (lista.size() == 1) {
			double[] resultado = {lista.get(0).getCantidad(), 0, lista.get(0).getCantidad(), 0};
			return resultado;
		} else if(lista.size() > 1){	
			double[] pronosticos = new double[lista.size()+1];
			double alpha = 0.1;
			pronosticos[0] = lista.get(0).getCantidad();
			for (int i = 1; i < lista.size() + 1; i++) {
				pronosticos[i] = pronosticos[i-1] + alpha * (lista.get(i-1).getCantidad() - pronosticos[i-1]);
			}
			double sumMad = 0;
			for (int i = 0; i < pronosticos.length - 2; i++) {
				double er = Math.abs(pronosticos[i+1] - lista.get(i+1).getCantidad());
				sumMad += er;
			}
			double mad = sumMad / (pronosticos.length-2);
			
			double[] pronosticos2 = new double[lista.size()+1];
			double alpha2 = 0.3;
			pronosticos2[0] = lista.get(0).getCantidad();
			for (int i = 1; i < lista.size() + 1; i++) {
				pronosticos2[i] = pronosticos2[i-1] + alpha2 * (lista.get(i-1).getCantidad() - pronosticos2[i-1]);
			}
			double sumMad2 = 0;
			for (int i = 0; i < pronosticos2.length - 2; i++) {
				double er = Math.abs(pronosticos2[i+1] - lista.get(i+1).getCantidad());
				sumMad2 += er;
			}
			double mad2 = sumMad2 / (pronosticos2.length-2);
			
			double[] resultado = {pronosticos[pronosticos.length-1], (double)Math.round(mad * 100d) / 100d, pronosticos2[pronosticos2.length-1], (double)Math.round(mad2 * 100d) / 100d};
			return resultado;
		}else {
			double[] r = {0,0,0,0};
			return r;
		}
	}
	
	public double[] suavizacionExponencialDoble(String producto, int index) {
		HashMap<Integer, ProductoDiario> map = ordenarPorDia(index, producto);
		List<ProductoDiario> lista = new ArrayList<ProductoDiario>(map.values());
		if (lista.size() == 1) {
			double[] resultado = {lista.get(0).getCantidad(), 0};
			return resultado;
		} else if(lista.size() > 1) {	
			double alpha = 0.3;
			double beta = 0.7;
			double[] ft = new double[lista.size()];
			ft[0] = lista.get(0).getCantidad();
			double[] tt = new double[lista.size()];
			tt[0] = 2;
			double[] fift = new double[lista.size()];
			fift[0] = ft[0] + tt[0];
			
			for (int i = 1; i < lista.size(); i++) {
				ft[i] = alpha * lista.get(i-1).getCantidad() + (1 - alpha) * (ft[i-1] + tt[i-1]);
				tt[i] = beta * (ft[i] - ft[i-1]) + (1 - beta) * tt[i-1];
				fift[i] = ft[i] + tt[i];
			}
			
			double sumMad = 0;
			for (int i = 0; i < fift.length - 2; i++) {
				double er = Math.abs(fift[i] - lista.get(i).getCantidad());
				sumMad += er;
			}
			double mad = sumMad / (fift.length-2);
			
			double[] resultado = {(double)Math.round(fift[fift.length - 1] * 100d) / 100d, (double)Math.round(mad * 100d) / 100d};
			return resultado; 
		}else {
			double[] r = {0,0,0,0};
			return r;
		}
	}
	
	public double[] promedioMovilPonderado(String producto, int index) {
		HashMap<Integer, ProductoDiario> map = ordenarPorDia(index, producto);
		List<ProductoDiario> lista = new ArrayList<ProductoDiario>(map.values());
		if (lista.size() == 1) {
			double[] resultado = {lista.get(0).getCantidad(), 0};
			return resultado;
		} else if(lista.size() > 1){
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
			double[] resultado = {(double)Math.round(pronosticos[pronosticos.length - 1] * 100d) / 100d, (double)Math.round(mad * 100d) / 100d};
			return resultado;
		}else {
			double[] r = {0,0,0,0};
			return r;
		}
	}
	
	public void punto3() {
		System.out.println("\n\n======================= MÉTODOS DE PRONÓSTICO POR CADA PRODUCTO EN CADA SEMANA  ============================\n\n");
		for (int i = 0; i < ventas.length; i++) {
			HashMap<String, ProductoDiario> map = unificar(ventas[i].getProductosVendidos());
			System.out.println("|| Para la semana "+(i+1)+": ||");
			System.out.println("=========================================");
			for(ProductoDiario producto : map.values()) {
				System.out.println("- Para el producto: "+producto.getNombre());
				double cvd = calcularCVD(ventas[i], producto.getNombre());
				String patron = patron(cvd, ventas[i], producto.getNombre());
				if(patron.equals("Horizontal")) {
					System.out.println("\t<< Método de pronóstico Suavización Exponencial Simple>>");
					double[] suavizacionSimple = suavizacionExponencialSimple(producto.getNombre(), i);
					System.out.println("\t\tPara la semana "+(i+2)+" la cantidad de ventas será para un alfa de 0.1 son: "+suavizacionSimple[0]+" unidades y un MAD de: +"+suavizacionSimple[1]+". Para un alfa de 0.3 son: "+suavizacionSimple[2]+" unidades y un MAD de: "+suavizacionSimple[3]);
					System.out.println("\t<< Método de pronóstico Promedio Móvil Simple>>");
					double[] movilSimple = promedioMovilSimple(producto.getNombre(), i);
					System.out.println("\t\tPara la semana "+(i+2)+" la cantidad de ventas será para un N = 2, de:  "+movilSimple[0]+" unidades. MAD = "+movilSimple[1]);
					System.out.println("\t<< Método de pronóstico Promedio Móvil Ponderado>>");
					double[] movilPonderado = promedioMovilPonderado(producto.getNombre(), i);
					System.out.println("\t\tPara la semana "+(i+2)+" la cantidad de ventas será para un N = 2, de:  "+movilPonderado[0]+" unidades. MAD = "+movilPonderado[1]);
				}else if(patron.equals("Tendencia Creciente") || patron.equals("Tendencia Decreciente")) {
					System.out.println("\t<< Método de pronóstico Suavización Exponencial Doble>>");
					double suavizacionDoble[] = suavizacionExponencialDoble(producto.getNombre(), i);
					System.out.println("\t\tPara la semana "+(i+2)+" la cantidad de ventas será para un alfa de 0.3 y un beta de 0.7 son: "+suavizacionDoble[0]+" unidades. MAD = "+suavizacionDoble[1]);
					System.out.println("\t<< Método de Proyección de Tendencia>>");
					double tendencia[] = proyeccionTendencia(producto.getNombre(), i);
					System.out.println("\t\tPara la semana "+(i+2)+" la cantidad de ventas serán "+tendencia[0]+" unidades.");

				}
			}
			System.out.println("=========================================");
		}
		System.out.println("== Para los patrones erráticos se recomiendan seguir las diferentes técnicas y métodos de pronóstico ==" );
		System.out.println("\t<<Método de Delphi>>");
		System.out.println("\t\t- Consiste en la selección de un grupo de expertos a los que se les pregunta si opinión sobre cuestiones"
				+ " \n\t\treferidas a acontecimientos futuros. Las estimaciones de los expertos se realizan en sucesivas rondas anónimas con el"
				+ " \n\t\tnobjetivo de lograr un consenso, pero con la máxima autonomía por parte de los participantes. Es decir, el este método"
				+ " \n\t\tprocede por medio de la interrogación a expertos con la ayuda de cuestionarios sucesivos, a fin de poner en manifiesto"
				+ " \n\t\tconvergencias de opiniones y deducir eventuales consensos.\n");
		System.out.println("\t<<Jurado de Opinión Ejecutiva>>");
		System.out.println("\t\t- Se basa en la experiencia y los conocimientos técnicos de los altos mandos de la empresa para llegar a un consenso."
				+ "\n\t\tEs una de las más utilizadas cuando se requiere actuar con rapidez ante eventos no previstos o lanzamiento de nuevos productos.\n");
		System.out.println("\t<<Encuesta de Mercado de Consumo>>");
		System.out.println("\t\t- Consiste en obtener la opinión o percepción de un grupo de personas acerca de su proyección de consumo o interés por un producto o servicio.");
	}
	

}
