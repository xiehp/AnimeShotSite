package xie.web.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.base.controller.BaseController;
import xie.common.string.XStringUtils;

@Controller
public class ToolController extends BaseController {

	@Autowired
	EntityCache entityCache;

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/cleanCache")
	@ResponseBody
	public Map<String, Object> masterLike(
			@RequestParam(required = false) String type) {
		Map<String, Object> map = getSuccessCode();
		if (XStringUtils.isBlank(type)) {
			int size = entityCache.clear();
			map.put("size", size);
		}

		return map;
	}
}
