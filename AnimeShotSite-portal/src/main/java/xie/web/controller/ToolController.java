package xie.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springside.modules.mapper.BeanMapper;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.service.ImageUrlService;
import xie.animeshotsite.setup.ShotSiteSetup;
import xie.animeshotsite.setup.UserConfig;
import xie.animeshotsite.utils.SiteUtils;
import xie.base.controller.BaseController;
import xie.base.module.ajax.vo.GoPageResult;
import xie.common.Constants;
import xie.common.json.XJsonUtil;
import xie.common.string.XStringUtils;
import xie.common.utils.XCookieUtils;
import xie.common.web.util.ConstantsWeb;
import xie.module.language.XELangLocal;
import xie.module.language.translate.baidu.XELangBaidu;
import xie.web.util.SiteConstants;

@Controller
@RequestMapping(value = "/tool")
public class ToolController extends BaseController {

	@Resource
	EntityCache entityCache;
	@Resource
	ImageUrlService imageUrlService;
	@Resource
	ShotSiteSetup shotSiteSetup;
	@Resource
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

	/**
	 * 切换是否显示所有字幕
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/changeShowAllSubtitle")
	public String changeShowAllSubtitle(
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		String showAllSubtitleFlag = XCookieUtils.getCookieValue(request, SiteConstants.COOKIE_SHOW_ALL_SUBTITLE_FLAG);
		String newFlag = Constants.FLAG_STR_YES.equals(showAllSubtitleFlag) ? Constants.FLAG_STR_NO : Constants.FLAG_STR_YES;
		XCookieUtils.addCookieValue(response, SiteConstants.COOKIE_SHOW_ALL_SUBTITLE_FLAG, newFlag);

		return "redirect:" + request.getSession().getAttribute(ConstantsWeb.SITE_SESSION_LAST_VISIT_PATH);
	}

	@RequestMapping(value = "/changeLanguage")
	@ResponseBody
	public GoPageResult changeLanguage(
			@RequestParam String new_lang,
			HttpServletRequest request, HttpServletResponse response) {
		GoPageResult goPageResult = null;

		try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (localeResolver == null) {
				throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
			}

			if ("clear".equals(new_lang)) {
				localeResolver.setLocale(request, response, null);
				// map = getSuccessCode(messageSource.getMessage("清除语言成功", null, localeResolver.resolveLocale(request)));
				goPageResult = createSuccess(request, "清除语言成功");
			} else if (XStringUtils.isNotBlank(new_lang)) {
				LocaleEditor localeEditor = new LocaleEditor();
				localeEditor.setAsText(new_lang);
				localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());
				// map = getSuccessCode(messageSource.getMessage("切换语言成功", null, localeResolver.resolveLocale(request)));
				goPageResult = createSuccess(request, "切换语言成功");
			} else {
				localeResolver.setLocale(request, response, null);
				// map = getSuccessCode(messageSource.getMessage("清除语言成功", null, localeResolver.resolveLocale(request)));
				goPageResult = createSuccess(request, "清除语言成功");
			}

			goPageResult.setNotAlertFlag(true);
		} catch (Exception ex) {
			// map = getFailCode("切换语言失败");
			goPageResult = createFail(request, "切换语言失败");
		}

		return goPageResult;
	}

	@RequestMapping(value = "/changeLanguage/{new_lang}")
	public String changeLanguageForm(
			@PathVariable String new_lang,
			HttpServletRequest request, HttpServletResponse response) {

		changeLanguage(new_lang, request, response);

		return "redirect:" + request.getSession().getAttribute(ConstantsWeb.SITE_SESSION_LAST_VISIT_PATH);
	}

	/**
	 * 改变翻译语言
	 */
	@RequestMapping(value = "/changeTranLan")
	@ResponseBody
	public GoPageResult changeTranLan(
			@RequestParam String newTranLan,
			HttpServletRequest request, HttpServletResponse response) {
		GoPageResult goPageResult = null;

		try {
			UserConfig userConfig = SiteUtils.getUserConfig(request);
			if ("notTranFlag".equals(newTranLan)) {
				userConfig.setNotTranFlag(!userConfig.isNotTranFlag());
			} else if ("defaultLan".equals(newTranLan)) {
				userConfig.setTranLanguage(null);
			} else {
				userConfig.setTranLanguage(XELangLocal.parseValue(newTranLan));
				userConfig.setNotTranFlag(false);
			}
			// map = getSuccessCode(messageSource.getMessage("切换翻译语言成功", null, localeResolver.resolveLocale(request)));
			goPageResult = createSuccess(request, "切换翻译语言成功");
			goPageResult.setNotAlertFlag(true);
		} catch (Exception ex) {
			// map = getFailCode("切换翻译语言失败");
			goPageResult = createFail(request, "切换翻译语言失败");
		}
		return goPageResult;
	}

	@RequestMapping(value = "/changeTranLan/{newTranLan}")
	public String changeTranLanForm(
			@PathVariable String newTranLan,
			HttpServletRequest request, HttpServletResponse response) {

		changeTranLan(newTranLan, request, response);

		return "redirect:" + request.getSession().getAttribute(ConstantsWeb.SITE_SESSION_LAST_VISIT_PATH);
	}

	/**
	 * 改变翻译颜色
	 */
	@RequestMapping(value = "/changeTranLanColor")
	@ResponseBody
	public GoPageResult changeTranLanColor(
			@RequestParam String newTranLanColor,
			HttpServletRequest request, HttpServletResponse response) {
		GoPageResult goPageResult = null;

		try {
			UserConfig userConfig = SiteUtils.getUserConfig(request);
			userConfig.setTranLanguageColor(newTranLanColor);
			// map = getSuccessCode(messageSource.getMessage("切换翻译颜色成功", null, localeResolver.resolveLocale(request)));
			goPageResult = createSuccess(request, "切换翻译颜色成功");
			//goPageResult.setNotAlertFlag(true);
		} catch (Exception ex) {
			// map = getFailCode(messageSource.getMessage("切换翻译颜色失败", null, localeResolver.resolveLocale(request)));
			goPageResult = createFail(request, "切换翻译颜色失败");
		}
		return goPageResult;
	}

	@RequestMapping(value = "/changeTranLanColor/{newTranLanColor}")
	public String changeTranLanColorForm(
			@PathVariable String newTranLanColor,
			HttpServletRequest request, HttpServletResponse response) {

		changeTranLanColor(newTranLanColor, request, response);

		return "redirect:" + request.getSession().getAttribute(ConstantsWeb.SITE_SESSION_LAST_VISIT_PATH);
	}

	/**
	 * 改变翻译字体大小
	 */
	@RequestMapping(value = "/changeTranLanFontsize")
	@ResponseBody
	public GoPageResult changeTranLanFontsize(
			@RequestParam String newTranLanFonsize,
			HttpServletRequest request, HttpServletResponse response) {
		GoPageResult goPageResult = null;

		try {
			UserConfig userConfig = SiteUtils.getUserConfig(request);
			userConfig.setTranLanFonsize(newTranLanFonsize);
			// map = getSuccessCode(messageSource.getMessage("切换翻译字体大小成功", null, localeResolver.resolveLocale(request)));
			goPageResult = createSuccess(request, "切换翻译字体大小成功");
			goPageResult.setNotAlertFlag(true);
		} catch (Exception ex) {
			// map = getFailCode("切换翻译字体大小失败");
			goPageResult = createFail(request, "切换翻译字体大小失败");
		}
		return goPageResult;
	}

	@RequestMapping(value = "/changeTranLanFontsize/{newTranLanFonsize}")
	public String changeTranLanFontsizeForm(
			@PathVariable String newTranLanFonsize,
			HttpServletRequest request, HttpServletResponse response) {

		changeTranLanFontsize(newTranLanFonsize, request, response);

		return "redirect:" + request.getSession().getAttribute(ConstantsWeb.SITE_SESSION_LAST_VISIT_PATH);
	}

	@RequestMapping(value = "/getTranLanList")
	@ResponseBody
	public Map<String, Object> getTranLanList(
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		GoPageResult goPageResult = null;

		try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			UserConfig userConfig = SiteUtils.getUserConfig(request);

			List<XELangBaidu> listBaiduLan = XELangBaidu.values(XELangBaidu.class);
			listBaiduLan.remove(XELangBaidu.auto);

			List<Map<String, Object>> tranLanList = new ArrayList<>();

			{
				// 增加不显示
				Map<String, Object> notTranFlagMap = new HashMap<>();
				notTranFlagMap.put("value", "notTranFlag");
				notTranFlagMap.put("name", messageSource.getMessage("不显示", null, localeResolver.resolveLocale(request)));
				notTranFlagMap.put("originalName", messageSource.getMessage("不显示", null, localeResolver.resolveLocale(request)));
				notTranFlagMap.put("selectedFlag", userConfig.isNotTranFlag() == true);
				tranLanList.add(notTranFlagMap);
			}

			{
				// 增加默认
				Map<String, Object> defaultMap = new HashMap<>();
				defaultMap.put("value", "defaultLan");
				defaultMap.put("name", messageSource.getMessage("默认", null, localeResolver.resolveLocale(request)));
				defaultMap.put("originalName", messageSource.getMessage("默认", null, localeResolver.resolveLocale(request)));
				defaultMap.put("selectedFlag", userConfig.getTranLanguage() == null);
				tranLanList.add(defaultMap);
			}

			// 增加百度的可翻译语言
			listBaiduLan.forEach(lanEnum -> {
				XELangLocal local = lanEnum.getLangLocal();
				if (local != null) {
					Map<String, Object> lanMap = new HashMap<>();
					tranLanList.add(lanMap);

					lanMap.put("value", local.getValue());
					lanMap.put("name", lanEnum.getName());
					lanMap.put("originalName", lanEnum.getOriginalName());
					if (userConfig.getTranLanguage() != null) {
						lanMap.put("selectedFlag", userConfig.getTranLanguage().equals((local)));
					}
				}
			});
			data.put("tranLanList", tranLanList);
			data.put("items", tranLanList);
			data.put("isEnd", 1);

			// 其他信息
			if (userConfig.getTranLanguage() != null) {
				data.put("nowTranLan", userConfig.getTranLanguage().getValue());
			}
			data.put("notTranFlag", userConfig.isNotTranFlag());
			data.put("tranLanColor", userConfig.getTranLanguageColor());
			data.put("tranLanFontsize", userConfig.getTranLanFonsize());

			// result = getSuccessCode();
			goPageResult = createSuccess(request, null);
			goPageResult.setData(data);
			goPageResult.setNotAlertFlag(true);
			BeanMapper.copy(goPageResult, result);
			result.put("data", data);
			result.put("status", 0); // mip用

		} catch (Exception ex) {
			// result = getFailCode("获得翻译列表失败");
			goPageResult = createFail(request, "获得翻译列表失败");
			BeanMapper.copy(goPageResult, result);
			result.put("status", 1); // mip用
		}
		return result;
	}

	@RequestMapping(value = "/getTranLanList.jsonp")
	@ResponseBody
	public String getTranLanList_jsonp(
			@RequestParam String callback,
			HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = getTranLanList(request, response);
		String jsonData = XJsonUtil.toJsonString(map);
		String returnStr = callback + "(" + jsonData + ")";
		return returnStr;
	}
}
