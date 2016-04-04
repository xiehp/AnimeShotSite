package xie.animeshotsite.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import xie.common.Constants;
import xie.common.web.util.WebConstants;

/**
 * 
 * 网页标题信息的拦截器类
 *
 * <pre>
 * Pattern : Value Object
 * Thread Safe : No
 *
 * Change History
 *
 * Name                 Date                    Description
 * -------              -------                 -----------------
 * 020191              2014-3-31            Create the class
 *
 * </pre>
 *
 * @author 020191
 * @version 1.0
 */
public class WebPageTitleInterceptor extends HandlerInterceptorAdapter {
	private final static String CHANGE_PAGE_DATA_FALG_NAME = "CHANGE_PAGE_DATA_FALG_NAME";
	
	/**
	 * 
	 * 修改网页标题信息
	 * @param request   请求对象
	 * @param response  回复对象
	 * @param handler   提交对象
	 * @return  成功则返回true，失败则返回false
	 */
	@Override
	public boolean preHandle(final HttpServletRequest request,final HttpServletResponse response,
			final Object handler) {		

		//set constants to request attribute
//		request.setAttribute("FLAG_YES", CommonConstants.FLAG_YES);
//		request.setAttribute("FLAG_NO", CommonConstants.FLAG_NO);
//		request.setAttribute("FLAG_INT_YES", CommonConstants.FLAG_INT_YES);
//		request.setAttribute("FLAG_INT_NO", CommonConstants.FLAG_INT_NO);
//		request.setAttribute("STATE_NORMAL", CommonConstants.STATE_NORMAL);
//		request.setAttribute("STATE_STOP", CommonConstants.STATE_STOP);
		
		return true;
	}
	
	/**
	 * 
	 * 如果网页数据修改标识为'Y',则生成新的网页信息
	 * @param request   请求对象
	 * @param response  回复对象
	 * @param handler   提交对象
	 * @param modelAndView   可视化对象
	 */
	@Override
	public void postHandle(final HttpServletRequest request,final HttpServletResponse response,final Object handler,final ModelAndView modelAndView) {
//		final WebPathVO webPathVO = ThreadUtil.getWebPathVO();
//
//		if(CommonConstants.FLAG_YES.equals(request.getAttribute(CHANGE_PAGE_DATA_FALG_NAME))){
//			generatePageData(request , webPathVO);
//		}

		// 传给jsp页面的常量
		// 后台管理页面
		if (request.getRequestURI().contains(WebConstants.MANAGE_URL_STR)) {
			System.out.println(request.getRequestURI());
			request.setAttribute("canBaiduRecord", false);
		} else {
			request.setAttribute("canBaiduRecord", true);
		}
		request.setAttribute("MANAGE_URL_STR", WebConstants.MANAGE_URL_STR);

		// 百度静态资源链接
		request.setAttribute("BAIDU_STATIC_URL", "http://apps.bdimg.com/libs/");
		
		// 系统常量
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
	}
	
//	/**
//	 * 
//	 * 生成网页信息
//	 * @param request   请求对象
//	 * @param webPathVO   网页路径对象
//	 */
//	protected void generatePageData(final HttpServletRequest request, final WebPathVO webPathVO){
////		final HtmlPageBean htmlPageBean = (HtmlPageBean)request.getAttribute(CommonConstants.HTML_PAGE_BEAN);
////		htmlPageBean.setWebPathVO(webPathVO);
////		
////		String title = htmlPageBean.getPageTitle();
////		if(title == null){
////			title = webPathVO.getPageTitle();
////		}
////		title = StringUtil.removeNullTrim(title);
////		
////		final List<MenuVO> menuVOList = MenuLoad.getInstance().getAllParentMenuById(webPathVO.getMenuId());
////
////		final String showTitle;
////		if("".equals(title) && !menuVOList.isEmpty()){
////			showTitle = menuVOList.get(menuVOList.size()-1).getMenuName();
////		}else{
////			showTitle = title;
////		}
////		
////		htmlPageBean.setMenuVOList(menuVOList);
////		htmlPageBean.setPageTitle(showTitle);
////		htmlPageBean.setMenuVOs(menuVOList.toArray(new MenuVO[menuVOList.size()]));
////		
////		fixHtmlPage(request,htmlPageBean);
//		
//	}
//	
//	/**
//	 * 
//	 * 装载网页信息
//	 * @param request  请求对象
//	 * @param htmlPageBean   网页信息
//	 */
//	protected void fixHtmlPage(final HttpServletRequest request, final HtmlPageBean htmlPageBean){
//		
//	}
}
