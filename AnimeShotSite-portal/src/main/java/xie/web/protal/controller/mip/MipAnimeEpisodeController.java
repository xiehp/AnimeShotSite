package xie.web.protal.controller.mip;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xie.common.web.util.ConstantsWeb;
import xie.web.protal.controller.AnimeEpisodeController;

@Controller
@RequestMapping(value = ConstantsWeb.MIP_URL_PREFIX_STR + "/episode")
public class MipAnimeEpisodeController extends AnimeEpisodeController {

	protected String getJspFileRootPath() {
		return getUrlRootPath();
	}

	@RequestMapping(value = "/list/{animeInfoId}")
	public String shotList(@PathVariable String animeInfoId,
			@RequestParam(value = "sortType", defaultValue = "sort") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, ServletRequest request, HttpServletResponse response)
					throws Exception {
		return super.shotList(animeInfoId, sortType, pageNumber, model, request, response);
	}
}
