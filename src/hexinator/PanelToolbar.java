package hexinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class PanelToolbar extends JPanel {

	private static final long serialVersionUID = 2L;
	private PanelToolbar self = this;
	private JComboBox<Settings.EntryMode> entryCombo;
	private JComboBox<Settings.Encoding> encodingCombo;
	
	
	public PanelToolbar() {		
		initGUI();
	}
	
	private void initGUI() {
		entryCombo = new JComboBox<Settings.EntryMode>();
		Settings.EntryMode[] modes = Settings.getSupportedEntryModes();
		for (Settings.EntryMode mode : modes) {
			entryCombo.addItem(mode);
		}
		entryCombo.setSelectedItem(Settings.getEntryMode());
		entryCombo.setBounds(10, 10, 200, 24);
		entryCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<Settings.EntryMode> combo = (JComboBox<Settings.EntryMode>)e.getSource();
				Settings.EntryMode mode = (Settings.EntryMode)combo.getSelectedItem();
				Settings.setEntryMode(mode);
				if (Settings.EntryMode.TEXT.equals(mode)) {
					self.add(encodingCombo);
				} else {
					self.remove(encodingCombo);	
				}
				self.validate();
				self.repaint();
			}
		});
		
		encodingCombo = new JComboBox<Settings.Encoding>();
		Settings.Encoding[] encodings = Settings.getSupportedEncodings();
		for (Settings.Encoding encoding : encodings) {
			encodingCombo.addItem(encoding);
		}
		encodingCombo.setSelectedItem(Settings.getCurrentEncoding());
		encodingCombo.setBounds(215, 10, 200, 24);
		encodingCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<Settings.Encoding> combo = (JComboBox<Settings.Encoding>)e.getSource();
				Settings.Encoding encoding = (Settings.Encoding)combo.getSelectedItem();
				Settings.setCurrentEncoding(encoding);
			}
		});
		
		this.add(entryCombo);
		this.add(encodingCombo);
	}
	
}
