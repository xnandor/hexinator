package hexinator;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.SortedMap;

public class Settings {

	public enum EntryMode {
		TEXT("Text Entry"),
		HEX("Hex Entry"),
		BINARY("Binary Entry"),
		FILE("File Entry");
		public final String displayName;
		EntryMode(String string) {
			this.displayName = string;
		}
		public String toString() {
			return displayName;
		}
	}
	
	public enum Encoding {
		ASCII   ("US-ASCII", "ASCII - US"),
		UTF8    ("UTF-8",    "Unicode - UTF8"),
		UTF16   ("UTF-16",   "Unicode - UTF16"),
		UTF16BE ("UTF-16BE", "Unicode - UTF16BE"),
		UTF16LE ("UTF-16LE", "Unicode - UTF16LE"),
		UTF32   ("UTF-32",   "Unicode - UTF32"),
		UTF32BE ("UTF-32BE", "Unicode - UTF32BE"),
		UTF32LE ("UTF-32LE", "Unicode - UTF32LE");
		public final String encodingName;
		public final String displayName;
		private Encoding(String name, String display) {
			this.encodingName = name;
			this.displayName = display;
		}
		public String toString() {
			return displayName;
		}
	}
	private static final Encoding[] encodings;
	private static final EntryMode[] modes;	
	private static Encoding encoding = Encoding.UTF8;
	private static EntryMode mode = EntryMode.TEXT;
	private static ArrayList<SettingsListener> listeners = new ArrayList<SettingsListener>();
	
	static {
		SortedMap<String, Charset> map = Charset.availableCharsets();
		ArrayList<Encoding> a = new ArrayList<Encoding>();
		for (Encoding encoding : Encoding.values()) {
			if (map.containsKey(encoding.encodingName)) {
				a.add(encoding);
			}
		}
		encodings = a.toArray(new Encoding[0]);
				
		ArrayList<EntryMode> b = new ArrayList<EntryMode>();
		for (EntryMode mode : EntryMode.values()) {
			b.add(mode);
		}
		modes = b.toArray(new EntryMode[0]);

	}
	
	public static void notifyListeners() {
		if (listeners != null) {
			for (SettingsListener listener : listeners) {
				listener.settingsChanged();
			}
		}
	}
	
	public static void addSettingsListener(SettingsListener listener) {
		if (listeners != null) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		} else {
			listeners = new ArrayList<SettingsListener>();
			listeners.add(listener);
		}
	}
	
	public static void removeSettingsListener(SettingsListener listener) {
		if (listeners != null) {
			if (listeners.contains(listener)) {
				listeners.remove(listener);
			}
		} else {
			listeners = new ArrayList<SettingsListener>();
		}
	}
	
	public static String convertEncoding(String string) {
		try {
			byte[] bytes = string.getBytes(encoding.encodingName);
			string = new String(bytes, encoding.encodingName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}
	
	public static byte[] getBytesFromCurrentEncoding(String string) {
		try {
			byte[] bytes = string.getBytes(encoding.encodingName);
			return bytes;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string.getBytes();
	}

	public static EntryMode getEntryMode() {
		return mode;
	}
	
	public static void setEntryMode(EntryMode mode) {
		Settings.mode = mode;
		notifyListeners();
	}
	
	public static Encoding getCurrentEncoding() {
		return encoding;
	}
	
	public static boolean setCurrentEncoding(Encoding encoding) {
		ArrayList<Encoding> encodings = new ArrayList<Encoding>();
		for (Encoding e : Settings.encodings) {
			encodings.add(e);
		}
		if (encodings.contains(encoding)) {
			Settings.encoding = encoding;
			notifyListeners();
			return true;
		} else {
			notifyListeners();
			return false;
		}
	}
	
	public static Encoding[] getSupportedEncodings() {
		return encodings;
	}
	
	public static EntryMode[] getSupportedEntryModes() {
		return modes;
	}
	
	public static void loadStaticClass() {
		
	}
	
}
