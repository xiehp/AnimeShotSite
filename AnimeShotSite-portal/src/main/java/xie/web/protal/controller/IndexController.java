package xie.web.protal.controller;

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
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.base.controller.BaseController;
import xie.common.Constants;
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
	@Autowired
	SubtitleInfoService subtitleInfoService;
	@Autowired
	SubtitleLineService subtitleLineService;
	@Autowired
	EntityCache entityCache;

	private static Map<String, Object> cacheData = new HashMap<>();
	private static XWaitTime waitTime = new XWaitTime(60000);

	@RequestMapping(value = "/index")
	public String index(HttpSession session, HttpServletRequest request) {
		// if (waitTime.isTimeout()) {
		// waitTime.setTimeout(600000);
		// waitTime.resetNowtime();
		//
		// // 获得最新剧集列表
		// List<AnimeEpisode> animeEpisodeList = animeEpisodeService.getNewestAnimeEpisodeList(20);
		// cacheData.put("animeEpisodeList", animeEpisodeList);
		//
		// // 获得剧集总数
		// cacheData.put("animeEpisodeCount", animeEpisodeDao.count());
		//
		// // 获得最新截图
		// List<ShotInfo> newestShotList = shotInfoService.getNewestShotList(20);
		// cacheData.put("newestShotList", newestShotList);
		//
		// // 获得截图总数
		// cacheData.put("shotCount", shotInfoDao.count());
		//
		// // 获得站长推荐
		// List<ShotInfo> masterRecommandShotList = shotInfoService.getMasterRecommandShotList(7, 20);
		// cacheData.put("masterRecommandShotList", masterRecommandShotList);
		//
		// // 获得公众推荐
		// List<ShotInfo> publicLikeShotList = shotInfoService.getPublicLikeShotList(20);
		// cacheData.put("publicLikeShotList", publicLikeShotList);
		// }
		//
		// request.setAttribute("animeEpisodeList", cacheData.get("animeEpisodeList"));
		// request.setAttribute("animeEpisodeCount", cacheData.get("animeEpisodeCount"));
		// request.setAttribute("newestShotList", cacheData.get("newestShotList"));
		// request.setAttribute("shotCount", cacheData.get("shotCount"));
		// request.setAttribute("masterRecommandShotList", cacheData.get("masterRecommandShotList"));
		// request.setAttribute("publicLikeShotList", cacheData.get("publicLikeShotList"));

		// 获得最新剧集列表
		{
			List<AnimeEpisode> animeEpisodeList = entityCache.get("animeEpisodeList" + "Index");
			if (animeEpisodeList == null) {
				animeEpisodeList = animeEpisodeService.getNewestAnimeEpisodeList(30);
				entityCache.put("animeEpisodeList" + "Index", animeEpisodeList);
			}
			request.setAttribute("animeEpisodeList", animeEpisodeList);
		}

		// 获得剧集总数
		{
			Long animeEpisodeCount = entityCache.get("animeEpisodeCount" + "Index");
			if (animeEpisodeCount == null) {
//				animeEpisodeCount = animeEpisodeDao.count();
				animeEpisodeCount = animeEpisodeDao.countByShowFlgAndDeleteFlag(Constants.FLAG_INT_YES, Constants.FLAG_INT_NO);

				entityCache.put("animeEpisodeCount" + "Index", animeEpisodeCount);
			}
			request.setAttribute("animeEpisodeCount", animeEpisodeCount);
		}

		// 获得最新截图
		{
			List<ShotInfo> newestShotList = entityCache.get("newestShotList" + "Index");
			if (newestShotList == null) {
				newestShotList = shotInfoService.getNewestShotList(30);
				entityCache.put("newestShotList" + "Index", newestShotList);
			}
			request.setAttribute("newestShotList", newestShotList);
		}

		// 获得截图总数
		{
			Long shotCount = entityCache.get("shotCount" + "Index");
			if (shotCount == null) {
				shotCount = shotInfoDao.count();
				entityCache.put("shotCount" + "Index", shotCount);
			}
			request.setAttribute("shotCount", shotCount);
		}

		// 获得站长推荐
		{
			List<ShotInfo> masterRecommandShotList = entityCache.get("masterRecommandShotList" + "Index");
			if (masterRecommandShotList == null) {
				masterRecommandShotList = shotInfoService.getMasterRecommandShotList(7, 30);
				if (masterRecommandShotList.size() == 0) {
					masterRecommandShotList = shotInfoService.getMasterRecommandShotList(365, 30);
				}
				if (masterRecommandShotList.size() == 0) {
					masterRecommandShotList = shotInfoService.getMasterRecommandShotList(3650, 30);
				}
				entityCache.put("masterRecommandShotList" + "Index", masterRecommandShotList);
			}
			request.setAttribute("masterRecommandShotList", masterRecommandShotList);
		}

		// 获得公众推荐
		{
			List<ShotInfo> publicLikeShotList = entityCache.get("publicLikeShotList" + "Index");
			if (publicLikeShotList == null) {
				publicLikeShotList = shotInfoService.getPublicLikeShotList(30);
				entityCache.put("publicLikeShotList" + "Index", publicLikeShotList);
			}
			request.setAttribute("publicLikeShotList", publicLikeShotList);
		}

		return "front";
	}

}
