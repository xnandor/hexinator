package hexinator;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.SortedMap;

public class Settings {

	public enum Encoding {
		ENCODING_ASCII   ("US-ASCII"),
		ENCODING_UTF8    ("UTF-8"),
		ENCODING_UTF16   ("UTF-16"),
		ENCODING_UTF16BE ("UTF-16BE"),
		ENCODING_UTF16LE ("UTF-16LE"),
		ENCODING_UTF32   ("UTF-32"),
		ENCODING_UTF32BE ("UTF-32BE"),
		ENCODING_UTF32LE ("UTF-32LE");
		public final String encodingName;
		private Encoding(String name) {
			this.encodingName = name;
		}
		
	}
	public static final String[] encodings;
	private static String currentEncoding = Encoding.ENCODING_UTF8.encodingName;
	
	static {
		SortedMap<String, Charset> map = Charset.availableCharsets();
		ArrayList<String> a = new ArrayList<String>();
		for (Encoding encoding : Encoding.values()) {
			if (map.containsKey(encoding.encodingName)) {
				a.add(encoding.encodingName);
			}
		}
		encodings = a.toArray(new String[0]);
		currentEncoding = Encoding.ENCODING_UTF8.encodingName;
	}
	
	public static String convertEncoding(String string) {
		try {
			byte[] bytes = string.getBytes(currentEncoding);
			string = new String(bytes, currentEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}
	
	public static byte[] getBytesFromCurrentEncoding(String string) {
		try {
			byte[] bytes = string.getBytes(currentEncoding);
			return bytes;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string.getBytes();
	}
	
	public static String getCurrentEncoding() {
		return currentEncoding;
	}
	
	public static boolean setCurrentEncoding(String encoding) {
		ArrayList<String> encodings = new ArrayList<String>();
		for (String s : Settings.encodings) {
			encodings.add(s);
		}
		if (encodings.contains(encoding)) {
			currentEncoding = encoding;
			return true;
		} else {
			return false;
		}
	}
	
	public static String[] getSupportedEncodings() {
		return encodings;
	}
	
	public static void loadStaticClass() {
		
	}
	
}
