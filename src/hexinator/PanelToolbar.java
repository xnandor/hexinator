package hexinator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class PanelToolbar extends JPanel {

	private static final long serialVersionUID = 2L;
	private PanelToolbar self = this;
	private JButton knob = new JButton();
	private JPanel shelf = new JPanel();
	private JPanel drawerRow = new JPanel();
	private JPanel textDrawer = new JPanel();
	private JPanel translateDrawer = new JPanel();
	private JPanel hexDrawer = new JPanel();
	
	private JComboBox<Settings.EntryMode> textEntryCombo;
	private JComboBox<Settings.Encoding> textEncodingCombo;

	private JComboBox<String> translateCombo;
	
	private JComboBox<Settings.HexMode> hexDisplayCombo;
	
	
	public PanelToolbar() {		
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(new BorderLayout());
		shelf.setLayout(new BorderLayout());
		drawerRow.setLayout(new GridLayout());
		
		drawerRow.setVisible(false);
		drawerRow.setPreferredSize(new Dimension(640, 35));
		
		shelf.setPreferredSize(new Dimension(640, 40));
		
		knob.setIcon(new ImageIcon("resources/icons/gear.png"));
		knob.setPreferredSize(new Dimension(40, 40));
		knob.addActionListener(new ActionListener() {
			private boolean toggle = true;
			@Override
			public void actionPerformed(ActionEvent e) {
				if (toggle) {
					drawerRow.setVisible(toggle);
					toggle = !toggle;
				} else {
					drawerRow.setVisible(toggle);
					toggle = !toggle;
				}
			}
		});
		
		Color dark = new Color(234, 234, 234);
		Color darker = new Color(210, 210, 210);
		
		shelf.setBackground(dark);
		drawerRow.setBackground(darker);
		textDrawer.setBackground(darker);
		translateDrawer.setBackground(darker);
		hexDrawer.setBackground(darker);
		
		initText();
		initTranslate();
		initHex();
		
		shelf.add(knob, BorderLayout.AFTER_LINE_ENDS);
		drawerRow.add(textDrawer);
		drawerRow.add(translateDrawer);
		drawerRow.add(hexDrawer);
		this.add(shelf, BorderLayout.PAGE_START);
		this.add(drawerRow, BorderLayout.PAGE_END);
	}
	
	private void initText() {
		textEntryCombo = new JComboBox<Settings.EntryMode>();
		Settings.EntryMode[] modes = Settings.getSupportedEntryModes();
		for (Settings.EntryMode mode : modes) {
			textEntryCombo.addItem(mode);
		}
		textEntryCombo.setSelectedItem(Settings.getEntryMode());
		textEntryCombo.setBounds(10, 10, 200, 24);
		textEntryCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<Settings.EntryMode> combo = (JComboBox<Settings.EntryMode>)e.getSource();
				Settings.EntryMode mode = (Settings.EntryMode)combo.getSelectedItem();
				Settings.setEntryMode(mode);
				if (Settings.EntryMode.TEXT.equals(mode)) {
					textDrawer.add(textEncodingCombo);
				} else {
					textDrawer.remove(textEncodingCombo);	
				}
				self.validate();
				self.repaint();
			}
		});
		textEncodingCombo = new JComboBox<Settings.Encoding>();
		Settings.Encoding[] encodings = Settings.getSupportedEncodings();
		for (Settings.Encoding encoding : encodings) {
			textEncodingCombo.addItem(encoding);
		}
		textEncodingCombo.setSelectedItem(Settings.getCurrentEncoding());
		textEncodingCombo.setBounds(215, 10, 200, 24);
		textEncodingCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<Settings.Encoding> combo = (JComboBox<Settings.Encoding>)e.getSource();
				Settings.Encoding encoding = (Settings.Encoding)combo.getSelectedItem();
				Settings.setCurrentEncoding(encoding);
			}
		});
		textDrawer.add(textEntryCombo);
		textDrawer.add(textEncodingCombo);
	}
	
	private void initTranslate() {
		translateCombo = new JComboBox<String>();
		translateCombo.addItem("Translation");
		translateCombo.setSelectedIndex(0);
		translateDrawer.add(translateCombo);
	}
	
	private void initHex() {
		hexDisplayCombo = new JComboBox<Settings.HexMode>();
		Settings.HexMode[] modes = Settings.getSupportedHexModes();
		for (Settings.HexMode mode : modes) {
			hexDisplayCombo.addItem(mode);
		}
		hexDisplayCombo.setSelectedIndex(0);
		hexDrawer.add(hexDisplayCombo);
	}
	
}
