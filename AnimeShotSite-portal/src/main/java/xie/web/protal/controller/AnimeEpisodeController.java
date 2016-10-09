package xie.web.protal.controller;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.Servlets;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.base.controller.BaseFunctionController;
import xie.common.Constants;

@Controller
@RequestMapping(value = "/episode")
public class AnimeEpisodeController extends BaseFunctionController<AnimeEpisode, String> {

	@Autowired
	private AnimeInfoService animeInfoService;
	@Autowired
	private AnimeEpisodeService animeEpisodeService;

	protected String getJspFileRootPath() {
		return "/episode/";
	};

	@RequestMapping(value = "/list/{animeInfoId}")
	public String shotList(@PathVariable String animeInfoId,
			@RequestParam(value = "sortType", defaultValue = "sort") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, ServletRequest request, HttpServletResponse response)
					throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_animeInfoId", animeInfoId);
		searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO + "");
		searchParams.put("EQ_showFlg", Constants.FLAG_STR_YES);

		AnimeInfo animeInfo = animeInfoService.findOne(animeInfoId);
		if (animeInfo == null) {
			// TODO 404跳转
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			request.setAttribute("canBaiduIndex", false);// 不要索引
			return getUrlRedirectPath("404");
		}
		Page<AnimeEpisode> animeEpisodePage = animeEpisodeService.searchPageByParams(searchParams, pageNumber, 54, sortType, AnimeEpisode.class);

		model.addAttribute("animeInfo", animeInfo);
		model.addAttribute("animeEpisodePage", animeEpisodePage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return getJspFilePath("list");
	}
}
