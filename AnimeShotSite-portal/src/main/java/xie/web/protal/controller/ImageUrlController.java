package xie.web.protal.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springside.modules.web.Servlets;
import xie.animeshotsite.constants.ShotCoreConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ImageUrl;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.controller.BaseFunctionController;
import xie.common.utils.XSSHttpUtil;

@Controller
public class ImageUrlController extends BaseFunctionController<ImageUrl, String> {

	@Resource
	AnimeInfoService animeInfoService;
	@Resource
	AnimeEpisodeService animeEpisodeService;
	@Resource
	ShotInfoService shotInfoService;

	@RequestMapping(value = "/image/{type}/{idTemp}")
	public void getImageByType(@PathVariable String type, @PathVariable String idTemp, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 图片访问，将在后面判断
		request.setAttribute("isImageRequest", true);

		// 首先判断当前访问是否正确，
		String hostName = XSSHttpUtil.getForwardedServerName(request);
		String remoteIp = XSSHttpUtil.getForwardedRemoteIpAddr(request);

		// 写入内容
		InputStream is = null;
		try {
			idTemp = idTemp.trim();
			String id = idTemp.trim();
			if (id.endsWith("t")) {
				id = id.substring(0, id.length() - 1);
			}
			if (id.endsWith("s")) {
				id = id.substring(0, id.length() - 1);
			}

			OutputStream out = response.getOutputStream();
			File file = null;
			if (ShotCoreConstants.IMAGE_URL_TYPE_ANIME.equals(type)) {
				AnimeInfo animeInfo = animeInfoService.findOne(id);
				if (animeInfo != null && animeInfo.getTitleUrlId() != null) {
					file = animeInfo.getTitleUrl().getLocalFullFilePath();
				}
			} else if (ShotCoreConstants.IMAGE_URL_TYPE_EPISODE.equals(type)) {
				AnimeEpisode animeEpisode = animeEpisodeService.findOne(id);
				AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
				if (animeEpisode != null) {
					file = FilePathUtils.getAnimeFullFilePath(animeInfo, animeEpisode, animeEpisode.getLocalFileName());
				}
			} else if (ShotCoreConstants.IMAGE_URL_TYPE_SHOT.equals(type)) {
				ShotInfo shotInfo = shotInfoService.findByTietukuUrlId(id);
				if (shotInfo != null) {
					AnimeEpisode animeEpisode = animeEpisodeService.findOneCache(shotInfo.getAnimeEpisodeId());
					AnimeInfo animeInfo = animeInfoService.findOneCache(shotInfo.getAnimeInfoId());
					file = FilePathUtils.getShotFullFilePath(shotInfo, animeEpisode, animeInfo);
				}
			}

			if (file != null) {
				is = new FileInputStream(file);
			} else {
				is = FilePathUtils.getNoImageFileStream();
			}

			// 设置返回头
			response.setContentType("image/jpg");
			Servlets.setExpiresHeader(response, Servlets.ONE_YEAR_SECONDS);
			Servlets.setLastModifiedHeader(response, file.lastModified());
			Servlets.setEtag(response, "W/\"" + file.lastModified() + "\"");

			// 根据结尾字符判断是否要修改尺寸
			if (idTemp.endsWith("t") || idTemp.endsWith("s")) {
				// 字节流转图片对象
				Image bi = ImageIO.read(is);

				// 设置尺寸大小
				int oldWidth = bi.getWidth(null);
				int oldHeight = bi.getHeight(null);

				int width = 300;
				if (idTemp.endsWith("s")) {
					width = 800;
				}
				int height = (int) (1.0 * oldHeight / oldWidth * width);

				// 构建图片流
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				// 绘制改变尺寸后的图
				tag.getGraphics().drawImage(bi, 0, 0, width, height, null);
				// 输出流
				ImageIO.write(tag, "jpg", out);
			} else {
				byte[] b = new byte[is.available()];
				is.read(b);
				out.write(b);
			}
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@RequestMapping(value = "/{id}")
	public void getImageWithTietuku1(@PathVariable String id, HttpServletRequest request, ServletResponse servletResponse) throws Exception {
		getImageByType(ShotCoreConstants.IMAGE_URL_TYPE_SHOT, id, request, servletResponse);
	}

	@RequestMapping(value = "/541950/{id}")
	public void getImageWithTietuku2(@PathVariable String id, HttpServletRequest request, ServletResponse servletResponse) throws Exception {
		getImageByType(ShotCoreConstants.IMAGE_URL_TYPE_SHOT, id, request, servletResponse);
	}

}
