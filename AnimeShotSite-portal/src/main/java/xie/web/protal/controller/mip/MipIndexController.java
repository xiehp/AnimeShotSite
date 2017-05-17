package xie.web.protal.controller.mip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.base.controller.BaseController;
import xie.common.web.util.ConstantsWeb;
import xie.web.protal.controller.IndexController;

@Controller
@RequestMapping(value = ConstantsWeb.MIP_URL_PREFIX_STR)
public class MipIndexController extends BaseController {

	@Autowired
	AnimeInfoDao animeInfoDao;
	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	AnimeEpisodeDao animeEpisodeDao;
	@Autowired
	AnimeEpisodeService animeEpisodeService;
	@Autowired
	ShotInfoDao shotInfoDao;
	@Autowired
	ShotInfoService shotInfoService;
	@Autowired
	SubtitleInfoService subtitleInfoService;
	@Autowired
	SubtitleLineService subtitleLineService;
	@Autowired
	EntityCache entityCache;
	@Autowired
	IndexController indexController;

	@RequestMapping(value = "")
	public String index(HttpSession session, HttpServletRequest request) {
		indexController.index(session, request);
		
		return getJspFilePath("front");
	}

}
