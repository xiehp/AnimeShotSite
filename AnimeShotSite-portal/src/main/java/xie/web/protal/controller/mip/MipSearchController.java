package xie.web.protal.controller.mip;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.common.web.util.ConstantsWeb;
import xie.web.protal.controller.SearchController;

@Controller
@RequestMapping(value = ConstantsWeb.MIP_URL_PREFIX_STR)
public class MipSearchController extends SearchController {

	@Resource
	private SubtitleLineService subtitleLineService;
	@Resource
	private EntityCache entityCache;

	protected String getJspFileRootPath() {
		return "/mip/search/";
	};

	@RequestMapping(value = "/search")
	public String searchByKeyword(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "createDate") String sortType,
			@RequestParam(value = "sort", defaultValue = "DESC") String sort,
			@RequestParam(value = "searchMode", required = false) Boolean searchMode,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "name", required = false) String name,
			Model model, HttpServletRequest request) throws Exception {

		return super.searchByKeyword(pageNumber, sortType, sort, searchMode, keyword, name, model, request);
	}
}
