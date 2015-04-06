package hexinator;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PanelHex extends JTextPane implements DocumentListener, HierarchyBoundsListener, PropertyChangeListener, SettingsListener {

	private static final long serialVersionUID = 4L;
	
	private PanelText source;
	private int characterWidth = 0;
	private int charactersPerLine = 0;
	private String octetDelimiter = " ";
	private String addressDelimiter = " | ";
	private int bytesPerLine = 16;
	private byte[] bytes;

	public PanelHex(PanelText pane) {
		this.source = pane;
		initGUI();
	}
	
	private void initGUI() {
		this.setEditable(false);
		this.addHierarchyBoundsListener(this);
	}
		
	private void refreshCharactersPerLine() {
		Graphics g = this.getGraphics();
		FontMetrics fontMetrics = g.getFontMetrics();
		characterWidth = fontMetrics.stringWidth("A");
		int width = this.getWidth();
		charactersPerLine = width/characterWidth;
	}
	
	public void refreshText() {
		long address = 0L;
		StringBuilder sb = new StringBuilder();
		bytes = source.getBytes();
		for (byte b : bytes) {
			if ((address % bytesPerLine) == 0) {
				sb.append("\n0x");
				String addressString = String.format("%H", address);
				for (int i = bytesPerLine - addressString.length(); i > 0; i--) {
					sb.append("0");
				}
				sb.append(addressString);
				sb.append(addressDelimiter);
			}
			String s = String.format("%h", b);
			while (s.length() > 2) {
					s = s.replaceAll("^.", "");
			}
			if (s.length() == 1) {
				sb.append("0");
			}
			sb.append(s);
			sb.append(octetDelimiter);
			address++;
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(0);
		}
		String finalString = sb.toString();
		this.setText(finalString);
		int numCharsOnFirstLine = finalString.indexOf("\n", 1);
		if (numCharsOnFirstLine == -1) {
			numCharsOnFirstLine = finalString.length();
		}
		while (numCharsOnFirstLine < charactersPerLine) {
			Font font = this.getFont();
			int size = font.getSize();
			this.setFont(new Font(font.getName(), font.getStyle(), size+1));
			refreshCharactersPerLine();
		}
		while (numCharsOnFirstLine > charactersPerLine) {
			Font font = this.getFont();
			int size = font.getSize();
			this.setFont(new Font(font.getName(), font.getStyle(), size-1));
			refreshCharactersPerLine();
		}
	}
	@Override
	public void ancestorMoved(HierarchyEvent e) {
		
	}

	@Override
	public void ancestorResized(HierarchyEvent e) {
		refreshCharactersPerLine();
		refreshText();
	}
	
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
		refreshText();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		refreshText();
	}

	@Override
	public void settingsChanged() {
		refreshText();
	}	
}
