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
import xie.common.utils.XListUtils;

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
	 * 决定使用哪些语言文本<br>
	 * 如果showLanguage为空，默认为中文或繁体+日语+英文<br>
	 * 如果showLanguage不为空，则获取数据库语言和设定语言的交集<br>
	 * 
	 * @return 字幕用语言的列表
	 */
	public List<String> findActualShowLanguage(String animeEpisodeId, String siteLocaleLanguage, List<String> showLanguage, boolean showAllSubtitleFlag) {
		if (siteLocaleLanguage == null) {
			siteLocaleLanguage = "";
		}
		String key = "DefaultShowLanguage_" + animeEpisodeId + "_" + siteLocaleLanguage + "_" + showLanguage + "_" + showAllSubtitleFlag;
		List<String> list = entityCache.get(key);
		if (list != null) {
			List<String> returnList = XListUtils.copy(list);
			return returnList;
		}

		// 生成所有语言的list
		List<String> defaultLanguageList = new ArrayList<>();
		List<String> defaultLanguageListTemp = new ArrayList<>();
		List<SubtitleInfo> listSubtitleInfo = subtitleInfoDao.findByAnimeEpisodeIdAndSubInStatusOrderByLocalFileName(animeEpisodeId, Constants.FLAG_INT_YES);
		for (SubtitleInfo subtitleInfo : listSubtitleInfo) {
			String language = subtitleInfo.getLanguage();
			defaultLanguageList.add(language);
			defaultLanguageListTemp.add(language);
		}

		if (showLanguage == null || showLanguage.size() == 0) {
			// 用户未指定，由系统决定，如果选择选择了网站语言，则显示该语言以及日语，如果没有选择，则显示全部字幕
			// 1.如果设定的简体，则删除繁体，如果设定繁体，则删除简体，默认留下简体
			if (Constants.LANGUAGE_ZH_TW.equalsIgnoreCase(siteLocaleLanguage)) {
				if (XStringUtils.existIgnoreCase(defaultLanguageList, SubtitleInfo.LANGUAGE_CHT)) {
					defaultLanguageList.remove(SubtitleInfo.LANGUAGE_CHS);
					defaultLanguageListTemp.remove(SubtitleInfo.LANGUAGE_CHS);
				}
			} else {
				if (XStringUtils.existIgnoreCase(defaultLanguageList, SubtitleInfo.LANGUAGE_CHS)) {
					defaultLanguageList.remove(SubtitleInfo.LANGUAGE_CHT);
					defaultLanguageListTemp.remove(SubtitleInfo.LANGUAGE_CHT);
				}
			}

			// 2.如果有超过一种字幕语言，且不显示所有字幕语言，则根据当前选择的网站语言，选出需要的语言，如果没有设定或不认识该网站语言，则显示全部字幕语言
			List<String> leaveLanguageList = new ArrayList<>();
			if (defaultLanguageList.size() > 1 && !showAllSubtitleFlag) {
				// 网站未设置显示所有字幕语言
				if (Constants.LANGUAGE_ZH_TW.equalsIgnoreCase(siteLocaleLanguage) || Constants.LANGUAGE_ZH_CN.equalsIgnoreCase(siteLocaleLanguage)) {
					// 如果网站语言选择中文，则只保留中文和日文
					leaveLanguageList.add(SubtitleInfo.LANGUAGE_MAPPING.get(Constants.LANGUAGE_JA.toLowerCase()));
					leaveLanguageList.add(SubtitleInfo.LANGUAGE_MAPPING.get(Constants.LANGUAGE_ZH_TW.toLowerCase()));
					leaveLanguageList.add(SubtitleInfo.LANGUAGE_MAPPING.get(Constants.LANGUAGE_ZH_CN.toLowerCase()));

				} else if (Constants.LANGUAGE_JA.equalsIgnoreCase(siteLocaleLanguage)) {
					// 如果网站语言选择日文，则只保留中文和日文
					leaveLanguageList.add(SubtitleInfo.LANGUAGE_MAPPING.get(Constants.LANGUAGE_JA.toLowerCase()));
					leaveLanguageList.add(SubtitleInfo.LANGUAGE_MAPPING.get(Constants.LANGUAGE_ZH_TW.toLowerCase()));
					leaveLanguageList.add(SubtitleInfo.LANGUAGE_MAPPING.get(Constants.LANGUAGE_ZH_CN.toLowerCase()));
				} else if (SubtitleInfo.LANGUAGE_MAPPING.containsKey(siteLocaleLanguage.toLowerCase())) {
					// 该语言在字幕mapping中存在，则只保留该网站语言对应字幕语言以及日文
					leaveLanguageList.add(SubtitleInfo.LANGUAGE_MAPPING.get(Constants.LANGUAGE_JA.toLowerCase()));
					leaveLanguageList.add(SubtitleInfo.LANGUAGE_MAPPING.get(siteLocaleLanguage.toLowerCase()));
				} else {
					// 其他情况，无法辨识语言，因此直接显示所有字幕，包括可能设定的网站语言all
				}

				if (leaveLanguageList.size() > 0) {
					XStringUtils.removeIfNotEqualIgnoreCase(defaultLanguageList, leaveLanguageList);
				}
			}
		} else {
			// 根据具体选定的字幕语言显示
			// TODO 如果希望显示的字幕语言不存在，还需要做其他处理
			XStringUtils.removeIfNotEqualIgnoreCase(defaultLanguageList, showLanguage);
		}

		// 没有可显示的字幕，则将所有字幕显示出来
		if (defaultLanguageList.size() == 0) {
			defaultLanguageList = defaultLanguageListTemp;
		}

		entityCache.put(key, defaultLanguageList, XConst.SECOND_05_HOUR * 1000);
		List<String> returnList = XListUtils.copy(defaultLanguageList);
		return returnList;
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
