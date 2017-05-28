package xie.animeshotsite.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.module.spring.SpringUtil;

import javax.annotation.Resource;

@Component
public class AutoCollectUtils {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private AnimeEpisodeService animeEpisodeService;
	@Resource
	private ResourceCollectUtils resourceCollectUtils;

	public static void main(String[] args) throws Exception {

		System.setProperty("spring.profiles.default", "productRemote");

		AutoCollectUtils autoCollectUtils = SpringUtil.getBean(AutoCollectUtils.class);
		autoCollectUtils.collectEpisodeSummary("2c9380825b61c4a1015b7a3a74c1000c", "http://baike.baidu.com/item/兽娘动物园/20379001", "第[0-9]+话 ", false);

		System.exit(0);
	}

	public void collectEpisodeSummary(String animeInfoId, String url, String titleReplaceReg, boolean forceUpdate) throws Exception {
		LinkedHashMap<Integer, Map<String, String>> summaryMaps = resourceCollectUtils.collectBaiduEpisodeSummary(url, titleReplaceReg);

		List<AnimeEpisode> list;
		list = animeEpisodeService.findByAnimeInfoId(animeInfoId);

		int index = 1;
		for (AnimeEpisode animeEpisode : list) {
			Integer division = null;
			try {
				division = Integer.parseInt(animeEpisode.getDivisionName().replaceAll("[^0-9]", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (division != null) {
				// Map<String, String> summaryMap = summaryMaps.get(division);
				Map<String, String> summaryMap = summaryMaps.get(index);
				if (summaryMap != null) {
					boolean updateFlag = false;
					if (forceUpdate || animeEpisode.getTitle() == null){
						animeEpisode.setTitle(summaryMap.get("title"));
						updateFlag = true;
					}
					if (forceUpdate || animeEpisode.getSummary() == null){
						animeEpisode.setSummary(summaryMap.get("summary"));
						updateFlag = true;
					}
					if (updateFlag) {
						animeEpisodeService.save(animeEpisode);
						logger.info("保存第{}集, title:{}", division, animeEpisode.getTitle());
					}
				}
			}

			index++;
		}

	}
}
