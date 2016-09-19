package xie.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.service.ImageUrlService;
import xie.animeshotsite.setup.ShotSiteSetup;
import xie.base.controller.BaseController;
import xie.common.string.XStringUtils;

@Controller
@RequestMapping(value = "/tool")
public class ToolController extends BaseController {

	@Autowired
	EntityCache entityCache;
	@Autowired
	ImageUrlService imageUrlService;
	@Autowired
	ShotSiteSetup shotSiteSetup;

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/cleanCache")
	@ResponseBody
	public Map<String, Object> cleanCache(@RequestParam(required = false) String type, HttpServletRequest request) {
		Map<String, Object> map = getSuccessCode();

		// 清除缓存
		if (XStringUtils.isBlank(type)) {
			int size = entityCache.clear();
			map.put("size", size);
		}

		// 重新获取排除IP地址
		shotSiteSetup.resetExcludeIpsRuleList(request);

		return map;
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/setAnimeTitleImage")
	@ResponseBody
	public Map<String, Object> setAnimeTitleImage(
			@RequestParam(required = false) String animeInfoId,
			@RequestParam(required = false) String animeEpisodeId,
			@RequestParam(required = true) String animeShotId) {

		Map<String, Object> map;
		try {
			if (XStringUtils.isBlank(animeInfoId) && XStringUtils.isBlank(animeEpisodeId)) {
				throw new Exception("动画ID和剧集ID不能同时为空");
			}

			if (XStringUtils.isNotBlank(animeInfoId) && XStringUtils.isNotBlank(animeEpisodeId)) {
				throw new Exception("动画ID和剧集ID不能同时有值");
			}

			if (XStringUtils.isNotBlank(animeInfoId)) {
				imageUrlService.saveAnimeTitleImage(animeInfoId, animeShotId);
			} else {
				imageUrlService.saveEpisodeTitleImage(animeEpisodeId, animeShotId);
			}

			map = getSuccessCode();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
			map = getFailCode(e.getMessage());
		}

		return map;
	}
}
