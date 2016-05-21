package xie.base.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtils {
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
					break;
				}
			}
		}

		return value;
	}
}
