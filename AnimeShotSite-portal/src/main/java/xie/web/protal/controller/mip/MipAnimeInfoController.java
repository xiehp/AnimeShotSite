package xie.web.protal.controller.mip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.common.web.util.ConstantsWeb;
import xie.web.protal.controller.AnimeInfoController;

@Controller
@RequestMapping(value = ConstantsWeb.MIP_URL_PREFIX_STR + "/anime")
public class MipAnimeInfoController extends AnimeInfoController {

	@Autowired
	AnimeInfoDao animeInfoDao;
	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	ShotInfoService shotInfoService;

	protected String getJspFileRootPath() {
		return super.getUrlRootPath();
	}


	@RequestMapping(value = "")
	public String list(
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, HttpSession session, HttpServletRequest request) {
		return super.list(sortType, pageNumber, model, session, request);
	}
}
