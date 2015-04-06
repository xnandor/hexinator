package hexinator;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TextFilter extends DocumentFilter {

	public TextFilter() {

	}
	
	public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		super.insertString(fb, offset, string, attr);
	}
	
	public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
		super.remove(fb, offset, length);
	}
	
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		text = Settings.convertEncoding(text);
		super.replace(fb, offset, length, text, attrs);
	}
	
}
