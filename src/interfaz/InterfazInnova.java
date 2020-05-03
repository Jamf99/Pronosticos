package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import modelo.Innova;

@SuppressWarnings("serial")
public class InterfazInnova extends JFrame implements ActionListener, ListSelectionListener{
	
	public static final String POLITICA_NUMEROS_ENTEROS = "Política números enteros";
	public static final String PROVISION_PERIODICA = "Provisión periódica";
	
	private JButton butPoliticaNum, butProvisionPeriodica;
	private JList<String> listaProductos;
	private JScrollPane scroll;
	
	private Innova modelo;
	String prodSeleccionado = "";
	int indexProd = 0;
	
	public InterfazInnova() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(650,480));
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Inventarios - Innova");
		setLayout(new BorderLayout());
		
		modelo = new Innova();
		
		butPoliticaNum = new JButton(POLITICA_NUMEROS_ENTEROS);
		butPoliticaNum.setActionCommand(POLITICA_NUMEROS_ENTEROS);
		butPoliticaNum.addActionListener(this);
		butProvisionPeriodica = new JButton(PROVISION_PERIODICA);
		butProvisionPeriodica.setActionCommand(PROVISION_PERIODICA);
		butProvisionPeriodica.addActionListener(this);
		
		JPanel panelListaProductos = new JPanel();
		Font font = new Font("Verdana", Font.BOLD, 15);
		TitledBorder border = BorderFactory.createTitledBorder("Lista de productos");
		border.setTitleColor(Color.BLUE);
		border.setTitleFont(font);
		panelListaProductos.setBorder(border);
		panelListaProductos.setLayout(new BorderLayout());
		panelListaProductos.setPreferredSize(new Dimension(0,400));
		
		listaProductos = new JList<String>(modelo.darProductos());
		scroll = new JScrollPane(listaProductos);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listaProductos.addListSelectionListener(this);
		panelListaProductos.add(scroll);
		
		JPanel aux3 = new JPanel();
		TitledBorder border2 = BorderFactory.createTitledBorder("Opciones");
		border2.setTitleColor(Color.BLUE);
		aux3.setBorder(border2);
		aux3.setLayout(new GridLayout(1,2));
		aux3.add(butPoliticaNum);
		aux3.add(butProvisionPeriodica);
		
		add(panelListaProductos, BorderLayout.NORTH);
		add(aux3, BorderLayout.SOUTH);
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){	
		}
	}
	
	public void politicaNumerosEnteros() {
		double[] resultados = modelo.politicaNumerosEnterosConEOQ(prodSeleccionado);
		String mensaje = "<< "+prodSeleccionado+" >>\n\t- Total Inventario Final: "+resultados[0]+"\n\t- Inventario Promedio: "+resultados[1]+"\n\t- Rotación Inventario: "+
				resultados[2]+"\n\t- Costo ordenar total: "+resultados[3]+"\n\t- Costo mantener total: "+resultados[4]+
				"\n\t- Costo total: "+resultados[5]+"\n\t- EOQ: "+resultados[6]+"\n\t- Demanda promedio: "+resultados[7]+
				"\n\t- Periodos: "+resultados[8];
		JOptionPane.showMessageDialog(this, mensaje);
	}
	
	public void provisionPeriodica() {
		ArrayList<String> semanas = modelo.getSemana(prodSeleccionado);
		
		String mensajeSemanas = "";
		for (int i = 0; i < semanas.size(); i++) {
			mensajeSemanas += (i+1)+". Semana "+semanas.get(i)+"\n";
		}
		int semanaInicio = Integer.parseInt(JOptionPane.showInputDialog(this, mensajeSemanas));
		int semanaFin = Integer.parseInt(JOptionPane.showInputDialog(this, mensajeSemanas));
		int nivelConfianza = Integer.parseInt(JOptionPane.showInputDialog(this, "Escriba el nivel de confianza deseado (90%, 95% o 98%)"));
		int resultado = (int) modelo.provisionPeriodica(prodSeleccionado, semanaInicio-1, semanaFin-1, nivelConfianza);
		JOptionPane.showMessageDialog(this, "La provisión periódica es: "+resultado);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		String comando = evento.getActionCommand();
		if(comando.equals(POLITICA_NUMEROS_ENTEROS)) {
			politicaNumerosEnteros();
		}else {
			provisionPeriodica();
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		prodSeleccionado = listaProductos.getSelectedValue();
		indexProd = listaProductos.getSelectedIndex();
	}
	
	public static void main(String[] args) {
		InterfazInnova main = new InterfazInnova();
		main.setVisible(true);
	}

}
