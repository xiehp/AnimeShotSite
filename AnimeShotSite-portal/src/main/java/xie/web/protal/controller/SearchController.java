package xie.web.protal.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.base.controller.BaseController;
import xie.base.page.PageRequestUtil;
import xie.common.string.XStringUtils;

@Controller
public class SearchController extends BaseController {

	@Autowired
	private SubtitleLineService subtitleLineService;
	@Autowired
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
			Model model, ServletRequest request) throws Exception {

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
			e.printStackTrace();
		}
		// Order order1 = PageRequestUtil.createOrder(SubtitleLine, Direction.ASC);

		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, 10, sortType, direction);
		// Page<SubtitleLine> subtitleLinePage = subtitleLineService.searchPageByParams(searchParams, pageRequest, SubtitleLine.class);
		Page<SubtitleLine> subtitleLinePage = subtitleLineService.searchSubtitleLineByText(searchMode, name, keyword, pageRequest);

		// Map<String, AnimeEpisode> animeEpisodeMap = new HashMap<>();
		// Map<String, AnimeInfo> animeInfoMap = new HashMap<>();
		Map<String, ShotInfo> shotInfoMap = new LinkedHashMap<String, ShotInfo>();
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

		if (subtitleLineList.isEmpty()) {
			// 没有数据
			request.setAttribute("canBaiduIndex", false);// 不要索引
		} else {
			// 有数据
			request.setAttribute("searchPageNumber", "第" + (subtitleLinePage.getNumber() + 1) + "页");
		}

		return getJspFilePath("list");
	}
}
