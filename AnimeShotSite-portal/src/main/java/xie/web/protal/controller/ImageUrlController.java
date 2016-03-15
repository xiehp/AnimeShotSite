package xie.web.protal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yjysh.framework.base.controller.BaseFunctionController;
import com.yjysh.framework.sys.auth.entity.User;

import xie.animeshotsite.constants.ShotConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.utils.FilePathUtils;

@Controller
@RequestMapping(value = "/image")
public class ImageUrlController extends BaseFunctionController<User, String> {

	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	AnimeEpisodeService animeEpisodeService;
	@Autowired
	ShotInfoService shotInfoService;

	@RequestMapping(value = "/${type}/{id}")
	public void getImage(@PathVariable String type, @PathVariable String id, ServletResponse servletResponse) throws Exception {
		FileInputStream fis = null;
		servletResponse.setContentType("image/jpg");
		try {
			OutputStream out = servletResponse.getOutputStream();
			File file = null;
			if (ShotConstants.IMAGE_URL_TYPE_ANIME.equals(type)) {
				AnimeInfo animeInfo = animeInfoService.findOne(id);
				if (animeInfo != null && animeInfo.getTitleUrlId() != null) {
					file = animeInfo.getTitleUrl().getLocalFullFilePath();
				}
			} else if (ShotConstants.IMAGE_URL_TYPE_EPISODE.equals(type)) {
				AnimeEpisode animeEpisode = animeEpisodeService.findOne(id);
				AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
				if (animeEpisode != null) {
					file = FilePathUtils.getAnimeFullFilePath(animeInfo, animeEpisode, animeEpisode.getLocalFileName());
				}
			} else if (ShotConstants.IMAGE_URL_TYPE_SHOT.equals(type)) {
				ShotInfo shotInfo = shotInfoService.findOne(id);
				AnimeEpisode animeEpisode = animeEpisodeService.findOne(shotInfo.getAnimeEpisodeId());
				if (shotInfo != null) {
					file = FilePathUtils.getShotFullFilePath(animeEpisode, shotInfo);
				}
			}
			if (file == null) {
				file = FilePathUtils.getNoImageFilePath();
			}

			fis = new FileInputStream(file);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			out.write(b);
			out.flush();
		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
