package xie.web.protal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
			Model model, ServletRequest request)
					throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_animeEpisodeId", animeEpisodeId);

		int pageSize = Constants.DEFAULT_PAGE_SIZE;
		AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
		AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
		Page<ShotInfo> shotInfoPage = shotInfoService.searchAllShots(searchParams, pageNumber, pageSize, sortType);

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
					subtitleLineList = subtitleLineDao.findBySubtitleInfoId(subtitleInfoList.get(0).getId());
					entityCache.put("subtitleLineList" + animeEpisodeId, subtitleLineList, XConst.SECOND_10_MIN * 1000);
				}
			}
			model.addAttribute("subtitleLineList", subtitleLineList);
		}

		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return

		getJspFilePath("list");
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
	public String shotView(@PathVariable String id, Model model, ServletRequest request) throws Exception {
		ShotInfo shotInfo = entityCache.findOne(shotInfoDao, id);
		AnimeInfo animeInfo = entityCache.findOne(animeInfoDao, shotInfo.getAnimeInfoId());
		AnimeEpisode animeEpisode = entityCache.findOne(animeEpisodeDao, shotInfo.getAnimeEpisodeId());

		model.addAttribute("shotInfo", shotInfo);
		model.addAttribute("animeI", animeInfo);
		model.addAttribute("animeEpisode", animeEpisode);

		// 算出当前数据在列表中的页数
		int rowNumber = shotInfoDao.getRowNumber(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp(), Constants.FLAG_INT_NO);
		int pageSize = Constants.DEFAULT_PAGE_SIZE;
		model.addAttribute("rowNumber", rowNumber);
		int pageNumber = (rowNumber - 1) / pageSize + 1;
		model.addAttribute("pageNumber", (rowNumber - 1) / pageSize + 1);
		if (pageNumber > 1) {
			model.addAttribute("pageNumberUrl", "/" + pageNumber);
		}

		// 搜索前后页
		ShotInfo previousShotInfo = entityCache.findPreviousShotInfo(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
		ShotInfo nextShotInfo = entityCache.findNextShotInfo(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
		model.addAttribute("previousShotInfo", previousShotInfo);
		model.addAttribute("nextShotInfo", nextShotInfo);

		// 搜索字幕
		Long startTime = shotInfo.getTimeStamp();
		Long endTime = nextShotInfo == null ? startTime + 5000 : nextShotInfo.getTimeStamp();
		List<SubtitleLine> subtitleLineList = subtitleLineService.findByTimeRemoveDuplicate(animeEpisode.getId(), startTime, endTime);
		model.addAttribute("subtitleLineList", subtitleLineList);

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
		for (int i = 0; i < 4; i++) {
			List<ShotInfo> list = shotInfoService.findRandom(5);
			shotInfoList.addAll(list);
		}
		model.addAttribute("shotInfoList", shotInfoList);

		return getJspFilePath("random");
	}
}
