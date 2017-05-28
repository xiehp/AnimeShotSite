package xie.web.protal.controller.mip;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.service.*;
import xie.base.controller.BaseController;
import xie.common.web.util.ConstantsWeb;
import xie.web.protal.controller.IndexController;

@Controller
@RequestMapping(value = ConstantsWeb.MIP_URL_PREFIX_STR)
public class MipIndexController extends BaseController {

	@Resource
	AnimeInfoDao animeInfoDao;
	@Resource
	AnimeInfoService animeInfoService;
	@Resource
	AnimeEpisodeDao animeEpisodeDao;
	@Resource
	AnimeEpisodeService animeEpisodeService;
	@Resource
	ShotInfoDao shotInfoDao;
	@Resource
	ShotInfoService shotInfoService;
	@Resource
	SubtitleInfoService subtitleInfoService;
	@Resource
	SubtitleLineService subtitleLineService;
	@Resource
	EntityCache entityCache;
	@Resource
	IndexController indexController;

	@RequestMapping(value = "")
	public String index(HttpSession session, HttpServletRequest request) {
		indexController.index(session, request);
		
		return getJspFilePath("front");
	}

}
