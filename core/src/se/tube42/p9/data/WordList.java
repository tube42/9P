
package se.tube42.p9.data;

import se.tube42.lib.service.*;
import java.util.*;

public final class WordList {
	public static final int FOUND_NONE = 0, FOUND_EXACT = 1, FOUND_PREFIX = 2;

	private Map<Character, Character> map2ascii, map2utf8;
	private String charset_ascii, charset_utf8;
	private String id, name;
	private byte[] data;

	public WordList(String id, String name, byte[] data, Map<Character, Character> map2ascii,
			Map<Character, Character> map2utf8) {
		this.id = id;
		this.name = name;
		this.data = data;
		this.map2ascii = map2ascii;
		this.map2utf8 = map2utf8;

		charset_ascii = "";
		charset_utf8 = "";
		for (Character c : map2utf8.keySet()) {
			charset_ascii = charset_ascii + c;
			charset_utf8 = charset_utf8 + map2utf8.get(c);
		}

		System.out.println("CHARSETS: " + charset_ascii + " / " + charset_utf8);
	}

	public String getAlphabeth(boolean asascii) {
		return asascii ? charset_ascii : charset_utf8;
	}

	/* convert from ascii to utf8 or vice versa */
	public char convert(char src, boolean ascii2utf8) {
		Character dst = (ascii2utf8 ? map2utf8 : map2ascii).get(src);
		return dst == null ? src : dst;
	}

	public char[] convert(char[] src, boolean ascii2utf8) {
		char[] dst = new char[src.length];
		for (int i = 0; i < dst.length; i++) {
			dst[i] = convert(src[i], ascii2utf8);
		}
		return dst;
	}

	public String convert(String src, boolean ascii2utf8) {
		return new String(convert(src.toCharArray(), ascii2utf8));
	}

	public int lookup(String str) {
		char[] aschars = convert(str.toCharArray(), false);
		byte[] asbytes = new byte[aschars.length];

		for (int i = 0; i < asbytes.length; i++)
			asbytes[i] = (byte) aschars[i];

		return lookup(asbytes, asbytes.length);
	}

	public String getId() {
		return id;
	}

	public String getName_() {
		return name;
	}

	// ---------------------------------------------------------------------------

	public String random() {
		int e, s = 2 + RandomService.getInt(data.length - 3);

		while (s > 0 && data[s] != '\0')
			s++;

		for (e = s + 1; data[e] != '\0' && e < data.length - 1; e++)
			;

		return new String(data, s, e - s);

	}

	public int lookup(final byte[] t, final int tlen) {
		final byte[] words = data;
		final int wlen = data.length;

		int mid, low = 1, high = wlen - 1;
		int tmp;
		boolean partial = false;

		while (low < high) {
			tmp = mid = (low + high) / 2;
			while (tmp > 0 && words[tmp] != 0)
				tmp--;
			tmp++;

			int k = strcmp(words, tmp, t, tlen);
			if (k == 0 && words[tmp + tlen] != 0) {
				partial = true;
				k = +1;
			}

			if (k == 0)
				return FOUND_EXACT;
			else if (k < 0)
				low = mid + 1;
			else
				high = mid;
		}
		return partial ? FOUND_PREFIX : FOUND_NONE;
	}

	private static final int strcmp(byte[] list, int offset, byte[] t, int tlen) {
		for (int i = 0; i < tlen; i++) {
			byte b1 = t[i];
			byte b2 = list[offset++];
			if (b1 < b2)
				return +1;
			if (b2 < b1)
				return -1;
		}
		return 0;
	}

}
