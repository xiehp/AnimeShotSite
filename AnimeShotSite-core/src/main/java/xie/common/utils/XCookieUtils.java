package xie.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xie.common.constant.XConst;
import xie.common.string.XStringUtils;

/**
 * Cookie操作<br>
 * PS:可以使用spring的cookie操作类{@link org.springframework.web.util.CookieGenerator}<br>
 * 或者WebUtils.getCookie<br>
 */
public class XCookieUtils {
	public static Map<String, String> getCookieMap(HttpServletRequest request, String name) {
		Map<String, String> map = new HashMap<String, String>();
		if (name == null || request == null) {
			return null;
		}

		Cookie[] cookieList = request.getCookies();
		if (cookieList != null && cookieList.length > 0) {
			for (Cookie cookie : cookieList) {
				String value = cookie.getValue();
				if (value != null) {
					try {
						value = URLDecoder.decode(value, XConst.CHARSET_UTF8);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				map.put(cookie.getName(), value);
			}
		}

		return map;
	}

	public static String getCookieValue(HttpServletRequest request, String name) {
		if (name == null || request == null) {
			return null;
		}

		String value = null;
		Cookie[] cookieList = request.getCookies();
		if (cookieList != null && cookieList.length > 0) {
			for (Cookie cookie : cookieList) {
				if (name.equals(cookie.getName())) {
					value = cookie.getValue();
					if (value != null) {
						try {
							value = URLDecoder.decode(value, XConst.CHARSET_UTF8);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		}

		return value;
	}

	public static boolean addCookieValue(HttpServletResponse response, String name, String value) throws UnsupportedEncodingException {
		if (XStringUtils.isBlank(name)) {
			return false;
		}

		String encodeName = URLEncoder.encode(name, XConst.CHARSET_UTF8);
		String encodeValue = value == null ? null : URLEncoder.encode(value, XConst.CHARSET_UTF8);
		Cookie cookie = new Cookie(encodeName, encodeValue);
		response.addCookie(cookie);
		return true;
	}
}
