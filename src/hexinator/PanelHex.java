package hexinator;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class PanelHex extends JTextArea implements DocumentListener, HierarchyBoundsListener, SettingsListener {

	private static final long serialVersionUID = 4L;
	
	private int characterWidth = 0;
	private int charactersPerLine = 0;
	private String octetDelimiter = " ";
	private String addressDelimiter = ": ";
	private int bytesPerLine = 16;
	private Vector<Byte> bytes = new Vector<Byte>();

	public PanelHex(PanelText pane) {
		initGUI();
	}
	
	private void initGUI() {
		this.setEditable(true);
		this.addHierarchyBoundsListener(this);
		this.setFont(new Font("Courier New", Font.PLAIN, 14));
		this.setText("");
	}

	public String byteToHexString(byte b) {
		String s = String.format("%h", b);
		while (s.length() > 2) {
				s = s.replaceAll("^.", "");
		}
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}
	
	public int stringLocationToByteLocation(int sl) {
		int asl = getAddressStringLength() + 1 + 2 + addressDelimiter.length(); // one for '\n' character. two for "0x"
		int w = ( asl + (3*(bytesPerLine)));
		int x = sl % w;
		int y = (sl - x) / w;
		x -= asl-1;
		x /= 3;
		int bl = (y*(bytesPerLine)) + x;
		return bl;
	}

	public int byteLocationToStringLocation(int bl) {
		int asl = getAddressStringLength() + 1 + 2 + addressDelimiter.length(); // one for '\n' character. two for "0x"
		int x = (bl % bytesPerLine);
		int y = (bl - x) / bytesPerLine;
		int sl;
		if (y == 0) {
			sl = (x*3) + (asl-1);
		} else {
			sl = (x*3) + (asl-1) + (asl+bytesPerLine*3) * (y);
		}
		return sl;
	}

	public String longToAddressString(long address) {
		String s = "";
		int addressSize = getAddressStringLength(); 		
		s = s + "0x";
		String addressString = String.format("%H", address);
		for (int i = addressSize - addressString.length(); i > 0; i--) {
			s = s +"0";
		}
		s = s + addressString;
		return s;
	}

	
	/**
	 * appendByte(Byte b) : void
	 * 	 This method is an optimization for adding bytes the Hex Panel.
	 * Normally when bytes are added the entire panel must be refreshed.
	 * This is problematic when the amount of data you are dealing with
	 * is huge. For most use cases the hex dump has bytes added to it
	 * at the end. This case is optimized by only adding text to
	 * the end of the hex panel instead of refreshing the entire thing.
	 * 
	 * @param b - This 
	 */
	public void appendByte(Byte b) {
		int x, size;
		size = bytes.size();
		x = ((size+bytesPerLine-1) % bytesPerLine);
		if (size == 1) {
			this.append(longToAddressString(0L)+addressDelimiter);
		}
		this.append(byteToHexString(b)+" ");
		size = bytes.size();
		x = ((size+bytesPerLine-1) % bytesPerLine);

		if (x >= bytesPerLine-1) {
			this.append("\n"+longToAddressString(size)+addressDelimiter);
		}
	}
	
	public void insertByte(Byte b, int i) {
		
	}

	public void removeByte(Byte b, int i) {
		
	}
	
	public void refreshAllText() {
		long address = 0L;
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			if ((address % bytesPerLine) == 0) {
				sb.append("\n");
				sb.append(longToAddressString(address));
				sb.append(addressDelimiter);
			}
			sb.append(byteToHexString(b));
			sb.append(octetDelimiter);
			address++;
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(0);
		}
		this.setText(sb.toString());
	}
	
	private int getAddressStringLength() {
		// N - Number Value of Symbol Arrangment
		// S - Symbol Value
		// B - Base of Number System
		// I - Index of Symbol (from right to left)
		//     	N = S*B^I   			|   
		//		N/S = B^I   			|  
		//		log(N/S) = I*log(B)  	|
		//		log(N) = I*log(16)		|
		//		I = log(N)/log(16)   	√
		int size = Integer.MAX_VALUE;//bytes.size();
		int length = (int) (Math.log((size-1==0)?size:size-1) / Math.log(16.0));  
		length++;
		if (length%2==1) {
			length++;
		}
		return length;
	}
	
	private void refreshCharactersPerLine() {
		Graphics g = this.getGraphics();
		FontMetrics fontMetrics = g.getFontMetrics();
		characterWidth = fontMetrics.stringWidth("A");
		int width = this.getWidth();
		charactersPerLine = (characterWidth>0)?width/characterWidth:0;
	}

	public void autoResizeFont() {
		refreshCharactersPerLine();
		String text = this.getText();
		int numCharsOnFirstLine = text.indexOf("\n", 1);
		if (numCharsOnFirstLine == -1) {
			numCharsOnFirstLine = text.length();
		}
		while (numCharsOnFirstLine < charactersPerLine) {
			refreshCharactersPerLine();			
			Font font = this.getFont();
			int size = font.getSize();
			this.setFont(new Font(font.getName(), font.getStyle(), size+1));
			
		}
		int size = this.getFont().getSize();
		while (numCharsOnFirstLine > charactersPerLine && size > 2) {
			refreshCharactersPerLine();
			Font font = this.getFont();
			size = font.getSize();
			this.setFont(new Font(font.getName(), font.getStyle(), size-1));
		}
	}
		
	@Override
	public void insertUpdate(DocumentEvent e) {
		Document doc = e.getDocument();		
		try {
			String text  = doc.getText(0, doc.getLength()); 
			String string = doc.getText(e.getOffset(), e.getLength());
			byte[] changedBytes = string.getBytes();
			boolean append = false;
			if (e.getOffset() >= text.length()-1) {
				append = true;
			}
			if (append) {
				for (byte b : changedBytes) {
					bytes.add(b);
					appendByte(b);
				}
				autoResizeFont();
			} else {
				bytes.removeAllElements();
				byte[] refreshedBytes = text.getBytes();
				for (byte b : refreshedBytes) {
					bytes.add(b);
				}
				refreshAllText();
				autoResizeFont();
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		Document doc = e.getDocument();	
		String text = "";
		try {
			text = doc.getText(0, doc.getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		} 
		bytes.removeAllElements();
		byte[] refreshedBytes = text.getBytes();
		for (byte b : refreshedBytes) {
			bytes.add(b);
		}
		refreshAllText();
		autoResizeFont();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		//This callback is only fired when an attribute is changed.
		// Ex: Font or Font style is changed.
	}

	@Override
	public void settingsChanged() {
		refreshAllText();
		autoResizeFont();
	}

	
	@Override
	public void ancestorMoved(HierarchyEvent e) {
		
	}

	@Override
	public void ancestorResized(HierarchyEvent e) {
		
	}	
}
