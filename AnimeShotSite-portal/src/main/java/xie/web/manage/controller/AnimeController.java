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
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ImageUrlService;
import xie.base.controller.BaseManagerController;
import xie.base.service.BaseService;
import xie.common.web.util.WebConstants;

@Controller
@RequestMapping(value = WebConstants.MANAGE_URL_STR + "/anime")
public class AnimeController extends BaseManagerController<AnimeInfo, String> {

	@Autowired
	private AnimeInfoService animeInfoService;

	@Autowired
	private AnimeEpisodeService animeEpisodeService;

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

		return getJspFilePath("new");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/new")
	public String newAnime(ServletRequest request) throws Exception {

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
