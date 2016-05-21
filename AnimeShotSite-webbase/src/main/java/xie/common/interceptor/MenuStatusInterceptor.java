package xie.common.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import xie.common.Constants;
import xie.common.utils.ResourceLoader;
import xie.sys.auth.entity.Resource;

public class MenuStatusInterceptor implements HandlerInterceptor {

	// private String[] exceptUrlList = new String[]{
	// "/web/webLogin",
	// "/web/index",
	// "/web"
	// };

	private List<String> exceptUrlList;

	{
		exceptUrlList = new ArrayList<String>();
		exceptUrlList.add("/rating/webLogin");
		exceptUrlList.add("/rating/index");
		exceptUrlList.add("/rating");
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		String url = request.getRequestURI();

		exceptUrlList = new ArrayList<String>();
		exceptUrlList.add(request.getContextPath() + "/webLogin");
		exceptUrlList.add(request.getContextPath() + "/index");
		exceptUrlList.add(request.getContextPath());
		if (exceptUrlList.contains(url)) {
			return;
		}

		String resourcePath = getResourceUrl(url);

		Map<String, Resource> urlMap = ResourceLoader.getInstance().getUrlMap();
		Map<String, Resource> idMap = ResourceLoader.getInstance().getIdMap();
		// 获取当前菜单
		Resource currResource = urlMap.get(resourcePath);
		if (currResource == null || currResource.getResourceLevel() == Constants.RESOURCE_LEVEL_ONE) {
			return;
		}

		// Resource parentResource = idMap.get(currResource.getParentId());

		setMenuStatus(currResource, request, urlMap, idMap);
	}

	private void setMenuStatus(Resource resource, HttpServletRequest request, Map<String, Resource> urlMap, Map<String, Resource> idMap) {
		if (resource == null || resource.getResourceLevel() == Constants.RESOURCE_LEVEL_ONE) {
			return;
		}
		if (resource.getResourceLevel() == Constants.RESOURCE_LEVEL_FOUR) {
			if (Constants.FLAG_INT_YES.equals(resource.getIsShow())) {
				// 资源显示标志
				request.setAttribute(Constants.FOURTH_MENU_STATUA_KEY, resource.getIdentity());
			}
		} else if (resource.getResourceLevel() == Constants.RESOURCE_LEVEL_THREE) {
			request.setAttribute(Constants.THIRD_MENU_STATUA_KEY, resource.getIdentity());
		} else if (resource.getResourceLevel() == Constants.RESOURCE_LEVEL_TWO) {
			request.setAttribute(Constants.SECOND_MENU_STATUA_KEY, resource.getIdentity());
		}
		resource = idMap.get(resource.getParentId());
		setMenuStatus(resource, request, urlMap, idMap);
	}

	private String getResourceUrl(String requestUrl) {
		String[] resources = requestUrl.split("/");
		StringBuffer resourceUrl = new StringBuffer("");
		// for(int i=2; i<4; i++) {
		for (int i = 2; i < resources.length; i++) {
			resourceUrl.append("/").append(resources[i]);
		}
		return resourceUrl.toString();
	}

}
