package xie.web.controller;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

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
	@Autowired
	protected MessageSource messageSource;

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

	@RequestMapping(value = "/changeLanguage")
	@ResponseBody
	public ModelMap changeLanguage(
			@RequestParam String new_lang,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map;

		try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (localeResolver == null) {
				throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
			}

			if (XStringUtils.isNotBlank(new_lang)) {
				LocaleEditor localeEditor = new LocaleEditor();
				localeEditor.setAsText(new_lang);
				localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());
				map = getSuccessCode(messageSource.getMessage("切换语言成功", null, localeResolver.resolveLocale(request)));
			} else {
				localeResolver.setLocale(request, response, null);
				map = getSuccessCode(messageSource.getMessage("清除语言成功", null, localeResolver.resolveLocale(request)));
			}

		} catch (Exception ex) {
			map = getFailCode("切换语言失败");
		}
		return new ModelMap(map);
	}
}
