package xie.web.protal.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.Servlets;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.SubtitleLine;
import xie.animeshotsite.db.entity.SubtitleSearchHistory;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.animeshotsite.db.service.SubtitleSearchHistoryService;
import xie.base.controller.BaseController;
import xie.base.page.PageRequestUtil;
import xie.common.string.XStringUtils;
import xie.common.utils.XCookieUtils;
import xie.common.web.util.ConstantsWeb;
import xie.module.language.XLanguageUtils;

@Controller
public class SearchController extends BaseController {

	@Resource
	private SubtitleLineService subtitleLineService;
	@Resource
	private SubtitleSearchHistoryService historyService;
	@Resource
	private EntityCache entityCache;

	protected String getJspFileRootPath() {
		return "/search/";
	};

	@RequestMapping(value = "/search")
	public String keyword(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "createDate") String sortType,
			@RequestParam(value = "sort", defaultValue = "DESC") String sort,
			@RequestParam(value = "searchMode", required = false) Boolean searchMode,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "name", required = false) String name,
			Model model, HttpServletRequest request) throws Exception {

		if (keyword == null && name == null) {
			// 没有进行检索操作标志
			model.addAttribute("isSearchFlag", false);
		} else {
			model.addAttribute("isSearchFlag", true);
		}

		if (name == null) {
			name = "";
		}
		if (keyword == null) {
			keyword = "";
		}

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		if (XStringUtils.isNotBlank(keyword)) {
			searchParams.put("LIKE_text", keyword);
		} else {
			searchParams.put("EQ_id", "1XXXXXXXXXsdfsdf234XXXXXXXXXXXXXXXXXXXXXX");
		}
		Direction direction = Direction.ASC;
		try {
			direction = Direction.fromStringOrNull(sort);
		} catch (Exception e) {
			_log.error("fromStringOrNull出错", e);
		}
		// Order order1 = PageRequestUtil.createOrder(SubtitleLine, Direction.ASC);

		// TODO 保存到搜索历史
		String cookieId  = XCookieUtils.getCookieValue(request, ConstantsWeb.SITE_COOKIE_ID);
		if (XStringUtils.isNotBlank(keyword)) {
			try {
				Map<String, Object> historySearchMap = new HashMap<>();
				historySearchMap.put("EQ_searchText", keyword);
				Page<SubtitleSearchHistory>	historyList = historyService.searchPageByParams(historySearchMap, SubtitleSearchHistory.class);

				int count = historyList.getSize() + 1;
				historyService.saveHistory(keyword, count, cookieId, "0", "0");
			} catch (Exception e) {
				_log.error("保存搜索历史出错", e);
			}
		}

		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, 10, sortType, direction);
		// Page<SubtitleLine> subtitleLinePage = subtitleLineService.searchPageByParams(searchParams, pageRequest, SubtitleLine.class);
		Page<SubtitleLine> subtitleLinePage = subtitleLineService.searchSubtitleLineByText(searchMode, name, keyword, pageRequest);

		// Map<String, AnimeEpisode> animeEpisodeMap = new HashMap<>();
		// Map<String, AnimeInfo> animeInfoMap = new HashMap<>();
		Map<String, ShotInfo> shotInfoMap = new LinkedHashMap<>();
		// Map<String, List<SubtitleLine>> subtitleLineMap = new LinkedHashMap<>();
		List<SubtitleLine> subtitleLineList = subtitleLinePage.getContent();

		Map<String, Map<String, Object>> searchResultMap = new LinkedHashMap<>();

		for (SubtitleLine subtitleLine : subtitleLineList) {
			subtitleLineService.fillParentData(subtitleLine);
			Long time = subtitleLine.getStartTime();
			if (subtitleLine.getEndTime() != null && subtitleLine.getStartTime() != null) {
				time = (subtitleLine.getEndTime() + subtitleLine.getStartTime()) / 2;
			}
			ShotInfo shotInfo = entityCache.findPreviousShotInfo(subtitleLine.getAnimeEpisodeId(), time);
			if (shotInfo == null) {
				shotInfo = entityCache.findNextShotInfo(subtitleLine.getAnimeEpisodeId(), time);
			}
			// ShotInfo shotInfo = shotInfoService.findPrevious(subtitleLine.getAnimeEpisodeId(), subtitleLine.getStartTime());
			shotInfoMap.put(subtitleLine.getId(), shotInfo);

			// 填充截图对应字幕列表数据
			// List<SubtitleLine> subtitleLineMapList = subtitleLineMap.get(shotInfo.getId());
			// if (subtitleLineMapList == null) {
			// subtitleLineMapList = new ArrayList<>();
			// subtitleLineMap.put(shotInfo.getId(), subtitleLineMapList);
			// }
			// subtitleLineMapList.add(subtitleLine);

			// mapResult
			String searchResultMapId = subtitleLine.getAnimeEpisodeId();
			if (shotInfo != null) {
				searchResultMapId = shotInfo.getId();
			}
			Map<String, Object> data = searchResultMap.get(searchResultMapId);
			if (data == null) {
				data = new LinkedHashMap<>();
				searchResultMap.put(searchResultMapId, data);
			}
			@SuppressWarnings("unchecked")
			List<SubtitleLine> subtitleListData = (List<SubtitleLine>) data.get("subtitleListData");
			if (subtitleListData == null) {
				subtitleListData = new ArrayList<>();
				data.put("shotInfoData", shotInfo);
				data.put("subtitleListData", subtitleListData);
			}
			subtitleListData.add(subtitleLine);
		}

		model.addAttribute("ASC", Direction.ASC);
		model.addAttribute("DESC", Direction.DESC);
		model.addAttribute("subtitleLinePage", subtitleLinePage);
		model.addAttribute("shotInfoMap", shotInfoMap);
		// model.addAttribute("subtitleLineMap", subtitleLineMap);
		model.addAttribute("searchResultMap", searchResultMap);

		// 将搜索条件编码成字符串，用于排序，分页的URL
		searchParams.remove("LIKE_text");
		searchParams.remove("EQ_id");
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		request.setAttribute("searchMode", searchMode);
		request.setAttribute("name", name);
		request.setAttribute("keyword", keyword);
		if (name != null) {
			request.setAttribute("nameHidden", XLanguageUtils.chineseFullTextChange(name));
		}
		if (keyword != null) {
			request.setAttribute("keywordHidden", XLanguageUtils.chineseFullTextChange(keyword));
		}

		if (subtitleLineList.isEmpty()) {
			// 没有数据
			request.setAttribute("canBaiduIndex", false);// 不要索引
		} else {
			// 有数据
			request.setAttribute("searchPageNumber", "第" + (subtitleLinePage.getNumber() + 1) + "页");
		}


		// TODO 搜索历史
		try {
			List<SubtitleSearchHistory>	historyTopList = historyService.searchTop(20);
			List<SubtitleSearchHistory>	historyCurrentList = historyService.searchCurrentTop(20);
			List<SubtitleSearchHistory>	historyCurrentByCookieIdList = historyService.searchCurrentByCookieId(20, cookieId);
			request.setAttribute("historyTopList", historyTopList);
			request.setAttribute("historyCurrentList", historyCurrentList);
			request.setAttribute("historyCurrentByCookieIdList", historyCurrentByCookieIdList);
		} catch (Exception e) {
			_log.error("保存搜索历史出错", e);
		}

		return getJspFilePath("list");
	}
}
