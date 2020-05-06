package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JTable;

import modelo.Producto;

@SuppressWarnings("serial")
public class DialogoABC extends JDialog {
	
	private InterfazInnova principal;
	private JTable table;
	private String[] nombresColumnas = {"Producto", "Volumen Anual", "Costo", "Unidades", "Volumen por Unidad", "Costo por Unidad", 
			"Volumen Anual $", "% Volumen anual $", "% Acumulado", "Clasificación ABC"}; 
	
	
	public DialogoABC(InterfazInnova principal) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.principal = principal;
		setSize(new Dimension(650,480));
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Método de ABC");
		setLayout(new BorderLayout());
	}
	
	public void inicializarTabla() {
		Producto[] productos = principal.getProductos();
		Object[][] matriz = new Object[57][10];
		for (int i = 0; i < productos.length; i++) {
			matriz[i][0] = productos[i].getNombre();
			matriz[i][1] = productos[i].getVolumenAnual();
			matriz[i][2] = productos[i].getCosto();
			matriz[i][3] = productos[i].getUnidades();
			matriz[i][4] = productos[i].getVolumenPorUnidad();
			matriz[i][5] = productos[i].getCostoPorUnidad();
			matriz[i][6] = productos[i].getVolumenAnualDinero();
			matriz[i][7] = productos[i].getPorcentajeVolumenAnualDinero();
			matriz[i][8] = productos[i].getAcumuladoPorcentaje();
			matriz[i][9] = productos[i].getClasificacion();
		}
	}
	
	@Override
	public void dispose() {
		System.exit(0);
	}

}
