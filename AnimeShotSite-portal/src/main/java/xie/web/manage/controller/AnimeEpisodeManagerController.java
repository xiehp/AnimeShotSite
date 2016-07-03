package xie.web.manage.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.controller.BaseManagerController;
import xie.base.service.BaseService;
import xie.common.date.DateUtil;
import xie.common.string.XStringUtils;
import xie.common.utils.SpringUtils;
import xie.common.web.util.RequestUtil;
import xie.common.web.util.ShotWebConstants;

@Controller
@RequestMapping(value = ShotWebConstants.MANAGE_URL_STR + "/animeEpisode")
public class AnimeEpisodeManagerController extends BaseManagerController<AnimeEpisode, String> {

	@Autowired
	private AnimeEpisodeService animeEpisodeService;
	@Autowired
	private AnimeInfoService animeInfoService;
	@Autowired
	private ShotTaskService shotTaskService;
	@Autowired
	private SpringUtils springUtils;

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
			@RequestParam(required = false) Boolean forceUpload) {

		Map<String, Object> map = null;

		System.out.println(springUtils);
		System.out.println(animeEpisodeService);

		if ("1".equals(taskType)) {
			shotTaskService.addRunNormalEpisideTimeTask(id, scheduleTime, forceUpload, startTime, endTime, timeInterval);
		} else if ("2".equals(taskType)) {
			if (specifyTimes == null) {
				map = getFailCode("type为2时，specifyTimes不能为空");
			}
			shotTaskService.addRunSpecifyEpisideTimeTask(id, scheduleTime, forceUpload, specifyTimes);
		}

		map = getSuccessCode();

		return getUrlRedirectPath("view/" + id);
	}

	@Override
	public Map<String, Object> updateOneColumn(String id, String columnName, String columnValue) {
		if ("showFlg".equals(columnName)) {
			super.updateOneColumn(id, "showDate", DateUtil.convertToString(new Date()));
		}
		return super.updateOneColumn(id, columnName, columnValue);
	}
}
