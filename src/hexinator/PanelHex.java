package hexinator;

import javax.swing.JTextPane;

public class PanelHex extends JTextPane {

	private static final long serialVersionUID = 4L;

	public PanelHex() {
		initGUI();
	}
	
	private void initGUI() {
		this.setEditable(false);
	}
	
}
