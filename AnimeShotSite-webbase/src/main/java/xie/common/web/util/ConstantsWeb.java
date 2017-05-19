package xie.common.web.util;

import xie.animeshotsite.setup.UserConfig;

public class ConstantsWeb {

	final public static String MANAGE_URL_PREFIX_STR = "/managesite333";
	final public static String MIP_URL_PREFIX_STR = "/mip";

	/** 默认网站所有人的ID */
	final public static String SITE_MANAGER_ID = "1";
	/** 默认网站所有人的登陆名 */
	final public static String SITE_MANAGER_LOGIN_ID = "admin";

	/** 截图列表页面中每页展示的图片数量 */
	public static final int SHOT_LIST_PAGE_NUMBER = 48;

	/** 存放在session中的key，使用{@link UserConfig} */
	public static final String SITE_USER_CONFIG = "SITE_USER_CONFIG";

	/** 标识某个人的CookieId的key */
	public static final String SITE_COOKIE_ID = "SiteCookieId";

	/** 标识某个人的CookieUserName */
	public static final String SITE_COOKIE_USER_NAME = "SiteCookieUserName";


	/** 标识某个人的CookieUserName */
	public static final String SITE_SESSION_LAST_VISIT_PATH = "lastVisitPath";
	public static final String SITE_SESSION_LAST_VISIT_URL = "lastVisitUrl";
	public static final String SITE_SESSION_PRE_VISIT_PATH = "preVisitPath";
	public static final String SITE_SESSION_PRE_VISIT_URL = "preVisitUrl";
	
}
