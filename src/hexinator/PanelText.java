package hexinator;

import java.io.UnsupportedEncodingException;

import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;

public class PanelText extends JTextPane implements SettingsListener {

	private static final long serialVersionUID = 3L;
		
	public PanelText() {
		AbstractDocument doc = (AbstractDocument)this.getDocument();
		doc.setDocumentFilter(new TextFilter());
	}
	
	public byte[] getBytes() {
		byte[] bytes;
		bytes = Settings.getBytesFromCurrentEncoding(this.getText());
		return bytes;
	}
	
	public void refreshText() {
		byte[] bytes = this.getBytes();
		String newText;
		try {
			newText = new String(bytes, Settings.getCurrentEncoding().encodingName);
		} catch (UnsupportedEncodingException e) {
			newText = this.getText();
			e.printStackTrace();
		}
		this.setText(newText);
	}

	@Override
	public void settingsChanged() {
		refreshText();
	}
		
}
