package xie.base.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;

import xie.base.page.PageInfo;
import xie.common.Constants;
import xie.sys.auth.service.realm.ShiroRDbRealm.ShiroUser;

public abstract class BaseController {

	protected Logger _log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 本Controll对应contextPath的root的url
	 */
	private String getUrlRootPath() {
		return defaultViewPrefix();
	}

	/**
	 * 本Controll的jsp页面的root路径，对应jsp文件的真实路径，同时要去掉mvc中定义的前后缀
	 */
	protected String getJspFileRootPath() {
		return getUrlRootPath();
	}

	/**
	 * 根据RequestMapping里的定义获得路径
	 * 
	 * @return
	 */
	private String defaultViewPrefix() {
		String currentViewPrefix = "";
		RequestMapping requestMapping = AnnotationUtils.findAnnotation(getClass(), RequestMapping.class);
		if (requestMapping != null && requestMapping.value().length > 0) {
			currentViewPrefix = requestMapping.value()[0];
		}

		// if (StringUtils.isEmpty(currentViewPrefix)) {
		// currentViewPrefix = this.entityClass.getSimpleName();
		// }

		return currentViewPrefix;
	}

	/**
	 * 获得页面jsp对应的文件路径，头和尾（.jsp）由mvc里配置
	 * 
	 * @param jspFileName 不带.jsp的jsp文件名
	 * @return
	 */
	public String getJspFilePath(String jspFileName) {
		return addSlash(getJspFileRootPath(), jspFileName);
	}

	/**
	 * 获得页面url路径
	 * 
	 * @param pageName 不带root路径的url路径
	 * @return
	 */
	public String getUrlPath(String pageName) {
		return addSlash(getUrlRootPath(), pageName);
	}

	private String addSlash(String a, String b) {
		String slash = "/";
		if (a != null && a.endsWith("/")) {
			slash = "";
		}
		if (b != null && b.startsWith("/")) {
			slash = "";
		}
		return a + slash + b;
	}

	public String getUrlForwardPath(String pageName) {
		return "forward:" + getUrlPath(pageName);
	}

	public String getUrlRedirectPath(String pageName) {
		return "redirect:" + getUrlPath(pageName);
	}

	public static String getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (user == null)
			return null;
		return user.id;
	}

	protected static <T> Page<T> getPageByList(PageInfo pageInfo, List<T> volist, Class<T> destinationClass) {
		if (volist == null || pageInfo == null) {
			return null;
		}

		if (volist.isEmpty()) {
			return new PageImpl<T>(volist, new PageRequest(pageInfo.getCurrentPageNumber(), pageInfo.getPageSize(), new Sort(pageInfo.getSortType(), pageInfo.getSortColumn())), 0);
		} else {
			return new PageImpl<T>(volist, new PageRequest(pageInfo.getCurrentPageNumber(), pageInfo.getPageSize(), new Sort(pageInfo.getSortType(), pageInfo.getSortColumn())), pageInfo.getTotalCount());
		}

	}

	protected static <T> Page<T> getPageByList(Page<?> page, List<T> volist, Class<T> destinationClass) {
		return new PageImpl<T>(volist, new PageRequest(page.getNumber(), page.getSize(), page.getSort()), page.getTotalElements());
	}

	public static Map<String, Object> getSuccessCode() {
		return getSuccessCode("");
	}

	public static Map<String, Object> getFailCode() {
		return getFailCode("");
	}

	public static Map<String, Object> getSuccessCode(String message) {
		Map<String, Object> map = Maps.newHashMap();
		map.put(Constants.JSON_RESPONSE_KEY_CODE, Constants.SUCCESS_CODE);
		map.put(Constants.JSON_RESPONSE_KEY_SUCCESS, true);
		map.put(Constants.JSON_RESPONSE_KEY_MESSAGE, message);
		return map;
	}

	public static Map<String, Object> getFailCode(String message) {
		Map<String, Object> map = Maps.newHashMap();
		map.put(Constants.JSON_RESPONSE_KEY_CODE, Constants.FAIL_CODE);
		map.put(Constants.JSON_RESPONSE_KEY_SUCCESS, false);
		map.put(Constants.JSON_RESPONSE_KEY_MESSAGE, message);
		return map;
	}
}
