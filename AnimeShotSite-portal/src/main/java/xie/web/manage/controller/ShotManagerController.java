package xie.web.manage.controller;

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
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.base.controller.BaseFunctionController;
import xie.sys.auth.entity.User;

@Controller
@RequestMapping(value = "managesite/shot")
public class ShotManagerController extends BaseFunctionController<User, String> {

	
	private @Autowired AnimeInfoService animeInfoService;
	
	private @Autowired AnimeEpisodeService animeEpisodeService;

	private @Autowired ShotInfoService shotInfoService;

	protected String getJspRootPath() {
		return "/managesite/shot/";
	};

	@RequestMapping(value = "/list/{animeEpisodeId}")
	public String shotList(@PathVariable String animeEpisodeId, @RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType, @RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model, ServletRequest request)
			throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_animeEpisodeId", animeEpisodeId);

		AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
		List<AnimeEpisode> animeEpisodeList = animeEpisodeService.findAll();
		AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
		Page<ShotInfo> shotInfoPage = shotInfoService.searchAllShots(searchParams, pageNumber, 50, sortType);

		model.addAttribute("animeInfo", animeInfo);
		model.addAttribute("animeEpisode", animeEpisode);
		model.addAttribute("animeEpisodeList", animeEpisodeList);
		model.addAttribute("shotInfoPage", shotInfoPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return getJspFilePath("list");
	}

	@RequestMapping(value = "/view/{id}")
	public String shotView(@PathVariable String id, Model model, ServletRequest request) throws Exception {
		ShotInfo shotInfo = shotInfoService.findOne(id);
		AnimeInfo animeInfo = animeInfoService.findOne(shotInfo.getAnimeInfoId());

		model.addAttribute("shotInfo", shotInfo);
		model.addAttribute("animeInfo", animeInfo);

		ShotInfo previousShotInfo = shotInfoService.findPrevious(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
		ShotInfo nextShotInfo = shotInfoService.findNext(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
		model.addAttribute("previousShotInfo", previousShotInfo);
		model.addAttribute("nextShotInfo", nextShotInfo);

		return getJspFilePath("view");
	}

	@RequestMapping(value = "/masterLike")
	@ResponseBody
	public Map<String, Object> masterLike(@RequestParam String id) {
		Map<String, Object> map = null;
		ShotInfo shotInfo = shotInfoService.masterLikeAdd(id);
		if (shotInfo != null) {
			map = getSuccessCode();
			map.put("newMasterCount", shotInfo.getMasterRecommendRank());
			map.put("newPublicCount", shotInfo.getPublicLikeCount());
		} else {
			map = getFailCode("截图不存在");
		}

		return map;
	}
}
