package xie.animeshotsite.utils;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import xie.animeshotsite.setup.UserConfig;
import xie.common.utils.XCookieUtils;
import xie.common.web.util.ConstantsWeb;

public class SiteUtils {
	/**
	 * 获取当前用户的cookieId
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String getSiteCookieId(HttpServletRequest request) throws UnsupportedEncodingException {
		return XCookieUtils.getCookieValue(request, ConstantsWeb.SITE_COOKIE_ID);
	}

	/**
	 * 获取当前用户的cookieUserName
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String getSiteCookieUserName(HttpServletRequest request) throws UnsupportedEncodingException {
		return XCookieUtils.getCookieValue(request, ConstantsWeb.SITE_COOKIE_USER_NAME);
	}

	public static void setUserConfig(HttpServletRequest request, UserConfig userConfig) {
		request.getSession().setAttribute(ConstantsWeb.SITE_USER_CONFIG, userConfig);
	}

	public static UserConfig getUserConfig(HttpServletRequest request) {
		UserConfig userConfig = (UserConfig) request.getSession().getAttribute(ConstantsWeb.SITE_USER_CONFIG);
		if (userConfig == null) {
			userConfig = new UserConfig();
			setUserConfig(request, userConfig);
		}
		return userConfig;
	}
}
