package hexinator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

public class PanelHex extends JTextArea implements DocumentListener, HierarchyBoundsListener, SettingsListener {

	private static final long serialVersionUID = 4L;
	
	private int characterWidth = 0;
	private int charactersPerLine = 0;
	private String byteDelimiter = " ";
	private String addressDelimiter = ": ";
	private int bytesPerLine = 16;
	private Vector<Byte> bytes = new Vector<Byte>();
	private JTextArea self = this;
	private HashSet<Integer> selectedBytes = new HashSet<Integer>();
	private boolean isShift = false;
	private boolean isAlt = false;
	private boolean isBinaryMode = false;
	private boolean isTyping = false;

	public PanelHex(PanelText pane) {
		initGUI();
	}
	
	private void initGUI() {
		this.setEditable(false);
		this.addHierarchyBoundsListener(this);
		this.setFont(new Font("Courier New", Font.PLAIN, 14));
		this.setText("");
		this.getCaret().setVisible(false);
		this.setSelectionColor(this.getBackground());
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				isShift = e.isShiftDown();
				isAlt = e.isAltDown();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				isShift = e.isShiftDown();
				isAlt = e.isAltDown();
			}

		});
		this.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if (!isTyping) {
					int start = self.getSelectionStart();
					int end = self.getSelectionEnd();
					if (isBinaryMode) {
						start = binaryStringLocationToByteLocation(start);
						end = binaryStringLocationToByteLocation(end);
					} else {
						start = hexStringLocationToByteLocation(start);
						end = hexStringLocationToByteLocation(end);
					}
					if (start < 0) {
						start = 0;
					}
					if (!(isAlt || isShift)) {
						selectedBytes.clear();
						if (selectedBytes.contains(start)) {
							selectedBytes.remove(start);
						} else {
							selectedBytes.add(start);
						}
					}
					if (isAlt) {
						if (selectedBytes.contains(start)) {
							selectedBytes.remove(start);
						} else {
							selectedBytes.add(start);
						}
					}
					if (isShift) {
						for (int i = start; i <= end; i++) {
							selectedBytes.add(i);
						}
						for (int i = end; i <= start; i++) {
							selectedBytes.add(i);
						}
					}
					try {
						selectSelectedBytes();
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			
		});
	}

	public String byteToHexString(byte b) {
		return String.format("%2s", Integer.toHexString(b & 0xFF)).replace(' ', '0');
	}
	
	public String byteToBinaryString(byte b) {
		return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
	}
	
	public int hexStringLocationToByteLocation(int sl) {
		int hexLength = 2+byteDelimiter.length();
		int asl = getAddressStringLength() + 1 + 2 + addressDelimiter.length(); // one for '\n' character. two for "0x"
		int w = ( asl + (hexLength*(bytesPerLine)));
		int sx = sl % w;
		int y = (sl - sx) / w;
		int x = (sx - (asl-1)) / hexLength;
		int bl = 0;
		if (sx < asl) {
			bl = (y*(bytesPerLine));
		} else if (sx >= w-1) {
			bl = (y*(bytesPerLine)) + bytesPerLine-1;
		} else {
			bl = (y*(bytesPerLine)) + x;
		}
		return bl;
	}

	public int byteLocationToHexStringLocation(int bl) {
		int hexLength = 2+byteDelimiter.length();
		int asl = getAddressStringLength() + 1 + 2 + addressDelimiter.length(); // one for '\n' character. two for "0x"
		int x = (bl % bytesPerLine);
		int y = (bl - x) / bytesPerLine;
		int sl;
		if (y == 0) {
			sl = (x*hexLength) + (asl-1);
		} else {
			sl = (x*hexLength) + (asl-1) + (asl+bytesPerLine*hexLength) * (y);
		}
		return sl;
	}

	public int binaryStringLocationToByteLocation(int sl) {
		int binaryLength = 8+byteDelimiter.length();
		int asl = getAddressStringLength() + 1 + 2 + addressDelimiter.length(); // one for '\n' character. two for "0x"
		int w = ( asl + (binaryLength*(bytesPerLine)));
		int sx = sl % w;
		int y = (sl - sx) / w;
		int x = (sx - (asl-1)) / binaryLength;
		int bl = 0;
		if (sx < asl) {
			bl = (y*(bytesPerLine));
		} else if (sx >= w-1) {
			bl = (y*(bytesPerLine)) + bytesPerLine-1;
		} else {
			bl = (y*(bytesPerLine)) + x;
		}
		return bl;
	}

	public int byteLocationToBinaryStringLocation(int bl) {
		int binaryLength = 8+byteDelimiter.length();
		int asl = getAddressStringLength() + 1 + 2 + addressDelimiter.length(); // one for '\n' character. two for "0x"
		int x = (bl % bytesPerLine);
		int y = (bl - x) / bytesPerLine;
		int sl;
		if (y == 0) {
			sl = (x*binaryLength) + (asl-1);
		} else {
			sl = (x*binaryLength) + (asl-1) + (asl+bytesPerLine*binaryLength) * (y);
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
		if (isBinaryMode) {
			this.append(byteToBinaryString(b)+" ");
		} else {
			this.append(byteToHexString(b)+" ");
		}
		size = bytes.size();
		x = ((size+bytesPerLine-1) % bytesPerLine);

		if (x >= bytesPerLine-1) {
			this.append("\n"+longToAddressString(size)+addressDelimiter);
		}
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
			if (isBinaryMode) {
				sb.append(byteToBinaryString(b));
			} else {
				sb.append(byteToHexString(b));
			}
			sb.append(byteDelimiter);
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
	
	public void selectSelectedBytes() throws BadLocationException {
				Color highlightColor = new Color(0xff, 0x8f, 0x7b);
				Highlighter h = self.getHighlighter();
				h.removeAllHighlights();
				for (int loc : selectedBytes) {
					if (isBinaryMode) {
						loc = byteLocationToBinaryStringLocation(loc);
						h.addHighlight(loc, loc+8, new DefaultHighlighter.DefaultHighlightPainter(highlightColor));
					} else {
						loc = byteLocationToHexStringLocation(loc);
						h.addHighlight(loc, loc+2, new DefaultHighlighter.DefaultHighlightPainter(highlightColor));
					}
					
					
				}
				self.getCaret().setVisible(false);
	}
		
	@Override
	public void insertUpdate(DocumentEvent e) {
		isTyping = true;
		Document doc = e.getDocument();		
		try {
			String text  = doc.getText(0, doc.getLength()); 
			String string = doc.getText(e.getOffset(), e.getLength());
			byte[] changedBytes = string.getBytes();
			boolean append = false;
			if (e.getOffset() >= text.length()-1) {
				append = true;
			}
			//long t_i = System.currentTimeMillis();
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
				selectSelectedBytes();
			}
			//long t_f = System.currentTimeMillis();
			//System.out.println(  ((append)?"Appended in ":"Inserted in ") + (t_f-t_i) + " ms" );
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		isTyping = false;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		isTyping = true;
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
		try {
			selectSelectedBytes();
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		isTyping = false;
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
