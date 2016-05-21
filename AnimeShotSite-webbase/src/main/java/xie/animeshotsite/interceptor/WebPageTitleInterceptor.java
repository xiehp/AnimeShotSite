package xie.animeshotsite.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import xie.animeshotsite.setup.ShotSiteSetup;
import xie.common.Constants;
import xie.common.string.XStringUtils;
import xie.common.web.util.WebConstants;
import xie.sys.auth.service.realm.ShiroRDbRealm.ShiroUser;

/**
 * 通用处理
 */
public class WebPageTitleInterceptor extends HandlerInterceptorAdapter {
	private final static String CHANGE_PAGE_DATA_FALG_NAME = "CHANGE_PAGE_DATA_FALG_NAME";
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = false)
	private ApplicationContext applicationContext;

	@Autowired
	private ShotSiteSetup shotSiteSetup;

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

		// 判断是否是合适的host
		logger.debug("getHeader Host:{1}", request.getHeader("Host"));
		logger.debug("getServerName:{1}", request.getServerName());
		logger.debug("X-Forwarded-Host:{1}", request.getHeader("X-Forwarded-Host"));

		if (XStringUtils.isNotBlank(shotSiteSetup.getAnimesiteServerHost())) {
			String serverName = request.getHeader("X-Forwarded-Host");
			if (XStringUtils.isBlank(serverName)) {
				serverName = request.getServerName();
			}

			if ("127.0.0.1".equals(serverName) || "localhost".equals(serverName)) {
				// 来自本地，则不做跳转
			} else if ("XXXXX".equals(serverName)) {
				// 其他不需要跳转的host
			} else {
				if (!shotSiteSetup.getAnimesiteServerHost().startsWith(serverName)) {
					// serverName不符合配置文件设定的值，进行跳转
					response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					// response.sendRedirect("http://" + shotSiteSetup.getAnimesiteServerHost() + request.getRequestURI());
					response.setHeader("Location", "http://" + shotSiteSetup.getAnimesiteServerHost() + request.getRequestURI());
					return false;
				}
			}
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
		if (requestURL.endsWith(".css")) {
			isStaticUrl = true;
		} else if (requestURL.endsWith(".js")) {
			isStaticUrl = true;
		} else if (requestURL.endsWith(".png")) {
			isStaticUrl = true;
		} else if (requestURL.endsWith(".jpg")) {
			isStaticUrl = true;
		}

		if (!isStaticUrl) {
			System.out.println(request.getRequestURL());
			System.out.println(applicationContext);

			// 告诉前台当前登陆用户角色或权限
			{
				ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
				System.out.println("shiroUser:" + shiroUser);
				if (shiroUser != null && WebConstants.SITE_MANAGER_ID.equals(shiroUser.getId()) && WebConstants.SITE_MANAGER_LOGIN_ID.equals(shiroUser.getLoginName())) {
					request.setAttribute("IS_MASTER", true);
				} else {
					request.setAttribute("IS_MASTER", false);
				}
			}
			
			// 是否需要js debug
			request.setAttribute("IS_JS_DEBUG", shotSiteSetup.getAnimesiteJsDebug());

			// 判断是否需要网站统计和搜索引擎推送
			{
				request.setAttribute("canBaiduRecord", false);
				if (requestURL.contains(WebConstants.MANAGE_URL_STR)) {
					// 后台页面，不统计
					request.setAttribute("canBaiduRecord", false);
				} else {
					// 其他页面，则判断配置文件是否允许
					if ("1".equals(shotSiteSetup.getAnimesiteSearchTrafficStatistics())) {
						request.setAttribute("canBaiduRecord", true);
					}
				}
				System.out.println("canBaiduRecord: " + request.getAttribute("canBaiduRecord"));
			}

			// 百度静态资源链接
			request.setAttribute("BAIDU_STATIC_URL", "http://apps.bdimg.com/libs/");

			// 系统常量
			{
				// 其他
				request.setAttribute("MANAGE_URL_STR", WebConstants.MANAGE_URL_STR);

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
			}
		}
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
