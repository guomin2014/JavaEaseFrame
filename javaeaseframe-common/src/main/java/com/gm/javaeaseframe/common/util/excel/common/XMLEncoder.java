package com.gm.javaeaseframe.common.util.excel.common;

public class XMLEncoder {

	private static final String[] xmlCode = new String[256];

	static {  
        // Special characters  
        xmlCode['\''] = "'";  
        xmlCode['\"'] = "\""; // double quote  
        xmlCode['&'] = "&"; // ampersand  
        xmlCode['<'] = "&lt;"; // lower than  
        xmlCode['>'] = "&gt;"; // greater than  
    }

	/**
	 * <p>
	 * Encode the given text into xml.
	 * </p>
	 * 
	 * @param string the text to encode
	 * @return the encoded string
	 */
	public static String encode(String string) {
		if (string == null)
			return "";
		if (!string.contains("<") && !string.contains(">")) {
			return string;
		}

		int n = string.length();
		char character;
		String xmlchar;
		StringBuffer buffer = new StringBuffer();
		// loop over all the characters of the String.
		for (int i = 0; i < n; i++) {
			character = string.charAt(i);
			// the xmlcode of these characters are added to a StringBuffer one by one
			try {
				xmlchar = xmlCode[character];
				if (xmlchar == null) {
					buffer.append(character);
				} else {
					buffer.append(xmlchar);
				}
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				buffer.append(character);
			}
		}
		return buffer.toString();
	}

	public static String encodeXML(String str) {
		if (str == null || str.trim().length() == 0) {
			return "";
		}
		return str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
