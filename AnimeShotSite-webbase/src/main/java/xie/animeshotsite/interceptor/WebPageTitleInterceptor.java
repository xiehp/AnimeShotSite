package xie.animeshotsite.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import xie.animeshotsite.setup.ShotSiteSetup;
import xie.animeshotsite.setup.UserConfig;
import xie.animeshotsite.utils.SiteUtils;
import xie.base.module.exception.CodeApplicationException;
import xie.common.Constants;
import xie.common.string.XStringUtils;
import xie.common.utils.XCookieUtils;
import xie.common.utils.XRequestUtils;
import xie.common.utils.XSSHttpUtil;
import xie.common.web.util.ConstantsWeb;
import xie.module.spring.SpringUtils;
import xie.sys.auth.service.realm.ShiroRDbRealm.ShiroUser;
import xie.web.util.SiteConstants;

/**
 * 通用处理
 */
@Component
public class WebPageTitleInterceptor extends HandlerInterceptorAdapter {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = false)
	private ApplicationContext applicationContext;

	@Autowired(required = true)
	private ShotSiteSetup shotSiteSetup;

	@Resource
	private MessageSource messageSource;

	private final Map<String, Integer> excludeIpsCount = new HashMap<>();

	/**
	 * 
	 * 修改网页标题信息
	 * 
	 * @param request 请求对象
	 * @param response 回复对象
	 * @param handler 提交对象
	 * @return 成功则返回true，失败则返回false
	 * @throws IOException
	 */
	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler) throws IOException {

		// 检查访问地址是否正确
		if (XStringUtils.isNotBlank(shotSiteSetup.getAnimesiteServerHost())) {
			String hostName = XSSHttpUtil.getForwardedServerName(request);
			String remoteIp = XSSHttpUtil.getForwardedRemoteIpAddr(request);

			if ("127.0.0.1".equals(hostName) || "localhost".equals(hostName)) {
				// 来自本地，则不做跳转
			} else if ("XXXXX".equals(hostName)) {
				// 其他不需要跳转的host
			} else {
				if (!shotSiteSetup.getAnimesiteServerHost().startsWith(hostName)) {
					if (hostName.contains("acgimage.cn") || hostName.contains("acgimage.com")) {
						// serverName不符合配置文件设定的值，进行跳转
						logger.warn("当前主机地址不符合访问规则，跳转到www域名。当前地址:" + hostName + ", 客户端IP:" + remoteIp);
						System.err.println("当前主机地址不符合访问规则，跳转到www域名。当前地址:" + hostName + ", 客户端IP:" + remoteIp);
						response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
						response.setHeader("Location", "//" + shotSiteSetup.getAnimesiteServerHost() + request.getRequestURI());
						return false;
					} else {
						// 除了acgimage.cn，acgimage.com 其他都显示404
						logger.warn("当前主机地址不符合访问规则，禁止访问，地址必须为acgimage。当前地址:" + hostName + ", 客户端IP:" + remoteIp);
						System.err.println("当前主机地址不符合访问规则，禁止访问，地址必须为acgimage。当前地址:" + hostName + ", 客户端IP:" + remoteIp);
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						return false;
					}
				}
			}
		}

		// 设置多语言信息
		CodeApplicationException.setMessageSource(messageSource);

		// 检查是否有网站CookieId，没有的话设置当前sessionId为CookieId
		UserConfig userConfig = SiteUtils.getUserConfig(request);
		if (userConfig == null) {
			userConfig = new UserConfig();
			SiteUtils.setUserConfig(request, userConfig);
		}
		String siteCookieId = SiteUtils.getSiteCookieId(request);
		if (XStringUtils.isBlank(siteCookieId)) {
			XCookieUtils.addCookieValue(response, ConstantsWeb.SITE_COOKIE_ID, request.getSession().getId());
		}
		userConfig.setSiteCookieId(siteCookieId);

		// 判断是否是合适的host
		logger.debug("getHeader Host:{}", request.getHeader("Host"));
		logger.debug("getServerName:{}", request.getServerName());
		logger.debug("X-Forwarded-Host:{}", request.getHeader("X-Forwarded-Host"));
		if (shotSiteSetup == null) {
			shotSiteSetup = SpringUtils.getBean(ShotSiteSetup.class);
			logger.warn("shotSiteSetup未初始化，从新获取shotSiteSetup：{}", shotSiteSetup);
		}

		return true;
	}

	/**
	 * 
	 * 如果网页数据修改标识为'Y',则生成新的网页信息
	 * 
	 * @param request 请求对象
	 * @param response 回复对象
	 * @param handler 提交对象
	 * @param modelAndView 可视化对象
	 */
	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) {
		// final WebPathVO webPathVO = ThreadUtil.getWebPathVO();
		//
		// if(CommonConstants.FLAG_YES.equals(request.getAttribute(CHANGE_PAGE_DATA_FALG_NAME))){
		// generatePageData(request , webPathVO);
		// }

		String requestURL = request.getRequestURL().toString();

		// 是否为静态资源
		boolean isStaticUrl = false;
		String lowCaseUrl = requestURL.toLowerCase();
		if (lowCaseUrl.endsWith(".css")) {
			isStaticUrl = true;
		} else if (lowCaseUrl.endsWith(".js")) {
			isStaticUrl = true;
		} else if (lowCaseUrl.endsWith(".png")) {
			isStaticUrl = true;
		} else if (lowCaseUrl.endsWith(".jpg")) {
			isStaticUrl = true;
		} else if (lowCaseUrl.endsWith(".jpeg")) {
			isStaticUrl = true;
		} else if (lowCaseUrl.endsWith(".gif")) {
			isStaticUrl = true;
		}

		if (!isStaticUrl) {
			System.out.println(request.getRequestURL());
			System.out.println(applicationContext);

			// 告诉前台当前登陆用户角色或权限
			{
				ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
				System.out.println("shiroUser:" + shiroUser);
				if (shiroUser != null && ConstantsWeb.SITE_MANAGER_ID.equals(shiroUser.getId()) && ConstantsWeb.SITE_MANAGER_LOGIN_ID.equals(shiroUser.getLoginName())) {
					request.setAttribute("IS_MASTER", true);
				} else {
					request.setAttribute("IS_MASTER", false);
				}
			}

			// 是否需要js debug
			request.setAttribute("IS_JS_DEBUG", shotSiteSetup.getAnimesiteJsDebug());

			boolean canBaiduRecord = true; // 是否让搜索引擎统计
			boolean canBaiduIndex = true; // 是否让搜索引擎索引
			// 判断是否需要网站统计和搜索引擎推送
			{
				if (!"1".equals(shotSiteSetup.getAnimesiteSearchTrafficStatistics())) {
					canBaiduRecord = false;
					canBaiduIndex = false;
				} else if (requestURL.contains(ConstantsWeb.MANAGE_URL_PREFIX_STR)) {
					// 后台页面，不统计
					canBaiduRecord = false;
					canBaiduIndex = false;
				} else if (isExcludeRecordIp(request)) {
					// 排除的IP地址
					canBaiduRecord = false;
					// canBaiduIndex = false; // 排除的ip有很多是搜索引擎的，因此不能排除掉索引
				} else {
					// 其他页面，则判断配置文件是否允许
				}

				// 是否进行网站统计
				request.setAttribute("canBaiduRecord", canBaiduRecord);

				// 是否让搜索引擎索引
				if (request.getAttribute("canBaiduIndex") == null) {
					// 页面没有自行设定是否可以索引，则统一设置
					request.setAttribute("canBaiduIndex", canBaiduIndex);
				}
				// System.out.println("canBaiduRecord: " + request.getAttribute("canBaiduRecord"));
			}

			// 百度静态资源链接
			request.setAttribute("BAIDU_STATIC_URL", "//apps.bdimg.com/libs/");

			// 系统常量
			{
				// 其他
				request.setAttribute("MANAGE_URL_STR", ConstantsWeb.MANAGE_URL_PREFIX_STR);

				// json
				request.setAttribute("JSON_RESPONSE_KEY_CODE", Constants.JSON_RESPONSE_KEY_CODE);
				request.setAttribute("JSON_RESPONSE_KEY_MESSAGE", Constants.JSON_RESPONSE_KEY_MESSAGE);
				request.setAttribute("SUCCESS_CODE", Constants.SUCCESS_CODE);
				request.setAttribute("FAIL_CODE", Constants.FAIL_CODE);

				// 标志
				request.setAttribute("FLAG_INT_YES", Constants.FLAG_INT_YES);
				request.setAttribute("FLAG_INT_NO", Constants.FLAG_INT_NO);
				request.setAttribute("FLAG_STR_YES", Constants.FLAG_STR_YES);
				request.setAttribute("FLAG_STR_NO", Constants.FLAG_STR_NO);

				// server处理
				request.setAttribute("requestURI", request.getRequestURI());
				request.setAttribute("requestURL", request.getRequestURL());

				// 生成远程访问本地的http或https地址
				String httpScheme = XSSHttpUtil.getForwardedRemoteProto(request);
				String portStr = XSSHttpUtil.getForwardedServerPort(request);
				String serverName = XSSHttpUtil.getForwardedServerName(request);
				if (!"localhost".equals(serverName)) {
					serverName = shotSiteSetup.getAnimesiteServerHost();
				}
				if (XStringUtils.isBlank(serverName) || "localhost".equals(serverName)) {
					// 获取访问host和port，主要用于本地调试
					serverName = XSSHttpUtil.getForwardedServerName(request);

					if (XStringUtils.isNotBlank(portStr)) {
						if ("http".equals(httpScheme) && "80".equals(portStr)) {
							portStr = "";
						} else if ("https".equals(httpScheme) && "443".equals(portStr)) {
							portStr = "";
						} else {
							portStr = ":" + portStr;
						}
					}
				} else {
					// TODO 设定了主机地址的情况下，由于前端web服务配置问题会导致取到本地port，会有安全问题，暂时不加port
					portStr = "";
				}

				String mipPrefix = ConstantsWeb.MIP_URL_PREFIX_STR;
				request.setAttribute("httpScheme", httpScheme);
				request.setAttribute("isSecureHttp", "https".equals(httpScheme));

				// 带contextPath的基本URL
				String siteBaseUrl = XSSHttpUtil.getForwardedRemoteProto(request) + "://" + serverName + portStr + request.getContextPath();
				String siteBaseUrlHttps = "https" + "://" + serverName + portStr + request.getContextPath();
				request.setAttribute("siteBaseUrl", siteBaseUrl);
				request.setAttribute("siteBaseUrlHttps", siteBaseUrlHttps);

				// path：不带contextPath，带有参数的路径， url：完整的url
				String thisPageParams = (XStringUtils.isBlank(request.getQueryString()) ? "" : "?" + request.getQueryString());
				request.setAttribute("thisPageParams", thisPageParams);

				String thisPagePathWithoutParams = request.getServletPath();
				String thisPageOriginalPathWithoutParams = thisPagePathWithoutParams.startsWith(mipPrefix) ? thisPagePathWithoutParams.replaceFirst(mipPrefix, "") : thisPagePathWithoutParams;
				String thisPageMipPathWithoutParams = mipPrefix + thisPageOriginalPathWithoutParams;
				request.setAttribute("thisPagePathWithoutParams", thisPagePathWithoutParams);
				request.setAttribute("thisPageOriginalPathWithoutParams", thisPageOriginalPathWithoutParams);
				request.setAttribute("thisPageMipPathWithoutParams", thisPageMipPathWithoutParams);

				String thisPageUrlWithoutParams = siteBaseUrl + thisPagePathWithoutParams;
				String thisPageMipUrlWithoutParams = siteBaseUrl + thisPageOriginalPathWithoutParams;
				String thisPageOriginalUrlWithoutParams = siteBaseUrl + thisPageMipPathWithoutParams;
				request.setAttribute("thisPageUrlWithoutParams", thisPageUrlWithoutParams);
				request.setAttribute("thisPageMipUrlWithoutParams", thisPageMipUrlWithoutParams);
				request.setAttribute("thisPageOriginalUrlWithoutParams", thisPageOriginalUrlWithoutParams);

				String thisPagePath = thisPagePathWithoutParams + thisPageParams;
				String thisPageOriginalPath = thisPageOriginalPathWithoutParams + thisPageParams;// 此处thisPagePathOriginal继续删除其他可能的路径前缀
				String thisPageMipPath = thisPageMipPathWithoutParams + thisPageParams;
				request.setAttribute("thisPagePath", thisPagePath);
				request.setAttribute("thisPageOriginalPath", thisPageOriginalPath);
				request.setAttribute("thisPageMipPath", thisPageMipPath);

				String thisPageUrl = siteBaseUrl + thisPagePath;
				String thisPageOriginalUrl = siteBaseUrl + thisPageOriginalPath;
				String thisPageMipUrl = siteBaseUrl + thisPageMipPath;
				request.setAttribute("thisPageUrl", thisPageUrl);
				request.setAttribute("thisPageOriginalUrl", thisPageOriginalUrl);
				request.setAttribute("thisPageMipUrl", thisPageMipUrl);

				// 告诉前台当前语言
				String localeLanguage = XRequestUtils.getLocaleLanguageCountry(request).toLowerCase();
				request.setAttribute("localeLanguage", localeLanguage);
				String showAllSubtitleFlag = XCookieUtils.getCookieValue(request, SiteConstants.COOKIE_SHOW_ALL_SUBTITLE_FLAG);
				request.setAttribute("showAllSubtitleFlag", showAllSubtitleFlag);

				// 告诉前台当前是否为mip页面
				boolean isMipPage = thisPagePath.startsWith(mipPrefix);
				request.setAttribute("isMipPage", isMipPage);

				// 保存最后一次访问url
				if (!thisPagePath.contains("/tool")) {
					request.getSession().setAttribute(ConstantsWeb.SITE_SESSION_PRE_VISIT_PATH, request.getSession().getAttribute(ConstantsWeb.SITE_SESSION_LAST_VISIT_PATH));
					request.getSession().setAttribute(ConstantsWeb.SITE_SESSION_PRE_VISIT_URL, request.getSession().getAttribute(ConstantsWeb.SITE_SESSION_LAST_VISIT_URL));
					request.getSession().setAttribute(ConstantsWeb.SITE_SESSION_LAST_VISIT_PATH, thisPagePath);
					request.getSession().setAttribute(ConstantsWeb.SITE_SESSION_LAST_VISIT_URL, thisPageUrl);
				}
			}
		}
	}

	/**
	 * 不希望进行网页统计的IP地址
	 */
	private boolean isExcludeRecordIp(final HttpServletRequest request) {
		String ip = XSSHttpUtil.getFirstForwardedRemoteIpAddr(request);
		if (ip == null) {
			logger.warn("无法识别IP地址。{}{}", "当前线程：", Thread.currentThread().getName());
			return true;
		}

		// 读取排除文件内容
		if (shotSiteSetup.getExcludeIpsRuleList() == null) {
			shotSiteSetup.resetExcludeIpsRuleList(request);
		}

		// 查询是否排除
		for (String excludeIpRule : shotSiteSetup.getExcludeIpsRuleList()) {
			if (XStringUtils.isNotBlank(excludeIpRule) && ip.startsWith(excludeIpRule)) {
				Integer count = excludeIpsCount.get(ip);
				Integer countAll = excludeIpsCount.get("ALL");
				Integer countRule = excludeIpsCount.get(excludeIpRule + "_rule");
				if (count == null) {
					count = 0;
				}
				if (countAll == null) {
					countAll = 0;
				}
				if (countRule == null) {
					countRule = 0;
				}
				count = count + 1;
				countAll = countAll + 1;
				countRule = countRule + 1;
				excludeIpsCount.put(ip, count);
				excludeIpsCount.put("ALL", countAll);
				excludeIpsCount.put(excludeIpRule + "_rule", countRule);
				logger.warn("当前被排除网页统计的IP地址:{}, 次数:{}, 匹配的规则IP:{}, 次数:{}, 全局已排除次数:{}, 访问的URL:{}", ip, count, excludeIpRule, countRule, countAll, request.getRequestURL());
				return true;
			}
		}

		return false;
	}

	// /**
	// *
	// * 生成网页信息
	// * @param request 请求对象
	// * @param webPathVO 网页路径对象
	// */
	// protected void generatePageData(final HttpServletRequest request, final WebPathVO webPathVO){
	//// final HtmlPageBean htmlPageBean = (HtmlPageBean)request.getAttribute(CommonConstants.HTML_PAGE_BEAN);
	//// htmlPageBean.setWebPathVO(webPathVO);
	////
	//// String title = htmlPageBean.getPageTitle();
	//// if(title == null){
	//// title = webPathVO.getPageTitle();
	//// }
	//// title = StringUtil.removeNullTrim(title);
	////
	//// final List<MenuVO> menuVOList = MenuLoad.getInstance().getAllParentMenuById(webPathVO.getMenuId());
	////
	//// final String showTitle;
	//// if("".equals(title) && !menuVOList.isEmpty()){
	//// showTitle = menuVOList.get(menuVOList.size()-1).getMenuName();
	//// }else{
	//// showTitle = title;
	//// }
	////
	//// htmlPageBean.setMenuVOList(menuVOList);
	//// htmlPageBean.setPageTitle(showTitle);
	//// htmlPageBean.setMenuVOs(menuVOList.toArray(new MenuVO[menuVOList.size()]));
	////
	//// fixHtmlPage(request,htmlPageBean);
	//
	// }
	//
	// /**
	// *
	// * 装载网页信息
	// * @param request 请求对象
	// * @param htmlPageBean 网页信息
	// */
	// protected void fixHtmlPage(final HttpServletRequest request, final HtmlPageBean htmlPageBean){
	//
	// }
}
