package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;

@SuppressWarnings("serial")
public class DialogoABC extends JDialog {
	
	private InterfazInnova principal;
	
	public DialogoABC(InterfazInnova principal) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.principal = principal;
		setSize(new Dimension(650,480));
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Método de ABC");
		setLayout(new BorderLayout());
	}
	
	@Override
	public void dispose() {
		System.exit(0);
	}

}
