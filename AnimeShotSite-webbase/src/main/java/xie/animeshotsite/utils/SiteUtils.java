package xie.animeshotsite.utils;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import xie.common.utils.XCookieUtils;
import xie.common.web.util.ConstantsWeb;

public class SiteUtils {
	/**
	 * 获取当前用户的cookieId
	 * @throws UnsupportedEncodingException 
	 */
	public static String getSiteCookieId(HttpServletRequest request) throws UnsupportedEncodingException {
		return XCookieUtils.getCookieValue(request, ConstantsWeb.SITE_COOKIE_ID);
	}

	/**
	 * 获取当前用户的cookieUserName
	 * @throws UnsupportedEncodingException 
	 */
	public static String getSiteCookieUserName(HttpServletRequest request) throws UnsupportedEncodingException {
		return XCookieUtils.getCookieValue(request, ConstantsWeb.SITE_COOKIE_USER_NAME);
	}
}
