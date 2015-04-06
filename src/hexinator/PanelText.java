package hexinator;

import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;

public class PanelText extends JTextPane {

	private static final long serialVersionUID = 3L;

	public enum EntryMode {
		TEXT("Text Entry"),
		HEX("Hex Entry"),
		BINARY("Binary Entry"),
		FILE("File Entry");
		public final String string;
		EntryMode(String string) {
			this.string = string;
		}
	}
	
	public PanelText() {
		AbstractDocument doc = (AbstractDocument)this.getDocument();
		doc.setDocumentFilter(new EncodingFilter(this));
	}
	
	public byte[] getBytes() {
		byte[] bytes = Settings.getBytesFromCurrentEncoding(this.getText());
		return bytes;
	}
		
}
