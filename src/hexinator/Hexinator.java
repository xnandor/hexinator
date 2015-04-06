/**
 * 
 */
package hexinator;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * @author ericalanbischoff
 *
 */
public class Hexinator extends JFrame {

	private static final long serialVersionUID = 1L;
	
	protected PanelToolbar toolbar = new PanelToolbar();
	protected PanelStatusBar statusBar = new PanelStatusBar();
	protected PanelText textPanel = new PanelText();
	protected JPanel texNoWrapPanel = new JPanel(new BorderLayout());
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
		toolbar.setPreferredSize(new Dimension(640, 40));
		
		statusBar.setPreferredSize(new Dimension(640, 24));
		
		textPanel.setText("");
		textPanel.setFont(new Font("Courier New", Font.PLAIN, 14));
		textPanel.getDocument().addDocumentListener(hexPanel);
		
		hexPanel.setMinimumSize(new Dimension(200, 400));
		hexPanel.setFont(new Font("Courier New", Font.PLAIN, 14));
		hexPanel.setText("");
		
		texNoWrapPanel.add(textPanel);
		textScroll = new JScrollPane(texNoWrapPanel);
		hexScroll = new JScrollPane(hexPanel);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScroll, hexScroll);
		splitPane.setOneTouchExpandable(true);
		splitPane.setMinimumSize(new Dimension(640, 100));
		splitPane.setDividerLocation(0.5);
		splitPane.setResizeWeight(0.5);
		
		Settings.addSettingsListener(textPanel);
		Settings.addSettingsListener(hexPanel);
		
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
		new Hexinator();
	}

}




