package xie.web.protal.controller.mip;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import xie.animeshotsite.constants.SysConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.GifInfo;
import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.GifInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.GifInfoService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.base.module.exception.CodeApplicationException;
import xie.common.json.XJsonUtil;
import xie.common.web.util.ConstantsWeb;
import xie.web.protal.controller.GifController;

@Controller
@RequestMapping(value = ConstantsWeb.MIP_URL_PREFIX_STR + "/gif")
public class MipGifController extends GifController {

	@Autowired
	private AnimeInfoService animeInfoService;
	@Autowired
	private AnimeInfoDao animeInfoDao;
	@Autowired
	private AnimeEpisodeService animeEpisodeService;
	@Autowired
	private AnimeEpisodeDao animeEpisodeDao;
	@Autowired
	private GifInfoService gifInfoService;
	@Autowired
	private GifInfoDao gifInfoDao;
	@Autowired
	private SubtitleInfoService subtitleInfoService;
	@Autowired
	private SubtitleLineService subtitleLineService;
	@Autowired
	private EntityCache entityCache;
	@Autowired
	private ShotTaskService shotTaskService;

	protected String getJspFileRootPath() {
		return super.getUrlRootPath();
	};

	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
			Model model, HttpServletRequest request) throws Exception {

		return super.list(pageNumber, sortType, model, request);
	}

	@RequestMapping(value = "/list/{animeEpisodeId}")
	public String listAnimeId(
			@PathVariable String animeEpisodeId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
			Model model, HttpServletRequest request)
			throws Exception {
		return super.listAnimeId(animeEpisodeId, pageNumber, sortType, model, request);
	}

	@RequestMapping(value = "/list/{animeEpisodeId}/{page}")
	public String shotList2(
			@PathVariable String animeEpisodeId,
			@PathVariable(value = "page") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
			Model model, ServletRequest request)
			throws Exception {

		return getUrlRedirectPath("list/" + animeEpisodeId + "?page=" + pageNumber);
	}

	@RequestMapping(value = "/view/{id}")
	public String shotView(
			@PathVariable String id,
			@RequestParam(required = false) String scorllTop,
			@RequestParam(required = false) List<String> showLanage,
			Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.shotView(id, scorllTop, showLanage, model, request, response);
	}

	@RequestMapping(value = "/publicLike")
	@ResponseBody
	public Map<String, Object> publicLike(@RequestParam String id) throws CodeApplicationException {
		Map<String, Object> map = null;
		GifInfo gifInfo = gifInfoService.publicLikeAdd(id);
		if (gifInfo != null) {
			map = getSuccessCode();
			map.put("newCount", gifInfo.getPublicLikeCount());
		} else {
			throw new CodeApplicationException("截图不存在");
		}

		return map;
	}

	@RequestMapping(value = "/random")
	public String random(Model model) throws Exception {
		List<GifInfo> gifInfoList = new ArrayList<GifInfo>();
		int count = (int) gifInfoDao.count();
		for (int i = 0; i < 10; i++) {
			List<GifInfo> list = gifInfoService.findRandom(count - 2, 2);
			gifInfoList.addAll(list);
		}
		model.addAttribute("gifInfoList", gifInfoList);

		return getJspFilePath("random");
	}

	@RequestMapping(value = "/task")
	public String task(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) throws Exception {
		sortType = "auto";
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_" + ShotTask.COLUMN_TASK_TYPE, ShotTask.TASK_TYPE_GIF);

		Page<ShotTask> shotTaskPage = shotTaskService.searchPageByParams(searchParams, pageNumber, 50, sortType, ShotTask.class);
		shotTaskService.fillParentData(shotTaskPage.getContent());
		model.addAttribute("shotTaskPage", shotTaskPage);

		// 生成任务相关信息
		Map<String, Map<String, Object>> taskValueMap = new HashMap<String, Map<String, Object>>();
		for (ShotTask task : shotTaskPage.getContent()) {
			Map<String, Object> paramMap = XJsonUtil.fromJsonString(task.getTaskParam());
			String animeEpisodeId = (String) paramMap.get(AnimeEpisode.COLUMN_ID);
			AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
			paramMap.put("animeEpisode", animeEpisode);

			taskValueMap.put(task.getId(), paramMap);
		}

		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return getJspFilePath("task");
	}

	@RequestMapping(value = "/addGifTask")
	public String addGifTask(
			@RequestParam String episodeInfoId,
			@RequestParam(required = false, defaultValue = "0") long startTimeMinute,
			@RequestParam long startTimeSecond,
			@RequestParam(required = false, defaultValue = "0") long startTimeMiSe,
			@RequestParam long continueTime,
			@RequestParam(required = false) String animeInfoId,
			@RequestParam(required = false) Date scheduleTime) throws CodeApplicationException {

		long startTime = startTimeMinute * 60 + startTimeSecond;
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.checkRole(SysConstants.ROLE_ADMIN);

			if (continueTime > 120) {
				continueTime = 120L;
			}
		} catch (Exception e) {
			if (continueTime > 10) {
				continueTime = 10L;
			}
		}
		shotTaskService.addCreateGifTask(animeInfoId, episodeInfoId, scheduleTime, startTime, continueTime);

		return getUrlRedirectPath("task");
	}
}
