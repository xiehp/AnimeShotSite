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
import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.entity.SubtitleLine;
import xie.animeshotsite.db.repository.SubtitleInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.controller.BaseManagerController;
import xie.base.service.BaseService;
import xie.common.string.XStringUtils;
import xie.common.web.util.ConstantsWeb;
import xie.common.web.util.RequestUtil;

@Controller
@RequestMapping(value = ConstantsWeb.MANAGE_URL_STR + "/subtitle")
public class SubtitleManagerController extends BaseManagerController<SubtitleInfo, String> {

	@Autowired
	private SubtitleInfoService subtitleInfoService;
	@Autowired
	private SubtitleLineService subtitleLineService;
	@Autowired
	private SubtitleInfoDao subtitleInfoDao;
	@Autowired
	private AnimeInfoService animeInfoService;
	@Autowired
	private AnimeEpisodeService animeEpisodeService;
	@Autowired
	private ShotTaskService shotTaskService;
	@Autowired
	private AnimeManagerController animeController;

	@Override
	protected BaseService<SubtitleInfo, String> getBaseService() {
		return subtitleInfoService;
	}

	protected String getJspFileRootPath() {
		return "/managesite/subtitle";
	};

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/list/{animeInfoId}")
	public String list(@PathVariable String animeInfoId, HttpSession session, HttpServletRequest request) {

		// 获得字幕列表
		List<SubtitleInfo> subtitleInfoList = subtitleInfoDao.findByAnimeInfoIdOrderByLocalFileName(animeInfoId);
		request.setAttribute("subtitleInfoList", subtitleInfoList);

		return getJspFilePath("list");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/view/{subtitleInfoId}")
	public String view(@PathVariable String subtitleInfoId, ServletRequest request) throws Exception {

		SubtitleInfo subtitleInfo = getBaseService().findOne(subtitleInfoId);
		request.setAttribute("subtitleInfo", subtitleInfo);
		AnimeInfo animeInfo = animeInfoService.findOne(subtitleInfo.getAnimeInfoId());
		request.setAttribute("animeInfo", animeInfo);
		AnimeEpisode animeEpisode = animeEpisodeService.findOne(subtitleInfo.getAnimeEpisodeId());
		request.setAttribute("animeEpisode", animeEpisode);

		List<SubtitleLine> subtitleLineList = subtitleLineService.findBySubtitleInfoId(subtitleInfoId);
		request.setAttribute("subtitleLineList", subtitleLineList);

		return getJspFilePath("new");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/new")
	public String newAnime(ServletRequest request) throws Exception {
		if (request.getAttribute("subtitleInfo") == null) {

			String animeInfoId = request.getParameter("animeInfoId");
			SubtitleInfo subtitleInfo = new SubtitleInfo();

			subtitleInfo.setAnimeInfoId(animeInfoId);
			subtitleInfo.setFileType("ass");

			subtitleInfo.setLocalRootPath(FilePathUtils.getAnimeRootDefault().getAbsolutePath());
			subtitleInfo.setLocalDetailPath("");
			subtitleInfo.setShowFlg(1);
			subtitleInfo.setFileLocation("1");
			subtitleInfo.setDeleteFlag(0);
			subtitleInfo.setLanguage("sc");
			subtitleInfo.setFileLocation("1");

			if (XStringUtils.isNotBlank(animeInfoId)) {
				AnimeInfo animeInfo = animeInfoService.findOne(animeInfoId);
				request.setAttribute("animeInfo", animeInfo);

				if (XStringUtils.isNotBlank(animeInfo.getLocalRootPath())) {
					subtitleInfo.setLocalRootPath(FilePathUtils.getAnimeRoot(animeInfo, null).getAbsolutePath());
				}

				if (XStringUtils.isNotBlank(animeInfo.getLocalDetailPath())) {
					subtitleInfo.setLocalDetailPath(animeInfo.getLocalDetailPath() + "\\字幕");
				}
			}

			request.setAttribute("subtitleInfo", subtitleInfo);
		}

		return getJspFilePath("new");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/submit")
	public String submit(SubtitleInfo subtitleInfo, ServletRequest request) throws Exception {

		SubtitleInfo newSubtitleInfo = getBaseService().save(subtitleInfo);
		request.setAttribute("subtitleInfo", newSubtitleInfo);

		return getUrlRedirectPath("view/" + subtitleInfo.getId());
	}

	/**
	 * 根据规则生成剧集
	 * 
	 * @param extention 开始和结束扩展的最小位数，如果不指定， 则以end当前位数做扩展
	 * @param param 参数
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

		String animeInfoId = (String) requestMap.get("animeInfoId");
		List<AnimeEpisode> list = animeEpisodeService.findByAnimeInfoId(animeInfoId);
		if (end == null || end > list.size()) {
			end = list.size();
		}
		SubtitleInfo firstSubtitleInfo = null;
		for (int i = start - 1; i < end; i++) {
			AnimeEpisode animeEpisode = list.get(i);
			requestMap.put("animeEpisodeId", animeEpisode.getId());
			SubtitleInfo subtitleInfo = getBaseService().saveMuti(param, i + 1, i + 1, extention, requestMap, SubtitleInfo.class);
			if (firstSubtitleInfo == null) {
				firstSubtitleInfo = subtitleInfo;
			}
		}

		request.setAttribute("subtitleInfo", firstSubtitleInfo);

		request.setAttribute("param", param);
		request.setAttribute("start", start);
		request.setAttribute("end", end);
		request.setAttribute("extention", extention);

		return getUrlRedirectPath("view/" + firstSubtitleInfo.getId());
	}

	/**
	 * 根据当前已有剧集以及规则生成字幕信息<br>
	 * 通过位数扩展生成的值和语言作为唯一标识，如果字幕中已存在，则不生成<br>
	 * 通过位数扩展生成的值，和剧集中的唯一标识符对应<br>
	 * 
	 * @param extention 开始和结束扩展的最小位数，如果不指定， 则以end当前位数做扩展
	 * @param param 参数
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/submitMutiByEpisode")
	public String submitMutiByEpisode(
			@RequestParam(required = false, defaultValue = "1") Integer start,
			@RequestParam Integer end,
			@RequestParam(required = false) Integer extention,
			@RequestParam(required = false) String param,
			HttpServletRequest request) throws Exception {

		Map<String, Object> requestMap = RequestUtil.getAllParams(request);

		SubtitleInfo firstSubtitleInfo = null;
		for (int i = start - 1; i < end; i++) {
			SubtitleInfo subtitleInfo = subtitleInfoService.saveMutiByEpisode(param, i + 1, i + 1, extention, requestMap, SubtitleInfo.class);
			if (firstSubtitleInfo == null) {
				firstSubtitleInfo = subtitleInfo;
			}
		}

		request.setAttribute("subtitleInfo", firstSubtitleInfo);

		request.setAttribute("param", param);
		request.setAttribute("start", start);
		request.setAttribute("end", end);
		request.setAttribute("extention", extention);

		return getUrlRedirectPath("view/" + firstSubtitleInfo.getId());
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/createSubtitleTask")
	public String createSubtitleTask(
			@RequestParam String id,
			@RequestParam String subtitleInfoId,
			@RequestParam String animeInfoId,
			@RequestParam String taskType,
			@RequestParam(required = false) Date scheduleTime,
			@RequestParam(required = false) Long startTime,
			@RequestParam(required = false) Long endTime,
			@RequestParam(required = false) Long timeInterval,
			@RequestParam(required = false) String specifyTimes,
			@RequestParam(required = false) Boolean forceUpdate,
			@RequestParam(required = false) Boolean forceDelete) {

		shotTaskService.addCreateSubtitleTask(subtitleInfoId, animeInfoId, scheduleTime, forceUpdate, forceDelete);

		if (XStringUtils.isNotBlank(subtitleInfoId)) {
			return getUrlRedirectPath("view/" + subtitleInfoId);
		} else if (XStringUtils.isNotBlank(id)) {
			return getUrlRedirectPath("view/" + id);
		} else {
			return animeController.getUrlRedirectPath("view/" + subtitleInfoId);
		}
	}
}
