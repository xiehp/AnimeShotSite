package xie.web.manage.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.spring.SpringUtil;
import xie.base.controller.BaseFunctionController;
import xie.common.utils.SpringUtils;
import xie.common.web.util.RequestUtil;
import xie.sys.auth.entity.User;

@Controller
@RequestMapping(value = "managesite/animeEpisode")
public class AnimeEpisodeManagerController extends BaseFunctionController<User, String> {

	@Autowired
	private AnimeEpisodeService animeEpisodeService;
	@Autowired
	private ShotTaskService shotTaskService;
	@Autowired
	private SpringUtils springUtils;

	protected String getJspRootPath() {
		return "/managesite/animeEpisode/";
	};

	@RequestMapping(value = "/list/{animeInfoId}")
	public String list(@PathVariable String animeInfoId, HttpSession session, HttpServletRequest request) {

		// 获得动画 列表
		List<AnimeEpisode> animeEpisodeList = animeEpisodeService.findByAnimeInfoId(animeInfoId);
		request.setAttribute("animeEpisodeList", animeEpisodeList);

		return getJspFilePath("list");
	}

	@RequestMapping(value = "/view/{animeEpisodeId}")
	public String view(@PathVariable String animeEpisodeId, ServletRequest request) throws Exception {

		AnimeEpisode animeEpisodeInfo = animeEpisodeService.findOne(animeEpisodeId);
		request.setAttribute("animeEpisodeInfo", animeEpisodeInfo);

		return getJspFilePath("new");
	}

	@RequestMapping(value = "/new")
	public String newAnime(ServletRequest request) throws Exception {

		return getJspFilePath("new");
	}

	@RequestMapping(value = "/submit")
	public String submit(AnimeEpisode animeEpisode, ServletRequest request) throws Exception {

		AnimeEpisode newAnimeInfo = animeEpisodeService.save(animeEpisode);
		request.setAttribute("animeEpisodeInfo", newAnimeInfo);

		return getJspRedirectFilePath("view/" + newAnimeInfo.getId());
	}

	/**
	 * 根据规则生成剧集
	 * 
	 * @param extention 开始和结束扩展的最小位数，如果不指定， 则以end当前位数做扩展
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitMuti")
	public String submitMuti(
			@RequestParam(required = false, defaultValue = "1") Integer start,
			@RequestParam Integer end,
			@RequestParam(required = false) Integer extention,
			@RequestParam(required = false) String param,
			HttpServletRequest request) throws Exception {

		Map<String, Object> requestMap = RequestUtil.getAllParams(request);
		String firstId = animeEpisodeService.saveMuti(param, start, end, extention, requestMap);

		AnimeEpisode animeEpisodeInfo = animeEpisodeService.findOne(firstId);
		request.setAttribute("animeEpisodeInfo", animeEpisodeInfo);

		request.setAttribute("param", param);
		request.setAttribute("start", start);
		request.setAttribute("end", end);
		request.setAttribute("extention", extention);

		return getJspRedirectFilePath("view/" + firstId);
	}

	@RequestMapping(value = "/addShotTask")
	public String addShotTask(
			@RequestParam String id,
			@RequestParam String taskType,
			@RequestParam(required = false) Date scheduleTime,
			@RequestParam(required = false) Long startTime,
			@RequestParam(required = false) Long endTime,
			@RequestParam(required = false) Long timeInterval,
			@RequestParam(required = false) String specifyTimes,
			@RequestParam(required = false) Boolean forceUpload) {

		Map<String, Object> map = null;
		
		System.out.println(springUtils);
		System.out.println(animeEpisodeService);
//		System.out.println((AnimeEpisodeService)springUtils.getBean(AnimeEpisodeService.class));
		
//		System.out.println(SpringUtil.getBean(SpringUtils.class));
//		System.out.println((AnimeEpisodeService)SpringUtil.getBean(AnimeEpisodeService.class));
//		

		if ("1".equals(taskType)) {
			shotTaskService.addRunNormalEpisideTimeTask(id, scheduleTime, forceUpload, startTime, endTime, timeInterval);
		} else if ("2".equals(taskType)) {
			if (specifyTimes == null) {
				map = getFailCode("type为2时，specifyTimes不能为空");
			}
			shotTaskService.addRunSpecifyEpisideTimeTask(id, scheduleTime, forceUpload, specifyTimes);
		}

		map = getSuccessCode();

		return getJspRedirectFilePath("view/" + id);
	}
}
