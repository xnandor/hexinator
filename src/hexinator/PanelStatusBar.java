package hexinator;

import java.awt.Dimension;

import javax.swing.JPanel;

public class PanelStatusBar extends JPanel {

	private static final long serialVersionUID = 5L;
	
	public PanelStatusBar() {
		initGUI();
	}
	
	private void initGUI() {
		this.setPreferredSize(new Dimension(640, 24));
	}

}
