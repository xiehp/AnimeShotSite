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
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.base.controller.BaseFunctionController;
import xie.common.Constants;

@Controller
@RequestMapping(value = "/anime")
public class AnimeInfoController extends BaseFunctionController<AnimeInfo, String> {

	@Autowired
	AnimeInfoDao animeInfoDao;
	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	ShotInfoService shotInfoService;

	protected String getJspFileRootPath() {
		return "/anime/";
	};

	@Deprecated
	@RequestMapping(value = "list")
	public String oldlist(
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, HttpSession session, HttpServletRequest request) {

		return getUrlRedirectPath("");
	}

	@RequestMapping(value = "")
	public String list(
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, HttpSession session, HttpServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// 增加删除过滤
		searchParams.put("EQ_deleteFlag", Constants.FLAG_STR_NO);
		searchParams.put("EQ_showFlg", Constants.FLAG_STR_YES);
		Page<AnimeInfo> animeInfoPage = animeInfoService.searchPageByParams(searchParams, pageNumber, 50, sortType, AnimeInfo.class);

		model.addAttribute("animeInfoPage", animeInfoPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return getJspFilePath("list");
	}

}
