package xie.web.manage.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import xie.base.controller.BaseManagerController;
import xie.base.service.BaseService;
import xie.common.web.util.ConstantsWeb;

@Controller
@RequestMapping(value = ConstantsWeb.MANAGE_URL_PREFIX_STR + "/shot")
public class ShotManagerController extends BaseManagerController<ShotInfo, String> {
	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private AnimeEpisodeService animeEpisodeService;
	@Resource
	private ShotInfoService shotInfoService;

	@Override
	protected BaseService<ShotInfo, String> getBaseService() {
		return shotInfoService;
	}

	protected String getJspFileRootPath() {
		return "/managesite/shot";
	};

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/list/{animeEpisodeId}")
	public String shotList(@PathVariable String animeEpisodeId,
			@RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, ServletRequest request)
			throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_animeEpisodeId", animeEpisodeId);

		AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
		List<AnimeEpisode> animeEpisodeList = animeEpisodeService.findAll();
		AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
		Page<ShotInfo> shotInfoPage = shotInfoService.searchPageByParams(searchParams, pageNumber, 50, sortType, ShotInfo.class);

		model.addAttribute("animeInfo", animeInfo);
		model.addAttribute("animeEpisode", animeEpisode);
		model.addAttribute("animeEpisodeList", animeEpisodeList);
		model.addAttribute("shotInfoPage", shotInfoPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return getJspFilePath("list");
	}

	@RequiresPermissions(value = "userList:add")
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

	@RequiresPermissions(value = "userList:add")
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
