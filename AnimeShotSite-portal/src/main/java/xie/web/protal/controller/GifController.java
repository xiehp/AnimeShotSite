package xie.web.protal.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.GifInfo;
import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.entity.SubtitleLine;
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
import xie.base.controller.BaseFunctionController;
import xie.common.Constants;
import xie.common.date.DateUtil;
import xie.common.json.XJsonUtil;
import xie.common.utils.XCookieUtils;
import xie.common.utils.XRequestUtils;
import xie.common.web.util.ConstantsWeb;
import xie.web.util.SiteConstants;

@Controller
@RequestMapping(value = "/gif")
public class GifController extends BaseFunctionController<GifInfo, String> {

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
		return "/gif/";
	};

	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
			Model model, HttpServletRequest request) throws Exception {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_" + GifInfo.COLUMN_DELETE_FLAG, Constants.FLAG_INT_NO);
		sortType = "auto";

		Page<GifInfo> gifInfoPage = gifInfoService.searchPageByParams(searchParams, pageNumber, ConstantsWeb.SHOT_LIST_PAGE_NUMBER, sortType, GifInfo.class);
		gifInfoService.fillParentData(gifInfoPage.getContent());
		model.addAttribute("gifInfoPage", gifInfoPage);

		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return getJspFilePath("list");
	}

	@RequestMapping(value = "/list/{animeEpisodeId}")
	public String listAnimeId(
			@PathVariable String animeEpisodeId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
			Model model, HttpServletRequest request)
					throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_animeEpisodeId", animeEpisodeId);

		AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);

		AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
		Page<GifInfo> gifInfoPage = gifInfoService.searchPageByParams(searchParams, pageNumber, ConstantsWeb.SHOT_LIST_PAGE_NUMBER, sortType, GifInfo.class);

		model.addAttribute("animeInfo", animeInfo);
		model.addAttribute("animeEpisode", animeEpisode);
		model.addAttribute("gifInfoPage", gifInfoPage);

		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return getJspFilePath("list");
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
			Model model, HttpServletRequest request) throws Exception {

		GifInfo gifInfo = gifInfoDao.findOne(id);
		gifInfo = gifInfoService.convertToVO(gifInfo);
		AnimeInfo animeInfo = entityCache.findOne(animeInfoDao, gifInfo.getAnimeInfoId());
		AnimeEpisode animeEpisode = entityCache.findOne(animeEpisodeDao, gifInfo.getAnimeEpisodeId());

		model.addAttribute("gifInfo", gifInfo);
		model.addAttribute("animeInfo", animeInfo);
		model.addAttribute("animeEpisode", animeEpisode);
		model.addAttribute("TimeStamp", DateUtil.formatTime(gifInfo.getTimeStamp(), 3));
		model.addAttribute("ContinueTime", DateUtil.formatTime(gifInfo.getContinueTime(), 3));

		// 算出当前数据在列表中的页数
		Integer rowNumber = entityCache.get("shotRowNumber_" + gifInfo.getId());
		if (rowNumber == null) {
			rowNumber = gifInfoDao.getRowNumber(gifInfo.getAnimeEpisodeId(), gifInfo.getTimeStamp(), Constants.FLAG_INT_NO);
			entityCache.put("shotRowNumber_" + gifInfo.getId(), rowNumber);
		}
		int pageSize = ConstantsWeb.SHOT_LIST_PAGE_NUMBER;
		model.addAttribute("rowNumber", rowNumber);
		int pageNumber = (rowNumber - 1) / pageSize + 1;
		model.addAttribute("pageNumber", (rowNumber - 1) / pageSize + 1);
		if (pageNumber > 1) {
			model.addAttribute("pageNumberUrl", "?page=" + pageNumber);
		}

		// 搜索前后页
		GifInfo previousGifInfo = entityCache.findPreviousGifInfo(gifInfo.getCreateDate());
		GifInfo nextGifInfo = entityCache.findNextGifInfo(gifInfo.getCreateDate());
		model.addAttribute("previousGifInfo", gifInfoService.convertToVO(previousGifInfo));
		model.addAttribute("nextGifInfo", gifInfoService.convertToVO(nextGifInfo));

		// 搜索字幕
		String localeLanguage = Constants.LANGUAGE_UNKNOW;
		if (!XRequestUtils.isSearchspider(request)) {
			localeLanguage = XRequestUtils.getLocaleLanguageCountry(request);
		}
		boolean showAllSubtitleFlag = Constants.FLAG_STR_YES.equals(XCookieUtils.getCookieValue(request, SiteConstants.COOKIE_SHOW_ALL_SUBTITLE_FLAG));
		List<String> actualShowLanage = subtitleInfoService.findActualShowLanguage(animeEpisode.getId(), localeLanguage, showLanage, showAllSubtitleFlag);
		Long startTime = gifInfo.getTimeStamp();
		Long endTime = gifInfo.getContinueTime() == null ? startTime + 10000 : startTime + gifInfo.getContinueTime();
		List<SubtitleLine> subtitleLineList = subtitleLineService.findByTimeRemoveDuplicate(animeEpisode.getId(), actualShowLanage, startTime, endTime);
		subtitleLineList = subtitleLineService.convertChinese(subtitleLineList, actualShowLanage, localeLanguage);
		model.addAttribute("subtitleLineList", subtitleLineList);

		// 前台页面cookie等参数设置
		model.addAttribute("scorllTop", scorllTop); // 用户提交时滚屏高度

		String ShotViewImgWidth = XCookieUtils.getCookieValue(request, "ShotViewImgWidth"); // 用户设定的图片展示宽度
		if (ShotViewImgWidth != null && !ShotViewImgWidth.matches("[0-9]+")) {
			ShotViewImgWidth = null;
		}
		model.addAttribute("ShotViewImgWidth", ShotViewImgWidth);
		String ShotImgDivWidth = null;// 需要设置的图片div宽度
		if (ShotViewImgWidth != null) {
			ShotImgDivWidth = ShotViewImgWidth;
		} else {
			ShotImgDivWidth = XCookieUtils.getCookieValue(request, "ShotImgDivWidth"); // 用户提交前获得的图片div宽度
			if (ShotImgDivWidth != null && !ShotImgDivWidth.matches("[0-9]+")) {
				ShotImgDivWidth = null;
			}
		}
		model.addAttribute("ShotImgDivWidth", ShotImgDivWidth);

		return getJspFilePath("view");
	}

	@RequestMapping(value = "/publicLike")
	@ResponseBody
	public Map<String, Object> publicLike(@RequestParam String id) {
		Map<String, Object> map = null;
		GifInfo gifInfo = gifInfoService.publicLikeAdd(id);
		if (gifInfo != null) {
			map = getSuccessCode();
			map.put("newCount", gifInfo.getPublicLikeCount());
		} else {
			map = getFailCode("截图不存在");
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
			Map<String, Object> map = new HashMap<>();

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
			@RequestParam(required = false) Date scheduleTime) {

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
