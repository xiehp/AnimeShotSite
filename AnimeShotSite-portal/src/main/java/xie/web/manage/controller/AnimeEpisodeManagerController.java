package xie.web.manage.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xie.animeshotsite.constants.SysConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.utils.AutoCollectUtils;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.controller.BaseManagerController;
import xie.base.module.ajax.vo.GoPageResult;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.common.date.DateUtil;
import xie.common.string.XStringUtils;
import xie.common.utils.XSSHttpUtil;
import xie.common.web.util.ConstantsWeb;
import xie.common.web.util.RequestUtil;
import xie.module.spring.SpringUtils;

@Controller
@RequestMapping(value = ConstantsWeb.MANAGE_URL_PREFIX_STR + "/animeEpisode")
public class AnimeEpisodeManagerController extends BaseManagerController<AnimeEpisode, String> {

	@Resource
	private AnimeEpisodeService animeEpisodeService;
	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private ShotTaskService shotTaskService;
	@Resource
	private SpringUtils springUtils;
	@Resource
	private AutoCollectUtils autoCollectUtils;

	@Override
	protected BaseService<AnimeEpisode, String> getBaseService() {
		return animeEpisodeService;
	}

	protected String getJspFileRootPath() {
		return "/managesite/animeEpisode";
	};

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/list/{animeInfoId}")
	public String list(@PathVariable String animeInfoId, HttpSession session, HttpServletRequest request) {

		// 获得动画 列表
		List<AnimeEpisode> animeEpisodeList = animeEpisodeService.findByAnimeInfoId(animeInfoId);
		request.setAttribute("animeEpisodeList", animeEpisodeList);

		return getJspFilePath("list");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/view/{animeEpisodeId}")
	public String view(@PathVariable String animeEpisodeId, ServletRequest request) throws Exception {

		AnimeEpisode animeEpisodeInfo = animeEpisodeService.findOne(animeEpisodeId);
		request.setAttribute("animeEpisodeInfo", animeEpisodeInfo);

		AnimeInfo animeInfo = animeInfoService.findOne(animeEpisodeInfo.getAnimeInfoId());
		request.setAttribute("animeInfo", animeInfo);

		// 自动运行参数表
		Map<String, AutoRunParam> autoRunParamList = autoRunParamService.getStringAutoRunParamMap(animeEpisodeId, false, true);
		request.setAttribute("autoRunParamList", autoRunParamList.values());

		return getJspFilePath("new");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/new")
	public String newAnime(ServletRequest request) throws Exception {
		if (request.getAttribute("animeEpisodeInfo") == null) {

			AnimeEpisode animeEpisodeInfo = new AnimeEpisode();

			animeEpisodeInfo.setAnimeInfoId(request.getParameter("animeInfoId"));
			animeEpisodeInfo.setDivisionName("第[[0]]集");
			animeEpisodeInfo.setProcessAction(0);
			animeEpisodeInfo.setShotStatus(0);
			animeEpisodeInfo.setStatus(0);
			animeEpisodeInfo.setType(0);
			animeEpisodeInfo.setLocalRootPath(FilePathUtils.getRootDefault().getAbsolutePath());
			animeEpisodeInfo.setShotLocalRootPath(FilePathUtils.getRootDefault().getAbsolutePath());
			animeEpisodeInfo.setWidth(1920);
			animeEpisodeInfo.setHeight(1080);
			animeEpisodeInfo.setShowFlg(0);
			animeEpisodeInfo.setDeleteFlag(0);

			String animeInfoId = request.getParameter("animeInfoId");
			if (XStringUtils.isNotBlank(animeInfoId)) {
				AnimeInfo animeInfo = animeInfoService.findOne(animeInfoId);
				request.setAttribute("animeInfo", animeInfo);

				if (XStringUtils.isNotBlank(animeInfo.getLocalRootPath())) {
					animeEpisodeInfo.setLocalRootPath(animeInfo.getLocalRootPath());
					animeEpisodeInfo.setShotLocalRootPath(animeInfo.getLocalRootPath());
				}

				if (XStringUtils.isNotBlank(animeInfo.getLocalDetailPath())) {
					animeEpisodeInfo.setLocalDetailPath(animeInfo.getLocalDetailPath());
					animeEpisodeInfo.setShotLocalDetailPath(animeInfo.getLocalDetailPath());
				}
			}

			request.setAttribute("animeEpisodeInfo", animeEpisodeInfo);
		}

		return getJspFilePath("new");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/submit")
	public String submit(AnimeEpisode animeEpisode, ServletRequest request) throws Exception {

		AnimeEpisode newAnimeInfo = animeEpisodeService.save(animeEpisode);
		request.setAttribute("animeEpisodeInfo", newAnimeInfo);

		return getUrlRedirectPath("view/" + newAnimeInfo.getId());
	}

	/**
	 * 根据规则生成剧集
	 * 
	 * @param extention 开始和结束扩展的最小位数，如果不指定， 则以end当前位数做扩展
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/submitMuti")
	public String submitMuti(
			@RequestParam(required = false, defaultValue = "1") Integer start,
			@RequestParam Integer end,
			@RequestParam(required = false) Integer extention,
			@RequestParam(required = false) String param,
			HttpServletRequest request) throws Exception {

		Map<String, Object> requestMap = RequestUtil.getAllParams(request);
		String firstId = animeEpisodeService.saveMuti(param, start, end, extention, requestMap);

		AnimeEpisode animeEpisodeInfo = animeEpisodeService.findOne(firstId);
		request.setAttribute("animeEpisodeInfo", animeEpisodeInfo);

		request.setAttribute("param", param);
		request.setAttribute("start", start);
		request.setAttribute("end", end);
		request.setAttribute("extention", extention);

		return getUrlRedirectPath("view/" + firstId);
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/addShotTask")
	public String addShotTask(
			@RequestParam String id,
			@RequestParam String taskType,
			@RequestParam(required = false) Date scheduleTime,
			@RequestParam(required = false) Long startTime,
			@RequestParam(required = false) Long endTime,
			@RequestParam(required = false) Long timeInterval,
			@RequestParam(required = false) String specifyTimes,
			@RequestParam(required = false) Boolean forceUpload,
			HttpServletRequest request) {

		System.out.println(springUtils);
		System.out.println(animeEpisodeService);

		if ("1".equals(taskType)) {
			shotTaskService.addRunNormalEpisideTimeTask(id, scheduleTime, forceUpload, startTime, endTime, timeInterval);
		} else if ("2".equals(taskType)) {
			if (specifyTimes == null) {
				return getUrlRedirectPath("view/" + id);
			}
			shotTaskService.addRunSpecifyEpisideTimeTask(id, scheduleTime, forceUpload, specifyTimes, XSSHttpUtil.getForwardedRemoteIpAddr(request), SysConstants.ROLE_ADMIN);
		}

		return getUrlRedirectPath("view/" + id);
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/addShotTaskAjax")
	@ResponseBody
	public Map<String, Object> addShotTaskAjax(
			@RequestParam String id,
			@RequestParam String taskType,
			@RequestParam(required = false) Date scheduleTime,
			@RequestParam(required = false) Long startTime,
			@RequestParam(required = false) Long endTime,
			@RequestParam(required = false) Long timeInterval,
			@RequestParam(required = false) String specifyTimes,
			@RequestParam(required = false) Boolean forceUpload,
			HttpServletRequest request) {

		Map<String, Object> map = null;

		System.out.println(springUtils);
		System.out.println(animeEpisodeService);

		if ("1".equals(taskType)) {
			shotTaskService.addRunNormalEpisideTimeTask(id, scheduleTime, forceUpload, startTime, endTime, timeInterval);
		} else if ("2".equals(taskType)) {
			if (specifyTimes == null) {
				map = getFailCode("type为2时，specifyTimes不能为空");
				return map;
			}
			shotTaskService.addRunSpecifyEpisideTimeTask(id, scheduleTime, forceUpload, specifyTimes, XSSHttpUtil.getForwardedRemoteIpAddr(request), SysConstants.ROLE_ADMIN);
		}

		map = getSuccessCode("操作成功");
		return map;
	}

	@Override
	public GoPageResult updateOneColumn(String id, String columnName, String columnValue, HttpServletRequest request) {
		GoPageResult goPageResult = null;
		if ("showFlg".equals(columnName) && Constants.FLAG_STR_YES.equals(columnValue)) {
			goPageResult = super.updateOneColumn(id, "showDate", DateUtil.convertToString(new Date()), request);
			if (!goPageResult.isSuccess()) {
				return goPageResult;
			}
		}

		return super.updateOneColumn(id, columnName, columnValue, request);
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/updateEpisodeTitleAndSummary")
	@ResponseBody
	public Map<String, Object> updateEpisodeTitleAndSummary(
			@RequestParam(required = false) String animeInfoId,
			@RequestParam(required = false) String episodeInfoId,
			@RequestParam(required = false, defaultValue = "false") Boolean forceUpdate,
			HttpServletRequest request) throws Exception {

		Map<String, Object> map;

		AnimeInfo animeInfo = animeInfoService.findOne(animeInfoId);

		autoCollectUtils.collectEpisodeSummary(animeInfoId, animeInfo.getSummaryCollectUrl(), animeInfo.getSummaryCollectTitleExp(), forceUpdate);

		map = getSuccessCode("操作成功");
		return map;
	}

}
