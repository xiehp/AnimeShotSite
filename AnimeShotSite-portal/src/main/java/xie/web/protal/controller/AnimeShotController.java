package xie.web.protal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springside.modules.web.Servlets;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.entity.SubtitleLine;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.repository.SubtitleLineDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.base.controller.BaseFunctionController;
import xie.common.Constants;
import xie.common.constant.XConst;
import xie.common.string.XStringUtils;
import xie.common.utils.XCookieUtils;
import xie.common.utils.XRequestUtils;
import xie.common.web.util.ConstantsWeb;

@Controller
@RequestMapping(value = "/shot")
public class AnimeShotController extends BaseFunctionController<ShotInfo, String> {

	@Autowired
	private AnimeInfoService animeInfoService;
	@Autowired
	private AnimeInfoDao animeInfoDao;
	@Autowired
	private AnimeEpisodeService animeEpisodeService;
	@Autowired
	private AnimeEpisodeDao animeEpisodeDao;
	@Autowired
	private ShotInfoService shotInfoService;
	@Autowired
	private ShotInfoDao shotInfoDao;
	@Autowired
	private SubtitleInfoService subtitleInfoService;
	@Autowired
	private SubtitleLineService subtitleLineService;
	@Autowired
	private SubtitleLineDao subtitleLineDao;
	@Autowired
	private EntityCache entityCache;

	protected String getJspFileRootPath() {
		return "/shot/";
	};

	@RequestMapping(value = "/list/{animeEpisodeId}")
	public String shotList(
			@PathVariable String animeEpisodeId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
			Model model, HttpServletRequest request)
					throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_animeEpisodeId", animeEpisodeId);

		AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
		if (animeEpisode == null) {
			// 剧集不存在，重定向到动画列表
			return "redirect:/anime/list";
		}
		AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
		Page<ShotInfo> shotInfoPage = shotInfoService.searchPageByParams(searchParams, pageNumber, ConstantsWeb.SHOT_LIST_PAGE_NUMBER, sortType, ShotInfo.class);
		if (pageNumber > shotInfoPage.getTotalPages() && shotInfoPage.getTotalPages() > 0) {
			// 页数不对， 并且有数据，直接定位到最后一页
			String pageUrl = shotInfoPage.getTotalPages() > 1 ? "?page=" + shotInfoPage.getTotalPages() : "";
			return getUrlRedirectPath("list/" + animeEpisode.getId() + pageUrl);
		}

		model.addAttribute("animeInfo", animeInfo);
		model.addAttribute("animeEpisode", animeEpisode);
		model.addAttribute("shotInfoPage", shotInfoPage);

		if (pageNumber == 1) {
			// 字幕
			List<SubtitleLine> subtitleLineList = entityCache.get("subtitleLineList" + animeEpisodeId);
			if (subtitleLineList == null) {
				List<SubtitleInfo> subtitleInfoList = entityCache.get("subtitleInfoList" + animeEpisodeId);
				if (subtitleInfoList == null) {
					subtitleInfoList = subtitleInfoService.findByAnimeEpisodeId(animeEpisodeId);
					entityCache.put("subtitleInfoList" + animeEpisodeId, subtitleInfoList, XConst.SECOND_10_MIN * 1000);
				}
				if (subtitleInfoList.size() > 0) {
					List<String> searchSubtitleInfoIdList = new ArrayList<String>();
					for (SubtitleInfo subtitleInfo : subtitleInfoList) {
						if (!Constants.FLAG_INT_YES.equals(subtitleInfo.getShowFlg())) {
							continue;
						}

						if (Constants.FLAG_INT_YES.equals(subtitleInfo.getDeleteFlag())) {
							continue;
						}

						searchSubtitleInfoIdList.add(subtitleInfo.getId());
					}

					subtitleLineList = subtitleLineDao.findBySubtitleInfoId(searchSubtitleInfoIdList);
					entityCache.put("subtitleLineList" + animeEpisodeId, subtitleLineList, XConst.SECOND_10_MIN * 1000);
				}
			}
			model.addAttribute("subtitleLineList", subtitleLineList);
		}

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

		ShotInfo shotInfo = shotInfoDao.findOne(id);
		shotInfo = shotInfoService.convertToVO(shotInfo);
		AnimeInfo animeInfo = entityCache.findOne(animeInfoDao, shotInfo.getAnimeInfoId());
		AnimeEpisode animeEpisode = entityCache.findOne(animeEpisodeDao, shotInfo.getAnimeEpisodeId());

		model.addAttribute("shotInfo", shotInfo);
		model.addAttribute("animeInfo", animeInfo);
		model.addAttribute("animeEpisode", animeEpisode);

		// 算出当前数据在列表中的页数
		Integer rowNumber = entityCache.get("shotRowNumber_" + shotInfo.getId());
		if (rowNumber == null) {
			rowNumber = shotInfoDao.getRowNumber(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp(), Constants.FLAG_INT_NO);
			entityCache.put("shotRowNumber_" + shotInfo.getId(), rowNumber);
		}
		int pageSize = ConstantsWeb.SHOT_LIST_PAGE_NUMBER;
		model.addAttribute("rowNumber", rowNumber);
		int pageNumber = (rowNumber - 1) / pageSize + 1;
		model.addAttribute("pageNumber", (rowNumber - 1) / pageSize + 1);
		if (pageNumber > 1) {
			model.addAttribute("pageNumberUrl", "?page=" + pageNumber);
		}

		// 搜索前后页
		ShotInfo previousShotInfo = entityCache.findPreviousShotInfo(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
		ShotInfo nextShotInfo = entityCache.findNextShotInfo(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
		model.addAttribute("previousShotInfo", shotInfoService.convertToVO(previousShotInfo));
		model.addAttribute("nextShotInfo", shotInfoService.convertToVO(nextShotInfo));

		// 搜索字幕
		String localeLanguage = "zh";
		if (!XRequestUtils.isSearchspider(request)) {
			if (RequestContextUtils.getLocale(request) != null) {
				String language = RequestContextUtils.getLocale(request).getLanguage();
				String country = RequestContextUtils.getLocale(request).getCountry();
				localeLanguage = language + (XStringUtils.isBlank(country) ? "" : "_" + country);
			}
		}
		List<String> actualShowLanage = subtitleInfoService.findActualShowLanguage(animeEpisode.getId(), localeLanguage, showLanage);
		Long startTime = shotInfo.getTimeStamp();
		Long endTime = nextShotInfo == null ? startTime + 5000 : nextShotInfo.getTimeStamp();
		List<SubtitleLine> subtitleLineList = subtitleLineService.findByTimeRemoveDuplicate(animeEpisode.getId(), actualShowLanage, startTime, endTime);
		subtitleLineList = subtitleLineService.convertChinese(subtitleLineList, actualShowLanage, localeLanguage);
		model.addAttribute("subtitleLineList", subtitleLineList);
		StringBuilder subtitleLineTextStrSb = new StringBuilder();
		for (SubtitleLine subtitleLine : subtitleLineList) {
			subtitleLineTextStrSb.append(subtitleLine.getText());
		}
		String subtitleLineTextStr = subtitleLineTextStrSb.toString();
		model.addAttribute("subtitleLineTextStr", subtitleLineTextStr);
		model.addAttribute("subtitleLineTextStr100", StringUtils.substring(subtitleLineTextStr.toString(), 0, 100));
		model.addAttribute("subtitleLineTextStr200", StringUtils.substring(subtitleLineTextStr.toString(), 0, 200));
		model.addAttribute("subtitleLineTextStr500", StringUtils.substring(subtitleLineTextStr.toString(), 0, 500));

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
		ShotInfo shotInfo = shotInfoService.publicLikeAdd(id);
		if (shotInfo != null) {
			map = getSuccessCode();
			map.put("newCount", shotInfo.getPublicLikeCount());
		} else {
			map = getFailCode("截图不存在");
		}

		return map;
	}

	@RequestMapping(value = "/random")
	public String random(Model model) throws Exception {
		List<ShotInfo> shotInfoList = new ArrayList<ShotInfo>();
		int count = (int) shotInfoDao.count();
		for (int i = 0; i < 10; i++) {
			List<ShotInfo> list = shotInfoService.findRandom(count - 2, 2);
			shotInfoList.addAll(list);
		}
		model.addAttribute("shotInfoList", shotInfoList);

		return getJspFilePath("random");
	}

	@RequestMapping(value = "/recommend")
	public String recommend(
			Model model,
			@RequestParam(value = "time", required = false) Integer time,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber) throws Exception {

		String cacheKey = "masterRecommandShotPage" + "_" + time + "_" + pageNumber;

		// 获得站长推荐
		Page<ShotInfo> masterRecommandShotPage = entityCache.get(cacheKey);
		if (masterRecommandShotPage == null) {
			masterRecommandShotPage = shotInfoService.getMasterRecommandShotPage(pageNumber, time, 48, false);
			entityCache.put(cacheKey, masterRecommandShotPage, XConst.SECOND_10_MIN * 1000);
		}

		model.addAttribute("shotInfoPage", masterRecommandShotPage);

		return getJspFilePath("recommend");
	}
}
