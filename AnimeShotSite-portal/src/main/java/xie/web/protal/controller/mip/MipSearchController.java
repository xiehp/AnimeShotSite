package xie.web.protal.controller.mip;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.Servlets;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.SubtitleLine;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.base.page.PageRequestUtil;
import xie.common.string.XStringUtils;
import xie.common.web.util.ConstantsWeb;
import xie.module.language.XLanguageUtils;
import xie.web.protal.controller.SearchController;

@Controller
@RequestMapping(value = ConstantsWeb.MIP_URL_PREFIX_STR)
public class MipSearchController extends SearchController {

	@Autowired
	private SubtitleLineService subtitleLineService;
	@Autowired
	private EntityCache entityCache;

	protected String getJspFileRootPath() {
		return "/mip/search/";
	};

	@RequestMapping(value = "/search")
	public String keyword(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "sortType", defaultValue = "createDate") String sortType,
			@RequestParam(value = "sort", defaultValue = "DESC") String sort,
			@RequestParam(value = "searchMode", required = false) Boolean searchMode,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "name", required = false) String name,
			Model model, ServletRequest request) throws Exception {

		return super.keyword(pageNumber, sortType, sort, searchMode, keyword, name, model, request);
	}
}
