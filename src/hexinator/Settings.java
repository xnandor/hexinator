package hexinator;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.SortedMap;

public class Settings {

	private static final String ENCODING_ASCII   = "US-ASCII";
	private static final String ENCODING_UTF16   = "UTF-16";
	private static final String ENCODING_UTF16BE   = "UTF-16BE";
	private static final String ENCODING_UTF16LE = "UTF-16LE";
	private static final String ENCODING_UTF32   = "UTF-32";
	private static final String ENCODING_UTF32BE = "UTF-32BE";
	private static final String ENCODING_UTF32LE = "UTF-32LE";
	private static final String ENCODING_UTF8    = "UTF-8";
	public static final String[] encodings;
	private static String currentEncoding = "US-ASCII";
	
	static {
		SortedMap<String, Charset> map = Charset.availableCharsets();
		ArrayList<String> a = new ArrayList<String>();
		if (map.containsKey(ENCODING_ASCII)) {
			a.add(ENCODING_ASCII);
		}
		if (map.containsKey(ENCODING_UTF8)) {
			a.add(ENCODING_UTF8);
		}
		if (map.containsKey(ENCODING_UTF16)) {
			a.add(ENCODING_UTF16);
		}
		if (map.containsKey(ENCODING_UTF16BE)) {
			a.add(ENCODING_UTF16BE);
		}
		if (map.containsKey(ENCODING_UTF16LE)) {
			a.add(ENCODING_UTF16LE);
		}
		if (map.containsKey(ENCODING_UTF32)) {
			a.add(ENCODING_UTF32);
		}
		if (map.containsKey(ENCODING_UTF32BE)) {
			a.add(ENCODING_UTF32BE);
		}
		if (map.containsKey(ENCODING_UTF32LE)) {
			a.add(ENCODING_UTF32LE);
		}
		encodings = a.toArray(new String[0]);
		currentEncoding = "US-ASCII";
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
			return string.getBytes(currentEncoding);
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
