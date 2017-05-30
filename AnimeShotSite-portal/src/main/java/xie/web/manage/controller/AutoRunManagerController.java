package xie.web.manage.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.AutoRunParamService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.base.controller.BaseManagerController;
import xie.base.module.ajax.vo.GoPageResult;
import xie.base.service.BaseService;
import xie.common.web.util.ConstantsWeb;

@Controller
@RequestMapping(value = ConstantsWeb.MANAGE_URL_PREFIX_STR + "/autorun")
public class AutoRunManagerController extends BaseManagerController<ShotInfo, String> {
	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private AnimeEpisodeService animeEpisodeService;
	@Resource
	private ShotInfoService shotInfoService;
	@Resource
	private AutoRunParamService autoRunParamService;

	@Override
	protected BaseService<ShotInfo, String> getBaseService() {
		return shotInfoService;
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/beginMonitor")
	@ResponseBody
	public GoPageResult beginMonitor(
			@RequestParam(required = false) String animeInfoId,
			@RequestParam(required = false) String animeEpisodeId,
			HttpServletRequest request) {

		if (animeEpisodeId != null) {
			autoRunParamService.beginEpisodeMonitor(animeEpisodeService.findOne(animeEpisodeId).getAnimeInfoId(), animeEpisodeId, false);
		} else {
			autoRunParamService.beginMonitor(animeInfoId);
		}

		GoPageResult goPageResult = goPageUtil.createSuccess(request);
		return goPageResult;
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/stopMonitor")
	@ResponseBody
	public GoPageResult stopMonitor(
			@RequestParam(required = false) String animeInfoId,
			@RequestParam(required = false) String animeEpisodeId,
			HttpServletRequest request) {

		if (animeEpisodeId != null) {
			autoRunParamService.stopMonitor(animeEpisodeId);
		} else {
			autoRunParamService.stopMonitor(animeInfoId);
		}

		GoPageResult goPageResult = goPageUtil.createSuccess(request);
		return goPageResult;
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/updateOneAutoRunParam")
	@ResponseBody
	public GoPageResult updateOneAutoRunParam(
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String animeInfoId,
			@RequestParam(required = false) String animeEpisodeId,
			@RequestParam(required = false) String refId,
			@RequestParam(required = false) String refType,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String key,
			@RequestParam(required = false) String value,
			HttpServletRequest request) {

		autoRunParamService.saveOrUpdateOneAutoRunParam(id, animeInfoId, animeEpisodeId, refId, refType, name, key, value);

		GoPageResult goPageResult = goPageUtil.createSuccess(request);
		return goPageResult;
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/deleteOneAutoRunParam")
	@ResponseBody
	public GoPageResult deleteOneAutoRunParam(
			@RequestParam String id,
			HttpServletRequest request) {

		GoPageResult goPageResult;
		try {
			autoRunParamService.delete(id);
			goPageResult = goPageUtil.createSuccess(request);

		} catch (Exception e) {
			_log.error("updateOneColumn发生错误", e);
			goPageResult = goPageUtil.createFail(request);
		}

		return goPageResult;
	}

}
