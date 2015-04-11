/**
 * 
 */
package hexinator;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * @author ericalanbischoff
 *
 */
public class Hexinator extends JFrame {

	private static final long serialVersionUID = 1L;
	
	protected PanelToolbar toolbar = new PanelToolbar();
	protected PanelStatusBar statusBar = new PanelStatusBar();
	protected PanelText textPanel = new PanelText();
	protected JSplitPane splitPane;
	protected JScrollPane textScroll;
	protected PanelHex hexPanel = new PanelHex(textPanel);
	protected JScrollPane hexScroll;
	protected Dimension minimumSize = new Dimension(320,240);
	protected Dimension preferredSize = new Dimension(1080, 720);
	
	private Container body = this.getContentPane();

	/**
	 * @throws HeadlessException
	 */
	public Hexinator() {
		Settings.loadStaticClass();
		initGUI();
	}

	private void initGUI() {
		BorderLayout layout = new BorderLayout();
		body.setLayout(layout);
		this.setTitle("Hexinator");
		this.setMinimumSize(minimumSize);
		this.setPreferredSize(preferredSize);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		populateGUI();
		finalizeGUI();
	}

	private void populateGUI() {
		textPanel.getDocument().addDocumentListener(hexPanel);
		textScroll = new JScrollPane(textPanel);
		hexScroll = new JScrollPane(hexPanel);
		Settings.addSettingsListener(textPanel);
		Settings.addSettingsListener(hexPanel);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScroll, hexScroll);
		splitPane.setOneTouchExpandable(true);
		splitPane.setMinimumSize(new Dimension(640, 100));
		splitPane.setDividerLocation(0.5);
		splitPane.setResizeWeight(0.5);
		body.add(toolbar, BorderLayout.PAGE_START);
		body.add(splitPane, BorderLayout.CENTER);
		body.add(statusBar, BorderLayout.PAGE_END);
	}
	
	private void finalizeGUI() {
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Hexinator();		
			}
		});
	}

}




