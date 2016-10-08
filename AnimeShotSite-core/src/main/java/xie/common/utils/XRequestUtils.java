package xie.common.utils;

import javax.servlet.http.HttpServletRequest;

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
}
