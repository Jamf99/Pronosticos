package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import modelo.Producto;

@SuppressWarnings("serial")
public class DialogoABC extends JDialog implements ActionListener{
	
	public static final String VOLVER = "Volver";
	
	private InterfazInnova principal;
	private JTable table, ABC;
	private String[] nombresColumnas = {"Producto", "Volumen Anual", "Costo", "Unidades", "Volumen por Unidad", "Costo por Unidad", 
			"Volumen Anual $", "% Volumen anual $", "% Acumulado", "Clasificación ABC"}; 
	private String[] nombresColumnas2 = {"Clasificación", "Unidades", "Porcentaje"};
	private JScrollPane scroll, scrollABC;	
	private JButton butVolver;
	
	public DialogoABC(InterfazInnova principal) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.principal = principal;
		setSize(new Dimension(1300,650));
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Inventario Productos - ABC");
		setLayout(new BorderLayout());
		
		butVolver = new JButton(VOLVER);
		butVolver.setActionCommand(VOLVER);
		butVolver.addActionListener(this);
		
		table = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel(inicializarTabla(), nombresColumnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		table.setModel(tableModel);
		scroll = new JScrollPane(table);
		
		JPanel panelProductos = new JPanel();
		TitledBorder border3 = BorderFactory.createTitledBorder("Tabla de productos");
		border3.setTitleColor(Color.BLUE);
		panelProductos.setBorder(border3);
		panelProductos.setLayout(new GridLayout(1,1));
		panelProductos.add(scroll);
		panelProductos.setPreferredSize(new Dimension(0, 450));
		
		JPanel clasificacionABC = new JPanel();
		TitledBorder border = BorderFactory.createTitledBorder("Clasificación ABC");
		border.setTitleColor(Color.BLUE);
		clasificacionABC.setBorder(border);
		clasificacionABC.setLayout(new GridLayout(1,1));
		
		ABC = new JTable();
		DefaultTableModel tableModel2 = new DefaultTableModel(tablaABC(), nombresColumnas2) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		ABC.setModel(tableModel2);
		scrollABC = new JScrollPane(ABC);
		
		clasificacionABC.add(scrollABC);
		
		JPanel opciones = new JPanel();
		TitledBorder border2 = BorderFactory.createTitledBorder("Opciones");
		border2.setTitleColor(Color.BLUE);
		opciones.setBorder(border2);
		opciones.setLayout(new GridLayout(1,1));
		opciones.add(butVolver);
		
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(250);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 1; i < nombresColumnas.length; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(tcr);
			
		}
		for (int j = 0; j < nombresColumnas2.length; j++) {
			ABC.getColumnModel().getColumn(j).setCellRenderer(tcr);
		}
		
		add(panelProductos, BorderLayout.NORTH);
		add(clasificacionABC, BorderLayout.CENTER);
		add(opciones, BorderLayout.SOUTH);
	}
	
	public Object[][] inicializarTabla() {
		NumberFormat porcentaje = NumberFormat.getPercentInstance();
		porcentaje.setMinimumFractionDigits(3);
		NumberFormat numero = NumberFormat.getNumberInstance();
		numero.setMaximumFractionDigits(3);
		NumberFormat pesos = NumberFormat.getCurrencyInstance();
		pesos.setMinimumFractionDigits(3);
		Producto[] productos = principal.getProductos();
		Object[][] matriz = new Object[58][10];
		for (int i = 0; i < productos.length; i++) {
			matriz[i][0] = productos[i].getNombre();
			matriz[i][1] = numero.format(productos[i].getVolumenAnual());
			matriz[i][2] = pesos.format(productos[i].getCosto());
			matriz[i][3] = numero.format(productos[i].getUnidades());
			matriz[i][4] = numero.format(productos[i].getVolumenPorUnidad());
			matriz[i][5] = pesos.format(productos[i].getCostoPorUnidad());
			pesos.setMinimumFractionDigits(0);
			matriz[i][6] = pesos.format(productos[i].getVolumenAnualDinero());
			matriz[i][7] = porcentaje.format(productos[i].getPorcentajeVolumenAnualDinero());
			matriz[i][8] = porcentaje.format(productos[i].getAcumuladoPorcentaje());
			matriz[i][9] = productos[i].getClasificacion();
		}
		
		matriz[57][0] = "TOTAL";
		matriz[57][1] = numero.format(principal.darTotalVolumenAnual());
		matriz[57][4] = numero.format(principal.darTotalVolumenPorUnidad());
		matriz[57][5] = pesos.format(principal.darTotalVolumenAnualDinero());
		matriz[57][7] = porcentaje.format(principal.darTotalPorcentaje());
		
		return matriz;
	}
	
	public Object[][] tablaABC(){
		NumberFormat porcentaje = NumberFormat.getPercentInstance();
		porcentaje.setMinimumFractionDigits(3);
		NumberFormat numero = NumberFormat.getNumberInstance();
		numero.setMaximumFractionDigits(3);
		Object[][] matriz = {
				{"A", numero.format(principal.darRepresentacionUnidadesABC()[0]), porcentaje.format(principal.darRepresentacionPorcentajesABC()[0])},
				{"B", numero.format(principal.darRepresentacionUnidadesABC()[1]), porcentaje.format(principal.darRepresentacionPorcentajesABC()[1])},
				{"C", numero.format(principal.darRepresentacionUnidadesABC()[2]), porcentaje.format(principal.darRepresentacionPorcentajesABC()[2])},
				{"TOTAL", numero.format(principal.darRepresentacionUnidadesABC()[3]), porcentaje.format(principal.darRepresentacionPorcentajesABC()[3])},
		};
		return matriz;
	}
	
	@Override
	public void dispose() {
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		String comando = evento.getActionCommand();
		if(comando.equals(VOLVER)) {
			principal.cerrarDialogo();
		}
	}

}
