package xie.animeshotsite.db.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.SubtitleInfoDao;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.common.constant.XConst;
import xie.common.string.XStringUtils;

@Service
public class SubtitleInfoService extends BaseService<SubtitleInfo, String> {

	@Autowired
	private SubtitleInfoDao subtitleInfoDao;
	@Autowired
	private AnimeInfoDao animeInfoDao;
	@Autowired
	private AnimeEpisodeDao animeEpisodeDao;
	@Autowired
	private EntityCache entityCache;

	@Override
	public BaseRepository<SubtitleInfo, String> getBaseRepository() {
		return subtitleInfoDao;
	}

	/** 获取某动画下所有剧集的所有字幕 */
	public List<SubtitleInfo> findByAnimeInfoId(String animeInfoId) {
		return subtitleInfoDao.findByAnimeInfoIdOrderByLocalFileName(animeInfoId);
	}

	/** 获取某剧集下所有字幕 */
	public List<SubtitleInfo> findByAnimeEpisodeId(String animeEpisodeId) {
		return subtitleInfoDao.findByAnimeEpisodeIdOrderByLocalFileName(animeEpisodeId);
	}

	/**
	 * 根据当前已有剧集以及规则生成字幕信息<br>
	 * 通过位数扩展生成的值和语言作为唯一标识，如果字幕中已存在，则不生成<br>
	 * 通过位数扩展生成的值，和剧集中的唯一标识符对应<br>
	 */
	public SubtitleInfo saveMutiByEpisode(String param, Integer start, Integer end, Integer extention,
			Map<String, Object> requestMap, Class<SubtitleInfo> entityClass) throws InstantiationException, IllegalAccessException {

		//
		String animeInfoId = (String) requestMap.get("animeInfoId");
		String language = (String) requestMap.get("language");
		String[] paramArray = new String[0];
		if (param != null && param.length() > 0) {
			paramArray = param.split(",");
		}

		if (extention == null || extention < 1) {
			extention = String.valueOf(end).length();
		}

		String pattern = "";
		for (int i = 0; i < extention; i++) {
			pattern += "0";
		}

		// 新建数据
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		SubtitleInfo firstEntity = null;
		for (int i = start; i < end + 1; i++) {

			// 生成唯一标识
			SubtitleInfo entity = entityClass.newInstance();
			String[] formatedParamArray = new String[paramArray.length + 1];
			formatedParamArray[0] = decimalFormat.format(i);
			for (int k = 1; k < formatedParamArray.length; k++) {
				formatedParamArray[k] = paramArray[k - 1];
			}

			// 是否字幕信息已经存在
			AnimeEpisode animeEpisode = animeEpisodeDao.findByAnimeInfoIdAndNumber(animeInfoId, formatedParamArray[0]);
			if (animeEpisode == null) {
				// 不存在剧集信息
				continue;
			}
			SubtitleInfo subtitleInfo = subtitleInfoDao.findByAnimeInfoIdAndAnimeEpisodeIdAndLanguage(animeInfoId, animeEpisode.getId(), language);
			if (subtitleInfo != null) {
				// 该剧集的该语言的字幕信息已经存在
				continue;
			}

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.putAll(requestMap);
			XStringUtils.formatStr(paramMap, formatedParamArray);
			BeanMapper.copy(paramMap, entity);
			entity.setAnimeEpisodeId(animeEpisode.getId());
			SubtitleInfo newEntity = save(entity);

			if (firstEntity == null) {
				firstEntity = newEntity;
			}
		}

		return firstEntity;
	}

	/**
	 * 决定使用哪些默认字幕<br>
	 * 默认为中文或繁体+日语+英文<br>
	 */
	public List<String> findDefaultShowLanguage(String animeEpisodeId, String siteLocaleLanguage) {
		String key = "DefaultShowLanguage_" + animeEpisodeId + "_" + siteLocaleLanguage;
		List<String> list = entityCache.get(key);
		if (list != null) {
			return list;
		}

		// 生成所有语言的list
		List<String> defaultLanguageList = new ArrayList<>();
		List<SubtitleInfo> listSubtitleInfo = subtitleInfoDao.findByAnimeEpisodeIdOrderByLocalFileName(animeEpisodeId);
		for (SubtitleInfo subtitleInfo : listSubtitleInfo) {
			String language = subtitleInfo.getLanguage();

			if (SubtitleInfo.LANGUAGE_CHS.equalsIgnoreCase(language)) {
				defaultLanguageList.add(language);
			}

			if (SubtitleInfo.LANGUAGE_CHT.equalsIgnoreCase(language)) {
				if (!defaultLanguageList.contains(SubtitleInfo.LANGUAGE_CHT)) {
					defaultLanguageList.add(language);
				}
			}

			if (SubtitleInfo.LANGUAGE_JAPAN.equalsIgnoreCase(language)) {
				defaultLanguageList.add(language);
			}

			if (SubtitleInfo.LANGUAGE_ENGLIST.equalsIgnoreCase(language)) {
				defaultLanguageList.add(language);
			}
		}

		// 如果设定的简体，则删除繁体，如果设定繁体，则删除简体，默认留下简体
		if (Constants.LANGUAGE_ZH_TW.equalsIgnoreCase(siteLocaleLanguage)) {
			if (XStringUtils.existIgnoreCase(defaultLanguageList, SubtitleInfo.LANGUAGE_CHT)) {
				defaultLanguageList.remove(SubtitleInfo.LANGUAGE_CHS);
			}
		} else {
			if (XStringUtils.existIgnoreCase(defaultLanguageList, SubtitleInfo.LANGUAGE_CHS)) {
				defaultLanguageList.remove(SubtitleInfo.LANGUAGE_CHT);
			}
		}

		entityCache.put(key, defaultLanguageList, XConst.SECOND_05_HOUR * 1000);
		return defaultLanguageList;

	}

	@Override
	public SubtitleInfo fillParentData(SubtitleInfo subtitleInfo) {
		if (subtitleInfo == null) {
			return subtitleInfo;
		}
		subtitleInfo.setAnimeInfo(animeInfoDao.findOne(subtitleInfo.getAnimeInfoId()));
		subtitleInfo.setAnimeEpisode(animeEpisodeDao.findOne(subtitleInfo.getAnimeEpisodeId()));
		return subtitleInfo;
	}
}
