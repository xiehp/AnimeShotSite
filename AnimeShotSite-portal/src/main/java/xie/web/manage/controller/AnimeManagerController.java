package xie.web.manage.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ImageUrlService;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.controller.BaseManagerController;
import xie.base.service.BaseService;
import xie.common.web.util.ConstantsWeb;

@Controller
@RequestMapping(value = ConstantsWeb.MANAGE_URL_STR + "/anime")
public class AnimeManagerController extends BaseManagerController<AnimeInfo, String> {

	@Autowired
	private AnimeInfoService animeInfoService;

	@Autowired
	private AnimeEpisodeService animeEpisodeService;

	@Autowired
	private SubtitleInfoService subtitleInfoService;

	@Autowired
	private ImageUrlService imageUrlService;

	@Override
	protected BaseService<AnimeInfo, String> getBaseService() {
		return animeInfoService;
	}

	protected String getJspFileRootPath() {
		return "/managesite/anime";
	};

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/list")
	public String list(HttpSession session, HttpServletRequest request) {

		// 获得动画 列表
		List<AnimeInfo> animeInfoList = animeInfoService.findAll();
		request.setAttribute("animeInfoList", animeInfoList);

		return getJspFilePath("list");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/view/{animeInfoId}")
	public String view(@PathVariable String animeInfoId, ServletRequest request) throws Exception {

		AnimeInfo animeInfo = animeInfoService.findOne(animeInfoId);
		request.setAttribute("animeInfo", animeInfo);

		// 获得剧集 列表
		List<AnimeEpisode> animeEpisodeList = animeEpisodeService.findByAnimeInfoId(animeInfoId);
		request.setAttribute("animeEpisodeList", animeEpisodeList);

		// 获得字幕 列表
		List<SubtitleInfo> subtitleInfoList = subtitleInfoService.findByAnimeInfoId(animeInfoId);
		subtitleInfoList = subtitleInfoService.fillParentData(subtitleInfoList);
		request.setAttribute("subtitleInfoList", subtitleInfoList);

		return getJspFilePath("new");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/new")
	public String newAnime(ServletRequest request) throws Exception {
		if (request.getAttribute("animeInfo") == null) {
			AnimeInfo animeInfo = new AnimeInfo();

			animeInfo.setProcessAction(0);
			animeInfo.setShotStatus(0);
			animeInfo.setStatus(0);
			animeInfo.setType(0);
			animeInfo.setLocalRootPath(FilePathUtils.getRootDefault().getAbsolutePath());
			animeInfo.setSort(1);
			animeInfo.setShowFlg(1);
			animeInfo.setDeleteFlag(0);

			request.setAttribute("animeInfo", animeInfo);
		}

		return getJspFilePath("new");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/submit")
	public String submit(AnimeInfo animeInfo, ServletRequest request) throws Exception {

		AnimeInfo newAnimeInfo = animeInfoService.save(animeInfo);
		request.setAttribute("animeInfo", newAnimeInfo);

		return getUrlRedirectPath("view/" + newAnimeInfo.getId());
	}
}
