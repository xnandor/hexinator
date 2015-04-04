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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author ericalanbischoff
 *
 */
public class Hexinator extends JFrame {

	private static final long serialVersionUID = 1L;
	
	protected PanelToolbar toolbar = new PanelToolbar();
	protected PanelStatusBar statusBar = new PanelStatusBar();
	protected PanelUnicode unicode = new PanelUnicode();
	protected JPanel unicodeNoWrapPanel = new JPanel(new BorderLayout());
	protected JSplitPane splitPane;
	protected JScrollPane unicodeScroll;
	protected PanelHex hex = new PanelHex();
	protected JScrollPane hexScroll;
	protected Dimension minimumSize = new Dimension(1080,720);
	
	private Container body = this.getContentPane();

	/**
	 * @throws HeadlessException
	 */
	public Hexinator() {
		initGUI();
	}


	private void initGUI() {
		
		BorderLayout layout = new BorderLayout();
		body.setLayout(layout);
		this.setTitle("Hexinator");
		this.setMinimumSize(minimumSize);
		this.setPreferredSize(minimumSize);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		populateGUI();
		finalizeGUI();
	}

	private void populateGUI() {
		//Common
		toolbar.setPreferredSize(new Dimension(640, 40));
		statusBar.setPreferredSize(new Dimension(640, 24));
		unicode.setText("");
		unicode.setFont(new Font("Courier New", Font.PLAIN, 14));
		unicode.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				changed(e);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				changed(e);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				changed(e);
			}
			private void changed(DocumentEvent e) {
				String text = unicode.getText();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < text.length(); i++) {
					char c = text.charAt(i);
					sb.append(String.format("%H ", c));
				}
				hex.setText(sb.toString());
			}
		});
		hex.setMinimumSize(new Dimension(200, 400));
		hex.setFont(new Font("Courier New", Font.PLAIN, 14));
		body.add(toolbar, BorderLayout.PAGE_START);
		unicodeNoWrapPanel.add(unicode);
		unicodeScroll = new JScrollPane(unicodeNoWrapPanel);
		hexScroll = new JScrollPane(hex);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, unicodeScroll, hexScroll);
		splitPane.setOneTouchExpandable(true);
		splitPane.setMinimumSize(new Dimension(640, 100));
		splitPane.setDividerLocation(this.getWidth()/2);
		splitPane.setResizeWeight(0.5);
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




