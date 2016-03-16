package xie.web.protal.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.Servlets;

import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.base.controller.BaseFunctionController;
import xie.base.entity.BaseEntity;
import xie.common.Constants;
import xie.sys.auth.entity.User;

@Controller
@RequestMapping(value = "/anime")
public class AnimeInfoController extends BaseFunctionController<AnimeInfo, String> {

	@Autowired
	AnimeInfoDao animeInfoDao;
	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	private AnimeEpisodeService animeEpisodeService;
	@Autowired
	ShotInfoService shotInfoService;

	protected String getJspRootPath() {
		return "/anime/";
	};

	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "sortType", defaultValue = "sort") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, HttpSession session, HttpServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO + "");
		sortType = BaseEntity.COLUMN_CREATE_DATE;
		Page<AnimeInfo> animeInfoPage = animeInfoService.searchAllShots(searchParams, pageNumber, Constants.DEFAULT_PAGE_SIZE, sortType, AnimeInfo.class);

		model.addAttribute("animeInfoPage", animeInfoPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return getJspFilePath("list");
	}

}
