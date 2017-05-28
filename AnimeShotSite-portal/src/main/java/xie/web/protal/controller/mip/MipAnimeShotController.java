package xie.web.protal.controller.mip;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.repository.SubtitleLineDao;
import xie.animeshotsite.db.service.*;
import xie.animeshotsite.setup.ShotSiteSetup;
import xie.common.web.util.ConstantsWeb;
import xie.web.protal.controller.AnimeShotController;

@Controller
@RequestMapping(value = ConstantsWeb.MIP_URL_PREFIX_STR + "/shot")
public class MipAnimeShotController extends AnimeShotController {

	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private AnimeInfoDao animeInfoDao;
	@Resource
	private AnimeEpisodeService animeEpisodeService;
	@Resource
	private AnimeEpisodeDao animeEpisodeDao;
	@Resource
	private ShotInfoService shotInfoService;
	@Resource
	private ShotInfoDao shotInfoDao;
	@Resource
	private SubtitleInfoService subtitleInfoService;
	@Resource
	private SubtitleLineService subtitleLineService;
	@Resource
	private SubtitleLineDao subtitleLineDao;
	@Resource
	private EntityCache entityCache;
	@Resource
	private ShotTaskService shotTaskService;
	@Resource
	private ShotSiteSetup shotSiteSetup;

	protected String getJspFileRootPath() {
		// return "/shot/";
		return super.getUrlRootPath();
	};

	@RequestMapping(value = "/list/{animeEpisodeId}")
	public String shotList(
			@PathVariable String animeEpisodeId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
			Model model, HttpServletRequest request)
			throws Exception {

		return super.shotList(animeEpisodeId, pageNumber, sortType, model, request);
	}

	@RequestMapping(value = "/list/{animeEpisodeId}/{page}")
	public String shotList2(
			@PathVariable String animeEpisodeId,
			@PathVariable(value = "page") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
			Model model, ServletRequest request)
			throws Exception {

		return getUrlRedirectPath("list/" + animeEpisodeId + "?page=" + pageNumber);
	}

	@RequestMapping(value = "/view/{id}")
	public String shotView(
			@PathVariable String id,
			@RequestParam(required = false) String scorllTop,
			@RequestParam(required = false) List<String> showLanage,
			Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.shotView(id, scorllTop, showLanage, model, request, response);
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/delete/{id}")
	@ResponseBody
	public Map<String, Object> checkCreateShot(
			@PathVariable String id,
			Model model, HttpServletRequest request) throws Exception {

		return super.checkCreateShot(id, model, request);
	}

	@RequestMapping(value = "/publicLike")
	@ResponseBody
	public Map<String, Object> publicLike(@RequestParam String id) {
		return super.publicLike(id);
	}

	@RequestMapping(value = "/random_old")
	public String random(Model model) throws Exception {
		return super.random(model);
	}

	@RequestMapping(value = "/random")
	public String random2(Model model) throws Exception {

		return super.random2(model);
	}

	@RequestMapping(value = "/recommend")
	public String recommend(
			Model model,
			@RequestParam(value = "time", required = false) Integer time,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber) throws Exception {

		return super.recommend(model, time, pageNumber);
	}

	/**
	 * 增加一张截图
	 * 
	 * @param refShotInfoId 参照的截图ID
	 * @param preFlg 向前还是向后
	 * @param offsetTime 偏移多少毫秒
	 * @return
	 */
	@RequestMapping(value = "/doCreateShot")
	@ResponseBody
	public Map<String, Object> doCreateShot(
			@RequestParam(required = false) String refShotInfoId,
			@RequestParam(required = false) Long offsetTime,
			@RequestParam(required = false, defaultValue = "false") boolean preFlg,
			Model model, HttpServletRequest request) {

		return super.doCreateShot(refShotInfoId, offsetTime, preFlg, model, request);
	}

	/**
	 * 检测是否截图成功
	 *
	 * @param taskId 参照的截图ID
	 * @param animeEpisodeId 向前还是向后
	 * @param timestamp 偏移多少毫秒
	 * @return
	 */
	@RequestMapping(value = "/checkCreateShot")
	@ResponseBody
	public Map<String, Object> checkCreateShot(
			@RequestParam(required = false) String taskId,
			@RequestParam(required = false) String animeEpisodeId,
			@RequestParam(required = false) Long timestamp,
			Model model, HttpServletRequest request) {

		return super.checkCreateShot(taskId, animeEpisodeId, timestamp, model, request);
	}

	public void clearPreNextShotCache() {
		entityCache.clearBegin(EntityCache.CACHE_ID_Previous_ShotInfo);
		entityCache.clearBegin(EntityCache.CACHE_ID_Next_ShotInfo);
	}
}
