package xie.common.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestContextUtils;

import xie.common.Constants;
import xie.common.string.XStringUtils;

public class XRequestUtils {
	public static final String USER_AGENT_SPIDER_BAIDU = "Baiduspider";
	public static final String USER_AGENT_SPIDER_GOOGLE = "Googlebot";
	public static final String USER_AGENT_SPIDER = "spider";

	public static boolean isSearchspider(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		// Baiduspider Googlebot spider
		if (userAgent != null) {
			String userAgentLowerCase = userAgent.toLowerCase();
			if (userAgentLowerCase.contains(USER_AGENT_SPIDER_BAIDU.toLowerCase())) {
				return true;
			}
			if (userAgentLowerCase.contains(USER_AGENT_SPIDER_GOOGLE.toLowerCase())) {
				return true;
			}
			if (userAgentLowerCase.contains(USER_AGENT_SPIDER.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 从spring里获得locale
	 * 
	 * @param request
	 * @return
	 */
	public static String getLocaleLanguageCountry(HttpServletRequest request) {
		String localeLanguage = Constants.LANGUAGE_UNKNOW;
		Locale locale = RequestContextUtils.getLocale(request);
		if (locale != null) {
			String language = locale.getLanguage();
			String country = locale.getCountry();
			localeLanguage = language + (XStringUtils.isBlank(country) ? "" : "_" + country);
		}

		return localeLanguage;
	}
}
