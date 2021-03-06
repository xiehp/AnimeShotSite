package xie.web.protal.image.controller;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.web.Servlets;

import com.google.common.net.HttpHeaders;

import net.coobird.thumbnailator.Thumbnails;
import xie.animeshotsite.constants.ShotCoreConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ImageUrl;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.controller.BaseFunctionController;
import xie.common.date.DateUtil;
import xie.common.utils.XSSHttpUtil;
import xie.common.utils.XWaitTime;
import xie.web.protal.image.service.ImageUrlAdapter;

@Controller
public class ImageUrlController extends BaseFunctionController<ImageUrl, String> {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private AnimeEpisodeService animeEpisodeService;
	private Map<String, AnimeInfo> animeInfoMap;
	private Map<String, ShotInfo> shotInfoMap;
	private Map<String, AnimeEpisode> animeEpisodeMap;

	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private EntityCache entityCache;
	@Resource
	private ShotInfoService shotInfoService;
	@Resource
	private ImageUrlAdapter imageUrlAdapter;

	@RequestMapping(value = "/{id}")
	public void getImageWithTietuku1(@PathVariable String id, HttpServletRequest request, HttpServletResponse servletResponse) throws Exception {
		getImageByTypeAndId(ShotCoreConstants.IMAGE_URL_TYPE_SHOT, id, request, servletResponse);
	}

	@RequestMapping(value = "/image/{type}/{idTemp}")
	public void getImageByTypeAndId(@PathVariable String type, @PathVariable String idTemp, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getRequestURL().toString().toLowerCase().endsWith(".gif")) {
			type = "gif";
		}

		// 图片访问，将在后面判断
		XWaitTime aaaa = new XWaitTime(5111100);
		logger.info(aaaa.getPastTime() + "");
		request.setAttribute("isImageRequest", true);
		String url = request.getRequestURL().toString();
		String idTempLower = idTemp.toLowerCase();
		String urlLower = url.toLowerCase();

		long lastModified = 10000000;
		long contentLength = 1000;

		// 首先判断当前访问是否正确，
		String hostName = XSSHttpUtil.getForwardedServerName(request);
		String remoteIp = XSSHttpUtil.getForwardedRemoteIpAddr(request);

		logger.info(aaaa.getPastTime() + "");
		// 写入内容
		InputStream is = null;
		try {
			// 截取ID号码
			String id = idTemp.trim();
			if (id.contains(".")) {
				id = id.split(".")[0].trim();
			}

			// 判断id是否合法
			if (!urlLower.endsWith(".jpg")
					&& !urlLower.endsWith(".jpeg")
					&& !urlLower.endsWith(".png")
					&& !urlLower.endsWith(".gif")
					&& !urlLower.endsWith(".svg")) {
				is = FilePathUtils.getProhibitFileStream();
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				logger.info("该路径禁止访问：{}", url);
				writeImage(url, id, is, response, lastModified, contentLength);
				return;
			}

			logger.info("开始 get info from db, " + aaaa.getPastTime() + "");
			// 去除可能带有的t或s
			String shotInfoId = id;
			if (id.endsWith("t")) {
				shotInfoId = id.substring(0, id.length() - 1);
			} else if (id.endsWith("s")) {
				shotInfoId = id.substring(0, id.length() - 1);
			}

			File file = imageUrlAdapter.getFile(type, shotInfoId, aaaa);
//			File file = null;
//			if (ShotCoreConstants.IMAGE_URL_TYPE_ANIME.equals(type)) {
//				AnimeInfo animeInfo = animeInfoService.findOne(shotInfoId);
//				if (animeInfo != null && animeInfo.getTitleUrlId() != null) {
//					file = animeInfo.getTitleUrl().getLocalFullFilePath();
//				}
//			} else if (ShotCoreConstants.IMAGE_URL_TYPE_EPISODE.equals(type)) {
//				AnimeEpisode animeEpisode = animeEpisodeService.findOne(shotInfoId);
//				AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
//				if (animeEpisode != null) {
//					file = FilePathUtils.getAnimeFullFilePath(animeInfo, animeEpisode, animeEpisode.getLocalFileName());
//				}
//			} else if (ShotCoreConstants.IMAGE_URL_TYPE_SHOT.equals(type)) {
//				if (shotInfoMap == null || animeInfoMap == null || animeEpisodeMap == null) {
//					synchronized (this) {
//						if (shotInfoMap == null) {
//							// List<ShotInfo> shotList = shotInfoService.findAll();
//							// shotInfoMap = shotList.stream().collect(Collectors.toMap(ShotInfo::getTietukuUrlId, shotInfo -> shotInfo));
//							shotInfoMap = new HashMap<>();
//						}
//						if (animeInfoMap == null) {
//							// List<AnimeInfo> animeList = animeInfoService.findAll();
//							// animeInfoMap = animeList.stream().collect(Collectors.toMap(AnimeInfo::getId,animeInfo -> animeInfo));
//							animeInfoMap = new HashMap<>();
//						}
//						if (animeEpisodeMap == null) {
//							// List<AnimeEpisode> episodeList = animeEpisodeService.findAll();
//							// animeEpisodeMap = episodeList.stream().collect(Collectors.toMap(AnimeEpisode::getId, animeEpisode -> animeEpisode));
//							animeEpisodeMap = new HashMap<>();
//						}
//					}
//				}
//				logger.info("check type, " + aaaa.getPastTime() + "");
//
////				Function<String, File> fun = (tempId) -> {
////					ShotInfo shotInfo = shotInfoMap.get(tempId);
////					if (shotInfo == null) {
////						shotInfo = shotInfoService.findByTietukuUrlId(tempId);
////						logger.info("get shotInfo, " + aaaa.getPastTime() + "");
////					}
////					if (shotInfo != null) {
////						AnimeEpisode animeEpisode = animeEpisodeMap.get(shotInfo.getAnimeEpisodeId());
////						AnimeInfo animeInfo = animeInfoMap.get(shotInfo.getAnimeInfoId());
////
////						if (animeEpisode == null) {
////							animeEpisode = animeEpisodeService.findOneCache(shotInfo.getAnimeEpisodeId());
////							logger.info("get animeEpisode, " + aaaa.getPastTime() + "");
////						}
////						if (animeInfo == null) {
////							animeInfo = animeInfoService.findOneCache(shotInfo.getAnimeInfoId());
////							logger.info("get animeInfo, " + aaaa.getPastTime() + "");
////						}
////						return FilePathUtils.getShotFullFilePath(shotInfo, animeEpisode, animeInfo);
////					} else {
////						return null;
////					}
////				};
////				file = entityCache.get("imageId_" + shotInfoId, fun, shotInfoId, XConst.SECOND_10_MIN);
//				file = shotInfoService.getShotFile(shotInfoId);
//			} else if (ShotCoreConstants.IMAGE_URL_TYPE_SHOT.equals(type)) {
//				file = shotInfoService.getGifFile(shotInfoId);
//			}

			logger.info("end get info from db, " + aaaa.getPastTime() + "");
			// file 可能存在于jar文件中，因此要小心
			String absolutePath = "/notExistsImage.jpg";
			if (file == null) {
				is = FilePathUtils.getNoImageFileStream();
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				logger.info("查询ID不存在：{}", url);
			} else {
				try {
					is = new FileInputStream(file);
					lastModified = file.lastModified();
					contentLength = file.length();
					absolutePath = file.getAbsolutePath();
				} catch (FileNotFoundException e) {
					is = FilePathUtils.getNoImageFileStream();
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					logger.info("文件未找到：{}", file.getAbsolutePath());
				}
			}

			logger.info("结束find file, new stream, " + aaaa.getPastTime() + "");
			// 判断http head是否有modify属性
			String eTag = "W/\"" + contentLength + "-" + lastModified + "\"";
			if (!checkAndSetIfModifiedSince(request, response, lastModified, eTag)) {
				logger.info("304:" + absolutePath);
				return;
			} else {
				logger.info("获取图片文件：" + absolutePath + ", 大小：" + contentLength / 1024 + "K, 更新时间：" + DateUtil.formatTime(lastModified, DateUtil.YMD_FULL));
			}

			logger.info("Before write, " + aaaa.getPastTime() + "");
			writeImage(url, id, is, response, lastModified, contentLength);

			logger.info("After write, " + aaaa.getPastTime());
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.info("程序出错：{}", url);
			logger.info("", e);
		}
	}

	private void writeImage(String url, String id, InputStream is, HttpServletResponse response, long lastModified, long contentLength) {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			// 设置返回头
			response.setContentType("image/jpeg");
			response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + (Servlets.ONE_YEAR_SECONDS * 1000));
			// response.setHeader(HttpHeaders.CACHE_CONTROL, "public, max-age=" + Servlets.ONE_YEAR_SECONDS);
			Servlets.setLastModifiedHeader(response, lastModified);
			Servlets.setEtag(response, "W/\"" + lastModified + "_" + contentLength + "\"");

			// 根据结尾字符判断是否要修改尺寸
			if (id.endsWith("t") || id.endsWith("s")) {
				// 字节流转图片对象
				BufferedImage bi = ImageIO.read(is);

				// 设置尺寸大小
				int oldWidth = bi.getWidth(null);
				int oldHeight = bi.getHeight(null);

				int width = 300;
				if (id.endsWith("s")) {
					width = 800;
				}
				int height = (int) (1.0 * oldHeight / oldWidth * width);

				// // 构建图片流
				// BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				// // 绘制改变尺寸后的图
				// tag.getGraphics().drawImage(bi, 0, 0, width, height, null);
				// // 输出流
				// ImageIO.write(tag, "jpeg", out);

				String outputFormat = "jpeg";
				if (url.toLowerCase().endsWith(".git")) {
					outputFormat = "gif";
				}
				Thumbnails.of(bi).size(width, height).outputQuality(0.85).outputFormat(outputFormat).toOutputStream(out);
			} else {
				byte[] b = new byte[is.available()];
				is.read(b);
				out.write(b);
			}
			out.flush();
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.info("程序出错：{}", url);
			logger.info("", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
	}

	public static boolean checkAndSetIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
			long lastModified, String etag) {
		try {
			long headerValue = request.getDateHeader("If-Modified-Since");
			if (headerValue != -1) {
				// If an If-None-Match header has been specified, if modified since
				// is ignored.
				if ((request.getHeader("If-None-Match") == null)
						&& (lastModified < headerValue + 1000)) {
					// The entity has not been modified since the date
					// specified by the client. This is not an error case.
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
					response.setHeader("ETag", etag);

					return false;
				}
			}
		} catch (IllegalArgumentException illegalArgument) {
			illegalArgument.printStackTrace();
		}

		return true;
	}

	@RequestMapping(value = "/541950/{id}")
	public void getImageWithTietuku2(@PathVariable String id, HttpServletRequest request, HttpServletResponse servletResponse) throws Exception {
		getImageByTypeAndId(ShotCoreConstants.IMAGE_URL_TYPE_SHOT, id, request, servletResponse);
	}

}
