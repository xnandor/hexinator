package hexinator;

import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;

public class PanelText extends JTextPane {

	private static final long serialVersionUID = 3L;

	public PanelText() {
		AbstractDocument doc = (AbstractDocument)this.getDocument();
		doc.setDocumentFilter(new EncodingFilter(this));
	}
		
}
