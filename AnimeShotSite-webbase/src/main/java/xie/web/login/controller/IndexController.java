package xie.web.login.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.base.controller.BaseController;
import xie.common.utils.XWaitTime;

@Controller
public class IndexController extends BaseController {

	@Autowired
	AnimeInfoDao animeInfoDao;
	@Autowired
	AnimeEpisodeDao animeEpisodeDao;
	@Autowired
	AnimeEpisodeService animeEpisodeService;
	@Autowired
	ShotInfoDao shotInfoDao;
	@Autowired
	ShotInfoService shotInfoService;

	private static Map<String, Object> cacheData = new HashMap<>();
	private static XWaitTime waitTime = new XWaitTime(60000);

	@RequestMapping(value = "/index")
	public String index(HttpSession session, HttpServletRequest request) {
		// waitTime.setTimeout(600);
		if (waitTime.isTimeout()) {
			waitTime.setTimeout(600000);
			waitTime.resetNowtime();

			// 获得最新剧集列表
			List<AnimeEpisode> animeEpisodeList = animeEpisodeService.getNewestAnimeEpisodeList(20);
			cacheData.put("animeEpisodeList", animeEpisodeList);

			// 获得剧集总数
			cacheData.put("animeEpisodeCount", animeEpisodeDao.count());

			// 获得最新截图
			List<ShotInfo> newestShotList = shotInfoService.getNewestShotList(20);
			cacheData.put("newestShotList", newestShotList);

			// 获得截图总数
			cacheData.put("shotCount", shotInfoDao.count());

			// 获得站长推荐
			List<ShotInfo> masterRecommandShotList = shotInfoService.getMasterRecommandShotList(7, 20);
			cacheData.put("masterRecommandShotList", masterRecommandShotList);

			// 获得公众推荐
			List<ShotInfo> publicLikeShotList = shotInfoService.getPublicLikeShotList(20);
			cacheData.put("publicLikeShotList", publicLikeShotList);
		}

		request.setAttribute("animeEpisodeList", cacheData.get("animeEpisodeList"));
		request.setAttribute("animeEpisodeCount", cacheData.get("animeEpisodeCount"));
		request.setAttribute("newestShotList", cacheData.get("newestShotList"));
		request.setAttribute("shotCount", cacheData.get("shotCount"));
		request.setAttribute("masterRecommandShotList", cacheData.get("masterRecommandShotList"));
		request.setAttribute("publicLikeShotList", cacheData.get("publicLikeShotList"));

		return "front";
	}

}
