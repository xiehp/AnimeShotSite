package xie.web.protal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.service.*;
import xie.base.controller.BaseController;
import xie.common.Constants;
import xie.common.constant.XConst;

@Controller
public class IndexController extends BaseController {

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

	@RequestMapping(value = "/index")
	public String index(HttpSession session, HttpServletRequest request) {
		// 获得最新剧集列表
		{
			List<AnimeEpisode> animeEpisodeList = entityCache.get("animeEpisodeList" + "Index", () -> {
				return animeEpisodeService.getNewestAnimeEpisodeList(42);
			}, XConst.SECOND_01_HOUR * 1000);

			request.setAttribute("animeEpisodeList", animeEpisodeList);
		}

		// 获得剧集总数
		{
			Long animeEpisodeCount = entityCache.get("animeEpisodeCount" + "Index", () -> {
				// animeEpisodeCount = animeEpisodeDao.count();
				return animeEpisodeDao.countByShowFlgAndDeleteFlag(Constants.FLAG_INT_YES, Constants.FLAG_INT_NO);
			}, XConst.SECOND_01_HOUR * 1000);

			request.setAttribute("animeEpisodeCount", animeEpisodeCount);
		}

		// 获得最新截图
		{
			List<ShotInfo> newestShotList = entityCache.get("newestShotList" + "Index", () -> {
				return shotInfoService.getNewestShotList(6);
			}, XConst.SECOND_05_MIN * 1000 + XConst.SECOND_01_MIN * 1000);

			request.setAttribute("newestShotList", newestShotList);
		}

		// 获得截图总数
		{
			Long shotCount = entityCache.get("shotCount" + "Index", () -> {
				return shotInfoDao.count();
			}, XConst.SECOND_05_MIN * 1000 + XConst.SECOND_01_MIN * 1000);

			request.setAttribute("shotCount", shotCount);
		}

		// 获得站长推荐
		{
			List<ShotInfo> masterRecommandShotList = entityCache.get("masterRecommandShotList" + "Index", () -> {
				// 一周之内的
				List<ShotInfo> list = shotInfoService.getMasterRecommandShotList(1, 7, 42);
				// 二周之内的
				if (list.size() == 0) {
					list = shotInfoService.getMasterRecommandShotList(1, 14, 42);
				}
				// 三周之内的
				if (list.size() == 0) {
					list = shotInfoService.getMasterRecommandShotList(1, 21, 42);
				}
				// 一月之内的
				if (list.size() == 0) {
					list = shotInfoService.getMasterRecommandShotList(1, 31, 42);
				}
				// 半年之内的
				if (list.size() == 0) {
					list = shotInfoService.getMasterRecommandShotList(1, 180, 42);
				}
				// 一年之内的
				if (list.size() == 0) {
					list = shotInfoService.getMasterRecommandShotList(1, 365, 42);
				}
				// 十年之内的
				if (list.size() == 0) {
					list = shotInfoService.getMasterRecommandShotList(1, 3650, 42);
				}
				return list;
			}, XConst.SECOND_05_MIN * 1000);

			request.setAttribute("masterRecommandShotList", masterRecommandShotList);
		}

		// 获得公众推荐
		{
			// List<ShotInfo> publicLikeShotList = entityCache.get("publicLikeShotList" + "Index");
			// if (publicLikeShotList == null) {
			// publicLikeShotList = shotInfoService.getPublicLikeShotList(42);
			// entityCache.put("publicLikeShotList" + "Index", publicLikeShotList, XConst.SECOND_20_MIN * 1000);
			// }
			// request.setAttribute("publicLikeShotList", publicLikeShotList);
		}

		{
			Page<AnimeInfo> animeInfoPage = entityCache.get("Index_AnimeInfoList", () -> {
				Map<String, Object> searchParams = new HashMap<>();
				// 增加删除过滤
				searchParams.put("EQ_deleteFlag", Constants.FLAG_STR_NO);
				searchParams.put("EQ_showFlg", Constants.FLAG_STR_YES);
				Page<AnimeInfo> page = animeInfoService.searchPageByParams(searchParams, 1, 500, "showDate", Sort.Direction.DESC, AnimeInfo.class);
				return page;
			}, XConst.SECOND_10_MIN * 1000 - XConst.SECOND_01_MIN * 1000);

			request.setAttribute("animeInfoPage", animeInfoPage);
		}

		return "front";
	}

}
