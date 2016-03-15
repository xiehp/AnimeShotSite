package xie.web.protal.controller;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.Servlets;

import com.yjysh.framework.base.controller.BaseFunctionController;
import com.yjysh.framework.common.Constants;
import com.yjysh.framework.sys.auth.entity.User;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;

@Controller
@RequestMapping(value = "/episode")
public class AnimeEpisodeController extends BaseFunctionController<User, String> {

	@Autowired
	private AnimeInfoService animeInfoService;
	@Autowired
	private AnimeEpisodeService animeEpisodeService;

	protected String getJspRootPath() {
		return "/episode/";
	};

	@RequestMapping(value = "/list/{animeInfoId}")
	public String shotList(@PathVariable String animeInfoId,
			@RequestParam(value = "sortType", defaultValue = "sort") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, ServletRequest request)
					throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_animeInfoId", animeInfoId);
		searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO + "");

		AnimeInfo animeInfo = animeInfoService.findOne(animeInfoId);
		Page<AnimeEpisode> animeEpisodePage = animeEpisodeService.searchAllShots(searchParams, pageNumber, Constants.DEFAULT_PAGE_SIZE, sortType, AnimeEpisode.class);

		model.addAttribute("animeInfo", animeInfo);
		model.addAttribute("animeEpisodePage", animeEpisodePage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return getJspFilePath("list");
	}
}
