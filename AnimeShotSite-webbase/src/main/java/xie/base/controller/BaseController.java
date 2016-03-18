package xie.base.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.google.common.collect.Maps;

import xie.base.page.PageInfo;
import xie.common.Constants;
import xie.sys.auth.service.realm.ShiroRDbRealm.ShiroUser;

public class BaseController {

	protected Logger _log = LoggerFactory.getLogger(this.getClass());

	public String getJspFilePath(String jspFileName) {
		return getJspRootPath() + jspFileName;
	}

	public String getJspForwardFilePath(String jspFileName) {
		return "forward:" + getJspFilePath(jspFileName);
	}

	public String getJspRedirectFilePath(String jspFileName) {
		return "redirect:" + getJspFilePath(jspFileName);
	}

	protected String getJspRootPath() {
		return "";
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
		map.put("code", Constants.SUCCESS_CODE);
		map.put("message", message);
		return map;
	}

	public static Map<String, Object> getFailCode(String message) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("code", Constants.FAIL_CODE);
		map.put("message", message);
		return map;
	}

}
