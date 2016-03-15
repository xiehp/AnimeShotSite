package xie.web.manage.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yjysh.framework.base.controller.BaseFunctionController;
import com.yjysh.framework.sys.auth.entity.User;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ImageUrl;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ImageUrlService;
import xie.animeshotsite.db.vo.ImageUrlVO;

@Controller
@RequestMapping(value = "managesite/anime")
public class AnimeController extends BaseFunctionController<User, String> {

	@Autowired
	private AnimeInfoService animeInfoService;

	@Autowired
	private AnimeEpisodeService animeEpisodeService;

	@Autowired
	private ImageUrlService imageUrlService;

	protected String getJspRootPath() {
		return "/managesite/anime/";
	};

	@RequestMapping(value = "/list")
	public String list(HttpSession session, HttpServletRequest request) {

		// 获得动画 列表
		List<AnimeInfo> animeInfoList = animeInfoService.findAll();
		request.setAttribute("animeInfoList", animeInfoList);

		return getJspFilePath("list");
	}

	@RequestMapping(value = "/view/{animeInfoId}")
	public String view(@PathVariable String animeInfoId, ServletRequest request) throws Exception {

		AnimeInfo animeInfo = animeInfoService.findOne(animeInfoId);
		request.setAttribute("animeInfo", animeInfo);

		// 获得剧集 列表
		List<AnimeEpisode> animeEpisodeList = animeEpisodeService.findByAnimeInfoId(animeInfoId);
		request.setAttribute("animeEpisodeList", animeEpisodeList);

		return getJspFilePath("new");
	}

	@RequestMapping(value = "/new")
	public String newAnime(ServletRequest request) throws Exception {

		return getJspFilePath("new");
	}

	@RequestMapping(value = "/submit")
	public String submit(AnimeInfo animeInfo, ServletRequest request) throws Exception {

		AnimeInfo newAnimeInfo = animeInfoService.save(animeInfo);
		request.setAttribute("animeInfo", newAnimeInfo);

		return getJspRedirectFilePath("view/" + newAnimeInfo.getId());
	}
}
