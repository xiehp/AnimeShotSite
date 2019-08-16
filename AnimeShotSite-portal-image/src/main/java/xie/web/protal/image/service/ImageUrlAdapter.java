package xie.web.protal.image.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xie.animeshotsite.constants.ShotCoreConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.GifInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.utils.FilePathUtils;
import xie.common.utils.XWaitTime;

@Service
public class ImageUrlAdapter {

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
	private GifInfoService gitInfoService;

	@Transactional(propagation = Propagation.REQUIRED)
	public File getFile(String type, String shotInfoId, XWaitTime aaaa) {
		File file = null;
		if (ShotCoreConstants.IMAGE_URL_TYPE_ANIME.equals(type)) {
			AnimeInfo animeInfo = animeInfoService.findOneCache(shotInfoId);
			if (animeInfo != null && animeInfo.getTitleUrlId() != null) {
				file = animeInfo.getTitleUrl().getLocalFullFilePath();
			}
		} else if (ShotCoreConstants.IMAGE_URL_TYPE_EPISODE.equals(type)) {
			AnimeEpisode animeEpisode = animeEpisodeService.findOneCache(shotInfoId);
			AnimeInfo animeInfo = animeInfoService.findOneCache(animeEpisode.getAnimeInfoId());
			if (animeEpisode != null) {
				file = FilePathUtils.getAnimeFullFilePath(animeInfo, animeEpisode, animeEpisode.getLocalFileName());
			}
		} else if (ShotCoreConstants.IMAGE_URL_TYPE_SHOT.equals(type)) {
			if (shotInfoMap == null || animeInfoMap == null || animeEpisodeMap == null) {
				synchronized (this) {
					if (shotInfoMap == null) {
						// List<ShotInfo> shotList = shotInfoService.findAll();
						// shotInfoMap = shotList.stream().collect(Collectors.toMap(ShotInfo::getTietukuUrlId, shotInfo -> shotInfo));
						shotInfoMap = new HashMap<>();
					}
					if (animeInfoMap == null) {
						// List<AnimeInfo> animeList = animeInfoService.findAll();
						// animeInfoMap = animeList.stream().collect(Collectors.toMap(AnimeInfo::getId,animeInfo -> animeInfo));
						animeInfoMap = new HashMap<>();
					}
					if (animeEpisodeMap == null) {
						// List<AnimeEpisode> episodeList = animeEpisodeService.findAll();
						// animeEpisodeMap = episodeList.stream().collect(Collectors.toMap(AnimeEpisode::getId, animeEpisode -> animeEpisode));
						animeEpisodeMap = new HashMap<>();
					}
				}
			}
			logger.info("check type, " + aaaa.getPastTime() + "");

			// Function<String, File> fun = (tempId) -> {
			// ShotInfo shotInfo = shotInfoMap.get(tempId);
			// if (shotInfo == null) {
			// shotInfo = shotInfoService.findByTietukuUrlId(tempId);
			// logger.info("get shotInfo, " + aaaa.getPastTime() + "");
			// }
			// if (shotInfo != null) {
			// AnimeEpisode animeEpisode = animeEpisodeMap.get(shotInfo.getAnimeEpisodeId());
			// AnimeInfo animeInfo = animeInfoMap.get(shotInfo.getAnimeInfoId());
			//
			// if (animeEpisode == null) {
			// animeEpisode = animeEpisodeService.findOneCache(shotInfo.getAnimeEpisodeId());
			// logger.info("get animeEpisode, " + aaaa.getPastTime() + "");
			// }
			// if (animeInfo == null) {
			// animeInfo = animeInfoService.findOneCache(shotInfo.getAnimeInfoId());
			// logger.info("get animeInfo, " + aaaa.getPastTime() + "");
			// }
			// return FilePathUtils.getShotFullFilePath(shotInfo, animeEpisode, animeInfo);
			// } else {
			// return null;
			// }
			// };
			// file = entityCache.get("imageId_" + shotInfoId, fun, shotInfoId, XConst.SECOND_10_MIN);
			file = shotInfoService.getShotFile(shotInfoId);
		} else if (ShotCoreConstants.IMAGE_URL_TYPE_GIF.equals(type)) {
			file = gitInfoService.getGifFile(shotInfoId);
		}

		return file;
	}
}
