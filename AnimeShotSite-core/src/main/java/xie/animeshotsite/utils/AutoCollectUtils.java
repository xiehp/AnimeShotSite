package xie.animeshotsite.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.spring.SpringUtil;

@Component
public class AutoCollectUtils {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AnimeEpisodeService animeEpisodeService;

	public static void main(String[] args) {

		System.setProperty("spring.profiles.default", "production");

		AutoCollectUtils autoCollectUtils = SpringUtil.getBean(AutoCollectUtils.class);
		autoCollectUtils.collectEpisodeSummary("f39c57f4579fd61c0157a21c05320000", "http://baike.baidu.com/item/NEW%20GAME%21/18751606#分集剧情", null);

		System.exit(0);
	}

	public void collectEpisodeSummary(String animeInfoId, String url, String titleReplaceReg) {
		ResourceCollectUtils resourceCollectUtils = SpringUtil.getBean(ResourceCollectUtils.class);
		LinkedHashMap<Integer, Map<String, String>> summaryMaps = resourceCollectUtils.collectBaiduEpisodeSummary(url, titleReplaceReg);

		List<AnimeEpisode> list = animeEpisodeService.findByAnimeInfoId(animeInfoId);
		for (AnimeEpisode animeEpisode : list) {
			Integer division = null;
			try {
				division = Integer.parseInt(animeEpisode.getDivisionName().replaceAll("[^0-9]", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (division != null) {
				Map<String, String> summaryMap = summaryMaps.get(division);
				if (summaryMap != null) {
					animeEpisode.setTitle(summaryMap.get("title"));
					animeEpisode.setSummary(summaryMap.get("summary"));
					animeEpisodeService.save(animeEpisode);
					logger.info("保存第{}集, title:{}", division, animeEpisode.getTitle());
				}
			}
		}

	}
}
