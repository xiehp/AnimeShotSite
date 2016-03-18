package xie.common.excel;

import java.io.UnsupportedEncodingException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.logging.Logger;

import xie.common.Constants;

/**
 * JSTL: ${s} ==== <c:out value="${s}" escapeXml="false"></c:out> ==== out.print(s) ==== <%=s%> JSTL: <c:out value="${s}"></c:out> ==== encodeHtml(s) JavaScript alert: must use javaScriptEncode(s)
 * This is a 1-sentence description of this class. This is high level description of this class's capability.
 *
 * <pre>
 * Pattern : Value Object
 * Thread Safe : No
 * 
 * Change History
 * 
 * Name                 Date                    Description
 * -------              -------                 -----------------
 * 020117              2011-11-16            Create the class
 *
 * </pre>
 *
 * @author 020117
 * @version 1.0
 */
public class XSSUtil {
	private static final Logger LOG = Logger.getLogger(XSSUtil.class.getName());
	private final static String EMPTY_STRING = "";
	static final String HEX_STR = "0123456789ABCDEF";

	private static String encodeHtml(final String strInput) {
		String rtn = EMPTY_STRING;
		if (strInput.length() > 0) {
			final StringBuilder builder = new StringBuilder(strInput.length() * 2);
			final CharacterIterator it = new StringCharacterIterator(strInput);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '['))) || (((ch == ' ') || ((ch > '/') && (ch < ':'))) || (((ch == '.') || (ch == ',')) || ((ch == '-') || (ch == '_'))))) {
					builder.append(ch);
				} else if (ch >= '\uD800' && ch <= '\uDBFF') {
					builder.append("&#" + Character.toCodePoint(ch, it.next()) + ";");
				} else {
					builder.append("&#" + (int) ch + ";");
				}
			}
			rtn = builder.toString();
		}
		return rtn;
	}

	private static String encodeHtmlAttribute(final String strInput) {
		String rtn = EMPTY_STRING;
		if (strInput.length() > 0) {
			final StringBuilder builder = new StringBuilder(strInput.length() * 2);
			final CharacterIterator it = new StringCharacterIterator(strInput);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '['))) || (((ch > '/') && (ch < ':')) || (((ch == '.') || (ch == ',')) || ((ch == '-') || (ch == '_'))))) {
					builder.append(ch);
				} else if (ch >= '\uD800' && ch <= '\uDBFF') {
					builder.append("&#" + Character.toCodePoint(ch, it.next()) + ";");
				} else {
					builder.append("&#" + (int) ch + ";");
				}
			}
			rtn = builder.toString();
		}
		return rtn;
	}

	private static String encodeJs(final String strInput) {
		String rtn = EMPTY_STRING;
		if (strInput.length() > 0) {
			final StringBuilder builder = new StringBuilder("");
			final CharacterIterator it = new StringCharacterIterator(strInput);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '['))) || (((ch == ' ') || ((ch > '/') && (ch < ':'))) || (((ch == '.') || (ch == ',')) || ((ch == '-') || (ch == '_'))))) {
					builder.append(ch);
				} else if (ch > '\u007f') {
					builder.append("\\u" + twoByteHex(ch));
				} else {
					builder.append("\\x" + singleByteHex(ch));
				}
			}
			builder.append("");
			rtn = builder.toString();
		}
		return rtn;
	}

	private static String encodeUrl(final String strInput, final String encode) {
		String rtn = EMPTY_STRING;
		if (strInput.length() > 0) {
			final StringBuilder builder = new StringBuilder(strInput.length() * 2);
			final CharacterIterator it = new StringCharacterIterator(strInput);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '['))) || (((ch > '/') && (ch < ':')) || (((ch == '.') || (ch == '-')) || (ch == '_')))) {
					builder.append(ch);
				} else if (ch >= '\uD800' && ch <= '\uDBFF') {
					builder.append(strToUrlStr(String.valueOf(new char[] { ch, it.next() }), encode));
				} else if (ch > '\u007f') {
					builder.append(strToUrlStr(String.valueOf(new char[] { ch }), encode));
				} else {
					builder.append("%" + singleByteHex(ch));
				}
			}
			rtn = builder.toString();
		}
		return rtn;
	}

	private static String encodeWholeUrl(final String strInput, final String encode) {
		String rtn = EMPTY_STRING;
		if (strInput.length() > 0) {
			final StringBuilder builder = new StringBuilder(strInput.length() * 2);
			final CharacterIterator it = new StringCharacterIterator(strInput);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '['))) || (((ch > '/') && (ch < ':')) || (((ch == '.') || (ch == '-')) || (ch == '_'))) || (ch == '/') || (ch == ':')
						|| (ch == '?') || (ch == '&') || (ch == '=')) {
					builder.append(ch);
				} else if (ch >= '\uD800' && ch <= '\uDBFF') {
					builder.append(strToUrlStr(String.valueOf(new char[] { ch, it.next() }), encode));
				} else if (ch > '\u007f') {
					builder.append(strToUrlStr(String.valueOf(new char[] { ch }), encode));
				} else {
					builder.append("%" + singleByteHex(ch));
				}
			}
			rtn = builder.toString();
		}
		return rtn;
	}

	private static String strToUrlStr(final String s, final String encode) {
		final StringBuffer out = new StringBuffer(4);
		try {
			final byte[] ba = s.getBytes(encode);

			for (int j = 0; j < ba.length; j++) {
				out.append('%');
				out.append(HEX_STR.charAt((ba[j] >> 4) & 0x0f));
				out.append(HEX_STR.charAt(ba[j] & 0x0f));
			}
		} catch (UnsupportedEncodingException e) {
			// LOG.error(e.getMessage(), e);
		}
		return out.toString();
	}

	private static String singleByteHex(final char c) {
		final long num = c;
		return leftPad(Long.toString(num, 16), "0", 2);
	}

	private static String twoByteHex(final char c) {
		final long num = c;
		return leftPad(Long.toString(num, 16), "0", 4);
	}

	private static String leftPad(final String stringToPad, final String padder, final int size) {
		String rtn = stringToPad;
		if (padder.length() > 0) {
			final StringBuffer strb = new StringBuffer(size);
			final StringCharacterIterator sci = new StringCharacterIterator(padder);

			while (strb.length() < (size - stringToPad.length())) {
				for (char ch = sci.first(); ch != CharacterIterator.DONE; ch = sci.next()) {
					if (strb.length() < size - stringToPad.length()) {
						strb.insert(strb.length(), String.valueOf(ch));
					}
				}
			}
			rtn = strb.append(stringToPad).toString();
		}
		return rtn;
	}

	/**
	 * Returns a string object encoded to use in HTML.
	 * <p>
	 * This method will return characters a-z, A-Z, space, 0-9, full stop, comma, dash, and underscore unencoded, and encode all other character in decimal HTML entity format (i.e. < is encoded as
	 * &#60;).
	 * 
	 * @param s a string to be encoded for use in an HTML context
	 * @return the encoded string
	 */
	public static String htmlEncode(final String s) {
		return encodeHtml(removeNull(s));
	}

	/**
	 * Returns a string object encoded to be used in an HTML attribute.
	 * <p>
	 * This method will return characters a-z, A-Z, 0-9, full stop, comma, dash, and underscore unencoded, and encode all other character in decimal HTML entity format (i.e. < is encoded as &#60;).
	 * 
	 * @param s a string to be encoded for use in an HTML attribute context
	 * @return the encoded string
	 */
	public static String htmlAttributeEncode(final String s) {
		return encodeHtmlAttribute(removeNull(s));
	}

	/**
	 * Returns a string object encoded to use in JavaScript as a string.
	 * <p>
	 * This method will return characters a-z, A-Z, space, 0-9, full stop, comma, dash, and underscore unencoded, and encode all other character in a 2 digit hexadecimal escaped format for non-unicode
	 * characters (e.g. \x17), and in a 4 digit unicode format for unicode character (e.g. \u0177).
	 * <p>
	 * The encoded string will be returned enclosed in single quote characters (i.e. ').
	 * 
	 * @param s a string to be encoded for use in a JavaScript context
	 * @return the encoded string
	 */
	public static String javaScriptEncode(final String s) {
		return encodeJs(removeNull(s));
	}

	/**
	 * Returns a string object encoded to use in a URL context.
	 * <p>
	 * This method will return characters a-z, A-Z, 0-9, full stop, dash, and underscore unencoded, and encode all other characters in short hexadecimal URL notation. for non-unicode character (i.e. <
	 * is encoded as %3c), and as unicode hexadecimal notation for unicode characters (i.e. %u0177).
	 * 
	 * @param s a string to be encoded for use in a URL context
	 * @return the encoded string
	 */
	public static String urlEncode(final String s) {
		return urlEncode(s, Constants.DEFAULT_SYSTEM_ENCODE);
	}

	public static String urlEncode(final String s, final String encode) {
		return encodeUrl(removeNull(s), encode);
	}

	/**
	 * Returns a string object encoded to use in a URL context.not encode &:?/
	 * <p>
	 * This method will return characters a-z, A-Z, 0-9, full stop, dash, and underscore unencoded, and encode all other characters in short hexadecimal URL notation. for non-unicode character (i.e. <
	 * is encoded as %3c), and as unicode hexadecimal notation for unicode characters (i.e. %u0177).
	 * 
	 * @param s a string to be encoded for use in a URL context
	 * @return the encoded string
	 */
	public static String wholeUrlEncode(final String s) {
		return wholeUrlEncode(s, Constants.DEFAULT_SYSTEM_ENCODE);
	}

	public static String wholeUrlEncode(final String s, final String encode) {
		return encodeWholeUrl(removeNull(s), encode);
	}

	/**
	 * 如果字符为NULL，则字符串赋一个“”值
	 * 
	 * @param sString 输入字符串
	 * 
	 * @return 字符串
	 */
	public static String removeNull(final String sString) {
		String s = "";
		if (null != sString) {
			s = sString;
		}
		return s;
	}
}