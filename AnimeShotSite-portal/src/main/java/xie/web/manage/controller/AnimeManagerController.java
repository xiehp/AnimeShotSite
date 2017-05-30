package xie.web.manage.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ImageUrlService;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.controller.BaseManagerController;
import xie.base.module.ajax.vo.GoPageResult;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.common.date.DateUtil;
import xie.common.web.util.ConstantsWeb;

@Controller
@RequestMapping(value = ConstantsWeb.MANAGE_URL_PREFIX_STR + "/anime")
public class AnimeManagerController extends BaseManagerController<AnimeInfo, String> {

	@Resource
	private AnimeInfoService animeInfoService;

	@Resource
	private AnimeEpisodeService animeEpisodeService;

	@Resource
	private SubtitleInfoService subtitleInfoService;

	@Resource
	private ShotInfoDao shotInfoDao;

	@Resource
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
		List<Object[]> list = shotInfoDao.countRowNumberGroupByAnimeEpisodeId(animeInfoId);
		Map<String, Long> shotCountMap = new LinkedHashMap<>();
		Map<String, Long> maxTimestampMap = new LinkedHashMap<>();
		for (Object ob : list) {
			Object[] obs = (Object[]) ob;
			shotCountMap.put((String) obs[1], (Long) obs[2]);
			maxTimestampMap.put((String) obs[1], (Long) obs[3] - ((Long) obs[3]) % 5000);
		}
		request.setAttribute("shotCountMap", shotCountMap);
		request.setAttribute("maxTimestampMap", maxTimestampMap);

		// 获得字幕 列表
		List<SubtitleInfo> subtitleInfoList = subtitleInfoService.findByAnimeInfoId(animeInfoId);
		subtitleInfoList = subtitleInfoService.fillParentData(subtitleInfoList);
		request.setAttribute("subtitleInfoList", subtitleInfoList);

		// 自动运行参数表
		Map<String, AutoRunParam> autoRunParamList = autoRunParamService.getStringAutoRunParamMap(animeInfoId, true, false);
		request.setAttribute("autoRunParamList", autoRunParamList.values());

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
			animeInfo.setLocalRootPath(FilePathUtils.getRootDefault().getPath());
			animeInfo.setSort(1);
			animeInfo.setShowFlg(1);
			animeInfo.setShowDate(new Date());
			animeInfo.setDeleteFlag(0);

			request.setAttribute("animeInfo", animeInfo);
		}

		return getJspFilePath("new");
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/submit")
	public String submit(AnimeInfo animeInfo, ServletRequest request) throws Exception {

		AnimeInfo db = animeInfoService.findOne(animeInfo.getId());
		AnimeInfo newAnimeInfo = new AnimeInfo();
		if (db != null) {
			db.copyTo(newAnimeInfo);
		}
		animeInfo.copyToWithOutBaseInfo(newAnimeInfo);
		newAnimeInfo.setVersion(animeInfo.getVersion());
		newAnimeInfo = animeInfoService.save(newAnimeInfo);
		request.setAttribute("animeInfo", newAnimeInfo);

		return getUrlRedirectPath("view/" + newAnimeInfo.getId());
	}

	@Override
	public GoPageResult updateOneColumn(String id, String columnName, String columnValue, HttpServletRequest request) {
		GoPageResult goPageResult;
		if ("showFlg".equals(columnName) && Constants.FLAG_STR_YES.equals(columnValue)) {
			goPageResult = super.updateOneColumn(id, "showDate", DateUtil.convertToString(new Date()), request);
			if (!goPageResult.isSuccess()) {
				return goPageResult;
			}
		}

		return super.updateOneColumn(id, columnName, columnValue, request);
	}
}
